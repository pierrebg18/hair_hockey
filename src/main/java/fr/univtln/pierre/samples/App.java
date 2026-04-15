package fr.univtln.pierre.samples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

import fr.univtln.pierre.samples.game.Rule;
import fr.univtln.pierre.samples.modele.*;
import fr.univtln.pierre.samples.ui.UiManager;
import lombok.Getter;
import lombok.Setter;
import fr.univtln.pierre.samples.game.GameScene;
import fr.univtln.pierre.samples.game.Ia;
import fr.univtln.pierre.samples.game.Move;


@Getter
@Setter
public class App extends SimpleApplication implements ActionListener {
    
    ////////
    //Definition des attribut
    //////

    private BulletAppState bulletAppState;
    //private InputManager inputManager;
    private UiManager uiManager;
    private GameScene gameScene;

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
        placeCameraMySide();
        //flyCam.setEnabled(false);
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        gameScene = new GameScene(assetManager, bulletAppState, rootNode , inputManager);
        gameScene.init();
        uiManager = new UiManager(this);
        initKeys();
        uiManager.showMenu();
        
    }





    /*
    Gestion des caméras
    */
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








    /*
    Réactualise a chaque frame
     */
    @Override
    public void simpleUpdate(float tpf) {
        gameScene.update(tpf);
        // UI
        //uiManager.isMultiplayerMode();
        uiManager.refreshHud(Rule.player1Count,Rule.player2Count);
    }
    




    /* 
    Bind touche pour le menu
    */
    private void initKeys() {
        inputManager.addMapping("MENU_UP", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("MENU_DOWN", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("SELECT", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addMapping("CHANGE_PLAYERS", new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("BACK_OR_QUIT", new KeyTrigger(KeyInput.KEY_ESCAPE));

        inputManager.addListener(this,
                "MENU_UP",
                "MENU_DOWN",
                "SELECT",
                "CHANGE_PLAYERS",
                "BACK_OR_QUIT");
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
        uiManager.onAction(name);
    }
}
