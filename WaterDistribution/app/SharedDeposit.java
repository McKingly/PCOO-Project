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
  public void useWater(int id, Position dest, SharedAlertConsole con, int waterVol) {
    assert con != null;
    assert dest != null;
    assert waterVol >=0;
    mtx.lock();
    try {
      while (!deposit.hasEnoughWater(waterVol)) {
        Console.println(Console.YELLOW, "> HOUSE #" + id + " NEEDS MORE WATER THAN IS AVAILABLE");
        con.addAlert(deposit.getId());
        mtxCV.await();
      }
      deposit.useWater(dest,waterVol);

    } finally {
      mtx.unlock();
    }
  }

  public void refillDeposit(int id) {
    assert id >= 0;
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
    assert volume > 0;
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
  public void startRepleneshing(int id, Position dest, int waterVol) {
    assert waterVol > 0;
    mtx.lock();
    try {
      System.out.println("> HOUSE #" + id + " FETCHING WATER.");
      deposit.startStopRepleneshing(dest, waterVol);
    } finally {
      mtx.unlock();
    }
  }

  /**
   * @param dest
   */
  public void stopRepleneshing(int id, Position dest, int waterVol) {
    mtx.lock();
    try {
      deposit.startStopRepleneshing(dest, -waterVol);
    } finally {
      System.out.println("> HOUSE #" + id + " STOPPED FETCHING WATER.");
      mtx.unlock();
    }
  }

  
  /** 
   * @return int
   */
  public int getId() {
    return deposit.getId();
  }

  /** 
   * @return int
   */
  public int getMaxCapacity() {
    return deposit.getMaxCapacity();
  }
}