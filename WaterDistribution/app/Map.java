package app;

import pt.ua.gboard.*;
import pt.ua.gboard.basic.*;
import pt.ua.gboard.games.Labyrinth;

import java.awt.Color;

import configuration.Configuration;

public class Map {

    GBoard board;

    Gelem[] gelems;

    public Position[] depositsPositions;
    Position[] consolesPositions;
    Position[] personsPositions;
    Position[] tJunction3Positions;

    char[] extraSymbols = { Configuration.DEPOSIT_SYMBOL, Configuration.CONSOLE_SYMBOL, Configuration.PERSON_SYMBOL,
            Configuration.HORIZONTAL_PIPE_SYMBOL, Configuration.VERTICAL_PIPE_SYMBOL,
            Configuration.T_JUNCTION_PIPE_1_SYMBOL, Configuration.T_JUNCTION_PIPE_2_SYMBOL,
            Configuration.T_JUNCTION_PIPE_3_SYMBOL, Configuration.CORNER_PIPE_UP_SYMBOL,
            Configuration.CORNER_PIPE_DOWN_SYMBOL, ' ' };

    final int pipeLayer = 0;
    final int waterLayer = 1;
    final int numberLayer = 2;

    String mapFile;

    static Labyrinth maze = null;

    
    /** 
     * @param mapFile
     * @return 
     */
    public Map(String mapFile) {

        maze = new Labyrinth(mapFile, extraSymbols, 1, true);
        int numberColumns = maze.board.numberOfColumns();
        int numberLines = maze.board.numberOfLines();

        this.board = new GBoard("Water Distribution Map", numberLines, numberColumns, 3);

        this.gelems = new Gelem[] { new ImageGelem(Configuration.DEPOSIT, board, 100.0, 1, 1), // DEPOSIT [0]
                new ImageGelem(Configuration.CONSOLE, board, 100.0, 1, 1), // CONSOLE [1]
                new ImageGelem(Configuration.PERSON, board, 100.0, 1, 1), // PERSON [2]
                new ImageGelem(Configuration.HORIZONTAL_PIPE, board, 100.0, 1, 2), // Empty pipe symbol [3]
                new ImageGelem(Configuration.VERTICAL_PIPE, board, 100.0, 2, 1), // Empty pipe symbol [4]
                new ImageGelem(Configuration.T_JUNCTION_PIPE_1, board, 100.0, 3, 2), // Split pipe symbol [5]
                new ImageGelem(Configuration.T_JUNCTION_PIPE_2, board, 100.0, 3, 2), // Split pipe symbol [6]
                new ImageGelem(Configuration.T_JUNCTION_PIPE_3, board, 100.0, 3, 2), // Split pipe symbol [7]
                new ImageGelem(Configuration.CORNER_PIPE_UP, board, 100.0, 2, 2), // gelems[8]
                new ImageGelem(Configuration.CORNER_PIPE_DOWN, board, 100.0, 2, 2), // gelems[9]
                new FilledGelem(Color.blue, 80.0, 1, 2), // Pipe water symbol
                new FilledGelem(Color.blue, 80.0, 1, 1), };

        this.depositsPositions = maze.roadSymbolPositions(Configuration.DEPOSIT_SYMBOL);
        for (Position pipe : depositsPositions) {
            board.draw(gelems[0], pipe.line(), pipe.column(), pipeLayer);
        }

        this.consolesPositions = maze.roadSymbolPositions(Configuration.CONSOLE_SYMBOL);
        for (Position pipe : consolesPositions) {
            board.draw(gelems[1], pipe.line(), pipe.column(), pipeLayer);
        }

        this.personsPositions = maze.roadSymbolPositions(Configuration.PERSON_SYMBOL);
        for (Position pipe : personsPositions) {
            board.draw(gelems[2], pipe.line(), pipe.column(), pipeLayer);
        }

        for (Position pipe : maze.roadSymbolPositions(Configuration.HORIZONTAL_PIPE_SYMBOL)) {
            board.draw(gelems[3], pipe.line(), pipe.column(), pipeLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column(), numberLayer);
        }

        for (Position pipe : maze.roadSymbolPositions(Configuration.VERTICAL_PIPE_SYMBOL)) {
            board.draw(gelems[4], pipe.line(), pipe.column(), pipeLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column(), numberLayer);
        }

        // Position[] tJunction1Positions =
        // maze.roadSymbolPositions(Configuration.T_JUNCTION_PIPE_1_SYMBOL);

        Position[] tJunction2Positions = maze.roadSymbolPositions(Configuration.T_JUNCTION_PIPE_2_SYMBOL);
        for (Position pipe : tJunction2Positions) {
            board.draw(gelems[6], pipe.line() - 1, pipe.column(), pipeLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line() - 1, pipe.column(), numberLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line() + 1, pipe.column(), numberLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column() + 1, numberLayer);
        }

        this.tJunction3Positions = maze.roadSymbolPositions(Configuration.T_JUNCTION_PIPE_3_SYMBOL);
        for (Position pipe : tJunction3Positions) {
            board.draw(gelems[7], pipe.line() - 1, pipe.column() - 1, pipeLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line() - 1, pipe.column(), numberLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line() + 1, pipe.column(), numberLayer);
        }

        for (Position pipe : maze.roadSymbolPositions(Configuration.CORNER_PIPE_UP_SYMBOL)) {
            board.draw(gelems[8], pipe.line(), pipe.column(), pipeLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column() + 1, numberLayer);
        }
        ;

        for (Position pipe : maze.roadSymbolPositions(Configuration.CORNER_PIPE_DOWN_SYMBOL)) {
            board.draw(gelems[9], pipe.line() - 1, pipe.column(), pipeLayer);
            board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column() + 1, numberLayer);
        }

    }

    /**
     * @param lin
     * @param col
     * @return boolean
     */
    public boolean waterMovementInMap(int lin, int col, Position dest, int waterVol) {
        boolean result = false;
        try {
            board.sleep(Configuration.MAP_UPDATE_SPEED);

            // System.out.println(lin+" "+col);
            // System.out.println(board.exists(lin , col));
            // System.out.println(board.topGelem(lin, col, pipeLayer,
            // pipeLayer).equals(gelems[6]));
            // System.out.println(board.exists(lin , col) && board.topGelem(lin, col,
            // pipeLayer, pipeLayer).equals(gelems[6]));

            if (dest.line() == lin && dest.column() == col)
                return true;

            // Horizontal Pipe
            if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[3])) {

                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col, numberLayer,
                            numberLayer);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[10], lin, col, waterLayer);
                    }

                    msGelem.setText(String.valueOf(volumeWater + waterVol));
                }

                waterMovementInMap(lin, col + 2, dest, waterVol);

            }
            // Corner Pipe Top
            else if (board.exists(lin - 1, col)
                    && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[8])) {
                // System.out.println("Corner Pipe Top");

                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin - 1, col + 1);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin, col, waterLayer);
                        board.draw(gelems[11], lin - 1, col + 1, waterLayer);
                    }

                    msGelem.setText(String.valueOf(volumeWater + waterVol));
                }
                waterMovementInMap(lin - 1, col + 2, dest, waterVol);

                // Corner Pipe Bottom
            } else if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[9])) {
                // System.out.println("Corner Pipe Bottom");

                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin + 1, col + 1);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin, col, waterLayer);
                        board.draw(gelems[11], lin + 1, col + 1, waterLayer);

                    }

                    msGelem.setText(String.valueOf(volumeWater + waterVol));
                }
                waterMovementInMap(lin + 1, col + 2, dest, waterVol);

            } else if (board.exists(lin - 2, col)
                    && board.topGelem(lin - 2, col, pipeLayer, pipeLayer).equals(gelems[6])) {

                // System.out.println("Split Pipe V2 GO UP ");
                /*
                 * System.out.println(lin+" "+col); System.out.println(board.topGelem(lin - 2,
                 * col, pipeLayer, pipeLayer).x(0, 0)); System.out.println(board.topGelem(lin -
                 * 2, col, pipeLayer, pipeLayer).y(0, 0));
                 * System.out.println(board.topGelem(lin, col, pipeLayer, pipeLayer).x(0, 0));
                 * System.out.println(board.topGelem(lin, col, pipeLayer, pipeLayer).y(0, 0));
                 * System.out.println("TRGIN");
                 */
                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin, col, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));

                    // split up
                    if (dest.line() == lin - 1) {
                        msGelem = (MutableStringGelem) board.topGelem(lin - 1, col + 1);
                        volumeWater = Integer.parseInt(msGelem.text());

                        if (volumeWater == 0) {
                            board.draw(gelems[10], lin - 1, col, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));

                        waterMovementInMap(lin - 1, col + 2, dest, waterVol);
                    }
                    // split down
                    else {
                        msGelem = (MutableStringGelem) board.topGelem(lin - 2, col);
                        volumeWater = Integer.parseInt(msGelem.text());

                        if (volumeWater == 0) {
                            board.draw(gelems[11], lin - 2, col, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));

                        waterMovementInMap(lin - 3, col, dest, waterVol);
                    }
                }
            } else if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[6])) {

                // System.out.println("Split Pipe V2 GO Down");

                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin, col, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));

                    // split up
                    if (dest.line() == lin + 1) {
                        msGelem = (MutableStringGelem) board.topGelem(lin + 1, col + 1);
                        volumeWater = Integer.parseInt(msGelem.text());

                        if (volumeWater == 0) {
                            board.draw(gelems[10], lin + 1, col, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));

                        waterMovementInMap(lin + 1, col + 2, dest, waterVol);
                    }
                    // split down
                    else {
                        msGelem = (MutableStringGelem) board.topGelem(lin + 2, col);
                        volumeWater = Integer.parseInt(msGelem.text());

                        if (volumeWater == 0) {
                            board.draw(gelems[11], lin + 2, col, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));

                        waterMovementInMap(lin + 3, col, dest, waterVol);
                    }
                }
            } else if (board.exists(lin, col - 1) && board.topGelem(lin, col - 1).equals(gelems[7])) {

                // System.out.println("Split Pipe V3 GO DOWN");

                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin, col, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));

                    msGelem = (MutableStringGelem) board.topGelem(lin + 2, col);

                    volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin + 2, col, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));
                }
                waterMovementInMap(lin + 3, col, dest, waterVol);
            } else if (board.exists(lin - 2, col - 1) && board.topGelem(lin - 2, col - 1).equals(gelems[7])) {

                // System.out.println("Split Pipe V3 GO UP");

                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin, col, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));

                    msGelem = (MutableStringGelem) board.topGelem(lin - 2, col);

                    volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater == 0) {
                        board.draw(gelems[11], lin - 2, col, waterLayer);
                    }
                    msGelem.setText(String.valueOf(volumeWater + waterVol));
                }
                waterMovementInMap(lin - 3, col, dest, waterVol);
            }
            // Split pipe 3
            else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col).equals(gelems[7])) {

                // System.out.println("Split Pipe V3 Entrance Left");

                // First straight part
                if (!board.exists(gelems[10], lin, col, waterLayer)) {
                    board.draw(gelems[10], lin, col, waterLayer);
                }

                // split up
                if (dest.line() < lin) {

                    synchronized (this) {

                        MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin - 1, col + 1);
                        int volumeWater = Integer.parseInt(msGelem.text());

                        if (volumeWater == 0) {
                            board.draw(gelems[11], lin - 1, col + 1, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));
                    }
                    waterMovementInMap(lin - 2, col + 1, dest, waterVol);
                }
                // split down
                else {
                    synchronized (this) {
                        MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin + 1, col + 1);
                        int volumeWater = Integer.parseInt(msGelem.text());

                        if (volumeWater == 0) {
                            board.draw(gelems[11], lin + 1, col + 1, waterLayer);
                        }
                        msGelem.setText(String.valueOf(volumeWater + waterVol));
                    }
                    waterMovementInMap(lin + 2, col + 1, dest, waterVol);
                }
            }

            else {
                System.out.println("MOVING WATER - TYPE OF PIPE NO FOUND");
            }
        } catch (Exception e) {

        }

        return result;

    }

    /**
     * @param lin
     * @param col
     * @param dest
     */
    public void removeMark(int lin, int col, Position dest, int waterVol) {

        try {
            board.sleep(Configuration.MAP_UPDATE_SPEED);
        } catch (Exception e) {
        }

        // System.out.println(lin+" "+col);
        // System.out.println(board.exists(lin , col));
        // System.out.println(board.topGelem(lin, col, pipeLayer,
        // pipeLayer).equals(gelems[7]));
        // System.out.println(board.exists(lin , col) && board.topGelem(lin, col,
        // pipeLayer, pipeLayer).equals(gelems[7]));

        if (dest.line() == lin && dest.column() == col)
            return;

        if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[3])) {
            // if (maze.roadSymbol(lin, col) == Configuration.HORIZONTAL_PIPE_SYMBOL) {

            // System.out.println("Horizontal Pipe");
            synchronized (this) {

                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));
            }
            removeMark(lin, col + 2, dest, waterVol);

        } else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[8])) {
            // else if (maze.roadSymbol(lin - 1, col) ==
            // Configuration.CORNER_PIPE_UP_SYMBOL) {
            // System.out.println("Corner Pipe top");
            synchronized (this) {
                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin - 1, col + 1);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin - 1, col + 1, waterLayer, waterLayer);
                    board.erase(lin, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));
            }

            removeMark(lin - 1, col + 2, dest, waterVol);

            // Corner Pipe Bottom
        } else if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[9])) {
            // else if (maze.roadSymbol(lin + 1, col) ==
            // Configuration.CORNER_PIPE_DOWN_SYMBOL) {
            // System.out.println("Corner Pipe Down");
            synchronized (this) {

                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin + 1, col + 1);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin + 1, col + 1, waterLayer, waterLayer);
                    board.erase(lin, col, waterLayer, waterLayer);
                }
                msGelem.setText(String.valueOf(volumeWater - waterVol));
            }
            removeMark(lin + 1, col + 2, dest, waterVol);

        } else if (board.exists(lin - 2, col) && board.topGelem(lin - 2, col, pipeLayer, pipeLayer).equals(gelems[6])) {
            // else if (maze.roadSymbol(lin - 1, col) ==
            // Configuration.T_JUNCTION_PIPE_2_SYMBOL) {
            // System.out.println("T-Junction V2 Pipe Water Goes Up");

            synchronized (this) {

                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0)
                    board.erase(lin, col, waterLayer, waterLayer);

                msGelem.setText(String.valueOf(volumeWater - waterVol));

                // split up
                if (dest.line() == lin - 1) {
                    msGelem = (MutableStringGelem) board.topGelem(lin - 1, col + 1);
                    volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater - waterVol == 0)
                        board.erase(gelems[10], lin - 1, col, waterLayer);

                    msGelem.setText(String.valueOf(volumeWater - waterVol));

                    removeMark(lin - 1, col + 2, dest, waterVol);
                }
                // split down
                else {
                    msGelem = (MutableStringGelem) board.topGelem(lin - 2, col);
                    volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater - waterVol == 0)
                        board.erase(gelems[11], lin - 2, col, waterLayer);

                    msGelem.setText(String.valueOf(volumeWater - waterVol));

                    removeMark(lin - 3, col, dest, waterVol);
                }
            }
        } else if (board.exists(lin, col) && board.topGelem(lin, col, pipeLayer, pipeLayer).equals(gelems[6])) {
            // System.out.println("T-Junction V2 Pipe Water Goes Down");

            synchronized (this) {

                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));

                // split up
                if (dest.line() == lin + 1) {
                    msGelem = (MutableStringGelem) board.topGelem(lin + 1, col + 1);
                    volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater - waterVol == 0)
                        board.erase(lin + 1, col, waterLayer, waterLayer);

                    msGelem.setText(String.valueOf(volumeWater - waterVol));

                    removeMark(lin + 1, col + 2, dest, waterVol);
                }
                // split down
                else {
                    msGelem = (MutableStringGelem) board.topGelem(lin + 2, col);
                    volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater - waterVol == 0)
                        board.erase(gelems[11], lin + 2, col, waterLayer);

                    msGelem.setText(String.valueOf(volumeWater - waterVol));

                    removeMark(lin + 3, col, dest, waterVol);
                }
            }
        } else if (board.exists(lin - 2, col - 1)
                && board.topGelem(lin - 2, col - 1, pipeLayer, pipeLayer).equals(gelems[7])) {
            // else if (maze.roadSymbol(lin - 1, col) ==
            // Configuration.T_JUNCTION_PIPE_3_SYMBOL) {

            // System.out.println("T-Junction Pipe V3 Goes UP");
            synchronized (this) {

                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));

                msGelem = (MutableStringGelem) board.topGelem(lin - 2, col);
                volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin - 2, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));
            }

            removeMark(lin - 3, col, dest, waterVol);

        } else if (board.exists(lin, col - 1) && board.topGelem(lin, col - 1, pipeLayer, pipeLayer).equals(gelems[7])) {
            // else if (maze.roadSymbol(lin + 1, col) ==
            // Configuration.T_JUNCTION_PIPE_3_SYMBOL) {

            // System.out.println("T-Junction Pipe V3 Goes DOWN");
            synchronized (this) {

                MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin, col);
                int volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));

                msGelem = (MutableStringGelem) board.topGelem(lin + 2, col);
                volumeWater = Integer.parseInt(msGelem.text());

                if (volumeWater - waterVol == 0) {
                    board.erase(lin + 2, col, waterLayer, waterLayer);
                }

                msGelem.setText(String.valueOf(volumeWater - waterVol));
            }

            removeMark(lin + 3, col, dest, waterVol);

        } else if (board.exists(lin - 1, col) && board.topGelem(lin - 1, col, pipeLayer, pipeLayer).equals(gelems[7])) {
            // else if (maze.roadSymbol(lin, col+1) ==
            // Configuration.T_JUNCTION_PIPE_3_SYMBOL) {

            // System.out.println("T-Junction Pipe V3");

            // if up
            if (dest.line() < lin) {
                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin - 1, col + 1);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater - waterVol == 0) {
                        board.erase(lin - 1, col + 1, waterLayer, waterLayer);
                    }

                    msGelem.setText(String.valueOf(volumeWater - waterVol));

                    if (!(board.exists(lin - 1, col + 1, waterLayer) || board.exists(lin + 1, col + 1, waterLayer)))
                        board.erase(lin, col, waterLayer, waterLayer);
                }

                removeMark(lin - 2, col + 1, dest, waterVol);

            }

            // if down
            else {
                synchronized (this) {

                    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(lin + 1, col + 1);
                    int volumeWater = Integer.parseInt(msGelem.text());

                    if (volumeWater - waterVol == 0) {
                        board.erase(lin + 1, col + 1, waterLayer, waterLayer);
                    }

                    msGelem.setText(String.valueOf(volumeWater - waterVol));

                    if (!(board.exists(lin - 1, col + 1, waterLayer) || board.exists(lin + 1, col + 1, waterLayer)))
                        board.erase(lin, col, waterLayer, waterLayer);
                }

                removeMark(lin + 2, col + 1, dest, waterVol);
            }
        } else {
            System.out.println("INSIDE THE ELSE");
            System.out.println(lin);
            System.out.println(col);
        }
    }

    
    /** 
     * @param p
     * @return boolean
     */
    public synchronized boolean validPosition(Position p) {
        try {
            assert p != null : "Destination variable can't be null.";
            assert p.line() >= 0 && p.line() < board.numberOfLines() : "Y position isn't valid.";
            ;
            assert p.column() >= 0 && p.column() < board.numberOfColumns() : "X position isn't valid";

            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}