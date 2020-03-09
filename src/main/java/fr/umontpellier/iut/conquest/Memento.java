package fr.umontpellier.iut.conquest;

import java.util.Scanner;

public class Memento {
    private Pawn[][] field;

    public Memento(Pawn[][] field) {
        this.field = field;
    }

    public Pawn[][] getField() {
        return field;
    }
    public int getSize() {
        return field.length;
    }

    /**
     * Affiche le plateau.
     */
    public String toString() {
        return new Board(field).toString();
    }

    /**
     * Transforme un memento en board, si ca vous plait pas suffit de supprimer Memento >_<
     * @return un board basé sur le field de memento
     */
    public Board memento2board() {
        return new Board(field);
    }
}
