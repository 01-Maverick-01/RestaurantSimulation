/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents a single cook that is currently working in the restaurant.        */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

import main.Restaurant;
import utility.*;

public class Cook implements Runnable {

    final private int id;                                   // cook id
    private Table table;                                    // table that cook is working for
    private CookState state;                                // current state of cook
    
    public Cook(int id) {
        this.id = id + 1;
        this.table = null;
        this.state = CookState.Free;
    }

    public void assignTable(Table table) {
        table.assignCook(this);
        this.table = table;
        this.state = CookState.MakingOrder;
    }

    public void orderCompleted() {
        table.orderServed();
        this.table = null;
        this.state = CookState.Free;
    }

    public boolean isFree() {
        return state == CookState.Free;
    }

    public Table getAssignedTable() {
        return table;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Cook-"+id);
        while(Restaurant.getInstance().isDinerInWaitingForFood())
        {
            if (isFree())
                continue;
            else {
                Order order = table.getOrder();
                while (!order.isComplete()){
                    try{
                        if (order.shouldPrepare(Order.BURGER)) {
                            Restaurant.getInstance().getMachine(Restaurant.BURGER_MACHINE).useMachine(this);
                            order.prepared(Order.BURGER);
                        }
                        if (order.shouldPrepare(Order.FRIES)) {
                            Restaurant.getInstance().getMachine(Restaurant.FRIES_MACHINE).useMachine(this);
                            order.prepared(Order.FRIES);
                        }
                        if (order.shouldPrepare(Order.SODA)) {
                            Restaurant.getInstance().getMachine(Restaurant.SODA_MACHINE).useMachine(this);
                            order.prepared(Order.SODA);
                        }
                    } catch(Exception e) {
                        OutputHandler.getInstance().logError(e);
                    }
                }

                Restaurant.getInstance().orderCompleted(this);
            }
        }
    }
}