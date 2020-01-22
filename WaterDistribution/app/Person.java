package app;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

public class Person extends CThread // implements IPerson
{

  private int id;
  private int totalWaterConsumed;
  private SharedDeposit dep;
  private SharedAlertConsole con;
  private Position position;

  private static int counter = 0;

  
  /** 
   * @param dep
   * @param con
   * @param position
   * @return 
   */
  public Person(SharedDeposit dep, SharedAlertConsole con, Position position) {
    totalWaterConsumed = 0;
    this.id = counter++;
    this.dep = dep;
    this.con = con;
    this.position = position;
  }

  public void run() {
    Console.println(Console.GREEN, "> STARTING PERSON THREAD #" + id);
    try {
      int waterConsumed;
      while (true) {
        waterConsumed = dep.useWater(id, position, con);
        dep.startRepleneshing(id, position);
        Console.println(Console.BLUE,
            "> PERSON THREAD #" + id + " CONSUMING " + waterConsumed + " LITTERS FROM DEPOSIT #" + dep.getId());
        dep.stopRepleneshing(id, position);

        totalWaterConsumed += waterConsumed;
        if (totalWaterConsumed >= 10) {
          Console.println(Console.GREEN,"> PERSON THREAD #" + id + " CONSUMED A TOTAL OF:\n    > " + totalWaterConsumed + " LITTERS OF WATER.");
          break;
        }
      }
    } catch (AssertionError e) {
      // con.addAlert(dep.getId());
    }
  }
}