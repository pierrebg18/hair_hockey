package fr.univtln.pierre.samples.ui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;

import fr.univtln.pierre.samples.App;

public class WinHudUI {

    
    private final App app;
    private final BitmapFont font;

    public WinHudUI(App app) {
        this.app = app;
        this.font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
    }

    public void show() {
        int width = app.getScreenWidth();
        int height = app.getScreenHeight();

        BitmapText title = new BitmapText(font);
        title.setSize(font.getCharSet().getRenderedSize() * 2.8f);
        title.setText("You Win");
        title.setColor(ColorRGBA.Green);
        title.setLocalTranslation(width / 2f - title.getLineWidth() / 2f, height - 80f, 0);
        app.getGuiNode().attachChild(title);

    }
}
