package app;

import pt.ua.gboard.basic.*;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Map map = new Map();

        Position start = new Position(5, 3);

        Deposit dep = new Deposit(30, start.line(), start.column(), map);
        Console con = new Console();

        Position p1 = new Position(2, 9);
        Position p2 = new Position(8, 9);

        Person h2 = new Person(dep, p2);
        new Thread(h2).start();
        
        Person h1 = new Person(dep, p2);
        new Thread(h1).start();
        
        Worker w = new Worker(dep, con);
        // new Thread(w).start();

    }
}