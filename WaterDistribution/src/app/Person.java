package app;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

public class Person extends CThread // implements IPerson
{

  private int id;
  private int totalWaterConsumed;
  private SharedDeposit dep;
  private SharedConsole con;
  private Position position;

  private static int counter = 0;

  
  /** 
   * @param dep
   * @param con
   * @param position
   * @return 
   */
  public Person(SharedDeposit dep, SharedConsole con, Position position) {
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
        Console.println(Console.BLUE,
            " > PERSON THREAD #" + id + " CONSUMING " + waterConsumed + " LITTERS FROM DEPOSIT #" + dep.getId());
        dep.startRepleneshing(id, position);
        dep.stopRepleneshing(id, position);

        totalWaterConsumed += waterConsumed;
        if (totalWaterConsumed >= 25) {
          Console.println(Console.BLUE,
              " > PERSON THREAD #" + id + " CONSUMED " + totalWaterConsumed + "LITTERS TOTAL.");
          break;
        }
      }
    } catch (AssertionError e) {
      // con.addAlert(dep.getId());
    }
  }
}