package fr.univtln.pierre.samples;

import com.bulletphysics.dynamics.RigidBody;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.PointLight;
import com.jme3.input.InputManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import fr.univtln.pierre.samples.modele.Paddle;
import fr.univtln.pierre.samples.modele.Puck;
import fr.univtln.pierre.samples.modele.Side;
import fr.univtln.pierre.samples.modele.Table;
import fr.univtln.pierre.samples.game.Rule;
import fr.univtln.pierre.samples.modele.*;
import fr.univtln.pierre.samples.game.Ia;
import fr.univtln.pierre.samples.game.Move;

import static fr.univtln.pierre.samples.modele.Puck.pinPuckHeight;
import static fr.univtln.pierre.samples.modele.Puck.stabilizePuck;

public class App extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;
    //private InputManager inputManager;
    private Move move;
    private Ia ia;
    private Vector3f Last_position;
    private int compteurFrames;
    //gestion des rounds
    private Puck puck;
    private Vector3f puckStartPosition;
    private float puckMaxHeight;
    private int player1Count = 0;
    private int player2Count = 0;
    private boolean ModeJeu = false;

    private fr.univtln.pierre.samples.ui.MainMenuUI mainMenuUI;
    private fr.univtln.pierre.samples.ui.GameHudUI gameHudUI;
    private int selectedMenuIndex = 0;
    private String player1Name = "Joueur 1";
    private String player2Name = "Joueur 2";

    private int score1 = 0;
    private int score2 = 0;

    private boolean menuVisible = true;

    public static void main(String[] args) {
        App app = new App();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setTitle("Hair Hockey");
        app.setSettings(settings);
        app.start();
    }

    public App(){
    }

    @Override
    public void simpleInitApp() {
        placeCameraMySide();
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene
        move = new Move();
        move.setUpKeys(inputManager);
        ia = new Ia();


        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

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
        InvisibleWall centerWall = new InvisibleWall(table);
        centerWall.createPhysicCenter(bulletAppState);

        InvisibleWall mySideWall = new InvisibleWall(table);
        mySideWall.createPhysicMySide(bulletAppState);

        InvisibleWall opponentSideWall = new InvisibleWall(table);
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
        pivot.attachChild(puckGeometry);
        pivot.attachChild(paddleGeometry);
        pivot.attachChild(opponentPaddleGeometry);
        //pivot.attachChild(me);
        //pivot.attachChild(opponent);

        //permet de gérer l'ia
        Last_position = ia.getPuck().getPuck_phy().getPhysicsLocation().clone();

        // UI
        mainMenuUI = new fr.univtln.pierre.samples.ui.MainMenuUI(this);
        gameHudUI = new fr.univtln.pierre.samples.ui.GameHudUI(this);

        initKeys();
        showMenu();
    }

    private void initKeys() {
        inputManager.addMapping("MENU_UP", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("MENU_DOWN", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("SELECT", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("CHANGE_PLAYERS", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("ADD_SCORE_LEFT", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("ADD_SCORE_RIGHT", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("BACK_OR_QUIT", new KeyTrigger(KeyInput.KEY_ESCAPE));

        inputManager.addListener(this,
                "MENU_UP",
                "MENU_DOWN",
                "SELECT",
                "CHANGE_PLAYERS",
                "ADD_SCORE_LEFT",
                "ADD_SCORE_RIGHT",
                "BACK_OR_QUIT");
    }



    private void executeSelectedMenu() {
        switch (selectedMenuIndex) {
            case 0:
                startGame();
                break;
            case 1:
                changePlayers();
                break;
            case 2:
                stop();
                break;
            default:
                break;
        }
    }

    public void placeCameraMySide(){
        cam.setLocation(new Vector3f(0, 4f, 9f));
        cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 1));
    }

    public void placeCameraOpponentSide(){
        cam.setLocation(new Vector3f(0, 4f, -9f));
        cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 1));
    }

    // rotation to adjust by 180-degree rotation around table
    public void placeCameraUp(){
        cam.setLocation(new Vector3f(0, 12f, 0f));
        cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, -1, 0));
        Quaternion roll90x = new Quaternion();
        roll90x.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
        cam.setRotation(roll90x);
    }

    private void resetPuck() {
        puck.getPuck_phy().setLinearVelocity(Vector3f.ZERO);
        puck.getPuck_phy().setAngularVelocity(Vector3f.ZERO);
        puck.getPuck_phy().setPhysicsLocation(puckStartPosition.clone());
        puck.getPuck_phy().clearForces();
    }







    @Override
    public void simpleUpdate(float tpf) {
        //gestion des déplacement du joueur
        compteurFrames++;
        move.simpleUpdateMove(tpf);
        //System.out.println(tpf);

        if (compteurFrames>=20){ //temps de réaction
        //gestion des déplacement de l'IA
        ia.simpleUpdateIaMove(tpf,this.Last_position);
        this.Last_position = puck.getPuck_phy().getPhysicsLocation().clone();
        compteurFrames=0;
        }

        move.simpleUpdateMove(tpf);

        if (ModeJeu){
        move.simpleUpdateMoveOpponent(tpf);
        }

        Puck.pinPuckHeight(puck,puckMaxHeight);
        Puck.stabilizePuck(puck);
        Rule.endRound(puck, player1Count, player2Count, puckStartPosition);
    }

    public void showMenu() {
        guiNode.detachAllChildren();
        menuVisible = true;
        mainMenuUI.show(player1Name, player2Name, selectedMenuIndex);
    }

    public void showHud() {
        guiNode.detachAllChildren();
        menuVisible = false;
        gameHudUI.show(player1Name, player2Name, score1, score2);
    }

    public void startGame() {
        score1 = 0;
        score2 = 0;
        showHud();
    }

    public void changePlayers() {
        if (player1Name.equals("Joueur 1")) {
            player1Name = "Yassine";
            player2Name = "Invité";
        } else {
            player1Name = "Joueur 1";
            player2Name = "Joueur 2";
        }

        if (menuVisible) {
            showMenu();
        } else {
            showHud();
        }
    }

    public void addScoreLeft() {
        score1++;
        showHud();
    }

    public void addScoreRight() {
        score2++;
        showHud();
    }

    public int getScreenWidth() {
        return cam.getWidth();
    }

    public int getScreenHeight() {
        return cam.getHeight();
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }

        if (menuVisible) {
            switch (name) {
                case "MENU_UP":
                    selectedMenuIndex--;
                    if (selectedMenuIndex < 0) {
                        selectedMenuIndex = 2;
                    }
                    showMenu();
                    break;

                case "MENU_DOWN":
                    selectedMenuIndex++;
                    if (selectedMenuIndex > 2) {
                        selectedMenuIndex = 0;
                    }
                    showMenu();
                    break;

                case "SELECT":
                    executeSelectedMenu();
                    break;

                case "CHANGE_PLAYERS":
                    selectedMenuIndex = 1;
                    changePlayers();
                    break;

                case "BACK_OR_QUIT":
                    stop();
                    break;

                default:
                    break;
            }
        } else {
            switch (name) {
                case "ADD_SCORE_LEFT":
                    addScoreLeft();
                    break;

                case "ADD_SCORE_RIGHT":
                    addScoreRight();
                    break;

                case "CHANGE_PLAYERS":
                    changePlayers();
                    break;

                case "BACK_OR_QUIT":
                    showMenu();
                    break;

                default:
                    break;
            }
        }
    }
}