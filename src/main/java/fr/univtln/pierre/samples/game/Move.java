package fr.univtln.pierre.samples.game;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;

import fr.univtln.pierre.samples.modele.Paddle;

import com.jme3.input.controls.ActionListener;
import fr.univtln.pierre.samples.modele.Puck;


public class Move implements ActionListener{
    /** On remplace certains raccourcis de navigation ici, afin de pouvoir
     * ajouter la marche et le saut contrôlés par la physique : */
    private boolean left = false, right = false, up = false, down = false;
    private boolean leftOp = false, rightOp = false, upOp = false, downOp = false;
    private Paddle myPaddle;
    private Paddle opponentPaddle;
    private Puck puck;

    public Move(){
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
    }

    public void simpleUpdateMoveOpponent(float tpf) {
        float speed = 3f ;
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

    public void resetPuck(){
        if (puck.getPuck_phy().getPhysicsLocation().y < -0.5){
            puck.putOnCenter();
            puck.getPuck_phy().setPhysicsLocation(puck.getPosition());
            // reset of velocity
            puck.getPuck_phy().setLinearVelocity(Vector3f.ZERO);
            puck.getPuck_phy().setAngularVelocity(Vector3f.ZERO);
        }
    }
}
