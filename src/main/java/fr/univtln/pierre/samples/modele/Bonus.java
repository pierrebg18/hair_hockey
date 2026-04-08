package fr.univtln.pierre.samples.modele;

import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import lombok.Getter;

import java.util.Random;
import java.util.random.RandomGenerator;

@Getter
public class Bonus {
    private float width;
    private float lenght;
    private float height;
    private ColorRGBA color;
    private Vector3f position;
    private CollisionShape bonusShape;
    private BonusType bonusType;
    //private RigidBodyControl bonus_phy;

    private Paddle myPaddle;
    private Paddle opponentPaddle;
    private Puck puck;
    private Table table;

    private static ColorRGBA colorPlus = ColorRGBA.Green;
    private static ColorRGBA speedUp = ColorRGBA.Yellow;
    private static ColorRGBA colorMinus = ColorRGBA.Red;
    private static Random randomGenerator = new Random();

    public Bonus(float width, BonusType bonusType, Paddle myPaddle, Paddle opponentPaddle, Puck puck, Table table) {
        this.width = width;
        this.lenght = width;
        this.height = width;
        this.bonusShape = new BoxCollisionShape(new Vector3f(width, lenght, height));
        this.bonusType = bonusType;
        this.defineColor();
        this.myPaddle = myPaddle;
        this.opponentPaddle = opponentPaddle;
        this.puck = puck;
        this.table = table;
        this.generatePosition();
    }

    // to adapt
    public void defineColor(){
        switch(bonusType){
            case SIZE_PLUS:
                case PADDLE_PLUS:
                color = colorPlus;
                break;
                case  SIZE_MINUS:
                case PADDLE_MINUS:
            case SHOT_ON_GOAL:
                color = colorMinus;
                break;
                case SPEED_PLUS:
                    color = speedUp;
        }
    }

    // to adapt
    public void generatePosition(){
        /*From documentation :
        nextFloat returns the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from this
        random number generator's sequence.
        */
        float x = randomGenerator.nextFloat();
        // if x < 0.5, we'll put bonus on positive x value, and negative if not
        if (x < 0.5)
            x = x*table.getWidth();
        else
            x = -x/2*table.getWidth();
        float y = table.getPosition().y + table.getThickness() + height;
        float z = randomGenerator.nextFloat();
        // if z < 0.5, we'll put bonus on positive z value, and negative if not
        if (z < 0.5)
            z = z*table.getWidth();
        else
            z = -z/2*table.getWidth();
        position = new Vector3f(x,y,z);
    }

    public Geometry createGeometry(){
        Box box = new Box(width, height, lenght);
        Geometry bonus = new Geometry("Box", box);
        bonus.setLocalTranslation(position);
        CollisionShape collisionShape = new BoxCollisionShape(new Vector3f(width, height, lenght));
        return bonus;
    }
}
