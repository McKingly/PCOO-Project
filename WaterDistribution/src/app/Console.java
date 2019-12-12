package app;

import pt.ua.concurrent.*;

/**
 * Console
 */
public class Console {

  private Boolean hasAlert;
  private final Mutex mtx = new Mutex();

  public Console(){
    hasAlert = false;
  }

  public void addAlert(){
    mtx.lock();
    try {
      hasAlert = true;
    } finally {
      mtx.unlock();
    }
  }

  public boolean readConsole(){
    mtx.lock();
    try {
      return hasAlert;
    }finally{
      mtx.unlock();
    }
  }

  public void removeAlert(){
    mtx.lock();
    try {
      hasAlert = false;
    } finally {
      mtx.unlock();
    }
  }
}