package app;

import pt.ua.concurrent.*;
import pt.ua.gboard.basic.Position;
import pt.ua.concurrent.MutexCV;


public class SharedDeposit {

  protected final Deposit deposit;
  private final Mutex mtx = new Mutex();
  private final MutexCV mtxCV = mtx.newCV();

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
      assert (!deposit.isEmpty()) : "ASSERT > No more water available";
      while (!deposit.hasEnoughWater(5)){
        System.out.println("Not enough water for Person #" + id);

        mtxCV.await();
      }

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

  public boolean hasEnoughWater(int volume){
    mtx.lock();
    boolean answer = false;
    try {
      answer = deposit.hasEnoughWater(volume);
    } finally{
      mtx.unlock();
    }
    return answer;
  }

  /**
   * @param dest
   */
  public void stopRepleneshing(int id, Position dest) {
    mtx.lock();

    try {
      deposit.stopRepleneshing(dest);
    } finally {
      System.out.println("Thread #"+id+" needing repleneshing water.");
      mtx.unlock();
    }
  }

}