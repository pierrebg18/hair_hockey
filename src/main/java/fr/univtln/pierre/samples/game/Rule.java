package fr.univtln.pierre.samples.game;

import com.jme3.math.Vector3f;
import fr.univtln.pierre.samples.modele.Puck;

public class Rule {
    public static int player1Count=0;
    public static int player2Count=0;
    

    public static void endRound(Puck puck, Vector3f puckStartPosition){
        if (puck.getPuck_phy().getPhysicsLocation().y < -0.5f) {
            if(puck.getPuck_phy().getPhysicsLocation().z < 1f){
                player1Count +=1;
                System.out.println("le joueur 1 à : "+player1Count+"pts");
            } else {
                player2Count +=1;
                System.out.println("le joueur 2 à : "+player2Count+"pts");
            }
            resetPuck(puck, puckStartPosition);
        }
    }


    private static void resetPuck(Puck puck, Vector3f puckStartPosition) {
        puck.getPuck_phy().setLinearVelocity(Vector3f.ZERO);
        puck.getPuck_phy().setAngularVelocity(Vector3f.ZERO);
        puck.getPuck_phy().clearForces();

        puck.putOnMySide();
        puck.getPuck_phy().setPhysicsLocation(puck.getPosition().clone());
    }
}
