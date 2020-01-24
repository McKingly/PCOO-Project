package app;

import java.awt.Color;

import configuration.Configuration;
import pt.ua.gboard.GBoard;
import pt.ua.gboard.Gelem;
import pt.ua.gboard.basic.FilledGelem;
import pt.ua.gboard.basic.ImageGelem;
import pt.ua.gboard.basic.MutableStringGelem;
import pt.ua.gboard.basic.Position;
import pt.ua.gboard.games.Labyrinth;

public class Map {

  GBoard board;

  Gelem[] gelems;

  public Position[] depositsPositions;
  public Position[] consolesPositions;
  public Position[] housesPositions;

  private final char[] extraSymbols = { Configuration.DEPOSIT_SYMBOL, Configuration.CONSOLE_SYMBOL,
      Configuration.PERSON_SYMBOL, Configuration.HORIZONTAL_PIPE_SYMBOL, Configuration.VERTICAL_PIPE_SYMBOL,
      Configuration.T_JUNCTION_PIPE_1_SYMBOL, Configuration.T_JUNCTION_PIPE_2_SYMBOL,
      Configuration.T_JUNCTION_PIPE_3_SYMBOL, Configuration.CORNER_PIPE_UP_SYMBOL,
      Configuration.CORNER_PIPE_DOWN_SYMBOL, ' ' };

  private final int pipeLayer = 0;
  private final int waterLayer = 1;
  private final int numberLayer = 2;

  private final int updateSpeed;

  private Labyrinth maze = null;

  /**
   * @param mapFile
   * @return
   */
  public Map(String mapFile, int updateSpeed) {

    maze = new Labyrinth(mapFile, extraSymbols, 1, true);
    int numberColumns = maze.board.numberOfColumns();
    int numberLines = maze.board.numberOfLines();

    this.updateSpeed = updateSpeed;

    this.board = new GBoard("Water Distribution Map", numberLines, numberColumns, 3);

    this.gelems = new Gelem[] { new ImageGelem(Configuration.DEPOSIT, board, 100.0, 1, 1), // DEPOSIT [0]
        new ImageGelem(Configuration.CONSOLE, board, 100.0, 3, 3), // CONSOLE [1]
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
      board.draw(gelems[1], pipe.line() - 1, pipe.column() - 2, pipeLayer);
      System.out.println(gelems[1]);
    }

    this.housesPositions = maze.roadSymbolPositions(Configuration.PERSON_SYMBOL);
    for (Position pipe : housesPositions) {
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

    Position[] tJunction3Positions = maze.roadSymbolPositions(Configuration.T_JUNCTION_PIPE_3_SYMBOL);
    for (Position pipe : tJunction3Positions) {
      board.draw(gelems[7], pipe.line() - 1, pipe.column() - 1, pipeLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column() - 1, numberLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line() - 1, pipe.column(), numberLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line() + 1, pipe.column(), numberLayer);
    }

    for (Position pipe : maze.roadSymbolPositions(Configuration.CORNER_PIPE_UP_SYMBOL)) {
      board.draw(gelems[8], pipe.line(), pipe.column(), pipeLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line() + 1, pipe.column(), numberLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column() + 1, numberLayer);
    }

    for (Position pipe : maze.roadSymbolPositions(Configuration.CORNER_PIPE_DOWN_SYMBOL)) {
      board.draw(gelems[9], pipe.line() - 1, pipe.column(), pipeLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line() - 1, pipe.column(), numberLayer);
      board.draw(new MutableStringGelem("0", Color.red), pipe.line(), pipe.column() + 1, numberLayer);
    }

  }

  
  /** 
   * @param lin
   * @param col
   * @param dest
   * @param newWaterVol
   */
  public synchronized void updateMap(int lin, int col, Position dest, int newWaterVol) {
    assert validPosition(dest);

    GBoard.sleep(updateSpeed);

    if (dest.line() == lin && dest.column() == col)
      return;

    Gelem currentPipe = board.topGelem(lin, col, pipeLayer, pipeLayer);

    if (currentPipe.equals(gelems[3])) {
      // System.out.println("Horizontal Pipe");
      updateVolumeWaterGelem(new Position(lin, col), newWaterVol, true);
      updateMap(lin, col + 2, dest, newWaterVol);

    } else if (currentPipe.equals(gelems[4])) {
      // System.out.println("Vertical Pipe");
      if (board.topGelem(lin, col, numberLayer, numberLayer) == null) {
        updateVolumeWaterGelem(new Position(lin - 1, col), newWaterVol, false);
        updateMap(lin - 2, col, dest, newWaterVol);

      } else {
        updateVolumeWaterGelem(new Position(lin, col), newWaterVol, false);
        updateMap(lin + 2, col, dest, newWaterVol);
      }
      // Corner Pipe Top
    } else if (currentPipe.equals(gelems[8])) {
      // System.out.println("Corner Pipe top");
      updateVolumeWaterGelem(new Position(lin, col), newWaterVol, false);
      updateVolumeWaterGelem(new Position(lin - 1, col + 1), newWaterVol, false);
      updateMap(lin - 1, col + 2, dest, newWaterVol);

      // Corner Pipe Bottom
    } else if (currentPipe.equals(gelems[9])) {
      // System.out.println("Corner Pipe Down");
      updateVolumeWaterGelem(new Position(lin, col), newWaterVol, false);
      updateVolumeWaterGelem(new Position(lin + 1, col + 1), newWaterVol, false);
      updateMap(lin + 1, col + 2, dest, newWaterVol);

      // T-Junction Pipe V2
    } else if (currentPipe.equals(gelems[6])) {
      // System.out.println("T-Junction Pipe V2");
      updateVolumeWaterGelem(new Position(lin, col), newWaterVol, false);

      if (board.exists(gelems[6], lin - 2, col)) {
        // System.out.println(" > Enters through the bottom opening");
        // Exits through the top opening
        if (dest.line() == lin - 1) {
          updateVolumeWaterGelem(new Position(lin - 1, col + 1), newWaterVol, true);
          updateMap(lin - 1, col + 2, dest, newWaterVol);
          // Exits through the right opening
        } else {
          updateVolumeWaterGelem(new Position(lin - 2, col), newWaterVol, false);
          updateMap(lin - 3, col, dest, newWaterVol);
        }
      } else {
        // System.out.println(" > Enters through the top opening");
        // Exits through the right opening
        if (dest.line() == lin + 1) {
          updateVolumeWaterGelem(new Position(lin + 1, col + 1), newWaterVol, true);
          updateMap(lin + 1, col + 2, dest, newWaterVol);
          // Exits through the bottom opening
        } else {
          updateVolumeWaterGelem(new Position(lin + 2, col), newWaterVol, false);
          updateMap(lin + 3, col, dest, newWaterVol);
        }
      }

      // T-Junction Pipe V3
    } else if (currentPipe.equals(gelems[7])) {

      // System.out.println("T-Junction Pipe V3");
      if (board.exists(gelems[7], lin - 1, col)) {
        // System.out.println(" > Enters through the left opening");
        updateVolumeWaterGelem(new Position(lin, col), newWaterVol, true);

        // Exits through the bottom opening
        if (lin > dest.line()) {
          updateVolumeWaterGelem(new Position(lin - 1, col + 1), newWaterVol, false);
          updateMap(lin - 2, col + 1, dest, newWaterVol);

          // Exits through the top opening
        } else {
          updateVolumeWaterGelem(new Position(lin + 1, col + 1), newWaterVol, false);
          updateMap(lin + 2, col + 1, dest, newWaterVol);
        }

      } else {

        updateVolumeWaterGelem(new Position(lin, col), newWaterVol, false);

        if (board.exists(gelems[7], lin, col - 1)) {
          // System.out.println(" > Enters through the top opening");
          updateVolumeWaterGelem(new Position(lin + 2, col), newWaterVol, false);
          updateMap(lin + 3, col, dest, newWaterVol);

        } else {
          // System.out.println(" > Enters through the bottom opening");
          updateVolumeWaterGelem(new Position(lin - 2, col), newWaterVol, false);
          updateMap(lin - 3, col, dest, newWaterVol);

        }
      }
    } else {
      System.err.println("INVALID PIPE TYPE FOUND");
      ;
      System.exit(1);
    }

  }

  /**
   * @param p
   * @return boolean
   */
  public boolean validPosition(Position p) {
    try {
      assert p != null : "Destination variable can't be null.";
      assert p.line() >= 0 && p.line() < board.numberOfLines() : "Y position isn't valid.";
      assert p.column() >= 0 && p.column() < board.numberOfColumns() : "X position isn't valid";

      return true;
    } catch (AssertionError e) {
      return false;
    }
  }

  /**
   * @param p
   * @param newWaterVol
   * @param isPipeHorizontal
   */
  private void updateVolumeWaterGelem(Position p, int newWaterVol, boolean isPipeHorizontal) {

    MutableStringGelem msGelem = (MutableStringGelem) board.topGelem(p.line(), p.column(), numberLayer, numberLayer);
    int pipeWaterVol = Integer.parseInt(msGelem.text());

    // When the flow of water in a pipe is decreasing
    if (pipeWaterVol + newWaterVol == 0) {
      if (board.topGelem(p.line(), p.column(), waterLayer, waterLayer).equals(gelems[10]))
        if (board.topGelem(p.line(), p.column(), pipeLayer, pipeLayer).equals(gelems[6]))
          board.erase(gelems[10], p.line(), p.column() - 1, waterLayer);
        else
          board.erase(gelems[10], p.line(), p.column(), waterLayer);
      else
        board.erase(gelems[11], p.line(), p.column(), waterLayer);

      // When the flow of water in a pipe is increasing
    } else if (pipeWaterVol == 0) {
      if (isPipeHorizontal)
        if (board.topGelem(p.line(), p.column(), pipeLayer, pipeLayer).equals(gelems[6]))
          board.draw(gelems[10], p.line(), p.column() - 1, waterLayer);
        else
          board.draw(gelems[10], p.line(), p.column(), waterLayer);
      else
        board.draw(gelems[11], p.line(), p.column(), waterLayer);
    }
    msGelem.setText(String.valueOf(pipeWaterVol + newWaterVol));
  }
}