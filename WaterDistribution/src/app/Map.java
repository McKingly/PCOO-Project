package app;

import pt.ua.gboard.*;
import pt.ua.gboard.games.*;
import pt.ua.gboard.basic.*;

import java.awt.Color;

public class Map {

    static final char startSymbol = 'D';
    static final char endSymbol = 'H';
    static final char pipeSymbol = 'T';
    static final char pipeWaterSymbol = 't'; 
    static final char biforSymbol = 'B';
    static final char markedPositionSymbol = ' ';
    static final char actualPositionSymbol = 'o';
    static final String map = "src/app/Map.txt";
    static final String water = "src/images/water_drop.png"; 

    static final String hPipe = "src/images/Hpipe.png";
    static final String vPipe = "src/images/Vpipe.png";
    static final String bPipeV1 = "src/images/BifurcatedPipev1.png";
    static final String bPipeV2 = "src/images/BifurcatedPipev2.png";
    static final String bPipeV3 = "src/images/BifurcatedPipev3.png";
    
    static Labyrinth maze;
    
    static GBoard board = new GBoard("Map", 10, 20, 3);

    static Gelem[] gelems = { new StringGelem("" + startSymbol, Color.red), // Deposit symbol
            new StringGelem("" + endSymbol, Color.red), // House symbol
            new ImageGelem(hPipe, new GBoard("pipe", 1, 2, 1), 100.0, 1, 2), // Empty pipe symbol
            new FilledGelem(Color.blue, 75.0, 1, 2), // Pipe water symbol
            new ImageGelem(bPipeV3, new GBoard("pipe", 3, 2, 1), 100.0, 3, 2), // Split pipe symbol
            new StringGelem("" + markedPositionSymbol, Color.blue),
            new StringGelem("" + actualPositionSymbol, Color.blue),

            new FilledGelem(Color.blue, 75.0, 2, 1) };

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        /*
         * LabyrinthGelem.setShowRoadBoundaries();
         * 
         * Labyrinth.setNumberOfLayers(3);
         * 
         * char[] extraSymbols = { startSymbol, endSymbol, pipeSymbol, pipeWaterSymbol,
         * biforSymbol, markedPositionSymbol, actualPositionSymbol };
         * 
         * maze = new Labyrinth(map, extraSymbols, 1, true);
         */
        /*
         * Gelem[] gelems = { new StringGelem("" + startSymbol, Color.red), // Deposit
         * symbol new StringGelem("" + endSymbol, Color.red), // House symbol new
         * ImageGelem(hPipe, new GBoard("pipe", 1, 2, 1), 100.0, 1, 2), // Empty pipe
         * symbol new FilledGelem(Color.blue, 75.0, 1, 2), // Pipe water symbol new
         * ImageGelem(bPipeV3, new GBoard("pipe", 3, 2, 1), 100.0, 3, 2), // Split pipe
         * symbol new StringGelem("" + markedPositionSymbol, Color.blue), new
         * StringGelem("" + actualPositionSymbol, Color.blue),
         * 
         * new FilledGelem(Color.blue, 75.0, 2,1) };
         */

        board.draw(gelems[2], 5, 4, 1);
        //board.draw(gelems[3], 5, 4, 2);
        
        board.draw(gelems[4], 4, 6, 1);
        //board.draw(gelems[3], 5, 6, 2);
        //board.draw(gelems[7], 5, 7, 2);
        //board.draw(gelems[7], 4, 7, 2);

        /*
        for (int i = 0; i < extraSymbols.length; i++)
            maze.attachGelemToRoadSymbol(extraSymbols[i], gelems[i]);
        
        Position start = maze.symbolPositions(startSymbol)[0];
        */
        
        Position start = new Position(5, 3);

        Deposit dep = new Deposit(10, start.line(), start.column());
        Console con = new Console();

        Position p1 = new Position(3, 7);
        Person p = new Person(dep, p1);
        new Thread(p).start();

        Worker w = new Worker(dep, con);
        //new Thread(w).start();
        
    }

    /**
     * @param lin
     * @param col
     * @return boolean
     */
    public static boolean waterMovementInMap(int lin, int col) {
        boolean result = false;
        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

        if(board.exists(lin, col+1)){
            Gelem gelem = board.topGelem(lin, col+1);
            if (gelem.equals(gelems[2])){
                board.draw(gelems[3], lin, col+1, 2);    
            }

        }

        /*
        if (maze.validPosition(lin, col) && maze.isRoad(lin, col)) {
            if (maze.roadSymbol(lin, col) == endSymbol) {
                return true;
            } else {
                if (maze.roadSymbol(lin, col) != startSymbol)
                    markPosition(lin, col);
                if (waterMovementInMap(lin, col + 1)) {
                    return true;
                }
            }
        } else {
            // System.out.println(maze.validPosition(lin, col));
            // System.out.println(maze.isRoad(lin, col));
        }
        */
        return true;
        
    }

    /**
     * @param lin
     * @param col
     */
    public static void removeMark(int lin, int col) {

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        Gelem gelem = board.topGelem(lin, col+1);
        board.erase(gelem, lin, col+1, 2);
        
        /*
        if (gelem.equals(new ImageGelem(hPipe, new GBoard("pipe", 1, 2, 1), 100.0, 1, 2))){
            board.draw(gelems[3], lin, col+1, 2);    
        }
        */

        /*
        if (maze.validPosition(lin, col) && maze.isRoad(lin, col)) {
            if (maze.roadSymbol(lin, col) == actualPositionSymbol) {
                unmarkPosition(lin, col);
                removeMark(lin, col + 1);
            }
        }
        */
    }

    /**
     * @param line
     * @param col
     * @return boolean
     */
    public static boolean freePosition(int line, int col) {
        System.out.println(Thread.currentThread());
        if (maze.roadSymbol(line, col) == actualPositionSymbol)
            return false;
        else
            return true;
    }

    /**
     * @param line
     * @param col
     */
    public static void markPosition(int line, int col) {
        GBoard.sleep(100);

        // System.out.print(maze.board.gelemLayer(new ImageGelem(hPipe,new
        // GBoard("pipe", 1, 3, 1),100.0,1,3), line, col));
        // System.out.print(maze.board.gelemLayer(null, line, col));

        if (maze.roadSymbol(line, col) == pipeSymbol) {
            Gelem g = new ImageGelem(hPipe, new GBoard("pipe", 1, 3, 1), 100.0, 1, 3);

            board.draw(g, line, col, 2);

            //maze.detachGelemToRoadSymbol(pipeSymbol, new ImageGelem(hPipe,new GBoard("pipe", 1, 3, 1),100.0,1,3));
            //maze.attachGelemToRoadSymbol(pipeSymbol,new FilledGelem(Color.blue, 90.0, 1, 3) );
        }

        //maze.putRoadSymbol(line, col, actualPositionSymbol);
        GBoard.sleep(100);
    }
    
    /** 
     * @param line
     * @param col
     */
    public static void unmarkPosition(int line, int col){
        if(maze.roadSymbol(line, col) == pipeSymbol){
            maze.putRoadSymbol(line, col, 't');
            //maze.detachGelemToRoadSymbol(pipeSymbol, new FilledGelem(Color.blue, 90.0, 1, 3) );
            //maze.attachGelemToRoadSymbol(pipeSymbol, new ImageGelem(hPipe,new GBoard("pipe", 1, 3, 1),100.0,1,3));
        }
        GBoard.sleep(100);
    }
}