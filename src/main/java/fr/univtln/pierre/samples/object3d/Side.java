package fr.univtln.pierre.samples.object3d;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import lombok.Getter;

import java.awt.*;

@Getter
public class Side{
    private float width;
    private float length;
    private float thickness;
    private Table table;
    private ColorRGBA color;

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

}
