package app;

import pt.ua.concurrent.*;
import pt.ua.gboard.basic.Position;


/**
 * SharedAlertConsole
 */
public class SharedAlertConsole {

  protected AlertConsole console;
  private final Mutex mtx = new Mutex();
  private final MutexCV mtxCV = mtx.newCV();

  
  /** 
   * @param console
   * @return 
   */
  public SharedAlertConsole(AlertConsole console) {
    assert console != null;
    this.console = console;
  }

  
  /** 
   * @param depositId
   */
  public void addAlert(int depositId) {
    assert depositId >= 0;
    mtx.lock();
    try {
      console.addAlert(depositId);
      mtxCV.broadcast();
    } finally {
      mtx.unlock();
    }
  }

  
  /** 
   * @return int
   */
  public int readConsole() {
    mtx.lock();
    try {
      while (console.isEmpty())
        mtxCV.await();
      return console.removeAlert();
    } finally {
      mtx.unlock();
    }
  }
  
  /** 
   * @param destination
   */
  public void startReplenishing(Position destination, int waterVol) {
    assert waterVol >=0;
    mtx.lock();
    try {
      console.startStopReplenishing(destination,waterVol);
    } finally {
      mtx.unlock();
    }
  }

  
  /** 
   * @param destination
   */
  public void stopReplenishing(Position destination, int waterVol) {
    assert waterVol>=0;
    mtx.lock();
    try {
      console.startStopReplenishing(destination, -waterVol);
    } finally {
      mtx.unlock();
    }
  }
}