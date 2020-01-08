package app;

import pt.ua.gboard.*;
import pt.ua.gboard.games.*;
import pt.ua.gboard.shapes.FillArc;
import pt.ua.gboard.shapes.ShapeGelem;
import pt.ua.gboard.basic.*;

import java.awt.Color;

public class Map {

    static final String map = "src/app/Map.txt";
    
    static final String deposit = "src/images/WaterDeposit.png";

    static final String hPipe = "src/images/Hpipe.png";
    static final String vPipe = "src/images/Vpipe.png";
    static final String bPipeV1 = "src/images/BifurcatedPipev1.png";
    static final String bPipeV2 = "src/images/BifurcatedPipev2.png";
    static final String bPipeV3 = "src/images/BifurcatedPipev3.png";
    static final String cPipeT = "src/images/CornerPipeTop.png";
    static final String cPipeB = "src/images/CornerPipeBottom.png";

    static Labyrinth maze;

    static GBoard board = new GBoard("Map", 10, 20, 3);

    static Gelem[] gelems = { 
            new ImageGelem(hPipe, board, 100.0, 1, 2), // Empty pipe symbol
            new FilledGelem(Color.blue, 80.0, 1, 2), // Pipe water symbol
            new ImageGelem(bPipeV3, board, 100.0, 3, 2), // Split pipe symbol
            new FilledGelem(Color.blue, 80.0, 1, 1),
            new ImageGelem(cPipeT, board, 100.0, 2, 2),
            new ImageGelem(cPipeB, board, 100.0, 2, 2)
        };

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

        board.draw(gelems[4], 2, 7, pipeLayer);

        board.draw(gelems[5], 7, 7, pipeLayer);

        /*
        Object[] a = { 
            "polygon", 5, 0.1, 0.1, 0.9, 0.8, 0.7, 0.95,
                       "color", Color.blue,
        };

        board.draw(new ShapeGelem(a), 2, 7, waterLayer);
        */

        Position start = new Position(5, 3);

        Deposit dep = new Deposit(30, start.line(), start.column());
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

    /**
     * @param lin
     * @param col
     * @return boolean
     */
    public static boolean waterMovementInMap(int lin, int col, Position dest, int waterVol) {
        boolean result = false;
        try {
            Thread.sleep(500);
        
            if (dest.line() == lin && dest.column() == col)
                return true;

            // Horizontal Pipe
            if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[0])) {

                if(board.exists(lin, col, waterLayer)){
                    MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin, col, numberLayer, numberLayer);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
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

                System.out.println("Split Pipe");

                // First straight part
                if(!board.exists(gelems[1], lin, col, waterLayer)){
                    board.draw(gelems[1], lin, col, waterLayer);
                }

                // split up
                if (dest.line() < lin) {

                    if(board.exists(lin - 1, col + 1, waterLayer)){
                        
                        /*
                        StringGelem sGelem = (StringGelem)board.topGelem(lin - 1, col + 1);
                        
                        //Thread.sleep(500);
                        
                        board.erase(sGelem, lin - 1, col + 1, numberLayer);
                        sGelem = new StringGelem(String.valueOf(Integer.parseInt(sGelem.text) + waterVol), Color.red);
                        board.draw(sGelem, lin-1, col + 1,numberLayer);

                        System.out.println(board.exists(lin, col, waterLayer));
                        System.out.println(board.topGelem(lin, col).numberOfLines());
                        System.out.println(board.topGelem(lin, col).numberOfColumns());
                        
                        //Thread.sleep(500);
                        */

                        MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin - 1, col + 1);
                        msGelem.setText(String.valueOf(Integer.parseInt(msGelem.text()) + waterVol));
                    }
                    else{
                        board.draw(gelems[3], lin - 1, col + 1, waterLayer);

                        /*
                        StringGelem sGelem = new StringGelem(String.valueOf(waterVol) , Color.red);
                        board.draw(sGelem, lin - 1, col + 1,numberLayer);
                        */

                        MutableStringGelem msGelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                        board.draw(msGelem, lin - 1, col + 1,numberLayer);

                    }
                    waterMovementInMap(lin - 2, col + 1, dest, waterVol);
                }
                // split down
                else {

                    if(board.exists(lin + 1, col + 1, waterLayer)){
                        MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin + 1, col + 1);
                        msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
                    }
                    else{
                        board.draw(gelems[3], lin + 1, col + 1, waterLayer);
                        
                        MutableStringGelem msgelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                        board.draw(msgelem, lin + 1, col + 1,numberLayer);
                    }

                    waterMovementInMap(lin + 2, col + 1, dest, waterVol);
                }
                
            // Corner Pipe Top
            } else if(board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[4])){
                if(!board.exists(gelems[3], lin, col, waterLayer)){
                    board.draw(gelems[3], lin, col, waterLayer);
                    board.draw(gelems[3], lin-1, col+1, waterLayer);

                    MutableStringGelem msgelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                    board.draw(msgelem, lin - 1, col + 1,numberLayer);
                }
                else{
                    MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin - 1, col + 1);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
                }

                waterMovementInMap(lin - 1, col + 2, dest, waterVol);

            // Corner Pipe Bottom
            } else if(board.exists(lin , col) && board.topGelem(lin , col, pipeLayer, pipeLayer).equals(gelems[5])){
                if(!board.exists(gelems[3], lin, col, waterLayer)){
                    board.draw(gelems[3], lin, col, waterLayer);
                    board.draw(gelems[3], lin+1, col+1, waterLayer);

                    MutableStringGelem msgelem = new MutableStringGelem(String.valueOf(waterVol) , Color.red);
                    board.draw(msgelem, lin + 1, col + 1,numberLayer);
                }
                else {
                    MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin + 1, col + 1);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) + waterVol));
                }

                waterMovementInMap(lin + 1, col + 2, dest, waterVol);


            }
            else {
                System.out.println("Not found ");
            }
        } catch (Exception e) {

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
                //board.erase(lin, col, waterLayer,waterLayer);
                //board.erase(lin, col, numberLayer,numberLayer);
                board.erase(lin, col, waterLayer,numberLayer);
            }
            else{
                //board.erase(lin, col, numberLayer, numberLayer);
                msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
                //board.draw(msgelem, lin, col, numberLayer);
            }
            removeMark(lin, col + 2, dest, waterVol);

        } else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[2])) {

            // if up
            if (dest.line() < lin) {
                System.out.println("Water going UP");

                MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin-1, col+1);
                if(Integer.parseInt(msGelem.text()) - waterVol == 0){
                    //board.erase(lin-1, col+1, waterLayer, waterLayer);
                    //board.erase(lin-1, col+1, numberLayer,numberLayer);
                    board.erase(lin-1, col+1, waterLayer,numberLayer);

                }
                else{
                    //board.erase(lin - 1, col + 1, numberLayer, numberLayer);
                    msGelem.setText(String.valueOf(Integer.parseInt(msGelem.text()) - waterVol));
                    //board.draw(sGelem, lin - 1, col + 1, numberLayer);
                }

                if(!(board.exists(lin-1, col+1, waterLayer) || board.exists(lin+1, col+1, waterLayer)))
                    board.erase(lin,col,waterLayer,waterLayer);

                removeMark(lin - 2, col + 1, dest, waterVol);

            }
            
            // if down
            else {
                MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin+1, col+1);
                if(Integer.parseInt(msgelem.text()) - waterVol == 0){
                    //board.erase(lin+1, col+1, waterLayer,waterLayer);
                    //board.erase(lin+1, col+1, numberLayer,numberLayer);
                    board.erase(lin+1, col+1, waterLayer,numberLayer);
                }
                else{
                    //board.erase(lin + 1, col + 1, numberLayer, numberLayer);
                    msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
                    //board.draw(msgelem, lin + 1, col + 1, numberLayer);
                }
                
                if(!(board.exists(lin-1, col+1, waterLayer) || board.exists(lin+1, col+1, waterLayer)))
                    board.erase(lin,col,waterLayer,waterLayer);
                
                removeMark(lin + 2, col + 1, dest, waterVol);
            }
            
        // Corner Pipe Top
        } else if(board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[4])){

            MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin-1, col+1);
            if(Integer.parseInt(msgelem.text()) - waterVol == 0){
                board.erase(lin-1, col+1, waterLayer,numberLayer);
                board.erase(lin, col, waterLayer, waterLayer);
            }
            else{
                msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
            }

            waterMovementInMap(lin - 1, col + 2, dest, waterVol);

        // Corner Pipe Bottom
        } else if(board.exists(lin , col) && board.topGelem(lin , col, pipeLayer, pipeLayer).equals(gelems[5])){
            
            MutableStringGelem msgelem = (MutableStringGelem)board.topGelem(lin+1, col+1);
            if(Integer.parseInt(msgelem.text()) - waterVol == 0){
                board.erase(lin+1, col+1, waterLayer,numberLayer);
                board.erase(lin, col, waterLayer, waterLayer);
            }
            else{
                msgelem.setText(String.valueOf(Integer.parseInt(msgelem.text()) - waterVol));
            }

            waterMovementInMap(lin + 1, col + 2, dest, waterVol);

        }else {
            System.out.println("Bad idea");
        }
    }
}