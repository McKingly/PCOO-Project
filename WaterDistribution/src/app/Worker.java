package app;

import interfaces.IPerson;

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
    while(true){
      interactConsole();
      interactDeposit();
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