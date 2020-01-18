package app;

import pt.ua.concurrent.*;

// Implement Actor here

/**
 * Console
 */
public class SharedConsole {

  protected Console console;
  private final Mutex mtx = new Mutex();
  private final MutexCV mtxCV = mtx.newCV();

  public SharedConsole(Console console) {
    this.console = console;
  }

  public void addAlert(){
    mtx.lock();
    try {
      console.addAlert();
      mtxCV.broadcast();
    } finally {
      mtx.unlock();
    }
  }

  public void readConsole(){
    mtx.lock();
    try {
      while(!console.hasAlert()){
        mtxCV.await();
      }
      console.removeAlert();
    }finally{
      mtx.unlock();
    }
  }
}