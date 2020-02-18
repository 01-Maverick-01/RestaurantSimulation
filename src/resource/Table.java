/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents a single table in the restaurant.                                 */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

import main.Restaurant;
import utility.*;

public class Table {
    final private int id;                                                       // table id
    private Diner diner;                                                        // diner to which the table is assigned
    private TableState state = TableState.Empty;                                // current table state
    private Cook cook;                                                          // cook assigned to table
    Integer waitForOrder;                                                       // monitor to wait for order to be prepared

    public Table(int id) {
        waitForOrder = 1;
        this.id = id + 1;
        this.diner = null;
        this.cook = null;
    }

    public void allocateTable(Diner diner) {
        this.state = TableState.Occupied;
        this.diner = diner;
    }

    public int getId() {
        return id;
    }

    public boolean isFree() {
        return state == TableState.Empty;
    }

    public boolean isCookAllocated() {
        return cook != null;
    }

    public int getDinerId() {
        return diner.getId();
    }

    public void assignCook(Cook cook) {
        this.cook = cook;
        this.state = TableState.CookAssigned;
        this.diner.cookAssigned();
    }

    public int getCookId() {
        return cook.getId();
    }

    public boolean isOrderServed() {
        return state == TableState.OrderServed;
    }

    public void orderServed() {
        
        int time = Restaurant.getInstance().getClock().getTime();
        OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Order servered to Diner %d by Cook %d on table %d.", time/60, 
            time%60, diner.getId(), cook.getId(), id));

        this.cook = null;
        this.state = TableState.OrderServed;        
        this.waitForOrder.notifyAll();
    }

    public void dinerFinishedEating() {
        this.diner = null;
        this.state = TableState.Empty;
    }

    public Order getOrder() {
        return diner.getOrder();
    }
}