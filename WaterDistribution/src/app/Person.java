package app;

import interfaces.IPerson;
import pt.ua.concurrent.CThread;
import pt.ua.concurrent.Event;
import pt.ua.concurrent.Console;
import pt.ua.gboard.basic.Position;

import static java.lang.System.out;

public class Person extends CThread //implements IPerson
{

  private int id;
  private int waterValue;
  private SharedDeposit dep;
  private SharedConsole con;
  private Position position; 
  private Event event;

  private static int counter = 0;

  public Person(SharedDeposit dep, SharedConsole con, Position position) {
    waterValue = 0;
    this.id = counter++;
    this.dep = dep;
    this.con = con;
    this.position = position;
  }

  public Person(SharedDeposit dep, SharedConsole con, Position position, Event event) {
    waterValue = 0;
    this.id = counter++;
    this.dep = dep;
    this.con = con;
    this.position = position;
    this.event = event;
  }
  
  /** 
   * @param dep
   
  //@Override
  public void interactDeposit() {
      dep.useWater(id, position);
  }*/
  
  //@Override
  public void interactConsole() {
    con.addAlert(dep.getId()); 
  }

  public void run() {
    out.println(" > STARTING PERSON THREAD #"+id);
    Console.println(Console.BACKGROUND_BLUE, "TESTE");
    try {
      Console.println(Console.RED," > PERSON THREAD #"+id+" CONSUMED "+ dep.useWater(id, position, con));
      dep.startRepleneshing(id, position);
      dep.stopRepleneshing(id, position);
    } catch (AssertionError e) {
      //con.addAlert(dep.getId());
    }
  }
}