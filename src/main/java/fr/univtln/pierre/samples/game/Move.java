package fr.univtln.pierre.samples.game;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

import fr.univtln.pierre.samples.modele.Paddle;

import com.jme3.input.controls.ActionListener;


public class Move implements ActionListener{
        /** On remplace certains raccourcis de navigation ici, afin de pouvoir
     * ajouter la marche et le saut contrôlés par la physique : */
    private boolean left = false, right = false, up = false, down = false;
    private Paddle paddle;
    private Geometry paddle_geo;

    public Move(){
    }

    public void setpaddle(Paddle paddle){
        this.paddle=paddle;
    }

    public void setpaddlegeo(Geometry paddle_geo){
        this.paddle_geo=paddle_geo;
    }

    public void setUpKeys(InputManager inputManager) {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
    }


    /** Voici nos actions personnalisées déclenchées par des pressions de touches.
     * On ne marche pas encore, on garde juste la direction pressée par l'utilisateur. */
    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("Left")) {
            left = isPressed;
        } else if (binding.equals("Right")) {
            right= isPressed;
        } else if (binding.equals("Up")) {
            up = isPressed;
        } else if (binding.equals("Down")) {
            down = isPressed;
        }
    }

    /**
     * C'est la boucle principale des événements -- la marche se fait ici.
     * On vérifie dans quelle direction le joueur marche en interprétant
     * la direction avant de la caméra (camDir) et sur le côté (camLeft).
     * La commande setWalkDirection() permet à un joueur contrôlé par la physique de marcher.
     * On s'assure aussi ici que la caméra se déplace avec le joueur.
     */

    public void simpleUpdateMove(float tpf) {
        Vector3f pos = paddle.getposition();
        float speed=5;
        if (left) {
            Vector3f pos_final = new Vector3f(pos.x,pos.y,pos.z+speed);
            paddle.setposition(pos_final);
            paddle_geo.move(pos_final);
        }
        if (right) {
            Vector3f pos_final = new Vector3f(pos.x,pos.y,pos.z-speed);
            paddle.setposition(pos_final);
            paddle_geo.move(pos_final);
        }
        if (up) {
            Vector3f pos_final = new Vector3f(pos.x+speed,pos.y,pos.z);
            paddle.setposition(pos_final);
            paddle_geo.move(pos_final);
        }
        if (down) {
            Vector3f pos_final = new Vector3f(pos.x-speed,pos.y,pos.z);
            paddle.setposition(pos_final);
            paddle_geo.move(pos_final);
        }
    }
    
}
