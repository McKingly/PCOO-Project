package app;

import java.util.Queue;
import java.util.LinkedList;

import pt.ua.concurrent.Console;

import pt.ua.gboard.basic.Position;

// Implement Actor here

/**
 * Console
 */
public class AlertConsole {

  private Map map;
  private Position position;
  private Queue<Integer> queue = new LinkedList<Integer>();

  /** 
   * @param position
   * @param map
   * @return 
   */
  public AlertConsole(Position position, Map map) {
    assert map != null: "Map can't be null";
    this.map = map;
    this.position = position;
  }

  
  /** 
   * @return boolean
   */
  public boolean isEmpty() {
    return queue.isEmpty();
  }

  
  /** 
   * @param depositId
   */
  public void addAlert(int depositId) {
    assert depositId >= 0: "Invalid depositID";
    if(!queue.contains(depositId)){
      Console.println(Console.YELLOW,"> ALERT SENT TO CONSOLE - DEPOSIT #" + depositId + " EMPTY.");
      queue.add(depositId);
    
    } else{
      Console.println(Console.YELLOW,"> ALERT FOR DEPOSIT #" + depositId + " ALREADY EXISTS.");
    }
  }
  
  /** 
   * @return Position
   */
  public int removeAlert() {
    assert !queue.isEmpty();
    return queue.remove();
  }

  
  /** 
   * @param destination
   */
  public void startStopReplenishing(Position destination, int waterVol) {
    assert destination != null;
    assert map.validPosition(destination);
    map.updateMap(position.line(), position.column() + 1, destination,  waterVol);
  }
}