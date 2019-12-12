package app;

import pt.ua.concurrent.*;

public class Deposit {

  /** 
  * How much water, in liters, can a deposit store. 
  */
  private final int maxCapacity;

  /** 
  * How much water in liters is present in the deposit 
  */
  private int waterLevel;

  private boolean isEmpty;
  private int line_pos, column_pos;
  private final Mutex mtx = new Mutex();

  
  
  /** 
   * @param maxCapacity
   * @param line_pos
   * @param column_pos
   * @return 
   */
  public Deposit(int maxCapacity, int line_pos, int column_pos) {
    this.maxCapacity = maxCapacity;
    this.waterLevel = maxCapacity;
    this.isEmpty = false;
    this.line_pos = line_pos;
    this.column_pos = column_pos;
  }

  
  /** 
   * @return int
   */
  public int useWater() {
    mtx.lock();
    
    try {
      assert (!isEmpty) : "No more water available";
      waterLevel = waterLevel - 10;
      App.waterMovementInMap(line_pos, column_pos);
      
      //App.removeMark(line_pos, column_pos + 1);
      if (waterLevel == 0) {
        isEmpty = true;
        System.out.println("Water deposit has run dry.");
      }
  
      return 10;
   
    } finally{
      mtx.unlock();
    }
  }

  public void closeValve() {

  }


  public void stopRepleneshing() {
    App.removeMark(line_pos, column_pos + 1);
  }

  public void refill() {
    waterLevel = maxCapacity;
    isEmpty = false;
  }

  
  @Override
  public String toString() {
    return "Deposit [column_pos=" + column_pos + ", isEmpty=" + isEmpty + ", line_pos=" + line_pos + ", maxCapacity="
        + maxCapacity + ", waterLevel=" + waterLevel + "]";
  }
}