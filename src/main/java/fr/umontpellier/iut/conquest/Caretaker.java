package fr.umontpellier.iut.conquest;

import java.util.ArrayDeque;
import java.util.Deque;

public class Caretaker {
    final private Deque<Memento> mementos = new ArrayDeque<>();

    public Memento popMemento() { return mementos.removeLast(); }

    public void addMemento(Memento memento) { mementos.addFirst(memento); }

    public void addLastMemento(Memento memento) {mementos.addLast(memento);}

    public boolean isEmpty() { return mementos.isEmpty(); }

    public int size() { return  mementos.size(); }

    public Memento peekMemento() { return mementos.peekLast();}


    public void showStack() {
        System.out.println("aaaaaaaaaaaaaaaaaaaaa");
        for (Memento memento : mementos){
            System.out.println(memento);
        }
        System.out.println("ooooooooooooooooooooooooooo");
    }

}
