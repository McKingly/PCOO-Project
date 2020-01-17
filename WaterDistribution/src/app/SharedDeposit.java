package app;

import pt.ua.concurrent.*;
import pt.ua.gboard.basic.Position;

public class SharedDeposit {

  protected final Deposit deposit;
  private final Mutex mtx = new Mutex();

  /**
   * @param maxCapacity
   * @param line_pos
   * @param column_pos
   * @return
   */
  public SharedDeposit(Deposit deposit) {
    this.deposit = deposit;
  }

  /**
   * @return int
   */
  public int useWater(Position dest) {
    mtx.lock();

    try {
      assert dest != null;  
      assert (!deposit.isEmpty()) : "No more water available";

      return deposit.useWater(dest);
      
    } finally {
      mtx.unlock();
    }
  }

  public void closeValve() {

  }

  /**
   * @param dest
   */
  public void stopRepleneshing(Position dest) {
    mtx.lock();

    try {
      deposit.stopRepleneshing(dest);
    } finally {
      System.out.println("Deposit stopped repleneshing water.");
      mtx.unlock();
    }
  }

  public void refill() {
    mtx.lock();

    try {
      deposit.refill();
    } finally {
      System.out.println("Deposit refilled.");
      mtx.unlock();
    }
  }

  /**
   * @return String
   */
  @Override
  public String toString() {
    return deposit.toString();
  }
}