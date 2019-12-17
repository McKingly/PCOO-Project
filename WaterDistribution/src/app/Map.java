package app;

import pt.ua.gboard.*;
import pt.ua.gboard.games.*;
import pt.ua.gboard.basic.*;

import java.awt.Color;

public class Map {

    static final char startSymbol = 'D';
    static final char endSymbol = 'H';
    static final char markedPositionSymbol = ' ';
    static final char actualPositionSymbol = 'o';
    static final String map = "/home/armando/Desktop/ECT/PCOO/Projecto/src/app/Map.txt";
    static Labyrinth maze;

    
    /** 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        LabyrinthGelem.setShowRoadBoundaries();

        char[] extraSymbols =
        {
            startSymbol,
            endSymbol,
            markedPositionSymbol,
            actualPositionSymbol
        };

        maze = new Labyrinth(map, extraSymbols, 1, true);

        Gelem[] gelems = {
            new StringGelem(""+startSymbol, Color.red),
            new StringGelem(""+endSymbol, Color.red),
            new StringGelem(""+markedPositionSymbol, Color.GREEN),
            new StringGelem(""+actualPositionSymbol, Color.GREEN)
         };

        for(int i = 0; i < extraSymbols.length; i++)
            maze.attachGelemToRoadSymbol(extraSymbols[i], gelems[i]);
   
        Position start = maze.symbolPositions(startSymbol)[0];
        System.out.println(start);

        Deposit dep = new Deposit(10,start.line(),start.column());
        Console con = new Console();

        Person p = new Person(dep, con);
        new Thread(p).start();

        Worker w = new Worker(dep, con);
        new Thread(w).start();

    }

    
    /** 
     * @param lin
     * @param col
     * @return boolean
     */
    public static boolean waterMovementInMap (int lin, int col){
        boolean result = false;
        try {
            Thread.sleep(100);
        } catch (Exception e) {
            //TODO: handle exception
        }

        if (maze.validPosition(lin, col) && maze.isRoad(lin, col)){
            if (maze.roadSymbol(lin, col) == endSymbol){
                return true;
            }
            else{
                if(maze.roadSymbol(lin, col) != startSymbol)
                    markPosition(lin, col);
                if (waterMovementInMap(lin, col+1)){
                    return true;
                }
            }
        }
        else{
            //System.out.println(maze.validPosition(lin, col));
            //System.out.println(maze.isRoad(lin, col));
        }
        return result;
    }

    
    /** 
     * @param lin
     * @param col
     */
    public static void removeMark(int lin, int col){

        try {
            Thread.sleep(100);
        } catch (Exception e) {
            //TODO: handle exception
        }

        if (maze.validPosition(lin, col) && maze.isRoad(lin, col)){
            if (maze.roadSymbol(lin, col) == actualPositionSymbol){
                unmarkPosition(lin, col);
                removeMark(lin, col+1);
            }
        }
    }

    
    /** 
     * @param line
     * @param col
     * @return boolean
     */
    public static boolean freePosition(int line, int col){
        System.out.println(Thread.currentThread());
        if(maze.roadSymbol(line, col)==actualPositionSymbol)
            return false;
        else
            return true;
    }

    
    /** 
     * @param line
     * @param col
     */
    public static void markPosition(int line, int col){

        maze.putRoadSymbol(line, col, actualPositionSymbol);
        GBoard.sleep(100);
    }

    
    /** 
     * @param line
     * @param col
     */
    public static void unmarkPosition(int line, int col){
        maze.putRoadSymbol(line, col, markedPositionSymbol);
        GBoard.sleep(100);
    }
}