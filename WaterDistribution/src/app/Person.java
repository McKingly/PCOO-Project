package app;

import interfaces.IPerson;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

public class Person implements IPerson, Runnable
{

  private int id;
  private int waterValue;
  private SharedDeposit dep;
  private SharedConsole con;
  private Position position; 

  private static int counter = 0;

  public Person(SharedDeposit dep, SharedConsole con, Position position) {
    waterValue = 0;
    this.id = counter++;
    this.dep = dep;
    this.con = con;
    this.position = position;
  }

  
  /** 
   * @param dep
   */
  @Override
  public void interactDeposit() {
    try {
      //if (!dep.hasEnoughWater(5))
        //System.out.print("Sending alert");
        //con.addAlert();
      if(dep.hasEnoughWater(5))
        dep.useWater(id, position);
      else{
        con.addAlert(dep.getId());
        dep.useWater(id, position);
      }
      //dep.stopRepleneshing();
      Thread.sleep(250);
    } catch (AssertionError e) {
      out.println(e.getMessage());
      //con.addAlert();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    finally{
      System.out.println("Have all the water I need");
      dep.stopRepleneshing(id, position);
    }

  }

  @Override
  public void interactConsole() {
    con.addAlert(dep.getId()); 
  }

  public void run() {
    System.out.println("Starting Thread Person #"+id);
    interactDeposit();
  }

}