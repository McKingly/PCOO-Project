package app;

import interfaces.IPerson;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

/**
 * Worker
 */
public class Worker implements IPerson, Runnable  {

  private SharedDeposit dep;
  private SharedConsole console;

  public Worker (SharedDeposit dep, SharedConsole console){
    this.dep = dep;
    this.console = console;
  }

  @Override
  public void run() {
    System.out.println(" > STARTING WORKER THREAD #");
    while(true){
      Position destination = console.readConsole();
      dep.refillDeposit();
      console.stopReplenishing(destination);

      //interactConsole();
      //interactDeposit();
    }
  }

  @Override
  public void interactDeposit() {
    dep.refillDeposit();
  }

  @Override
  public void interactConsole() {
    console.readConsole();
  }
}