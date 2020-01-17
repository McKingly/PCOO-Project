package app;

import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;

import java.awt.Color;

public class Map {

    final String DEFAULT_MAP = "src/app/Map.txt";
    final String DEPOSIT = "src/images/WaterDeposit.png";
    // final String CONSOLE = ; 
    // final String PERSON =  "/images/WaterDeposit.png";
    final String VERTICAL_PIPE = "src/images/Vpipe.png";
    final String HORIZONTAL_PIPE = "src/images/Hpipe.png";
    final String T_JUNCTION_PIPE_1 = "src/images/TJunctionPipe_1.png";
    final String T_JUNCTION_PIPE_2 = "src/images/TJunctionPipe_2.png";
    final String T_JUNCTION_PIPE_3 = "src/images/TJunctionPipe_3.png";
    final String CORNER_PIPE_UP = "src/images/CornerPipeTop.png";
    final String CORNER_PIPE_DOWN = "src/images/CornerPipeBottom.png";
    
    GBoard board = new GBoard("Map", 10, 20, 3);

    Gelem[] gelems = { 
        new ImageGelem(HORIZONTAL_PIPE, board, 100.0, 1, 2), // Empty pipe symbol
        new FilledGelem(Color.blue, 80.0, 1, 2), // Pipe water symbol
        new ImageGelem(T_JUNCTION_PIPE_3, board, 100.0, 3, 2), // Split pipe symbol
        new FilledGelem(Color.blue, 80.0, 1, 1),
        new ImageGelem(CORNER_PIPE_UP, board, 100.0, 2, 2),
        new ImageGelem(CORNER_PIPE_DOWN, board, 100.0, 2, 2),
        new ImageGelem(VERTICAL_PIPE, board, 100.0, 1, 2), // Empty pipe symbol
        new ImageGelem(T_JUNCTION_PIPE_1, board, 100.0, 3, 2), // Split pipe symbol
        new ImageGelem(T_JUNCTION_PIPE_2, board, 100.0, 3, 2), // Split pipe symbol
    }; 

    final int pipeLayer = 0;
    final int waterLayer = 1;
    final int numberLayer = 2;

    String mapFile;

    public Map(){
        this.mapFile = DEFAULT_MAP;
    
        board.draw(gelems[0], 5, 4, pipeLayer);

        board.draw(gelems[2], 4, 6, pipeLayer);

        board.draw(gelems[4], 2, 7, pipeLayer);

        board.draw(gelems[5], 7, 7, pipeLayer);
    }

    public Map(String mapFile){
        
        this.mapFile = mapFile;
    
        board.draw(gelems[0], 5, 4, pipeLayer);

        board.draw(gelems[2], 4, 6, pipeLayer);

        board.draw(gelems[4], 2, 7, pipeLayer);

        board.draw(gelems[5], 7, 7, pipeLayer);
    }

    public void drawMap(){

        board.draw(gelems[0], 5, 4, pipeLayer);

        board.draw(gelems[2], 4, 6, pipeLayer);

        board.draw(gelems[4], 2, 7, pipeLayer);

        board.draw(gelems[5], 7, 7, pipeLayer);
    }

    /**
     * @param lin
     * @param col
     * @return boolean
     */
    public synchronized boolean waterMovementInMap(int lin, int col, Position dest, int waterVol) {
        boolean result = false;
        try {
            Thread.sleep(250);
        
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
    public synchronized void removeMark(int lin, int col, Position dest, int waterVol) {

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

    public synchronized boolean validPosition(Position p){
        try {
            assert p != null: "Destination variable can't be null.";
            assert p.line() >= 0 && p.line() < board.numberOfLines(): "Y position isn't valid.";;
            assert p.column() >= 0 && p.column() < board.numberOfColumns(): "X position isn't valid";
            
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }   
}