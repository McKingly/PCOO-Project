package app;

import interfaces.IPerson;

import static java.lang.System.out;

public class Person implements IPerson, Runnable
{
  private int waterValue;
  private Deposit dep;

  
  /** 
   * @param dep
   * @return 
   */
  public Person(Deposit dep){
    waterValue = 0;
    this.dep = dep;
  }

  
  /** 
   * @param dep
   */
  @Override
  public boolean interactWaterDeposit(Deposit dep) {
    waterValue += dep.useWater();
    System.out.println("Consumed "+waterValue+" liters total.");
    return waterValue < 20;

  }

  @Override
  public void interactConsole() {
    out.print("Not implemented yet."); 
  }

  public void run() {
    try {
      while (interactWaterDeposit(dep)){ 
        out.println("Fetching water");
      }
      System.out.println("Have all the water I need");
      dep.stopRepleneshing();
    } catch (AssertionError e) {
      out.println(e.getMessage());
    }
    finally{
      dep.stopRepleneshing();
    }
  }

}