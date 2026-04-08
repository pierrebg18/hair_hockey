package fr.univtln.pierre.samples.game;

import com.jme3.app.SimpleApplication;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

import com.jme3.scene.Geometry;
import fr.univtln.pierre.samples.modele.Bonus;
import fr.univtln.pierre.samples.modele.Paddle;

import com.jme3.input.controls.ActionListener;
import fr.univtln.pierre.samples.modele.Puck;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Move implements ActionListener{
    /** On remplace certains raccourcis de navigation ici, afin de pouvoir
     * ajouter la marche et le saut contrôlés par la physique : */
    private boolean left = false, right = false, up = false, down = false;
    private boolean leftOp = false, rightOp = false, upOp = false, downOp = false;
    private Paddle myPaddle;
    private Paddle opponentPaddle;
    private Puck puck;
    private float speed = 3f ;
    private Bonus bonus = null;
    private Geometry puckShape;
    private int lastTouch = 0; // 0 for me, 1 for opponent

    public Move(){
    }

    public void NiveauJoueur(int Niveau){
        if (Niveau==1){
        setSpeed(2f);
        }
        else if(Niveau==2){
        setSpeed(2.5f);
        }
        else if(Niveau==3){
        setSpeed(3f);
        }
        else if(Niveau==4){
        setSpeed(3.5f);
        }
        else if(Niveau==5){
        setSpeed(4f);
        }
        else{
            System.out.println("erreur de niveau selectionné");
        }
    }

    public void setpaddle(Paddle myPaddle, Paddle opponentPaddle, Puck puck){
        this.myPaddle =myPaddle;
        this.opponentPaddle =opponentPaddle;
        this.puck =puck;
    }

    public void setUpKeys(InputManager inputManager) {
        // my keys
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");

        // opponent's keys
        inputManager.addMapping("LeftOp", new KeyTrigger(KeyInput.KEY_F));
        inputManager.addMapping("RightOp", new KeyTrigger(KeyInput.KEY_H));
        inputManager.addMapping("UpOp", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("DownOp", new KeyTrigger(KeyInput.KEY_G));
        inputManager.addListener(this, "LeftOp");
        inputManager.addListener(this, "RightOp");
        inputManager.addListener(this, "UpOp");
        inputManager.addListener(this, "DownOp");
    }

    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
            left = isPressed;
        } else if (binding.equals("Right")) {
            right= isPressed;
        } else if (binding.equals("Up")) {
            up = isPressed;
        } else if (binding.equals("Down")) {
            down = isPressed;
        }
        if (binding.equals("LeftOp")) {
            leftOp = isPressed;
        } else if (binding.equals("RightOp")) {
            rightOp = isPressed;
        } else if (binding.equals("UpOp")) {
            upOp = isPressed;
        } else if (binding.equals("DownOp")) {
            downOp = isPressed;
        }
    }

    public void simpleUpdateMove(float tpf) {
        float speed = 3f;
        Vector3f velocity = new Vector3f(0, 0, 0);
        if (left) {
            velocity.x -= speed;
        }
        if (right) {
            velocity.x += speed;
        }
        if (up) {
            velocity.z -= speed;
        }
        if (down) {
            velocity.z += speed;
        }

        myPaddle.getPaddle_phy().setLinearVelocity(velocity);
        // clone() crée une copie indépendante de la position physique
        myPaddle.setposition(myPaddle.getPaddle_phy().getPhysicsLocation().clone());


        //System.out.println(paddle.getposition());
        myPaddle.getPaddle_phy().setLinearVelocity(velocity);
    }

    public void simpleUpdateMoveOpponent(float tpf) {

        Vector3f velocity = new Vector3f(0, 0, 0);
        if (leftOp) {
            velocity.x -= speed;
        }
        if (rightOp) {
            velocity.x += speed;
        }
        if (upOp) {
            velocity.z -= speed;
        }
        if (downOp) {
            velocity.z += speed;
        }
        opponentPaddle.getPaddle_phy().setLinearVelocity(velocity);
    }

    public void resetPuckFall(){
        if (puck.getPuck_phy().getPhysicsLocation().y < -0.5){
            // put on side of that who lost
            System.out.println(puck.getPuck_phy().getPhysicsLocation().z);
            puck.resetPuck(puck.getPuck_phy().getPhysicsLocation().z>0?0:1);
            //System.out.println("Last touch :" + lastTouch);
        }
    }

    public void bonusTouch(){
        if (bonus != null) {
            // Calculate detection results
            CollisionResults results = new CollisionResults();
            bonus.getBonusBoundingBox().collideWith(puckShape, results);
            // Use the results
            if (results.size() > 0) {
                // how to react when a collision was detected
                CollisionResult closest = results.getClosestCollision();
                System.out.println("What was hit? " + closest.getGeometry().getName());
                System.out.println("Where was it hit? " + closest.getContactPoint());
                System.out.println("Distance? " + closest.getDistance());
            } else {
                // how to react when no collision occurred
            }
        }
    }

    private int timeInCenter = 0;
    public void blockInCenter(float tpf){
        float z = puck.getPuck_phy().getPhysicsLocation().z;
        Vector3f velocity = puck.getPuck_phy().getLinearVelocity();
        //System.out.println("Velocity" + velocity);
        float radius = puck.getRadius();
        float minVelocity = 0.1f;
        if (z < 1.5f-radius && z > -1.5f+-radius
                && velocity.x <= minVelocity &&  velocity.y <= minVelocity && velocity.z <= minVelocity) {
            timeInCenter += 1;
            if (timeInCenter > 240){
                // put on opposite side from that one who touched the last
                puck.resetPuck(lastTouch==0?1:0);
            }
        }
        else timeInCenter = 0;
        //System.out.println("Time in center: " + timeInCenter);
    }

    public void lastPlayerTouch(){
        // Calculate detection results for my puck
        CollisionResults results = new CollisionResults();
        BoundingBox myPaddleBoundingBox =new BoundingBox(myPaddle.getPaddle_phy().getPhysicsLocation(),
                myPaddle.getWidth(), myPaddle.getThickness(), myPaddle.getLenght());
        puckShape.collideWith(myPaddleBoundingBox, results);
        if (results.size() > 0) {
            System.out.println("Touch with me");
            // how to react when a collision was detected
            CollisionResult closest = results.getClosestCollision();
//            System.out.println("What was hit? " + closest.getGeometry().getName());
//            System.out.println("Where was it hit? " + closest.getContactPoint());
//            System.out.println("Distance? " + closest.getDistance());
            lastTouch = 0;
        } else {
            BoundingBox opponentPaddleBoundingBox = new BoundingBox(opponentPaddle.getPaddle_phy().getPhysicsLocation(),
                    opponentPaddle.getWidth(), opponentPaddle.getThickness(), opponentPaddle.getLenght());
            puckShape.collideWith(opponentPaddleBoundingBox, results);
            if (results.size() > 0) {
                System.out.println("Touch with opponent");
                // how to react when a collision was detected
                CollisionResult closest = results.getClosestCollision();
                System.out.println("What was hit? " + closest.getGeometry().getName());
                System.out.println("Where was it hit? " + closest.getContactPoint());
                System.out.println("Distance? " + closest.getDistance());
                lastTouch = 1;
            }
        }
    }
}
