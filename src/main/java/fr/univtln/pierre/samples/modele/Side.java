package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import lombok.Getter;

@Getter
public class Side{
    private float width;
    private float length;
    private float thickness;
    private Table table;
    private ColorRGBA color;
    private CollisionShape sideShape;
    private RigidBodyControl side_phy;

    public Side(Table table, ColorRGBA color) {
        length = table.getLenght();
        thickness = table.getThickness()*3;
        width = thickness;
        this.color = color;
        this.table = table;
    }

    public Geometry createGeometryLeft(){
        Box box = new Box(width, thickness, length);
        Geometry leftSide = new Geometry("Box", box);
        leftSide.setLocalTranslation(table.getPosition().add(-table.getWidth()-width, thickness-table.getThickness(), 0F));
        return leftSide;
    }

    public Geometry createGeometryRight(){
        Box box = new Box(width, thickness, length);
        Geometry rightSide = new Geometry("Box", box);
        rightSide.setLocalTranslation(table.getPosition().add(table.getWidth()+width, thickness-table.getThickness(), 0F));
        return rightSide;
    }

    public void createPhysic(Geometry side_geo, BulletAppState bulletAppState){
        sideShape = CollisionShapeFactory.createMeshShape(side_geo);
        side_phy = new RigidBodyControl(sideShape,0f);
        side_geo.addControl(side_phy);
        bulletAppState.getPhysicsSpace().add(side_phy);
    }

}
