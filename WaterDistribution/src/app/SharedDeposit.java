package app;

import pt.ua.concurrent.*;
import pt.ua.gboard.basic.Position;
import pt.ua.concurrent.MutexCV;


public class SharedDeposit {

  protected final Deposit deposit;
  private final Mutex mtx = new Mutex();
  private MutexCV waterCV = mtx.newCV();
  private MutexCV mtxCV = mtx.newCV();

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
  public int useWater(int id, Position dest) {
    mtx.lock();

    try {
      assert dest != null;  
      assert (!deposit.isEmpty()) : "No more water available";
      while (!deposit.hasEnoughWater(5)){
        System.out.println("Not enough water for Person " + id);

        mtxCV.await();
      }

      System.out.println("Yummy water "+ id);
      return deposit.useWater(dest);
      
    } finally {
      mtx.unlock();
    }
  }

  public void refillDeposit() {
    mtx.lock();

    try {
      deposit.refill();
      mtxCV.broadcast();
    } finally {
      System.out.println("Deposit refilled.");
      mtx.unlock();
    }
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

  /**
   * @return String
   */
  @Override
  public String toString() {
    return deposit.toString();
  }
}