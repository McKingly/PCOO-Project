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
    assert deposit != null;
    this.deposit = deposit;
    ;
  }

  /**
   * @return int
   */
  public int useWater(int id, Position dest, SharedAlertConsole con) {
    mtx.lock();
    try {
      assert dest != null;
      // assert (!deposit.isEmpty()) : "ASSERT > No more water available";
      while (!deposit.hasEnoughWater(5)) {
        Console.println(Console.YELLOW, "> PERSON THREAD #" + id + " NEEDS MORE WATER");
        con.addAlert(deposit.getId());
        mtxCV.await();
      }
      return deposit.useWater(dest);

    } finally {
      mtx.unlock();
    }
  }

  public void refillDeposit(int id) {
    mtx.lock();

    try {
      deposit.refill();
      mtxCV.broadcast();
    } finally {
      Console.println(Console.GREEN, "> DEPOSIT REFILLED BY WORKER #"+id);
      mtx.unlock();
    }
  }

  
  /** 
   * @param volume
   * @return boolean
   */
  public boolean hasEnoughWater(int volume) {
    mtx.lock();
    boolean answer = false;
    try {
      answer = deposit.hasEnoughWater(volume);
    } finally {
      mtx.unlock();
    }
    return answer;
  }

  
  /** 
   * @param id
   * @param dest
   */
  public void startRepleneshing(int id, Position dest) {
    mtx.lock();
    try {
      System.out.println("> PERSON THREAD #" + id + " FETCHING WATER.");
      deposit.startRepleneshing(dest);
    } finally {
      mtx.unlock();
    }
  }

  /**
   * @param dest
   */
  public void stopRepleneshing(int id, Position dest) {
    mtx.lock();
    try {
      deposit.stopRepleneshing(dest);
    } finally {
      System.out.println("> PERSON THREAD #" + id + " STOPPED FETCHING WATER.");
      mtx.unlock();
    }
  }

  
  /** 
   * @return int
   */
  public int getId() {
    return deposit.getId();
  }

  public void grab() {
    mtx.lock();
  }

  public void release() {
    mtx.unlock();
  }

  public void waitForWater() {
    mtxCV.await();
  }

}