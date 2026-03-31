package fr.univtln.pierre.samples.object3d;

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
}
