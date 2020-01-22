package app;

import configuration.Configuration;
import pt.ua.concurrent.CThread;
import pt.ua.gboard.basic.*;
import static java.lang.System.*;

import java.util.Random;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Map map = null;
        if (args.length > 0) {
            if (!Map.maze.validMapFile(args[0])) {
                err.println("ERROR: invalid map file \"" + args[0] + "\"");
                err.println("Using Default Map and Update Speed");
                map = new Map(Configuration.DEFAULT_MAP,Configuration.MAP_UPDATE_SPEED);
            }
            else if (args.length == 1)
                map = new Map(args[0],Configuration.MAP_UPDATE_SPEED);
            else
                map = new Map(args[0],Integer.parseInt(args[1]));
        } else {
            out.println("Using Default Map and Update Speed");
            map = new Map(Configuration.DEFAULT_MAP, Configuration.MAP_UPDATE_SPEED);
        }

        Random rand = new Random();

        int i = 0;
        SharedAlertConsole con = new SharedAlertConsole(new AlertConsole(map.consolesPositions[0], map));

        SharedDeposit[] dep = new SharedDeposit[map.depositsPositions.length];
        for (Position position : map.depositsPositions) {
            dep[i] = new SharedDeposit(new Deposit(10, i, position, map));
            i++;
        }

        new CThread(new Worker(0,dep, con)).start();
        new CThread(new Worker(1,dep, con)).start();
        
        CThread[] t = new CThread[map.personsPositions.length];

        i = 0;
        for (Position position : map.personsPositions) {
            t[i] = new CThread(new Person(dep[rand.nextInt(map.depositsPositions.length)], con, position));
            t[i].start();
            i++;
        }

        /*
         * for (i = 0; i < t.length; i++) { t[i].join(); } System.exit(0);
         */
    }
}