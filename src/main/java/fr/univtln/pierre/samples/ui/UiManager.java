package fr.univtln.pierre.samples.ui;

import fr.univtln.pierre.samples.App;
import fr.univtln.pierre.samples.game.Ia;
import fr.univtln.pierre.samples.game.Move;
import fr.univtln.pierre.samples.game.Rule;
import fr.univtln.pierre.samples.game.Tournament;

public class UiManager {

    private final App app;
    private final Ia ia;
    private final Move move;

    //le final pour plus de sécurité
    private final MainMenuUI mainMenuUI;
    private final GameHudUI gameHudUI;
    private final WinHudUI winHudUI;
    //private final LoseHudUI loseHudUI;

    private int selectedMenuIndex = 0;
    private String player1Name = "Joueur 1";
    private String player2Name = "Joueur 2";
    private boolean menuVisible = true;
    private boolean multiplayerMode = true;

    public UiManager(App app, Ia ia, Move move) {
        this.app = app;
        this.ia = ia;
        this.move = move;
        this.mainMenuUI = new MainMenuUI(app);
        this.gameHudUI = new GameHudUI(app);
        this.winHudUI = new WinHudUI(app);
        //this.loseHudUI = new LoseHudUI(app);
    }

    public boolean isMultiplayerMode() {
        return multiplayerMode;
    }

    public void showMenu() {
        app.getFlyByCamera().setEnabled(false);
        app.getGuiNode().detachAllChildren();
        menuVisible = true;
        mainMenuUI.show(player1Name, player2Name, selectedMenuIndex);
    }

    public void showHud() {
        app.getGuiNode().detachAllChildren();
        menuVisible = false;
        gameHudUI.show(player1Name, player2Name, Rule.player1Count, Rule.player2Count);
    }

    public void startGame() {
        app.getFlyByCamera().setEnabled(true);
        Rule.player1Count = 0;
        Rule.player2Count = 0;
        showHud();
    }

    public void changeMode() {
        if (player1Name.equals("Joueur 1")) {
            player1Name = "Joueur";
            player2Name = "BOT";
            multiplayerMode = false;
            ia.niveauIa(1);
        } else {
            player1Name = "Joueur 1";
            player2Name = "Joueur 2";
            multiplayerMode = true;
        }

        if (menuVisible) {
            showMenu();
        } else {
            showHud();
        }
    }

    public void updateScore() {
        if (!menuVisible) {
            showHud();
            if (!multiplayerMode) {
                if (Rule.player1Count == 1) {
                    winHudUI.show();
                    Tournament.addLevel();
                    Tournament.updateLevel(ia, move);
                    if (Tournament.getLevel() == 6) {
                        winHudUI.show();
                    }
                    Rule.player1Count = 0;
                    Rule.player2Count = 0;
                } else if (Rule.player2Count == 12) {
                    System.out.println("You lose");
                    app.stop();
                }
            }
        }
    }

    public void onAction(String name) {
        if (menuVisible) {
            switch (name) {
                case "MENU_UP":
                    selectedMenuIndex--;
                    if (selectedMenuIndex < 0) {
                        selectedMenuIndex = 2;
                    }
                    showMenu();
                    break;

                case "MENU_DOWN":
                    selectedMenuIndex++;
                    if (selectedMenuIndex > 2) {
                        selectedMenuIndex = 0;
                    }
                    showMenu();
                    break;

                case "SELECT":
                    executeSelectedMenu();
                    break;

                case "CHANGE_PLAYERS":
                    selectedMenuIndex = 1;
                    changeMode();
                    break;

                case "BACK_OR_QUIT":
                    app.stop();
                    break;

                default:
                    break;
            }
        } else {
            switch (name) {
                case "CHANGE_PLAYERS":
                    changeMode();
                    break;

                case "BACK_OR_QUIT":
                    showMenu();
                    break;

                default:
                    break;
            }
        }
    }

    private void executeSelectedMenu() {
        switch (selectedMenuIndex) {
            case 0:
                startGame();
                break;
            case 1:
                changeMode();
                break;
            case 2:
                app.stop();
                break;
            default:
                break;
        }
    }
}