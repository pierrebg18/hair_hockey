package fr.univtln.pierre.samples;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.light.PointLight;
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

import fr.univtln.pierre.samples.modele.Paddle;
import fr.univtln.pierre.samples.modele.Puck;
import fr.univtln.pierre.samples.modele.Side;
import fr.univtln.pierre.samples.modele.Table;
import fr.univtln.pierre.samples.modele.LightManager;
public class App extends SimpleApplication {

    private BulletAppState bulletAppState;

    public static void main(String[] args){
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
        placeCameraOpponentSide();
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene

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
        Material matTable = LightManager.createMaterial(assetManager,ColorRGBA.Blue);
        Geometry tableGeometry = table.createGeometry();
        table.createPhysic(tableGeometry,bulletAppState);
        tableGeometry.setMaterial(matTable);

        // left side of the table
        Side leftSide = new Side(table, ColorRGBA.Brown);
        Material matSide = LightManager.createMaterial(assetManager,leftSide.getColor());
        Geometry leftSideGeometry = leftSide.createGeometryLeft();
        table.createPhysic(leftSideGeometry,bulletAppState);
        leftSideGeometry.setMaterial(matSide);

        // right side of the table
        Side rightSide = new Side(table, ColorRGBA.Brown);
        Geometry rightSideGeometry = rightSide.createGeometryRight();
        table.createPhysic(rightSideGeometry,bulletAppState);
        rightSideGeometry.setMaterial(matSide);

        // puck
        Puck puck = new Puck(20, 10, 0.4F, 0.2F, ColorRGBA.LightGray, table);
        puck.putOnMySide();
        Material matPuck = LightManager.createMaterial(assetManager,puck.getColor());
        Geometry puckGeometry = puck.createGeometry();
        puck.createPhysic(puckGeometry, bulletAppState);
        puckGeometry.setMaterial(matPuck);

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

        pivot.attachChild(tableGeometry);
        pivot.attachChild(leftSideGeometry);
        pivot.attachChild(rightSideGeometry);
        pivot.attachChild(puckGeometry);
        pivot.attachChild(paddleGeometry);
        pivot.attachChild(opponentPaddleGeometry);
        pivot.attachChild(me);
        pivot.attachChild(opponent);
    }

    public void placeCameraPlayerSide(){
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

}
