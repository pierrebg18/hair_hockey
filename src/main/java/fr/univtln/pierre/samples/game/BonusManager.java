package fr.univtln.pierre.samples.game;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import fr.univtln.pierre.samples.modele.*;

public class BonusManager {

    private final AssetManager assetManager;
    private final Node pivot;
    private final Table table;
    private final Paddle myPaddle;
    private final Paddle opponentPaddle;
    private final Puck puck;
    private final Move move;
    private final Geometry puckGeometry;

    private Bonus currentBonus;
    private Geometry currentBonusGeometry;

    //respawn

    private float respawnTimer = 0f;
    private boolean waitingForRespawn = false;

    public BonusManager(AssetManager assetManager,
                        Node pivot,
                        Table table,
                        Paddle myPaddle,
                        Paddle opponentPaddle,
                        Puck puck,
                        Move move,
                        Geometry puckGeometry) {
        this.assetManager = assetManager;
        this.pivot = pivot;
        this.table = table;
        this.myPaddle = myPaddle;
        this.opponentPaddle = opponentPaddle;
        this.puck = puck;
        this.move = move;
        this.puckGeometry = puckGeometry;
    }

    public void initBonus() {
        spawnRandomBonus();
    }
    public void update(float tpf) {
        if (move.getBonus() == null && currentBonus != null) {
            currentBonus = null;
            currentBonusGeometry = null;
            waitingForRespawn = true;
            respawnTimer = 5f;
        }

        if (waitingForRespawn) {
            respawnTimer -= tpf;

            if (respawnTimer <= 0f) {
                spawnRandomBonus();
                waitingForRespawn = false;
                respawnTimer = 0f;
            }
        }
    }



    private void spawnRandomBonus() {
        BonusType randomType = randomBonusType();

        currentBonus = new Bonus(0.2f, randomType, myPaddle, opponentPaddle, puck, table);

        Material matBonus = LightManager.createMaterial(assetManager, currentBonus.getColor());
        currentBonusGeometry = currentBonus.createGeometry();
        currentBonusGeometry.setMaterial(matBonus);

        pivot.attachChild(currentBonusGeometry);

        move.setBonus(currentBonus);
        move.setBonusGeometry(currentBonusGeometry);
        move.setPivot(pivot);
        move.setPuckShape(puckGeometry);

        System.out.println("Nouveau bonus : " + randomType);
    }

    private BonusType randomBonusType() {
        BonusType[] values = BonusType.values();
        int index = (int) (Math.random() * values.length);
        return values[index];
    }
}