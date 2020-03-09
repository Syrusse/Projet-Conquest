package fr.umontpellier.iut.conquest.strategies;

import fr.umontpellier.iut.conquest.Board;
import fr.umontpellier.iut.conquest.Move;
import fr.umontpellier.iut.conquest.Player;

import java.util.List;
import java.util.Random;

public class Naive implements Strategy {
    @Override
    public Move getMove(Board board, Player player) {
        List<Move> listOfMovesPossible = board.getValidMoves(player);

        int nombreAleatoire = new Random().nextInt(listOfMovesPossible.size());

        return listOfMovesPossible.remove(nombreAleatoire);
    }
}