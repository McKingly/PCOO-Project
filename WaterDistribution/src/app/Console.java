package app;

import java.util.LinkedList;
import java.util.Queue;

import pt.ua.gboard.basic.Position;

// Implement Actor here

/**
 * Console
 */
public class Console {

  private Map map;
  private Position position;
  private Queue<Integer> queue = new LinkedList<Integer>();

  
  /** 
   * @param position
   * @param map
   * @return 
   */
  public Console(Position position, Map map) {
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
    System.out.println("> ALERT SENT TO CONSOLE - DEPOSIT #" + depositId + " EMPTY.");
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