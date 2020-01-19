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

        //Position p1 = map.personsPositions[0];
        //Position p2 = new Position(8, 9);

        //Person h1 = new Person(dep, con, p2);
        //Person h2 = new Person(dep, con, p1);
        
        Thread[] t = new Thread[map.personsPositions.length];

        int i = 0;
        for (Position position : map.personsPositions) {
            t[i] = new Thread(new Person(dep, con, position));
            i++;
        }
        
        for (i = 0; i < t.length; i++) {
            t[i].start();
        }
        
        Worker w = new Worker(dep, con);
        new Thread(w).start();

        for (i = 0; i < t.length; i++) {
            t[i].join();
        }
        
        //System.exit(0);

    }
}