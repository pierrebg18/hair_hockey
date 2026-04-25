package fr.univtln.pierre.samples;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class Camera {
    private final com.jme3.renderer.Camera camera;

    public Camera(com.jme3.renderer.Camera camera) {
        this.camera = camera;
    }

    public void placeCameraMySide() {
        camera.setLocation(new Vector3f(0, 4f, 9f));
        camera.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 2, -5));
    }

    public void placeCameraOpponentSide() {
        camera.setLocation(new Vector3f(0, 4f, -9f));
        camera.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 1));
    }

    public void placeCameraUp() {
        camera.setLocation(new Vector3f(0, 12f, 0f));
        camera.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, -1, 0));
        Quaternion roll90x = new Quaternion();
        roll90x.fromAngleAxis(FastMath.PI / 2, new Vector3f(1, 0, 0));
        camera.setRotation(roll90x);
    }
}
