package app;

import pt.ua.concurrent.*;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

/**
 * SharedConsole
 */
public class SharedConsole {

  protected Console console;
  private final Mutex mtx = new Mutex();
  private final MutexCV mtxCV = mtx.newCV();

  public SharedConsole(Console console) {
    this.console = console;
  }

  public void addAlert(int depositId){
    assert depositId >= 0;
    mtx.lock();
    try {
      console.addAlert(depositId);
      mtxCV.broadcast();
    } finally {
      mtx.unlock();
    }
  }

  public Position readConsole(){
    mtx.lock();
    try {
      while(console.isEmpty())
        mtxCV.await();
      return console.removeAlert();
    }finally{
      mtx.unlock();
    }
  }

  public void startReplenishing(Position destination){
    mtx.lock();
    try {
      console.startReplenishing(destination);  
    }finally{
      mtx.unlock();
    }
  }

  public void stopReplenishing(Position destination){
    mtx.lock();
    try {
      console.stopReplenishing(destination);  
    }finally{
      mtx.unlock();
    }
  }
}