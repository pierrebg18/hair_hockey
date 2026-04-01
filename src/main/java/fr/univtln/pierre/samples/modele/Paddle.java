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
    private float lenght;
    private float thickness;
    private ColorRGBA color;
    private Vector3f position;

    public Paddle(float width, float length, float thickness, ColorRGBA color) {
        this.width = width;
        this.lenght = length;
        this.thickness = thickness;
        this.color = color;
    }

    public Geometry createGeometryMy(Table table){
        Box box = new Box(width, thickness, lenght);
        Geometry paddle = new Geometry("Box", box);
        position = table.getPosition().add(table.getWidth()/2, table.getThickness()+thickness, table.getLenght()-lenght);
        paddle.setLocalTranslation(position);
        return paddle;
    }

    public Geometry createGeometryOpponent(Table table){
        Box box = new Box(width, thickness, lenght);
        Geometry paddle = new Geometry("Box", box);
        position = table.getPosition().add(-table.getWidth()/2, table.getThickness()+thickness, -table.getLenght()+lenght);
        paddle.setLocalTranslation(position);
        return paddle;
    }

    public void createPhysic(Geometry paddle_geo,BulletAppState bulletAppState){
        RigidBodyControl paddle_phy = new RigidBodyControl(5.0f);
        paddle_geo.addControl(paddle_phy);
        bulletAppState.getPhysicsSpace().add(paddle_phy);
    }


    public Vector3f getposition(){
        return this.position;
    }

    public void setposition(Vector3f vector3f){
        this.position=vector3f;
    }

}
