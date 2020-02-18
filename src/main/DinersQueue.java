/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents all diners that are waiting to eat in the restaurant.             */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package main;

import java.util.ArrayList;
import resource.*;

public class DinersQueue {

    final private ArrayList<Diner> diners;                      // list of all the diners

    public DinersQueue() {
        diners = new ArrayList<Diner>();
    }

    public void addNewDiner(int id, int arrivalTime, int burgersOrdered, int friesOrdered, int cokeOrdered) {
        diners.add(new Diner(id, arrivalTime, burgersOrdered, friesOrdered, cokeOrdered));
   }

    public boolean isAnyDinerInRestaurant() {
        for (Diner diner : diners) {
            if (diner.isDinerInRestaurant())
                return true;    
        }

        return false;
    }

    public int getCount() {
        return diners.size();
    }

    public boolean isAnyDinerInWaitingForFood() {
        for (Diner diner : diners) {
            if (diner.isDinerInWaitingForFood())
                return true;    
        }

        return false;
    }

    public void start() {
        for (Diner diner : diners) {
            new Thread(diner).start();
        }
    }
}
