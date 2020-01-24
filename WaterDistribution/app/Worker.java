package app;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

/**
 * Worker
 */
public class Worker extends CThread // implements IPerson, Runnable
{
  private int id;
  private SharedDeposit[] deposits;
  private SharedAlertConsole console;

  private static int counter = 0;
  
  /** 
   * @param dep
   * @param console
   * @return 
   */
  public Worker(int id, SharedDeposit[] deposits, SharedAlertConsole console) {
    assert deposits != null;
    assert console != null;
    this.id = counter++;
    this.console = console;
    this.deposits = deposits;
  }

  @Override
  public void run() {
    Console.println(Console.RED,"> STARTING WORKER THREAD #"+id);
    int depositId, waterVol;
    Position destination;
    while (true) {
      depositId = console.readConsole();
      waterVol = deposits[depositId].getMaxCapacity();
      destination = console.removeAlert();
      Console.println(Console.MAGENTA, "> WORKER #" + id + " TURNING VALVE TO REFILL DEPOSIT #"+depositId);
      console.startReplenishing(destination, waterVol);
      deposits[depositId].refillDeposit(id);
      console.stopReplenishing(destination, waterVol);
    }
  }
}