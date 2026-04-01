package fr.univtln.pierre.samples;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import fr.univtln.pierre.samples.modele.Paddle;
import fr.univtln.pierre.samples.modele.Puck;
import fr.univtln.pierre.samples.modele.Side;
import fr.univtln.pierre.samples.modele.Table;

public class App extends SimpleApplication {

    public static void main(String[] args){
        App app = new App();
        app.start();
    }


    public App(){
    }

    @Override
    public void simpleInitApp() {
        Node pivot = new Node("pivot");
        rootNode.attachChild(pivot); // put this node in the scene

        // table
        Table table =  new Table(2F, 4F, 0.1F, ColorRGBA.Blue);
        Material matTable = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        matTable.setColor("Color", table.getColor());
        Geometry tableGeometry = table.createGeometry();
        tableGeometry.setMaterial(matTable);

        // left side of the table
        Side leftSide = new Side(table, ColorRGBA.Brown);
        Material matSide = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        matSide.setColor("Color", leftSide.getColor());
        Geometry leftSideGeometry = leftSide.createGeometryLeft();
        leftSideGeometry.setMaterial(matSide);

        // right side of the table
        Side rightSide = new Side(table, ColorRGBA.Brown);
        Geometry rightSideGeometry = rightSide.createGeometryRight();
        rightSideGeometry.setMaterial(matSide);

        // puck
        Puck puck = new Puck(20, 10, 0.4F, 0.2F, ColorRGBA.LightGray, table);
        puck.putOnMySide();
        Material matPuck = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        matPuck.setColor("Color", puck.getColor());
        Geometry puckGeometry = puck.createGeometry();
        puckGeometry.setMaterial(matPuck);

        // my paddle
        Paddle myPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Material matPaddle = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        matPaddle.setColor("Color", myPaddle.getColor());
        Geometry paddleGeometry = myPaddle.createGeometryMy(table);
        paddleGeometry.setMaterial(matPaddle);

        // opponent's paddle
        Paddle opponentPaddle = new Paddle(0.4F, 0.2F, 0.1F, ColorRGBA.Gray);
        Geometry opponentPaddleGeometry = opponentPaddle.createGeometryOpponent(table);
        opponentPaddleGeometry.setMaterial(matPaddle);

        pivot.attachChild(tableGeometry);
        pivot.attachChild(leftSideGeometry);
        pivot.attachChild(rightSideGeometry);
        pivot.attachChild(puckGeometry);
        pivot.attachChild(paddleGeometry);
        pivot.attachChild(opponentPaddleGeometry);
    }
}
