package app;

import pt.ua.concurrent.CThread;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

/**
 * Worker
 */
public class Worker extends CThread // implements IPerson, Runnable
{
  private SharedAlertConsole console;
  private SharedDeposit[] deposits;
  private int id;
  
  /** 
   * @param dep
   * @param console
   * @return 
   */
  public Worker(int id, SharedDeposit[] deposits, SharedAlertConsole console) {
    this.console = console;
    this.deposits = deposits;
    this.id = id;
  }

  @Override
  public void run() {
    out.println("> STARTING WORKER THREAD #"+id);
    int depositId;
    Position destination;
    while (true) {
      depositId = console.readConsole();
      destination = console.removeAlert();
      console.startReplenishing(destination);
      deposits[depositId].refillDeposit(id);
      console.stopReplenishing(destination);
    }
  }
}