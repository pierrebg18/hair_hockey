package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import lombok.Getter;

@Getter
public class Table {
    private float width;
    private float lenght;
    private float thickness;
    private ColorRGBA color;
    private Vector3f position = new Vector3f(0, 0, 0);

    public Table(float width, float length, float thickness, ColorRGBA color) {
        this.width = width;
        this.lenght = length;
        this.thickness = thickness;
        this.color = color;
    }

    public Geometry createGeometry(){
        Box box = new Box(width, thickness, lenght);
        Geometry table = new Geometry("Box", box);
        table.setLocalTranslation(position);
        return table;
    }

    public void createPhysic(Geometry table_geo,BulletAppState bulletAppState){
        RigidBodyControl table_phy = new RigidBodyControl(0.0f);
        table_geo.addControl(table_phy);
        bulletAppState.getPhysicsSpace().add(table_phy);
    }
}
