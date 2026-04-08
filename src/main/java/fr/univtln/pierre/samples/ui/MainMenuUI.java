package fr.univtln.pierre.samples.ui;

import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import fr.univtln.pierre.samples.App;

public class MainMenuUI {

    private final App app;
    private final BitmapFont font;

    public MainMenuUI(App app) {
        this.app = app;
        this.font = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
    }

    public void show(String player1, String player2, int selectedIndex) {
        int width = app.getScreenWidth();
        int height = app.getScreenHeight();

        BitmapText title = new BitmapText(font);
        title.setSize(font.getCharSet().getRenderedSize() * 2.8f);
        title.setText("HAIR HOCKEY");
        title.setColor(ColorRGBA.Cyan);
        title.setLocalTranslation(width / 2f - title.getLineWidth() / 2f, height - 80f, 0);
        app.getGuiNode().attachChild(title);

        BitmapText subtitle = new BitmapText(font);
        subtitle.setSize(font.getCharSet().getRenderedSize() * 1.1f);
        subtitle.setText("Menu principal");
        subtitle.setColor(ColorRGBA.White);
        subtitle.setLocalTranslation(width / 2f - subtitle.getLineWidth() / 2f, height - 130f, 0);
        app.getGuiNode().attachChild(subtitle);

        BitmapText intro = new BitmapText(font);
        intro.setSize(font.getCharSet().getRenderedSize());
        intro.setText("Utilise HAUT / BAS puis ENTREE");
        intro.setColor(ColorRGBA.LightGray);
        intro.setLocalTranslation(width / 2f - intro.getLineWidth() / 2f, height - 190f, 0);
        app.getGuiNode().attachChild(intro);

        BitmapText users = new BitmapText(font);
        users.setSize(font.getCharSet().getRenderedSize());
        users.setText("MODE : " + player1 + " VS " + player2);
        users.setColor(ColorRGBA.Orange);
        users.setLocalTranslation(width / 2f - users.getLineWidth() / 2f, height - 240f, 0);
        app.getGuiNode().attachChild(users);

        createMenuItem("[ JOUER ]", 0, selectedIndex, height - 340f);
        createMenuItem("[ CHANGER MODE ]", 1, selectedIndex, height - 400f);
        createMenuItem("[ QUITTER ]", 2, selectedIndex, height - 460f);

        BitmapText footer = new BitmapText(font);
        footer.setSize(font.getCharSet().getRenderedSize() * 0.95f);
        footer.setText("ECHAP = quitter | U = changer rapidement");
        footer.setColor(ColorRGBA.Gray);
        footer.setLocalTranslation(width / 2f - footer.getLineWidth() / 2f, 70f, 0);
        app.getGuiNode().attachChild(footer);
    }

    private void createMenuItem(String text, int index, int selectedIndex, float y) {
        BitmapText item = new BitmapText(font);
        item.setSize(font.getCharSet().getRenderedSize() * 1.4f);

        if (index == selectedIndex) {
            item.setText("> " + text + " <");
            item.setColor(ColorRGBA.Yellow);
        } else {
            item.setText(text);
            item.setColor(ColorRGBA.White);
        }

        item.setLocalTranslation(
                app.getScreenWidth() / 2f - item.getLineWidth() / 2f,
                y,
                0
        );

        app.getGuiNode().attachChild(item);
    }
}