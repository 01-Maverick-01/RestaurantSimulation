/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents a diner who is at the restaurant.                                 */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

import main.Restaurant;
import utility.*;

public class Diner implements Runnable {

    final private int id;                                                   // diner id
    final private int arrivalTime;                                          // time when dinner arrives at the restaurant
    final private int numBurgersOrdered;
    final private int numFriesOrdered;
    final private boolean isCokeOrdered;
    private DinerState state;                                               // current state of diner
    private Table table;                                                    // table assigned to the diner
    
    public Diner(int id, int arrivalTime, int numBurgersOrdered, int numFriesOrdered, int cokeOrdered) {
        this.id = id + 1;
        this.arrivalTime = arrivalTime;
        this.numBurgersOrdered = numBurgersOrdered;
        this.numFriesOrdered = numFriesOrdered;
        this.isCokeOrdered = cokeOrdered == 1 ? true : false;
        this.state = DinerState.WaitingForTable;
        this.table = null;
    }

    public void assignTable(Table table) {
        table.allocateTable(this);
        this.table = table;
        this.state = DinerState.WaitingForCook;
    }

    public void cookAssigned() {
        this.state = DinerState.WaitingForOrder;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public Order getOrder() {
        return new Order(numBurgersOrdered, numFriesOrdered, isCokeOrdered);
    }

    public boolean isDinerInRestaurant() {
        return state != DinerState.Eaten;
    }

    public boolean isDinerInWaitingForFood() {
        return state == DinerState.WaitingForTable || state == DinerState.WaitingForCook || state == DinerState.WaitingForOrder;
    }

    public void orderServed() {
        int time = Restaurant.getInstance().getClock().getTime();
        OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Diner %d starts eating.", time/60, time%60, id));
        this.state = DinerState.Eating;
    }

    @Override
    public void run() {
        Restaurant restaurant = Restaurant.getInstance();
        while (restaurant.getClock().getTime() < arrivalTime) {
            synchronized(restaurant.getClock()) {
                try {  
                    restaurant.getClock().wait();
                } catch (Exception e) {
                    OutputHandler.getInstance().logError(e);
                }
            }
        }

        int time = restaurant.getClock().getTime();
        OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Diner %d arrives.", time/60, time%60, id));

        restaurant.assignTable(this);
        restaurant.assignCook(table);
        waitForOrder();

        int time_started_eating = restaurant.getClock().getTime();
        while(restaurant.getClock().getTime()-time_started_eating < 30) {
            synchronized (restaurant.getClock()) {
                try {
                    restaurant.getClock().wait();
                } catch (InterruptedException e) {
                    OutputHandler.getInstance().logError(e);
                }
            }
        }
        this.state = DinerState.Eaten;
        time = restaurant.getClock().getTime();
        OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Diner %d finishes eating and leaves the restaurant.",
            time/60, time%60, id));
        restaurant.releaseTable(table);
    }

    private void waitForOrder() {
        while(!table.isOrderServed() ) {
            synchronized (table.waitForOrder) {
                try {
                    table.waitForOrder.wait();
                    orderServed();
                } catch (InterruptedException e) {
                    OutputHandler.getInstance().logError(e);
                }
            }
        }
    } 
}