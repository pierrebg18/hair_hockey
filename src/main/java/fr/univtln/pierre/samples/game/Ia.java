package fr.univtln.pierre.samples.game;

import com.jme3.math.Vector3f;

import fr.univtln.pierre.samples.modele.Paddle;
import fr.univtln.pierre.samples.modele.Puck;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ia {
    private Paddle paddle;
    private Puck puck;
    private boolean back=false;
    private float speed_x = 2f;
    private float speed_z = 5f;
    
    public void niveauIa(int Niveau){
        if (Niveau==1){
        setSpeed_x(0.5f);
        setSpeed_z(3f);
        }
        else if(Niveau==2){
        setSpeed_x(1f);
        setSpeed_z(3f);
        }
        else if(Niveau==3){
        setSpeed_x(1.5f);
        setSpeed_z(3f);
        }
        else if(Niveau==4){
        setSpeed_x(2f);
        setSpeed_z(5f);
        }
        else if(Niveau==5){
        setSpeed_x(2.5f);
        setSpeed_z(8f);
        }
        else{
            System.out.println("erreur de niveau selectionné");
        }
    }
    public void simpleUpdateIaMove(float tpf, Vector3f lastPos) {

        boolean hasMoved = puck.hasMoved(lastPos);
        // sait si mouvement du palais en x ou z
        if (hasMoved) {
            
            Vector3f velocity = new Vector3f(0, 0, 0);

            Vector3f paddleCoord = paddle.getPaddle_phy().getPhysicsLocation();
            Vector3f puckCoord = puck.getPuck_phy().getPhysicsLocation();

            if (paddleCoord.x > puckCoord.x) {
                velocity.x -= speed_x;
            } else {
                velocity.x += speed_x;
            }
            
            if (puckCoord.z < -1.5f){
                velocity.z += speed_z;
                back=true;
            }
            else if(back){
                velocity.z -= speed_z;
                back=false;

            }
            else{
                velocity.z = 0 ;
            }


            paddle.getPaddle_phy().setLinearVelocity(velocity);
            // clone() cree une copie independante de la position physique
            paddle.setposition(paddle.getPaddle_phy().getPhysicsLocation().clone());
        }

        puck.setPosition(puck.getPuck_phy().getPhysicsLocation().clone());
    }
}
