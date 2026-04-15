package fr.univtln.pierre.samples.game;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import fr.univtln.pierre.samples.modele.Bonus;
import fr.univtln.pierre.samples.modele.BonusType;
import fr.univtln.pierre.samples.modele.InvisibleWall;
import fr.univtln.pierre.samples.modele.LightManager;
import fr.univtln.pierre.samples.modele.Paddle;
import fr.univtln.pierre.samples.modele.Puck;
import fr.univtln.pierre.samples.modele.Side;
import fr.univtln.pierre.samples.modele.Table;
import fr.univtln.pierre.samples.modele.TableBase;
import fr.univtln.pierre.samples.ui.UiManager;
import lombok.Setter;

@Setter
public class GameScene {
    private final AssetManager assetManager;
    private final BulletAppState bulletAppState;
    private final Node rootNode;
    private final InputManager inputManager;
    private Move move;
    private Ia ia;
    private Vector3f Last_position;
    private int compteurFrames;
    //gestion des rounds
    private Puck puck;
    private Vector3f puckStartPosition;
    private float puckMaxHeight;
    // 0 en cours // -1 lose // 1 win
    private int gameOver=0; 
    private int lvl=1;

    //va récupérer le mode présent dans UiManager
    private boolean modeJeu = true;

    public GameScene(AssetManager assetManager, BulletAppState bulletAppState, Node rootNode,InputManager inputManager) {
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
        this.inputManager = inputManager;
    }

    public void init() {

        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene
        move = new Move();
        move.setUpKeys(inputManager);
        ia = new Ia();

        

        //light
        //LightManager.setUpLight(rootNode);
        ColorRGBA blancChaud = new ColorRGBA(1f, 0.9f, 0.7f, 1f);
        ColorRGBA blancBienChaud = new ColorRGBA(1f, 0.85f, 0.6f, 1f);
        ColorRGBA ultraChaud = new ColorRGBA(1f, 0.75f, 0.45f, 1f);
        //LightManager.addLight(rootNode,new Vector3f(0f, 3f, 0f), ColorRGBA.Orange, 2f, 10f);
        LightManager.addLight(rootNode,new Vector3f(0f, 3f, 0f), ultraChaud, 2f, 10f);








        // table
        Table table =  new Table(2F, 4F, 0.1F, ColorRGBA.Blue);
        Material matTable = LightManager.createMaterial(assetManager, table.getColor());
        Geometry tableGeometry = table.createGeometry();
        table.createPhysic(tableGeometry,bulletAppState);
        tableGeometry.setMaterial(matTable);

        // left side of the table
        Side leftSide = new Side(table, ColorRGBA.Brown);
        Material matSide = LightManager.createMaterial(assetManager, leftSide.getColor());
        Geometry leftSideGeometry = leftSide.createGeometryLeft();
        leftSide.createPhysic(leftSideGeometry, bulletAppState);
        leftSideGeometry.setMaterial(matSide);

        // right side of the table
        Side rightSide = new Side(table, ColorRGBA.Brown);
        Geometry rightSideGeometry = rightSide.createGeometryRight();
        rightSide.createPhysic(rightSideGeometry, bulletAppState);
        rightSideGeometry.setMaterial(matSide);

        // table base
        TableBase tableBase =  new TableBase(table, leftSide, 2F, ColorRGBA.Brown);
        Material matBase = LightManager.createMaterial(assetManager, tableBase.getColor());
        Geometry tableBaseGeometry = table.createGeometry();
        tableBase.createPhysic(tableGeometry, bulletAppState);
        tableBaseGeometry.setMaterial(matBase);

        // puck
        puck = new Puck(20, 10, 0.3F, 0.15F, ColorRGBA.LightGray, table);
        puck.putOnMySide();
        Material matPuck = LightManager.createMaterial(assetManager,puck.getColor());
        Geometry puckGeometry = puck.createGeometry();
        puck.createPhysic(puckGeometry, bulletAppState);
        puckGeometry.setMaterial(matPuck);
        ia.setPuck(puck);
        this.puck = puck;
        puckStartPosition = new Vector3f(0f, 0.2f, 0f);
        puckMaxHeight = puckStartPosition.y + 0.05f;

        // my paddle
        Paddle myPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Material matPaddle = LightManager.createMaterial(assetManager,myPaddle.getColor());
        Geometry paddleGeometry = myPaddle.createGeometryMy(table);
        myPaddle.createPhysic(paddleGeometry,bulletAppState);
        paddleGeometry.setMaterial(matPaddle);

        // opponent's paddle
        Paddle opponentPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Geometry opponentPaddleGeometry = opponentPaddle.createGeometryOpponent(table);
        opponentPaddleGeometry.setMaterial(matPaddle);
        opponentPaddle.createPhysic(opponentPaddleGeometry,bulletAppState);
        ia.setPaddle(opponentPaddle);
        opponentPaddle.createPhysic(opponentPaddleGeometry, bulletAppState);

        move.setpaddle(myPaddle, opponentPaddle, puck);

        // collision groups to block each puddle in its zone

        // invisible walls
        InvisibleWall centerWall = new InvisibleWall(table, 1.5f);
        centerWall.createPhysicCenter(bulletAppState);

        InvisibleWall mySideWall = new InvisibleWall(table, 0.1f);
        mySideWall.createPhysicMySide(bulletAppState);

        InvisibleWall opponentSideWall = new InvisibleWall(table, 0.1f);
        opponentSideWall.createPhysicOpponentSide(bulletAppState);

        // collision groups (powers of 2)
        int groupPaddle = 1;
        int groupWall = 2;
        int groupPuck = 4;

        // definition of groups
        myPaddle.getPaddle_phy().setCollisionGroup(groupPaddle);
        opponentPaddle.getPaddle_phy().setCollisionGroup(groupPaddle);
        centerWall.getWall_phy().setCollisionGroup(groupWall);
        mySideWall.getWall_phy().setCollisionGroup(groupWall);
        opponentSideWall.getWall_phy().setCollisionGroup(groupWall);
        puck.getPuck_phy().setCollisionGroup(groupPuck);

        // definition of collision interactions
        myPaddle.getPaddle_phy().setCollideWithGroups(groupPuck | groupWall); // collision with invisible walls and puck
        opponentPaddle.getPaddle_phy().setCollideWithGroups(groupPuck | groupWall); // the same as previous
        centerWall.getWall_phy().setCollideWithGroups(groupPaddle); // collision with puddle, but not puck
        mySideWall.getWall_phy().setCollideWithGroups(groupPaddle);
        opponentSideWall.getWall_phy().setCollideWithGroups(groupPaddle);
        puck.getPuck_phy().setCollideWithGroups(groupPaddle); // collision with puddle, but not with invisible walls

        // test of bonus
        Bonus bonus = new Bonus(0.2f, BonusType.PADDLE_MINUS, myPaddle, opponentPaddle, puck, table);
        Material matBonus = LightManager.createMaterial(assetManager, bonus.getColor());
        Geometry bonusGeometry = bonus.createGeometry();
        //myPaddle.createPhysic(paddleGeometry,bulletAppState);
        bonusGeometry.setMaterial(matBonus);
        move.setBonus(bonus);
        move.setPuckShape(puckGeometry);

        // persons figures
        /*
        // me
        Spatial me = assetManager.loadModel("person/source/model/model_mesh.obj");
        me.scale(4f, 4f, 4f);
        me.rotate(0.0f, -3.0f, 0.0f);
        me.setLocalTranslation(0.0f, 0.0f, table.getLenght()+2f);

        // opponent
        Spatial opponent = assetManager.loadModel("person2/source/model/model_mesh.obj");
        opponent.scale(4f, 4f, 4f);
        //opponent.rotate(0.0f, 1.5f, 0.0f);
        opponent.setLocalTranslation(0.0f, 0.0f, -table.getLenght()-2f);
         */

        // to display collision shapes
        // bulletAppState.setDebugEnabled(true);

        pivot.attachChild(tableGeometry);
        pivot.attachChild(leftSideGeometry);
        pivot.attachChild(rightSideGeometry);
        //pivot.attachChild(tableBaseGeometry);
        pivot.attachChild(puckGeometry);
        pivot.attachChild(paddleGeometry);
        pivot.attachChild(opponentPaddleGeometry);
        pivot.attachChild(bonusGeometry);
        //pivot.attachChild(me);
        //pivot.attachChild(opponent);

        //permet de gérer l'ia
        Last_position = ia.getPuck().getPuck_phy().getPhysicsLocation().clone();

        
    }


    /*
    Position bien le puck
    */
    private void resetPuck() {
        puck.getPuck_phy().setLinearVelocity(Vector3f.ZERO);
        puck.getPuck_phy().setAngularVelocity(Vector3f.ZERO);
        puck.getPuck_phy().setPhysicsLocation(puckStartPosition.clone());
        puck.getPuck_phy().clearForces();
    }

    
    public void updateGame() {
    Rule.endRound(puck, puckStartPosition);
    if (!modeJeu) {
        if (Rule.player1Count == 12) {
            //Tournament.addLevel();
            //Tournament.addLevel();
            //Tournament.addLevel();
            //Tournament.addLevel();
            Tournament.addLevel();
            lvl=Tournament.updateLevel(ia, move);
            Rule.player1Count = 0;
            Rule.player2Count = 0;
            //condition win
            if (lvl==6){
                this.gameOver=1;
            }
        } else if (Rule.player2Count == 12) {
            this.gameOver = -1;
        }
    }
    else{
        if (Rule.player1Count == 12) {
             this.gameOver=1;
             System.out.println("player 1 win");
        }
        else if (Rule.player1Count == 12) {
             this.gameOver=1;
             System.out.println("player 2 win");
        }

    }
}


    public int update(float tpf) {
        //gestion des déplacement du joueur
        compteurFrames++;
        move.simpleUpdateMove(tpf);

        Rule.endRound(puck, puckStartPosition);
        move.resetPuckFall();
        move.bonusTouch();
        move.blockInCenter(tpf);
        move.lastPlayerTouch();
        //System.out.println(tpf);

        if (compteurFrames>=20){ //temps de réaction
        //gestion des déplacement de l'IA
        ia.simpleUpdateIaMove(tpf,this.Last_position);
        this.Last_position = puck.getPuck_phy().getPhysicsLocation().clone();
        compteurFrames=0;
        }

        move.simpleUpdateMove(tpf);

        
        if (modeJeu){
            move.simpleUpdateMoveOpponent(tpf);
        }
        Puck.pinPuckHeight(puck,puckMaxHeight);
        Puck.stabilizePuck(puck);

        
        updateGame();
        return this.gameOver;
    }
}
