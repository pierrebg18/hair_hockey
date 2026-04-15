package fr.univtln.pierre.samples.ui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import fr.univtln.pierre.samples.App;

public class GameHudUI {

    private final App app;
    private final BitmapFont font;

    public GameHudUI(App app) {
        this.app = app;
        this.font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
    }

    public void show(String player1, String player2, int score1, int score2) {
        app.getGuiNode().detachAllChildren();
        int width = app.getScreenWidth();
        int height = app.getScreenHeight();

        BitmapText topTitle = new BitmapText(font);
        topTitle.setSize(font.getCharSet().getRenderedSize() * 1.2f);
        topTitle.setText("MATCH");
        topTitle.setColor(ColorRGBA.White);
        topTitle.setLocalTranslation(width / 2f - topTitle.getLineWidth() / 2f, height - 20f, 0);
        app.getGuiNode().attachChild(topTitle);

        BitmapText leftPlayer = new BitmapText(font);
        leftPlayer.setSize(font.getCharSet().getRenderedSize() * 1.1f);
        leftPlayer.setText(player1);
        leftPlayer.setColor(ColorRGBA.Cyan);
        leftPlayer.setLocalTranslation(30f, height - 70f, 0);
        app.getGuiNode().attachChild(leftPlayer);

        BitmapText rightPlayer = new BitmapText(font);
        rightPlayer.setSize(font.getCharSet().getRenderedSize() * 1.1f);
        rightPlayer.setText(player2);
        rightPlayer.setColor(ColorRGBA.Orange);
        rightPlayer.setLocalTranslation(width - rightPlayer.getLineWidth() - 30f, height - 70f, 0);
        app.getGuiNode().attachChild(rightPlayer);

        BitmapText score = new BitmapText(font);
        score.setSize(font.getCharSet().getRenderedSize() * 2.0f);
        score.setText(score1 + " - " + score2);
        score.setColor(ColorRGBA.Yellow);
        score.setLocalTranslation(width / 2f - score.getLineWidth() / 2f, height - 70f, 0);
        app.getGuiNode().attachChild(score);


    }
}