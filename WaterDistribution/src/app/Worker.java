package app;

import interfaces.IPerson;
import pt.ua.concurrent.CThread;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

/**
 * Worker
 */
public class Worker extends CThread //implements IPerson, Runnable  
{

  private SharedDeposit dep;
  private SharedConsole console;

  public Worker (SharedDeposit dep, SharedConsole console){
    this.dep = dep;
    this.console = console;
  }

  @Override
  public void run() {
    out.println("> STARTING WORKER THREAD #");
    while(true){
      Position destination = console.readConsole();
      console.startReplenishing(destination);
      dep.refillDeposit();
      console.stopReplenishing(destination);

      //interactConsole();
      //interactDeposit();
    }
  }

  //@Override
  public void interactDeposit() {
    dep.refillDeposit();
  }

  //@Override
  public void interactConsole() {
    console.readConsole();
  }
}