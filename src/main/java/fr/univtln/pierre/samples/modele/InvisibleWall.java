package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvisibleWall {
    private float width;
    private float thickness;
    private float height;
    private Vector3f position;
    private CollisionShape wallShape;
    private RigidBodyControl wall_phy;
    private Table table;

    public InvisibleWall(Table table) {
        this.table = table;
        this.width = table.getWidth();
        this.thickness = 0.1f;
        this.height = 0.5f;
        this.wallShape = new BoxCollisionShape(new Vector3f(width, height, thickness));
    }

    public void createPhysicCenter(BulletAppState bulletAppState){
        position = table.getPosition().add(0, height, 0);
        wall_phy = new RigidBodyControl(wallShape, 0f);
        wall_phy.setPhysicsLocation(position);
        bulletAppState.getPhysicsSpace().add(wall_phy);
    }

    public void createPhysicMySide(BulletAppState bulletAppState){
        position = table.getPosition().add(0, height, table.getLenght()+2*thickness);
        wall_phy = new RigidBodyControl(wallShape, 0f);
        wall_phy.setPhysicsLocation(position);
        bulletAppState.getPhysicsSpace().add(wall_phy);
    }

    public void createPhysicOpponentSide(BulletAppState bulletAppState){
        position = table.getPosition().add(0, height, -(table.getLenght()+thickness));
        wall_phy = new RigidBodyControl(wallShape, 0f);
        wall_phy.setPhysicsLocation(position);
        bulletAppState.getPhysicsSpace().add(wall_phy);
    }
}
