package fr.univtln.pierre.samples.game;

import com.jme3.anim.AnimComposer;
import com.jme3.anim.SkinningControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
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
import lombok.Getter;
import lombok.Setter;

@Setter
public class GameScene {
    private final AssetManager assetManager;
    private final BulletAppState bulletAppState;
    private final Node rootNode;
    private final InputManager inputManager;
    @Getter
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

    // détéction des touches de la balle
    private Paddle myPaddle;
    private Paddle opponentPaddle;

    //gestion des bonus :
    private BonusManager bonusManager;

    public GameScene(AssetManager assetManager, BulletAppState bulletAppState, Node rootNode,InputManager inputManager) {
        this.assetManager = assetManager;
        this.bulletAppState = bulletAppState;
        this.rootNode = rootNode;
        this.inputManager = inputManager;
    }

    private static Node pivot;
    private static Spatial enemy1;
    private static Spatial enemy2;
    private static Spatial enemy3;
    private static Spatial enemy4;
    private static Spatial enemy5;

    public void init() {

        pivot = new Node("pivot");
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
        Texture tableTex = assetManager.loadTexture("textures/blue_surface.jpg");
        Material matTable = LightManager.createMaterial(assetManager, tableTex);
        matTable.setFloat("Shininess", 60f);
        Geometry tableGeometry = table.createGeometry();
        table.createPhysic(tableGeometry,bulletAppState);
        tableGeometry.setMaterial(matTable);

        // left side of the table
        Side leftSide = new Side(table, ColorRGBA.Brown);
        Texture sideTex = assetManager.loadTexture("textures/wood.jpg");
        Material matSide = LightManager.createMaterial(assetManager, sideTex);
        matSide.setFloat("Shininess", 120f);
        Geometry leftSideGeometry = leftSide.createGeometryLeft();
        leftSide.createPhysic(leftSideGeometry, bulletAppState);
        leftSideGeometry.setMaterial(matSide);

        // right side of the table
        Side rightSide = new Side(table, ColorRGBA.Brown);
        Geometry rightSideGeometry = rightSide.createGeometryRight();
        rightSide.createPhysic(rightSideGeometry, bulletAppState);
        rightSideGeometry.setMaterial(matSide);

        // table base - not used finally
        TableBase tableBase =  new TableBase(table, leftSide, 2F, ColorRGBA.Brown);
        Material matBase = LightManager.createMaterial(assetManager, tableBase.getColor());
        Geometry tableBaseGeometry = table.createGeometry();
        tableBase.createPhysic(tableGeometry, bulletAppState);
        tableBaseGeometry.setMaterial(matBase);

        // puck
        puck = new Puck(20, 10, 0.3F, 0.15F, ColorRGBA.LightGray, table);
        puck.putOnMySide();
        Texture puckTex = assetManager.loadTexture("textures/metal.jpg");
        Material matPuck = LightManager.createMaterial(assetManager, puckTex);
        Geometry puckGeometry = puck.createGeometry();
        puck.createPhysic(puckGeometry, bulletAppState);
        puckGeometry.setMaterial(matPuck);
        ia.setPuck(puck);
        //this.puck = puck;
        puckStartPosition = new Vector3f(0f, 0.2f, 0f);
        puckMaxHeight = puckStartPosition.y + 0.05f;


        // my paddle
        myPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Texture myPaddleTex = assetManager.loadTexture("textures/yellow_wood.jpg");
        Material matPaddle = LightManager.createMaterial(assetManager, myPaddleTex);
        matPaddle.setFloat("Shininess", 120f);
        Geometry paddleGeometry = myPaddle.createGeometryMy(table);
        myPaddle.createPhysic(paddleGeometry,bulletAppState);
        paddleGeometry.setMaterial(matPaddle);

        // opponent's paddle
        opponentPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Texture opponentPaddleTex = assetManager.loadTexture("textures/pink_wood.jpg");
        Material matOpponentPaddle = LightManager.createMaterial(assetManager, opponentPaddleTex);
        matOpponentPaddle.setFloat("Shininess", 120f);
        Geometry opponentPaddleGeometry = opponentPaddle.createGeometryOpponent(table);
        opponentPaddleGeometry.setMaterial(matOpponentPaddle);
        opponentPaddle.createPhysic(opponentPaddleGeometry,bulletAppState);
        ia.setPaddle(opponentPaddle);

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

        bonusManager = new BonusManager(
                assetManager,
                pivot,
                table,
                myPaddle,
                opponentPaddle,
                puck,
                move,
                puckGeometry
        );
        bonusManager.initBonus();

//        Bonus bonus = new Bonus(0.2f, BonusType.PADDLE_MINUS, myPaddle, opponentPaddle, puck, table);
//        Material matBonus = LightManager.createMaterial(assetManager, bonus.getColor());
//        Geometry bonusGeometry = bonus.createGeometry();
//        //myPaddle.createPhysic(paddleGeometry,bulletAppState);
//        bonusGeometry.setMaterial(matBonus);
//        move.setBonus(bonus);
//        move.setPuckShape(puckGeometry);
//        move.setBonusGeometry(bonusGeometry);
//        move.setPivot(pivot);

        // persons figures
        /*
        // me
        Spatial me = assetManager.loadModel("person/source/model/model_mesh.obj");
        me.scale(4f, 4f, 4f);
        me.rotate(0.0f, -3.0f, 0.0f);
        me.setLocalTranslation(0.0f, 0.0f, table.getLenght()+2f);

         */

        // enemy 1
        enemy1 = assetManager.loadModel("person/source/model/model_mesh.obj");
        enemy1.scale(4f, 4f, 4f);
        enemy1.setLocalTranslation(0.0f, 0.0f, -table.getLenght()-2f);

        // enemy 2
        enemy2 = assetManager.loadModel("grandmother/source/model/grandmother.glb");
        enemy2.scale(0.015f, 0.015f, 0.015f);
        enemy2.rotate(0.0f, 3f, 0.0f);
        enemy2.setLocalTranslation(0.0f, -2.0f, -table.getLenght()-1f);

        // supprimer les contrôles des textures
        enemy2.depthFirstTraversal(spatial -> {
            spatial.removeControl(SkinningControl.class);
            spatial.removeControl(AnimComposer.class);
        });

        // enemy 3
        /*
        enemy3 = assetManager.loadModel("man_in_suit/source/model/model.glb");
        enemy3.scale(4f, 4f, 4f);
        enemy3.setLocalTranslation(0.0f, -1.0f, -table.getLenght()-2f);
         */
        enemy3 = assetManager.loadModel("king/armored_king.glb");
        enemy3.scale(5f, 5f, 5f);
        enemy3.rotate(0.0f, 3.2f, 0.0f);
        enemy3.setLocalTranslation(0f, -1f, -table.getLenght()-2f);

        enemy3.depthFirstTraversal(spatial -> {
            spatial.removeControl(SkinningControl.class);
            spatial.removeControl(AnimComposer.class);
        });

        // enemy 4
        enemy4 = assetManager.loadModel("dark_fairy/source/model/dark_fairy.glb");
        enemy4.scale(6f, 6f, 6f);
        enemy4.setLocalTranslation(0.0f, 0.0f, -table.getLenght()-1f);

        enemy4.depthFirstTraversal(spatial -> {
            spatial.removeControl(SkinningControl.class);
            spatial.removeControl(AnimComposer.class);
        });

        // enemy 5
        enemy5 = assetManager.loadModel("viking/viking_warrior.glb");
        enemy5.scale(0.5f, 0.5f, 0.5f);
        enemy5.setLocalTranslation(0f, 3f, -table.getLenght()-2f);

        enemy5.depthFirstTraversal(spatial -> {
            spatial.removeControl(SkinningControl.class);
            spatial.removeControl(AnimComposer.class);
        });

        // to display collision shapes
        // bulletAppState.setDebugEnabled(true);

        pivot.attachChild(tableGeometry);
        pivot.attachChild(leftSideGeometry);
        pivot.attachChild(rightSideGeometry);
        //pivot.attachChild(tableBaseGeometry);
        pivot.attachChild(puckGeometry);
        pivot.attachChild(paddleGeometry);
        pivot.attachChild(opponentPaddleGeometry);
        //pivot.attachChild(bonusGeometry);
        //pivot.attachChild(me);
        pivot.attachChild(enemy1);

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

    public static void displayEnemy1(){
        System.out.println("Display enemy 1");
        pivot.attachChild(enemy1);
    }

    public static void hideEnemy1(){
        System.out.println("Hide enemy 1");
        pivot.detachChild(enemy1);
    }

    public static void displayEnemy2(){
        System.out.println("Display enemy 2");
        pivot.attachChild(enemy2);
    }

    public static void hideEnemy2(){
        System.out.println("Hide enemy 2");
        pivot.detachChild(enemy2);
    }

    public static void displayEnemy3(){
        System.out.println("Display enemy 3");
        pivot.attachChild(enemy3);
    }

    public static void hideEnemy3(){
        System.out.println("Hide enemy 3");
        pivot.detachChild(enemy3);
    }

    public static void displayEnemy4(){
        System.out.println("Display enemy 4");
        pivot.attachChild(enemy4);
    }

    public static void hideEnemy4(){
        System.out.println("Hide enemy 4");
        pivot.detachChild(enemy4);
    }

    public static void displayEnemy5(){
        System.out.println("Display enemy 5");
        pivot.attachChild(enemy5);
    }

    public static void hideEnemy5(){
        System.out.println("Hide enemy 5");
        pivot.detachChild(enemy5);
    }

    
    public void updateGame() {
        Rule.endRound(puck, puckStartPosition);
        if (!modeJeu) {
            if (Rule.player1Count == 2) {
                //Tournament.addLevel();
                //Tournament.addLevel();
                //Tournament.addLevel();
                //Tournament.addLevel();
                Tournament.addLevel();
                lvl = Tournament.updateLevel(ia, move);
                Rule.player1Count = 0;
                Rule.player2Count = 0;
                //condition win
                if (lvl == 6) {
                    this.gameOver = 1;
                }
            } else if (Rule.player2Count == 10) {
                this.gameOver = -1;
            }
        }
        else {
            if (Rule.player1Count == 12) {
                this.gameOver = 1;
                System.out.println("player 1 win");
            } else if (Rule.player1Count == 12) {
                this.gameOver = 1;
                System.out.println("player 2 win");
            }
        }
    }


    public int update(float tpf) {
        //gestion des déplacements du joueur
        compteurFrames++;
        move.simpleUpdateMove(tpf);

        Rule.endRound(puck, puckStartPosition);
        move.resetPuckFall();
        move.bonusTouch();
        bonusManager.update(tpf);
        move.blockInCenter(tpf);
        move.lastPlayerTouch();
        move.notMoving(tpf);
        //System.out.println(tpf);

        puck.updateBonus(tpf);
        myPaddle.updateBonus(tpf);
        opponentPaddle.updateBonus(tpf);

        if (compteurFrames>=20){ //temps de réaction
            //gestion des déplacements de l'IA
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
