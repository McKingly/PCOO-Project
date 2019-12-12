package app;

import interfaces.IPerson;

import static java.lang.System.out;

/**
 * Worker
 */
public class Worker implements IPerson, Runnable  {

  private Deposit dep;
  private Console console;

  public Worker (Deposit dep, Console console){
    this.dep = dep;
    this.console = console;
  }

  @Override
  public void run() {
    while(true){
      if(console.readConsole()){
        out.println("Turning the valve");
        this.interactWaterDeposit();
        break;
      }
    }
  }

  @Override
  public boolean interactWaterDeposit() {
    dep.refill();
    out.println("Opening valve");
    console.removeAlert();
    return false;
  }

  @Override
  public void interactConsole() {
    out.print(console.readConsole());
    if(console.readConsole()){
      out.println("Turning the valve");
      this.interactWaterDeposit();
    }
  }
}