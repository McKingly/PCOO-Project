package app;

import pt.ua.gboard.basic.*;

public class Main {
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
        Map map = new Map();
        //map.board.terminate();
        //map.drawMap();

        Position start = map.depositsPositions[0];

        SharedDeposit dep = new SharedDeposit(new Deposit(24, start.line(), start.column(), map));
        SharedConsole con = new SharedConsole(new Console());

        Position p1 = map.personsPositions[0];
        Position p2 = new Position(8, 9);

        //Person h1 = new Person(dep, con, p2);
        //Person h2 = new Person(dep, con, p1);
        
        int size = 1;

        Thread[] t = new Thread[size];

        for (int i = 0; i < size; i++) {
            t[i] = new Thread(new Person(dep, con, p1));
            //t[i+1] = new Thread(new Person(dep, con, p2));
        }
        
        for (int i = 0; i < size; i++) {
            t[i].start();
        }
        
        Worker w = new Worker(dep, con);
        new Thread(w).start();

        for (int i = 0; i < size; i++) {
            t[i].join();
        }
        
        //System.exit(0);

    }
}