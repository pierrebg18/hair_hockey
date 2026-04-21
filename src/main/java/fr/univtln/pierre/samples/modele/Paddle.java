package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paddle {
    private float width;
    private float baseWidth;
    private float lenght;
    private float thickness;
    private ColorRGBA color;
    private Vector3f position;
    private RigidBodyControl paddle_phy;

    private float bonusTimer = 0f;
    private float widthScale = 1f;
    private Geometry paddleGeometry;

    public Paddle(float width, float length, float thickness, ColorRGBA color) {
        this.width = width;
        this.baseWidth = width;
        this.lenght = length;
        this.thickness = thickness;
        this.color = color;

    }

    public Geometry createGeometryMy(Table table){
        Box box = new Box(width, thickness, lenght);
        Geometry paddle = new Geometry("Box", box);
        position = table.getPosition().add(table.getWidth()/2, table.getThickness()+thickness, table.getLenght()-lenght);
        paddle.setLocalTranslation(position);

        this.paddleGeometry = paddle;
        return paddle;
    }

    public Geometry createGeometryOpponent(Table table){
        Box box = new Box(width, thickness, lenght);
        Geometry paddle = new Geometry("Box", box);
        position = table.getPosition().add(-table.getWidth()/2, table.getThickness()+thickness, -table.getLenght()+lenght);
        paddle.setLocalTranslation(position);

        this.paddleGeometry = paddle;
        return paddle;
    }

    public void createPhysic(Geometry paddle_geo, BulletAppState bulletAppState){
        this.paddle_phy = new RigidBodyControl(500f);
        paddle_geo.addControl(paddle_phy);
        paddle_phy.setAngularFactor(0f); // to block rotation
        bulletAppState.getPhysicsSpace().add(paddle_phy);
    }


    public Vector3f getposition(){
        return this.position;
    }


    public void setposition(Vector3f vector3f){
        this.position=vector3f;
    }


    // méthodes pour gérer les bonus

    public void applyTemporaryWidthScale(float scale, float duration) {
        this.widthScale = scale;
        this.bonusTimer = duration;
        updateScale();
    }

    public void updateBonus(float tpf) {
        if (bonusTimer > 0f) {
            bonusTimer -= tpf;
            if (bonusTimer <= 0f) {
                widthScale = 1f;
                bonusTimer = 0f;
                updateScale();
            }
        }
    }

    private void updateScale() {
        this.width = baseWidth * widthScale;

        if (paddleGeometry != null) {
            System.out.println("paddleGeometry: " + paddleGeometry);
            paddleGeometry.setLocalScale(widthScale, 1f, 1f);
        }

        System.out.println("BONUS PADDLE APPLIQUE, scale = " + widthScale);
    }



}
