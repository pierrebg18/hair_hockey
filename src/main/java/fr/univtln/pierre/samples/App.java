package fr.univtln.pierre.samples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.system.AppSettings;
import com.jme3.input.controls.ActionListener;

import fr.univtln.pierre.samples.game.Move;
import fr.univtln.pierre.samples.game.Rule;
import fr.univtln.pierre.samples.ui.UiManager;
import lombok.Getter;
import lombok.Setter;
import fr.univtln.pierre.samples.game.GameScene;


@Getter
@Setter
public class App extends SimpleApplication implements ActionListener {
    
    ////////
    //Definition des attribut
    //////

    private BulletAppState bulletAppState;
    private UiManager uiManager;
    private GameScene gameScene;
    private int gameOver;
    private Camera cameraController;
    private Inputbinding inputbinding;

    /* 
    Lance le jeu
    */



    public static void main(String[] args) {
        App app = new App();
        AppSettings settings = new AppSettings(true);
        settings.setResolution(1280, 720);
        settings.setTitle("Hair Hockey");
        app.setSettings(settings);
        app.start();
    }


    /*
    Block Initialisation
    */
    @Override
    public void simpleInitApp() {
        cameraController = new Camera(cam);
        cameraController.placeCameraMySide();
        //flyCam.setEnabled(false);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        gameScene = new GameScene(assetManager, bulletAppState, rootNode , inputManager);
        gameScene.init();
        uiManager = new UiManager(this);
        inputbinding = new Inputbinding(inputManager, this);
        inputbinding.menuBindings();
        uiManager.showMenu();
        
    }






    /*
    Réactualise à chaque frame
     */
    @Override
    public void simpleUpdate(float tpf) {
        gameOver=gameScene.update(tpf);
        if(gameOver<0){
            uiManager.showLoseHud();
        }
        else if(gameOver>0){
            uiManager.showWinHud();
        }
        else{
            // UI
            //uiManager.isMultiplayerMode();
            uiManager.refreshHud(Rule.player1Count,Rule.player2Count);
        }
    }
    



    
    public int getScreenWidth() {
        return cam.getWidth();
    }

    public int getScreenHeight() {
        return cam.getHeight();
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public void setGameScene(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    public void onAction(String name, boolean isPressed, float tpf) {
        if (!isPressed) {
            return;
        }
        uiManager.onAction(name);
    }
}
