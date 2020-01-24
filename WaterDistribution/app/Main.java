package app;

import configuration.Configuration;
import pt.ua.concurrent.CThread;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.Labyrinth;

import static java.lang.System.*;

public class Main {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Map map = null;
        if (args.length > 0) {
            if (!Labyrinth.validMapFile(args[0])) {
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
        
        SharedAlertConsole con = new SharedAlertConsole(new AlertConsole(map.consolesPositions[0], map));
        
        int i = 0;
        SharedDeposit[] dep = new SharedDeposit[map.depositsPositions.length];
        for (Position position : map.depositsPositions) {
            dep[i] = new SharedDeposit(new Deposit(Configuration.DEPOSIT_MAX_WATER_CAPACITY, i, position, map));
            i++;
        }

        for (int j = 0; j < Configuration.NB_WORKERS; j++) {
            new CThread(new Worker(j,dep, con)).start();
        }
        
        CThread[] t = new CThread[map.housesPositions.length];
        
        i = 0;
        for (Position position : map.housesPositions) {
            t[i] = new CThread(new House(dep, con, position, Configuration.HOUSE_WATER_CONSUMPTION_RATE,
                                                             Configuration.HOUSE_MAX_WATER_CONSUMPTION));
            t[i].start();
            i++;
        }

        

    }
}