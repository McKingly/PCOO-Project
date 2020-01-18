package app;

import pt.ua.concurrent.*;

// Implement Actor here

/**
 * Console
 */
public class Console {

  private Boolean hasAlert;

  public Console(){
    hasAlert = false;
  }

  public void addAlert(){
    hasAlert = true;
  }

  public boolean hasAlert(){
    return hasAlert;
  }

  public void removeAlert(){
    hasAlert = false;
  }
}