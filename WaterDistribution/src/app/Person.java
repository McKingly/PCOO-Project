package app;

import interfaces.IPerson;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

public class Person implements IPerson, Runnable
{
  private int waterValue;
  private SharedDeposit dep;
  private Console con;
  private Position position; 

  private static int counter = 0;

  private int id;
  
  /** 
   * @param dep
   * @return 
   */
  public Person(SharedDeposit dep, Console con){
    waterValue = 0;
    this.dep = dep;
    this.con = con;
    this.id = counter++;
  }

  public Person(SharedDeposit dep, Position position) {
    waterValue = 0;
    this.dep = dep;
    this.position = position;
    this.id = counter++;
  }

  
  /** 
   * @param dep
   */
  @Override
  public void interactDeposit() {
    try {
      dep.useWater(id, position);
      //dep.stopRepleneshing();
      Thread.sleep(200);
    } catch (AssertionError e) {
      out.println(e.getMessage());
      //con.addAlert();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    finally{
      System.out.println("Have all the water I need");
      dep.stopRepleneshing(position);
    }

  }

  @Override
  public void interactConsole() {
    con.addAlert(); 
  }

  public void run() {
    interactDeposit();
  }

}