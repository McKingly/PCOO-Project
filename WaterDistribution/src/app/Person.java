package app;

import interfaces.IPerson;

import static java.lang.System.out;

public class Person implements IPerson, Runnable
{
  private int waterValue;
  private Deposit dep;
  private Console con;
  
  /** 
   * @param dep
   * @return 
   */
  public Person(Deposit dep, Console con){
    waterValue = 0;
    this.dep = dep;
    this.con = con;
  }

  
  /** 
   * @param dep
   */
  @Override
  public boolean interactWaterDeposit() {
    waterValue += dep.useWater();
    System.out.println("Consumed "+waterValue+" liters total.");
    return waterValue < 20;

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
      System.out.println("Have all the water I need");
      dep.stopRepleneshing();
    } catch (AssertionError e) {
      out.println(e.getMessage());
      con.addAlert();
    }
    finally{
      dep.stopRepleneshing();
    }
  }

}