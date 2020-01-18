package app;

import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;

import java.awt.Color;

import configuration.Configuration;

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
        new MutableStringGelem("0" , Color.red);
    
        board.draw(gelems[0], 5, 4, pipeLayer);
        board.draw(new MutableStringGelem("0" , Color.red), 5,4,numberLayer);
        //board.draw(gelems[1], 5, 4, waterLayer);


        board.draw(gelems[2], 4, 6, pipeLayer);
        board.draw(new MutableStringGelem("0" , Color.red), 4,6+1,numberLayer);
        board.draw(new MutableStringGelem("0" , Color.red), 4+2,6+1,numberLayer);

        board.draw(gelems[4], 2, 7, pipeLayer);
        board.draw(new MutableStringGelem("0" , Color.red), 2,7+1,numberLayer);

        board.draw(gelems[5], 7, 7, pipeLayer);
        board.draw(new MutableStringGelem("0" , Color.red), 7+1,7+1,numberLayer);

    }
/*
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
    */

    /**
     * @param lin
     * @param col
     * @return boolean
     */
    public boolean waterMovementInMap(int lin, int col, Position dest, int waterVol) {
        boolean result = false;
        try {
            Thread.sleep(500);
        
            if (dest.line() == lin && dest.column() == col)
                return true;

            // Horizontal Pipe                
            
            if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[0])) {

                synchronized(this){

                MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin, col, numberLayer, numberLayer);
                int volumeWater = Integer.parseInt(msGelem.text());
                
                if(volumeWater == 0){
                    board.draw(gelems[1], lin, col, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater + waterVol));
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

                    synchronized(this){

                    MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin - 1, col + 1);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if(volumeWater == 0){
                        board.draw(gelems[3], lin - 1, col + 1, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));
                    }
                    waterMovementInMap(lin - 2, col + 1, dest, waterVol);
                }
                // split down
                else {
                    synchronized(this){
                        MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin + 1, col + 1);
                        int volumeWater = Integer.parseInt(msGelem.text());
                        
                        if(volumeWater == 0){
                            board.draw(gelems[3], lin + 1, col + 1, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));
                    }
                    waterMovementInMap(lin + 2, col + 1, dest, waterVol);
                }
                
            // Corner Pipe Top
            } else if(board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[4])){
                
                synchronized(this){

                MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin - 1, col + 1);
                int volumeWater = Integer.parseInt(msGelem.text());

                if(volumeWater == 0){
                    board.draw(gelems[3], lin, col, waterLayer);
                    board.draw(gelems[3], lin-1, col+1, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater + waterVol));
                }
                waterMovementInMap(lin - 1, col + 2, dest, waterVol);

            // Corner Pipe Bottom
            } else if(board.exists(lin , col) && board.topGelem(lin , col, pipeLayer, pipeLayer).equals(gelems[5])){
                synchronized(this){

                    MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin + 1, col + 1);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if(volumeWater == 0){
                        board.draw(gelems[3], lin, col, waterLayer);
                        board.draw(gelems[3], lin+1, col+1, waterLayer);

                    }

                    msGelem.setText(String.valueOf(volumeWater + waterVol));
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
    public void removeMark(int lin, int col, Position dest, int waterVol) {

        try {
            Thread.sleep(Configuration.MAP_UPDATE_SPEED);
        } catch (Exception e) {
        }

        if (dest.line() == lin && dest.column() == col)
            return;

        if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[0])) {

            synchronized(this){

            MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin, col);
            int volumeWater = Integer.parseInt(msGelem.text());
       
            if(volumeWater - waterVol == 0){
                board.erase(lin, col, waterLayer,waterLayer);
            }

            msGelem.setText(String.valueOf(volumeWater - waterVol));
            }
            removeMark(lin, col + 2, dest, waterVol);

        } else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[2])) {

            // if up
            if (dest.line() < lin) {
                synchronized(this){

                MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin-1, col+1);
                int volumeWater = Integer.parseInt(msGelem.text());

                if(volumeWater - waterVol == 0){
                    board.erase(lin-1, col+1, waterLayer,waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));

                if(!(board.exists(lin-1, col+1, waterLayer) || board.exists(lin+1, col+1, waterLayer)))
                    board.erase(lin,col,waterLayer,waterLayer);
                }

                removeMark(lin - 2, col + 1, dest, waterVol);

            }
            
            // if down
            else {
                synchronized(this){

                MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin+1, col+1);
                int volumeWater = Integer.parseInt(msGelem.text());

                if(volumeWater - waterVol == 0){
                    board.erase(lin+1, col+1, waterLayer,waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));
                
                if(!(board.exists(lin-1, col+1, waterLayer) || board.exists(lin+1, col+1, waterLayer)))
                    board.erase(lin,col,waterLayer,waterLayer);
            }
                
                removeMark(lin + 2, col + 1, dest, waterVol);
            }
            
        // Corner Pipe Top
        } else if(board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[4])){
            synchronized(this){

            MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin-1, col+1);
            int volumeWater = Integer.parseInt(msGelem.text());

            if(volumeWater - waterVol == 0){
                board.erase(lin-1, col+1, waterLayer,waterLayer);
                board.erase(lin, col, waterLayer, waterLayer);
            }

            msGelem.setText(String.valueOf(volumeWater - waterVol));
        }

            waterMovementInMap(lin - 1, col + 2, dest, waterVol);

        // Corner Pipe Bottom
        } else if(board.exists(lin , col) && board.topGelem(lin , col, pipeLayer, pipeLayer).equals(gelems[5])){
            synchronized(this){

            MutableStringGelem msGelem = (MutableStringGelem)board.topGelem(lin+1, col+1);
            int volumeWater = Integer.parseInt(msGelem.text());
            
            if(volumeWater - waterVol == 0){
                board.erase(lin+1, col+1, waterLayer,waterLayer);
                board.erase(lin, col, waterLayer, waterLayer);
            }
            msGelem.setText(String.valueOf(volumeWater - waterVol));
            }
            waterMovementInMap(lin + 1, col + 2, dest, waterVol);

        }else {
            System.out.println(lin);
            System.out.println(col);
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