package app;

import java.util.Random;

import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

public class House extends CThread // implements IPerson
{

  private int id;
  private final Position position;
  private final int waterConsumptionRate;
  private final int maxWaterConsumption;

  private final SharedDeposit[] deposits;
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
  public House(SharedDeposit[] deposits, SharedAlertConsole console, Position position, int waterConsumptionRate, int maxWaterConsumption) {
    assert deposits != null;
    assert console != null;
    assert position != null;
    assert waterConsumptionRate > 0;
    
    this.id = counter++;
    this.position = position;
    this.waterConsumptionRate = waterConsumptionRate;
    this.maxWaterConsumption = maxWaterConsumption;
    
    this.deposits = deposits;
    this.console = console;
  }

  public void run() {
    Console.println(Console.GREEN, "> STARTING HOUSE THREAD #" + id);
    try {
      int depositID;
      int totalWaterConsumed = 0;
      while (true) {
        depositID = pickDeposit();
        deposits[depositID].useWater(id, position, console, waterConsumptionRate);
        deposits[depositID].startRepleneshing(id, position, waterConsumptionRate);
        Console.println(Console.BLUE,
            "> HOUSE #" + id + " IS CONSUMING " + waterConsumptionRate + " LITTERS FROM DEPOSIT #" + depositID);
            deposits[depositID].stopRepleneshing(id, position, waterConsumptionRate);

        totalWaterConsumed += waterConsumptionRate;
        if (totalWaterConsumed >= maxWaterConsumption) {
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