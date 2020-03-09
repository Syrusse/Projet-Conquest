package fr.umontpellier.iut.conquest.strategies;

import fr.umontpellier.iut.conquest.Board;
import fr.umontpellier.iut.conquest.Move;
import fr.umontpellier.iut.conquest.Player;

import java.util.List;

public class Minmax implements Strategy {
    private int depth;

    public Minmax(int depth) {
        this.depth = depth;
    }

    @Override
    public Move getMove(Board board, Player player) {
        Move moveBon = null;
        int highscore = -2147483648;

        for (Move aMove : board.getValidMoves(player))
        {
            //on simule tout les fils sur 1 génération
            Board nextBoard = new Board(board.cloneTheField());
            nextBoard.movePawn(aMove);

            int score = minmax(board, depth-1, player, false);

            if (score >= highscore) {
                highscore = score;
                moveBon = aMove;
            }
        }

        return moveBon;
    }

    private int minmax(Board node, int depth, Player player, boolean maximizingPlayer) {
        List<Move> listOfMovesPossible = null;

        if (maximizingPlayer)
            listOfMovesPossible = node.getValidMoves(player);
        else
            listOfMovesPossible = node.getValidMoves(player.getGame().getOtherPlayer(player));

        if (depth == 0 || listOfMovesPossible.isEmpty()) //node is a terminal node, une feuille ...
        {
            return heuristique(node, player);
        }

        if (maximizingPlayer)
        {
            int value = -2147483648; //-2 147 483 648 = minimum value of int

            for (Move aMove : listOfMovesPossible)
            {
                // on simule tout les coups possibles
                Board nextBoard = new Board(node.cloneTheField());
                nextBoard.movePawn(aMove);

                //si la simulation donne une sortie meilleur que celle qu'on a déjà, on garde le coup amenant à cette situation
                value = Math.max(minmax(nextBoard, depth -1, player, false), value);
            }
            return value;
        }
        else //minimizing player
        {
            int value = 2147483647; //2 147 483 647 = maximum value of int

            for (Move aMove : listOfMovesPossible)
            {
                // on simule tout les coups possibles
                Board nextBoard = new Board(node.cloneTheField());
                nextBoard.movePawn(aMove);

                //l'adversaire va probablement jouer son meilleur coup
                //on va chercher SON meilleur coup, car il est intelligent
                value = Math.min(minmax(nextBoard, depth - 1, player, true), value);
            }
            return value;
        }
    }

    /**
     * pré-requis 2 joueurs
     *
     * Evalue la partie pour player.
     *
     * @return les "points" du player
     */
    private int heuristique(Board board, Player player) {
        int nbPawnsPlayer, nbPawnsOpponent;

        nbPawnsPlayer = board.getNbPawns(player);
        nbPawnsOpponent = board.getNbPawns(player.getGame().getOtherPlayer(player));

        return nbPawnsPlayer - nbPawnsOpponent;
    }
}