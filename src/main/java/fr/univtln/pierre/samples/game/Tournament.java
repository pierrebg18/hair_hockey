package fr.univtln.pierre.samples.game;


public class Tournament {
    private static int level=1;

    

    public Tournament() {
    }

    public static int getLevel() {
        return level;
    }
    
    public static void addLevel(){
        level+=1;
    }

    public static int updateLevel(Ia ia,Move move){
        if (level==1){
            ia.niveauIa(1);
            move.NiveauJoueur(1);
            System.out.println("Niveau 1");
            //GameScene.displayEnemy1();
            return 1;
        }
        else if (level==2){
            ia.niveauIa(2);
            move.NiveauJoueur(2);
            System.out.println("Niveau 2");
            GameScene.hideEnemy1();
            GameScene.displayEnemy2();
            return 2;
        }
        else if (level==3){
            ia.niveauIa(3);
            move.NiveauJoueur(3);
            System.out.println("Niveau 3");
            GameScene.hideEnemy2();
            GameScene.displayEnemy3();
            return 3;
        }
        else if (level==4){
            ia.niveauIa(4);
            move.NiveauJoueur(4);
            System.out.println("Niveau 4");
            GameScene.hideEnemy3();
            GameScene.displayEnemy4();
            return 4;
        }
        else if (level==5){
            ia.niveauIa(5);
            move.NiveauJoueur(5);
            System.out.println("Niveau 5");
            GameScene.hideEnemy4();
            GameScene.displayEnemy5();
            return 5;
        }
        else if (level==6){
            
            System.out.println("You Win");
            return 6;
            
        }
        else{
            System.out.println("Erreur level");
            return -1;
        }
    }
}
