package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBase {
    private float width;
    private float lenght;
    private float height;
    private ColorRGBA color;
    private Vector3f position;
    private CollisionShape collisionShape;
    private RigidBodyControl tableBase_phy;
    private Table table;
    private Side oneSide;

    public TableBase(Table table, Side oneSide, float height, ColorRGBA color) {
        this.table = table;
        this.oneSide = oneSide;
        this.width = table.getWidth() + 2*oneSide.getWidth();
        this.lenght = table.getLenght();
        this.height = height;
        this.color = color;
        this.position = new Vector3f(table.getPosition()).add(0, -height-table.getThickness(), 0);
    }

    public Geometry createGeometry(){
        Box box = new Box(width, height, lenght);
        Geometry tableBase = new Geometry("Box", box);
        tableBase.setLocalTranslation(position);
        return tableBase;
    }

    public void createPhysic(Geometry tableBase_geo, BulletAppState bulletAppState){
        collisionShape = CollisionShapeFactory.createMeshShape(tableBase_geo);
        tableBase_phy = new RigidBodyControl(collisionShape, 0f);
        tableBase_geo.addControl(tableBase_phy);
        bulletAppState.getPhysicsSpace().add(tableBase_phy);
    }
}
