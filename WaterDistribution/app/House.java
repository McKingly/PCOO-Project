package app;

import java.util.Random;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

public class House extends CThread // implements IPerson
{

  private int id;
  private int totalWaterConsumed;
  private final Position position;
  private final int waterConsumptionRate;
  
  private SharedDeposit[] deposits;
  private final SharedAlertConsole console;
  
  private static int counter = 0;

  private final Random random = new Random();

  
  /** 
   * @param deposits
   * @param console
   * @param position
   * @param waterConsumptionRate
   * @return 
   */
  public House(SharedDeposit[] deposits, SharedAlertConsole console, Position position, int waterConsumptionRate) {
    assert deposits != null;
    assert console != null;
    assert position != null;
    assert waterConsumptionRate > 0;
    
    this.id = counter++;
    this.position = position;
    this.totalWaterConsumed = 0;
    this.waterConsumptionRate = waterConsumptionRate;
    
    this.deposits = deposits;
    this.console = console;
  }

  public void run() {
    Console.println(Console.GREEN, "> STARTING HOUSE THREAD #" + id);
    try {
      int depositID;
      while (true) {
        depositID = pickDeposit();
        deposits[depositID].useWater(id, position, console, waterConsumptionRate);
        deposits[depositID].startRepleneshing(id, position, waterConsumptionRate);
        Console.println(Console.BLUE,
            "> HOUSE #" + id + " IS CONSUMING " + waterConsumptionRate + " LITTERS FROM DEPOSIT #" + depositID);
            deposits[depositID].stopRepleneshing(id, position, waterConsumptionRate);

        totalWaterConsumed += waterConsumptionRate;
        if (totalWaterConsumed >= 10) {
          Console.println(Console.GREEN,"> HOUSE #" + id + " CONSUMED A TOTAL OF:\n    > " + totalWaterConsumed + " LITTERS OF WATER.");
          break;
        }
      }
    } catch (AssertionError e) {

    }
  }

  private int pickDeposit(){
    return random.nextInt(deposits.length);
  }
}