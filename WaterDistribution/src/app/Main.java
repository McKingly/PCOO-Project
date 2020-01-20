package app;

import pt.ua.gboard.basic.*;

public class Main {
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
        Map map = new Map();
        
        int i = 0;
        SharedDeposit[] dep = new SharedDeposit[map.depositsPositions.length];

        for (Position position : map.depositsPositions ){
            dep[i] = new SharedDeposit(new Deposit(7, i, position, map));
            i++;
        }

        SharedConsole con = new SharedConsole(new Console(map.consolesPositions[0],map));
        
        Thread[] t = new Thread[map.personsPositions.length];

        i = 0;
        for (Position position : map.personsPositions) {
            t[i] = new Thread(new Person(dep[1], con, position));
            i++;
        }
        
        for (i = 0; i < t.length; i++) {
            t[i].start();
        }
        
        Worker w = new Worker(dep[0], con);
        new Thread(w).start();

        for (i = 0; i < t.length; i++) {
            t[i].join();
        }
        
        //System.exit(0);

    }
}