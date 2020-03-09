package fr.umontpellier.iut.conquest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Modélise un plateau.
 */
public class Board {
    /**
     * Tableau des pions.
     */
    private Pawn[][] field;

    /**
     * Constructeur.
     *
     * @param size : la taille du plateau.
     */
    public Board(int size) {
        field = new Pawn[size][size];
    }

    /**
     * Constructeur pour Test.
     *
     * @param field : plateau prédéfini.
     */
    public Board(Pawn[][] field) {
        this.field = field;
    }

    /**
     * Les méthodes suivantes sont utilisées pour les tests automatiques. Il ne faut pas les utiliser.
     */
    public Pawn[][] getField() {
        return field;
    }

    /**
     * Retourne la taille du plateau.
     */
    public int getSize() {
        return field.length;
    }

    /**
     * Affiche le plateau.
     */
    public String toString() {
        int size = field.length;
        StringBuilder b = new StringBuilder();
        for (int r = -1; r < size; r++) {
            for (int c = -1; c < size; c++) {
                if (r == -1 && c == -1) {
                    b.append("_");
                } else if (r == -1) {
                    b.append("_").append(c);
                } else if (c == -1) {
                    b.append(r).append("|");
                } else if (field[r][c] == null) {
                    b.append("_ ");
                } else if (field[r][c].getPlayer().getColor() == 1) {
                    b.append("X ");
                } else {
                    b.append("O ");
                }
            }
            b.append("\n");
        }
        b.append("---");
        return b.toString();
    }

    /**
     * Initialise le plateau avec les pions de départ.
     * Rappel :
     * - player1 commence le jeu avec un pion en haut à gauche (0,0) et un pion en bas à droite.
     * - player2 commence le jeu avec un pion en haut à droite et un pion en bas à gauche.
     */
    public void initField(Player player1, Player player2) {
        field[0][0] = new Pawn(player1);
        field[0][getSize() - 1] = new Pawn(player2);
        field[getSize() - 1][0] = new Pawn(player2);
        field[getSize() - 1][getSize() - 1] = new Pawn(player1);
    }

    /**
     * Vérifie si un coup est valide.
     * Rappel :
     * - Les coordonnées du coup doivent être dans le plateau.
     * - Le pion bougé doit appartenir au joueur.
     * - La case d'arrivée doit être libre.
     * - La distance entre la case d'arrivée est au plus 2.
     */
    public boolean isValid(Move move, Player player) {
        //arrivé et départ non négatifs
        if (move.getColumn1() < 0 || move.getRow2() < 0 || move.getColumn2() < 0 || move.getRow1() < 0)
            return false;

        //arrivé et départ pas trop grands
        if (move.getColumn1() > getSize() - 1 || move.getRow2() > getSize() - 1 || move.getColumn2() > getSize() - 1 || move.getRow1() > getSize() - 1 )
            return false;

        //porté max de 2
        if (Math.abs(move.getColumn2() - move.getColumn1()) > 2 || Math.abs(move.getRow2() - move.getRow1()) > 2)
            return false;

        //case d'arrivé libre
        if (field[move.getRow2()][move.getColumn2()] != null)
            return false;

        //case de départ non null
        if (field[move.getRow1()][move.getColumn1()] == null)
            return false;

        //pion appartient à player
        if(field[move.getRow1()][move.getColumn1()].getPlayer() != player)
            return false;

        return true;
    }

    /**
     * Déplace un pion.
     *
     * @param move : un coup valide.
     *             Rappel :
     *             - Si le pion se déplace à distance 1 alors il se duplique pour remplir la case d'arrivée et la case de départ.
     *             - Si le pion se déplace à distance 2 alors il ne se duplique pas : la case de départ est maintenant vide et la case d'arrivée remplie.
     *             - Dans tous les cas, une fois que le pion est déplacé, tous les pions se trouvant dans les cases adjacentes à sa case d'arrivée prennent sa couleur.
     */
    public void movePawn(Move move) {
        Player player = field[move.getRow1()][move.getColumn1()].getPlayer();

        //déplacement
        if (Math.abs(move.getColumn2() - move.getColumn1()) == 2 || Math.abs(move.getRow2() - move.getRow1()) == 2) {
            field[move.getRow2()][move.getColumn2()] = field[move.getRow1()][move.getColumn1()];
            field[move.getRow1()][move.getColumn1()] = null;
        } else {

            field[move.getRow2()][move.getColumn2()] = new Pawn(player);
        }


        //contamination
        Pawn aPawn;

        //pas tout à droite
        if (move.getColumn2() < getSize() - 1) {
            //on peut regarde vers la droite
            aPawn = field[move.getRow2()][move.getColumn2() + 1];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas tout à gauche
        if (move.getColumn2() > 0) {
            //on peut regarde vers la gauche
            aPawn = field[move.getRow2()][move.getColumn2() - 1];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas tout en bas
        if (move.getRow2() < getSize() - 1) {
            //on peut regarde vers le bas
            aPawn = field[move.getRow2() + 1][move.getColumn2()];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas tout en haut
        if (move.getRow2() > 0) {
            //on peut regarde vers le haut
            aPawn = field[move.getRow2() - 1][move.getColumn2()];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas dans le coin supérieur gauche
        if (move.getColumn2() != 0 && move.getRow2() != 0) {
            //on peut regarde en haut à gauche
            aPawn = field[move.getRow2() - 1][move.getColumn2() - 1];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas dans le coin supérieur droite
        if (move.getColumn2() != getSize() - 1 && move.getRow2() != 0) {
            //on peut regarde en haut à droite
            aPawn = field[move.getRow2() - 1][move.getColumn2() + 1];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas dans le coin inférieur gauche
        if (move.getColumn2() != 0 && move.getRow2() != getSize() - 1) {
            //on peut regarde en bas à gauche
            aPawn = field[move.getRow2() + 1][move.getColumn2() - 1];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }

        //pas dans le coin inférieur droite
        if (move.getColumn2() != getSize() - 1 && move.getRow2() != getSize() - 1) {
            //on peut regarde en bas à droite
            aPawn = field[move.getRow2() + 1][move.getColumn2() + 1];
            if (aPawn != null && aPawn.getPlayer() != player) aPawn.setPlayer(player);
        }
    }

    /**
     * Retourne la liste de tous les coups valides de player.
     * S'il n'y a de coup valide, retourne une liste vide.
     */
    public List<Move> getValidMoves(Player player) {
        LinkedList<Move> listOfMoves = new LinkedList<>();
        Pawn aPawn;
        int Size;
        Size = getSize();

        //parcours du plateau pour trouver tous les pions de player
        for (int numLine = 0; numLine < Size; numLine++) {
            for (int numCol = 0; numCol < Size; numCol++) {

                aPawn = field[numLine][numCol];
                if (aPawn != null) {
                    if (aPawn.getPlayer().getColor() == player.getColor()) { //if the pawn belong to player
                        addValidMovesAux(numCol, numLine, listOfMoves, player);
                    }
                }
            }
        }
        return listOfMoves;
    }

    /**
     * Ajoute à curruntListOfMoves tout les coups valides de Pawn -ajouté pour faire getValidMoves()
     */
    private void addValidMovesAux(int x, int y, List<Move> currentListOfMoves, Player player) {
        Move aMove;

        //on regarde les cases proches autour de la case (x,y)
        for (int i = -2; i <= 2; i++) //i = ligne
        {
            for (int j = -2; j <= 2; j++) //j = colonne
            {
                aMove = new Move(y, x, y-i, x-j);
                if (isValid(aMove, player) == true) currentListOfMoves.add(aMove);
            }
        }
    }

    /**
     * Retourne le nombre de pions d'un joueur.
     */
    public int getNbPawns(Player player) {
        Pawn aPawn;
        int Size, nbPawn;
        Size = getSize();
        nbPawn = 0;

        for (int numLine = 0; numLine < Size; numLine++) {
            for (int numCol = 0; numCol < Size; numCol++) {

                aPawn = field[numLine][numCol];
                if (aPawn != null) {
                    if (aPawn.getPlayer() == player) nbPawn++; //if the pawn belong to player
                }

            }
        }

        return nbPawn;
    }

    /**
     * pré-requis : seulement 2 joueurs
     *
     * Retourne le nombre de pions du joueur adversaire.
     */
    public int getNbPawnsOpponent(Player player) {
        Pawn aPawn;
        int Size, nbPawn;
        Size = getSize();
        nbPawn = 0;

        for (int numLine = 0; numLine < Size; numLine++) {
            for (int numCol = 0; numCol < Size; numCol++) {

                aPawn = field[numLine][numCol];
                if (aPawn != null) {
                    if (aPawn.getPlayer() != player) nbPawn++; //if the pawn belong to player
                }

            }
        }

        return nbPawn;
    }

    public Pawn[][] cloneTheField() {
        Pawn[][] fieldCopy = new Pawn[getSize()][getSize()];
        Pawn aPawn;

        for (int r = 0; r < getSize(); r++)
        {
            for (int c = 0; c < getSize(); c++)
            {
                aPawn = field[r][c];
                if (aPawn != null) fieldCopy[r][c] = new Pawn(aPawn.getPlayer());
            }
        }

        return fieldCopy;
    }
}
