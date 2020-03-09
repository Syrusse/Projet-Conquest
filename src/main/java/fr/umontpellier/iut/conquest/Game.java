package fr.umontpellier.iut.conquest;

import fr.umontpellier.iut.conquest.strategies.Strategy;

import java.io.InputStream;
import java.util.Scanner;
//TODO run(), initGame(), isFinished(), getWinner(), confirmOrUndoMove()
/**
 * Modélise une partie de Conquest.
 */

public class Game {
    /**
     * Scanner permettant de lire les entrées.
     */
    private static Scanner scan;
    /**
     * Le plateau de jeu.
     */
    private Board board;
    /**
     * Les joueurs.
     */
    private Player[] players = new Player[2];
    /**
     * L'historique
     */
    private Caretaker caretaker = new Caretaker();

    /**
     * Constructeur.
     * Crée un plateau à partir de sa taille (impaire).
     * Crée les joueurs à partir de leur stratégie et leur nom.
     */
    public Game(int size, Strategy strategy1, String name1, Strategy strategy2, String name2) {
        board = new Board(size);
        players[0] = new Player(strategy1, this, name1, 1);
        players[1] = new Player(strategy2, this, name2, 2);
    }

    /**
     * Constructeur pour Test.
     * Prend un plateau prédéfini.
     * Crée les joueurs à partir de leur stratégie et leur nom.
     */
    public Game(Board board, Strategy strategy1, String name1, Strategy strategy2, String name2) {
        this.board = board;
        players[0] = new Player(strategy1, this, name1, 1);
        players[1] = new Player(strategy2, this, name2, 2);
    }

    /**
     * Les méthodes suivantes sont utilisées pour les tests automatiques. Il ne faut pas les utiliser.
     */
    public static Scanner getScan() {
        return scan;
    }

    public static void initInput(InputStream inputStream) {
        scan = new Scanner(inputStream);
    }

    public Player[] getPlayers() {
        return players;
    }

    /**
     * Getter.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Fait tourner une partie.
     *
     * @param hardcore : Si hardcore = 1, les joueurs ne peuvent pas annuler les coups joués.
     *                 Sinon harcdore = 0 et les joueurs ont le droit d'annuler un ou plusieurs coups à chaque fois qu'un coup est joué.
     */
    public void run(int hardcore) {
        // Le joueur qui commence est le premier joueur.
        Player player = players[0];

        // Initialise le jeu.
        initGame();

        // Fait tourner le jeu.
        while (!isFinished()) {

            // Affiche le plateau.
            System.out.println(board);

            // Demande au joueur courant de joueur.
            if (!board.getValidMoves(player).isEmpty()) {
                board.movePawn(player.play());
            }

            // En mode non-hardcore, demande au joueur s'il veut annuler un ou plusieurs coups.
            if (hardcore == 0) {
                //Sauvegarde du board
                Memento memento = new Memento(board.cloneTheField());
                caretaker.addLastMemento(memento);

                player = confirmOrUndoMove(player);
            }
            // Change de joueur.
            player = getOtherPlayer(player);
        }

        // Affiche le plateau final.
        System.out.println(board);

        // Affiche le nom du vainqueur.
        System.out.println("Le vainqueur est " + getWinner().getName() + " !");
    }

    /**
     * Initialise le jeu.
     */
    private void initGame() {
        board.initField(players[0],players[1]);
        caretaker.addLastMemento(new Memento(board.cloneTheField()));
    }

    /**
     * Prends un joueur en entrée et retourne l'autre joueur.
     */
    public Player getOtherPlayer(Player player) { return player.equals(players[0]) ? players[1] : players[0]; }

    /**
     * Teste si la partie est finie.
     * Rappel :
     * - La partie est finie quand il n'y a plus de case libre.
     * - La partie est finie quand l'un des deux joueurs n'a plus de pions.
     */
    public boolean isFinished() {
        int nbCaseP0 = board.getNbPawns(players[0]);
        int nbCaseP1 = board.getNbPawns(players[1]);

        if (nbCaseP0 == 0) return true;
        if (nbCaseP1 == 0) return true;
        if (nbCaseP0 + nbCaseP1 == board.getSize()*board.getSize()) return true;

        return false;
    }

    /**
     * Retourne le joueur qui a gagné la partie.
     * Rappel : Le joueur qui gagne est celui qui possède le plus de pions.
     */
    public Player getWinner() {
        return board.getNbPawns(players[0]) > board.getNbPawns(players[1]) ? players[0] : players[1];
    }

    /**
     * Demande au joueur s'il veut annuler (ou pas) un ou plusieurs coups.
     * <p>
     * Tant que le joueur player le désire et que l'on n'est pas revenu au début de la partie en cours,
     * propose à player de reculer d'un coup en faisant saisir 1, ou 0 sinon.
     * Cette méthode doit donc modifier l'état de l'attribut board.
     *
     * @param player : le joueur courant.
     * @return Player : le joueur dont il est le tour de jouer.
     */
    private Player confirmOrUndoMove(Player player) {


        System.out.println(board.toString());

        if (player.getName() != null) {
            //L'IA va pas se tromper donc il va toujours dire qu'il ne veut pas undo, par conséquent on pose même pas la question
            if (player.getName().equals("Bonaparte") || player.getName().equals("Waterloo")) {
                return player;
            }
        }

        int choix;
        Player playerAretourner = player;

        while (caretaker.size() != 1) {

            System.out.println("Annuler un coup (1) ou non (0) ?");
            choix = scan.nextInt();

            if (choix == 1) {
                //On annule le dernier coup
                caretaker.popMemento().memento2board();
                //On inspecte le dernier element de la pile et il devient le board
                board = new Board(caretaker.peekMemento().memento2board().cloneTheField());
                System.out.println(board.toString());

                //C'est juste pour alterner le player à chaque itération de boucle
                if (player == playerAretourner)
                    playerAretourner = getOtherPlayer(player);
                else playerAretourner = player;

                caretaker.showStack();

            }
            //On stoppe la boucle si le joueur ne veut pas annuler son coup
            if (choix == 0){
                break;
            }

        }

        if (caretaker.size() == 1) {
            System.out.println("Nous sommes revenus au stade intial de la partie");
        }

        return playerAretourner;
    }
}


