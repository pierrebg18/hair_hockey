package fr.univtln.pierre.samples.modele;

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
    private Vector3f position = new Vector3f(0, 0.4F, 0);
    private ColorRGBA color;

    public Puck(int axisSamples, int radialSamples, float radius, float height, ColorRGBA color) {
        this.axisSamples = axisSamples;
        this.radialSamples = radialSamples;
        this.radius = radius;
        this.height = height;
        this.color = color;
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

}
