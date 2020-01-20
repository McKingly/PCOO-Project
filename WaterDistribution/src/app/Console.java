package app;

import java.util.ArrayList;
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

  public Console(Position position, Map map){
    this.map = map;
    this.position = position;
  }

  public boolean isEmpty(){
    return queue.isEmpty();
  }

  public void addAlert(int depositId){
    System.out.println("> ALERT SENT TO CONSOLE");
    queue.add(depositId);
  }

  public Position removeAlert(){
    int depositId = queue.remove();
    return map.depositsPositions[depositId];
  }
  
  public void startReplenishing(Position destination){
    map.waterMovementInMap(position.line() , position.column()+1, destination, 20);
  }
  
  public void stopReplenishing(Position destination){
    map.removeMark(position.line() , position.column()+1, destination, 20);
  }
}