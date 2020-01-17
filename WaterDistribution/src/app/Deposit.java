package app;

import pt.ua.gboard.basic.Position;

public class Deposit {

  /** 
  * How much water, in liters, can a deposit store. 
  */
  private final int maxCapacity;

  /** 
  * How much water in liters is present in the deposit 
  */
  private int waterLevel;
  private int line_pos, column_pos;
  private Map map;
  /** 
   * @param maxCapacity
   * @param line_pos
   * @param column_pos
   * @return 
   */
  public Deposit(int maxCapacity, int line_pos, int column_pos, Map map) {
    this.maxCapacity = maxCapacity;
    this.waterLevel = maxCapacity;
    this.line_pos = line_pos;
    this.column_pos = column_pos;
    this.map = map;
  }

  /** 
   * @return int
   */
  public int useWater(Position dest) {
    
      assert (!isEmpty()) : "No more water available";
      assert (map.validPosition(dest)) : "Destination is outside map";
      
      waterLevel = waterLevel - 5;
      map.waterMovementInMap(line_pos, column_pos + 1, dest, 5);
      
      //App.removeMark(line_pos, column_pos + 1);
      /*
      if (waterLevel == 0) {
        System.out.println("Water deposit has run dry.");
      }
      */
      return 5;
  }

  public void closeValve() {

  }

  public boolean isEmpty(){
    return waterLevel == 0;
  }

  public void stopRepleneshing(Position dest) {
    
    try {
      map.removeMark(line_pos, column_pos + 1, dest, 10);
    } finally{
      System.out.println("Deposit stopped repleneshing water.");
    }
  }

  public void refill() {
    
    try {
      waterLevel = maxCapacity;
    } finally{
      System.out.println("Deposit refilled.");
    }
  }

  @Override
  public String toString() {
    return "Deposit [column_pos=" + column_pos + ", line_pos=" + line_pos + ", maxCapacity="
        + maxCapacity + ", waterLevel=" + waterLevel + "]";
  }
}