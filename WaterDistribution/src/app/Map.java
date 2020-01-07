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

    static Gelem[] gelems = { 
            new ImageGelem(hPipe, new GBoard("pipe", 1, 2, 1), 100.0, 1, 2), // Empty pipe symbol
            new FilledGelem(Color.blue, 80.0, 1, 2), // Pipe water symbol
            new ImageGelem(bPipeV3, new GBoard("pipe", 3, 2, 1), 100.0, 3, 2), // Split pipe symbol
            new FilledGelem(Color.blue, 80.0, 1, 1) };

    static final int pipeLayer = 0;
    static final int waterLayer = 1;
    static final int numberLayer = 2;

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        board.draw(gelems[0], 5, 4, pipeLayer);

        board.draw(gelems[2], 4, 6, pipeLayer);

        Position start = new Position(5, 3);

        Deposit dep = new Deposit(30, start.line(), start.column());
        Console con = new Console();

        Position p2 = new Position(7, 7);
        Position p1 = new Position(3, 7);

        Person h2 = new Person(dep, p2);
        new Thread(h2).start();
        
        Person h1 = new Person(dep, p2);
        new Thread(h1).start();
        

        Worker w = new Worker(dep, con);
        // new Thread(w).start();

    }

    /**
     * @param lin
     * @param col
     * @return boolean
     */
    public static boolean waterMovementInMap(int lin, int col, Position dest, int waterVol) {
        boolean result = false;
        try {
            Thread.sleep(500);
        } catch (Exception e) {

        }

        if (dest.line() == lin && dest.column() == col)
            return true;


        // Horizontal Pipe
        if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[0])) {
            if(board.exists(lin, col, waterLayer)){
                MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin, col);
                board.erase(lin, col, numberLayer, numberLayer);
                msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
                board.draw(msgelem, lin, col, numberLayer);
            }
            else{
                Gelem gelem = board.topGelem(lin, col);
                if (gelem.equals(gelems[0])) {
                    board.draw(gelems[1], lin, col, waterLayer);

                    MutableStringGelem msgelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                    board.draw(msgelem, lin,col,numberLayer);

                }
            }

            waterMovementInMap(lin, col + 2, dest, waterVol);
        
        } 
        // Split pipe right
        else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col).equals(gelems[2])) {
            //Gelem gelem = board.topGelem(lin, col);

            // First straight part
            if(!board.exists(gelems[1], lin, col, waterLayer)){
                board.draw(gelems[1], lin, col, waterLayer);
            }

            // split up
            if (dest.line() < lin) {

                if(board.exists(lin - 1, col + 1, waterLayer)){
                    MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin - 1, col + 1);
                    board.erase(msgelem, lin - 1, col + 1, numberLayer);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
                    board.draw(msgelem, lin - 1, col + 1, numberLayer);
                }
                else{
                    board.draw(gelems[3], lin - 1, col + 1, waterLayer);

                    MutableStringGelem msgelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                    board.draw(msgelem, lin - 1, col + 1,numberLayer);

                }
                waterMovementInMap(lin - 2, col + 1, dest, waterVol);
            }
            // split down
            else {

                if(board.exists(lin + 1, col + 1, waterLayer)){
                    MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin + 1, col + 1);
                    board.erase(msgelem, lin + 1, col + 1, numberLayer);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
                    board.draw(msgelem, lin + 1, col + 1, numberLayer);
                    
                }
                else{
                    board.draw(gelems[3], lin + 1, col + 1, waterLayer);
                    
                    MutableStringGelem msgelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                    board.draw(msgelem, lin + 1, col + 1,numberLayer);
                }

                waterMovementInMap(lin + 2, col + 1, dest, waterVol);
            }
        } else {
            System.out.println("Not found ");
        }

        return true;

    }

    
    /** 
     * @param lin
     * @param col
     * @param dest
     */
    public static void removeMark(int lin, int col, Position dest, int waterVol) {

        try {
            Thread.sleep(500);
        } catch (Exception e) {
        }

        if (dest.line() == lin && dest.column() == col)
            return;

        if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[0])) {

            MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin, col);
            if(Integer.parseInt(msgelem.text()) - waterVol == 0){
                board.erase(lin, col, waterLayer,waterLayer);
                board.erase(lin, col, numberLayer,numberLayer);
            }
            else{
                board.erase(lin, col, numberLayer, numberLayer);
                msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
                board.draw(msgelem, lin, col, numberLayer);
            }
            removeMark(lin, col + 2, dest, waterVol);

        } else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[2])) {

            // if up
            if (dest.line() < lin) {
                System.out.println("Water going UP");

                MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin-1, col+1);
                if(Integer.parseInt(msgelem.text()) - waterVol == 0){
                    board.erase(lin-1, col+1, waterLayer, waterLayer);
                    board.erase(lin-1, col+1, numberLayer,numberLayer);
                }
                else{
                    board.erase(lin - 1, col + 1, numberLayer, numberLayer);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
                    board.draw(msgelem, lin - 1, col + 1, numberLayer);
                }

                if(!(board.exists(lin-1, col+1, waterLayer) || board.exists(lin+1, col+1, waterLayer)))
                    board.erase(lin,col,waterLayer,waterLayer);

                removeMark(lin - 2, col + 1, dest, waterVol);

            }
            
            // if down
            else {
                MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin+1, col+1);
                if(Integer.parseInt(msgelem.text()) - waterVol == 0){
                    board.erase(lin+1, col+1, waterLayer,waterLayer);
                    board.erase(lin+1, col+1, numberLayer,numberLayer);
                }
                else{
                    board.erase(lin + 1, col + 1, numberLayer, numberLayer);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
                    board.draw(msgelem, lin + 1, col + 1, numberLayer);
                }
                
                System.out.println(board.exists(lin-1, col+1, waterLayer));
                System.out.println(board.exists(lin+1, col+1, waterLayer));

                System.out.println(!(board.exists(lin+1, col+1, waterLayer)||board.exists(lin-1, col+1, waterLayer)));

                if(!(board.exists(lin-1, col+1, waterLayer) || board.exists(lin+1, col+1, waterLayer)))
                    board.erase(lin,col,waterLayer,waterLayer);
                
                removeMark(lin + 2, col + 1, dest, waterVol);
            }
            

        } else {
            System.out.println("Bad idea");
        }
    }
}