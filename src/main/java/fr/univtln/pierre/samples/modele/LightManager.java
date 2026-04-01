package fr.univtln.pierre.samples.modele;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


public class LightManager {



    public static void setUpLight(Node rootNode) {
        // We add light so we see the scene
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }


    public static Material createMaterial(AssetManager assetManager, ColorRGBA color) {
        Material mat = new Material(assetManager,
                "Common/MatDefs/Light/Lighting.j3md");

        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", color);
        mat.setColor("Ambient", color);

        //tentative d'amélioration
        mat.setColor("Specular", ColorRGBA.White);
        mat.setFloat("Shininess", 16f);

        return mat;
    }

    public static void addLight(Node rootNode,Vector3f position, ColorRGBA color, float intensity, float radius) {
        PointLight light = new PointLight();
        light.setColor(color.mult(intensity));
        light.setRadius(radius);
        light.setPosition(position);

        rootNode.addLight(light);
    }
}
