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
    private float speed_x = 0.5f;
    private float speed_z = 3f;
    private float bad_position = 0; // compris entre 0 et 1 sert a rendre l'ia pas parfaite
    private static boolean service = false;
    private static int cmpt=0;

    public static void setService(boolean service) {
        Ia.service = service;
    }

    public static boolean consumeService() {
        if (service) {
            cmpt+=1;
            if (cmpt==20){
                service = false;
                cmpt=0;
            }
            return true;
        }
        return false;
    }

    public void niveauIa(int Niveau){
        if (Niveau==1){
        setSpeed_x(0.5f);
        setSpeed_z(3f);
        setBad_position(0.5f);
        }
        else if(Niveau==2){
        setSpeed_x(1f);
        setSpeed_z(3f);
        setBad_position(0.5f);
        }
        else if(Niveau==3){
        setSpeed_x(1.5f);
        setSpeed_z(3f);
        setBad_position(0.5f);
        }
        else if(Niveau==4){
        setSpeed_x(2f);
        setSpeed_z(5f);
        setBad_position(0.4f);
        }
        else if(Niveau==5){
        setSpeed_x(2.5f);
        setSpeed_z(8f);
        setBad_position(0.35f);
        }
        // pour les tests
        else if(Niveau==66){
            setSpeed_x(0f);
            setSpeed_z(0f);
        }
        else{
            System.out.println("erreur de niveau selectionné");
        }
    }
    public void simpleUpdateIaMove(float tpf, Vector3f lastPos) {

        boolean hasMoved = puck.hasMoved(lastPos);
        boolean launchService = consumeService();
        boolean shouldMove = hasMoved || launchService || back;
        // sait si mouvement du palais en x ou z
        if (shouldMove) {
            
            Vector3f velocity = new Vector3f(0, 0, 0);

            Vector3f paddleCoord = paddle.getPaddle_phy().getPhysicsLocation();
            Vector3f puckCoord = puck.getPuck_phy().getPhysicsLocation();

            //en x

            double r = Math.random();
            if (r < bad_position) {
            // pour le rendre moins parfait
                if (Math.random() < 0.5) {
                    velocity.x += -speed_x; // va vers la gauche
                } else {
                    velocity.x += speed_x;  // va vers la droite
                }
            }
            else if (paddleCoord.x > puckCoord.x) {
                velocity.x -= speed_x;
            } else {
                velocity.x += speed_x;
            }
            
            //en z pour taper
            if (launchService && cmpt==3){
                velocity.z += speed_z;
                back=true;
            }
            if (puckCoord.z <= -1.5f){
                velocity.z += speed_z;
                back=true;
            }
            else if(back){
                velocity.z -= speed_z*2;
                back=false;

            }
            else{
                velocity.z = 0 ;
            }


            paddle.getPaddle_phy().setLinearVelocity(velocity);
            // clone() cree une copie independante de la position physique
            paddle.setposition(paddle.getPaddle_phy().getPhysicsLocation().clone());
        } else {
            paddle.getPaddle_phy().setLinearVelocity(Vector3f.ZERO);
        }

        puck.setPosition(puck.getPuck_phy().getPhysicsLocation().clone());
    }
}
