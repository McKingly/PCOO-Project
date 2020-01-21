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
    Console.println(Console.YELLOW,"> ALERT SENT TO CONSOLE - DEPOSIT #" + depositId + " EMPTY.");
    queue.add(depositId);
  }

  
  /** 
   * @return Position
   */
  public Position removeAlert() {
    int depositId = queue.remove();
    return map.depositsPositions[depositId];
  }

  
  /** 
   * @param destination
   */
  public void startReplenishing(Position destination) {
    map.waterMovementInMap(position.line(), position.column() + 1, destination, 20);
  }

  
  /** 
   * @param destination
   */
  public void stopReplenishing(Position destination) {
    map.removeMark(position.line(), position.column() + 1, destination, 20);
  }
}