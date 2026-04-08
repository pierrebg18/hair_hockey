package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Cylinder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Puck {
    private int axisSamples; // Number of triangle samples along the axis
    private int radialSamples; // Number of triangle samples along the radial.
    private float radius;
    private float height;
    private Vector3f position;
    private ColorRGBA color;
    private Table table;
    private RigidBodyControl puck_phy;

    public Puck(int axisSamples, int radialSamples, float radius, float height, ColorRGBA color, Table table) {
        this.axisSamples = axisSamples;
        this.radialSamples = radialSamples;
        this.radius = radius;
        this.height = height;
        this.color = color;
        this.table = table;
        this.position = table.getPosition().add(0, 2*height, 0);
    }

    public Geometry createGeometry(){
        // closed - true to create a cylinder with top and bottom surface
        Cylinder cylinder = new Cylinder(radialSamples, axisSamples, radius, height, true);
        Geometry puck = new Geometry("Cylinder", cylinder);
        puck.setLocalTranslation(position.addLocal(0F, -height, 0F));
        Quaternion roll90x = new Quaternion();
        //rotate the cylinder to be vertical
        roll90x.fromAngleAxis(FastMath.PI/2, new Vector3f(1,0,0));
        puck.setLocalRotation(roll90x);
        return puck;
    }

    public void createPhysic(Geometry puck_geo, BulletAppState bulletAppState){
        puck_phy = new RigidBodyControl( 50f);
        puck_geo.addControl(puck_phy);
        puck_phy.setRestitution(5f);
        puck_phy.setFriction(0f);
        bulletAppState.getPhysicsSpace().add(puck_phy);
    }

    public void putOnMySide(){
        position = table.getPosition().add(0, 2*height, 1.5f);
    }

    public void putOnOpponentSide(){
        position = table.getPosition().add(0, 2*height, -1.5f);
    }

    /*
    public void putOnCenter(){
        this.position = table.getPosition().add(0, 2*height, 0);
    }

     */




    //permet de savoir si un objet a bouge sur l'axe x et z
    public boolean hasMoved(Vector3f lastCoord){
        Vector3f currentCoord = this.puck_phy.getPhysicsLocation();
        return Math.abs(lastCoord.x - currentCoord.x) > 0.001f || Math.abs(lastCoord.z - currentCoord.z) > 0.1f;
    }


    public static void pinPuckHeight( Puck puck, Float puckMaxHeight) {
        Vector3f pos = puck.getPuck_phy().getPhysicsLocation();

        if (pos.y > puckMaxHeight) {
            pos.y = puckMaxHeight;
            puck.getPuck_phy().setPhysicsLocation(pos);

            Vector3f vel = puck.getPuck_phy().getLinearVelocity();
            if (vel.y > 0f) {
                vel.y = 0f;
                puck.getPuck_phy().setLinearVelocity(vel);
            }
        }
    }


    public static void stabilizePuck(Puck puck) {
        Vector3f angularVel = puck.getPuck_phy().getAngularVelocity();

        // on garde éventuellement un peu de rotation sur Y,
        // mais on supprime le basculement sur X et Z
        angularVel.x = 0f;
        angularVel.z = 0f;

        puck.getPuck_phy().setAngularVelocity(angularVel);
    }
}
