package fr.univtln.pierre.samples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import fr.univtln.pierre.samples.modele.Paddle;
import fr.univtln.pierre.samples.modele.Puck;
import fr.univtln.pierre.samples.modele.Side;
import fr.univtln.pierre.samples.modele.Table;

public class App extends SimpleApplication implements ActionListener {

    private BulletAppState bulletAppState;

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

    public App() {
    }

    @Override
    public void simpleInitApp() {
        placeCameraUp();

        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot);

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // table
        Table table = new Table(2F, 4F, 0.1F, ColorRGBA.Blue);
        Material matTable = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matTable.setColor("Color", table.getColor());
        Geometry tableGeometry = table.createGeometry();
        table.createPhysic(tableGeometry, bulletAppState);
        tableGeometry.setMaterial(matTable);

        // left side of the table
        Side leftSide = new Side(table, ColorRGBA.Brown);
        Material matSide = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matSide.setColor("Color", leftSide.getColor());
        Geometry leftSideGeometry = leftSide.createGeometryLeft();
        table.createPhysic(leftSideGeometry, bulletAppState);
        leftSideGeometry.setMaterial(matSide);

        // right side of the table
        Side rightSide = new Side(table, ColorRGBA.Brown);
        Geometry rightSideGeometry = rightSide.createGeometryRight();
        table.createPhysic(rightSideGeometry, bulletAppState);
        rightSideGeometry.setMaterial(matSide);

        // puck
        Puck puck = new Puck(20, 10, 0.4F, 0.2F, ColorRGBA.LightGray, table);
        puck.putOnMySide();
        Material matPuck = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matPuck.setColor("Color", puck.getColor());
        Geometry puckGeometry = puck.createGeometry();
        puck.createPhysic(puckGeometry, bulletAppState);
        puckGeometry.setMaterial(matPuck);

        // my paddle
        Paddle myPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Material matPaddle = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matPaddle.setColor("Color", myPaddle.getColor());
        Geometry paddleGeometry = myPaddle.createGeometryMy(table);
        myPaddle.createPhysic(paddleGeometry, bulletAppState);
        paddleGeometry.setMaterial(matPaddle);

        // opponent's paddle
        Paddle opponentPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Geometry opponentPaddleGeometry = opponentPaddle.createGeometryOpponent(table);
        opponentPaddleGeometry.setMaterial(matPaddle);
        opponentPaddle.createPhysic(opponentPaddleGeometry, bulletAppState);

        pivot.attachChild(tableGeometry);
        pivot.attachChild(leftSideGeometry);
        pivot.attachChild(rightSideGeometry);
        pivot.attachChild(puckGeometry);
        pivot.attachChild(paddleGeometry);
        pivot.attachChild(opponentPaddleGeometry);

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

    public void placeCameraPlayerSide() {
        cam.setLocation(new Vector3f(0, 4f, 9f));
        cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 1));
    }

    public void placeCameraOpponentSide() {
        cam.setLocation(new Vector3f(0, 4f, -9f));
        cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 1));
    }

    public void placeCameraUp() {
        cam.setLocation(new Vector3f(0, 12f, 0f));
        cam.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, -1, 0));
        Quaternion roll90x = new Quaternion();
        roll90x.fromAngleAxis(FastMath.PI / 2, new Vector3f(1, 0, 0));
        cam.setRotation(roll90x);
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

    @Override
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