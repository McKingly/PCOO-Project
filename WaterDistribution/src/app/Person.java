package app;

import interfaces.IPerson;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

public class Person implements IPerson, Runnable
{
  private int waterValue;
  private Deposit dep;
  private Console con;
  private Position position; 
  
  /** 
   * @param dep
   * @return 
   */
  public Person(Deposit dep, Console con){
    waterValue = 0;
    this.dep = dep;
    this.con = con;
  }

  public Person(Deposit dep, Position position) {
    waterValue = 0;
    this.dep = dep;
    this.position = position;
  }

  
  /** 
   * @param dep
   */
  @Override
  public boolean interactWaterDeposit() {
    waterValue += dep.useWater(position);
    System.out.println("Consumed "+waterValue+" liters total.");
    return waterValue < 10;

  }

  @Override
  public void interactConsole() {
    con.addAlert(); 
  }

  public void run() {
    try {
      while (interactWaterDeposit()){ 
        out.println("Fetching water");
      }
      //dep.stopRepleneshing();
    } catch (AssertionError e) {
      out.println(e.getMessage());
      //con.addAlert();
    }
    finally{
      System.out.println("Have all the water I need");
      dep.stopRepleneshing(position);
    }
  }

}