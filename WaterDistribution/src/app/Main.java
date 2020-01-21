package app;

import java.util.LinkedList;

import configuration.Configuration;
import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Event;
import pt.ua.concurrent.MutexCV;
import pt.ua.gboard.basic.*;
import static java.lang.System.*;

public class Main {
    
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        
        Map map = null;
        if(args.length > 0){
            if (!Map.maze.validMapFile(args[0])){
                err.println("ERROR: invalid map file \""+args[0]+"\"");
                err.println("USING DEFAULT MAP");
                map = new Map(Configuration.DEFAULT_MAP);
            }
            map = new Map(args[0]);
        }else{
            out.println("USING DEFAULT MAP");
            map = new Map(Configuration.DEFAULT_MAP);
        }
        int i = 0;
        SharedConsole con = new SharedConsole(new Console(map.consolesPositions[0],map));

        SharedDeposit[] dep = new SharedDeposit[map.depositsPositions.length];
        for (Position position : map.depositsPositions ){
            dep[i] = new SharedDeposit(new Deposit(10, i, position, map));
            i++;
        }
        
        Worker w = new Worker(dep[0], con);
        
        new CThread(w).start();
        
        CThread[] t = new CThread[map.personsPositions.length];

        i = 0;
        for (Position position : map.personsPositions) {
            t[i] = new CThread(new Person(dep[0], con, position));
            t[i].start();
            i++;
        }

        /*
        for (i = 0; i < t.length; i++) {
            t[i].join();
        }
        System.exit(0);
        */
    }
}