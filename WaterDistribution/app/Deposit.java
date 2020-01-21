package app;

import pt.ua.gboard.basic.Position;
import pt.ua.concurrent.Console;

public class Deposit {

  /**
   * How much water, in liters, can a deposit store.
   */
  private final int maxCapacity;

  /**
   * How much water in liters is present in the deposit
   */
  private Map map;
  private int waterLevel;
  private int id;
  private Position position;

  /**
   * @param maxCapacity
   * @param line_pos
   * @param column_pos
   * @return
   */
  public Deposit(int maxCapacity, int id, Position position, Map map) {
    this.maxCapacity = maxCapacity;
    this.waterLevel = maxCapacity;
    this.id = id;
    this.position = position;
    this.map = map;
  }

  /**
   * @return int
   */
  public int useWater(Position dest) {

    assert (!isEmpty() && hasEnoughWater(5)) : "No more water available";
    assert (map.validPosition(dest)) : "Destination is outside map";

    waterLevel = waterLevel - 5;
    // map.waterMovementInMap(position.line(), position.column() + 1, dest, 5);

    return 5;
  }

  
  /** 
   * @param volume
   * @return boolean
   */
  public boolean hasEnoughWater(int volume) {
    assert volume >= 0;
    return waterLevel >= volume;
  }

  
  /** 
   * @return boolean
   */
  public boolean isEmpty() {
    return waterLevel == 0;
  }

  public void refill() {
    waterLevel = maxCapacity;
    Console.println(Console.YELLOW,"> DEPOSIT #" + id + " REFILLED.");
  }

  
  /** 
   * @param dest
   */
  public void startRepleneshing(Position dest) {
    try {
      map.waterMovementInMap(position.line(), position.column() + 1, dest, 5);
    } finally {
    }
  }

  
  /** 
   * @param dest
   */
  public void stopRepleneshing(Position dest) {
    try {
      map.removeMark(position.line(), position.column() + 1, dest, 5);
    } finally {
    }
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  
  /** 
   * @return String
   */
  @Override
  public String toString() {
    return "Deposit [Position=" + position + ", maxCapacity=" + maxCapacity + ", waterLevel=" + waterLevel + "]";
  }
}