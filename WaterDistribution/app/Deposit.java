package app;

import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

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
    this.id = id;
    this.map = map;
    this.position = position;
    this.waterLevel = maxCapacity;
    this.maxCapacity = maxCapacity;
  }

  
  /** 
   * @param dest
   * @param waterVolume
   */
  public void useWater(Position dest, int waterVolume) {
    assert waterVolume >= 0 : "Water consumed can't be lower than 0";
    assert (!isEmpty() && hasEnoughWater(waterVolume)) : "No more water available";
    assert (map.validPosition(dest)) : "Destination is outside map";

    waterLevel -= waterVolume;
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
    Console.println(Console.YELLOW, "> DEPOSIT #" + id + " REFILLED.");
  }

  /**
   * @param dest
   */
  public void startStopRepleneshing(Position dest, int volWater) {
    assert dest != null;
    assert map.validPosition(dest);
    map.updateMap(position.line(), position.column() + 1, dest, volWater);
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @return the maxCapacity
   */
  public int getMaxCapacity() {
    return maxCapacity;
  }
}