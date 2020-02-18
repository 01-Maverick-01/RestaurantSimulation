/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents all the cooks that current are working in the restaurant.         */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package main;

import java.util.ArrayList;
import resource.*;

public class Cooks {

    final private ArrayList<Cook> cooks;                                // list of cooks currently in restaurant
    Integer waitForCook;                                                // monitor to request a cook to work on order

    public Cooks(int count) {
        waitForCook = 1;
        cooks = new ArrayList<Cook>(count);
        for (int i = 0; i < count; i++) {
            cooks.add(new Cook(i));
        }
    }

    public Cook getFree() {
        for (Cook cook : cooks) {
            if (cook.isFree())
                return cook;
        }

        return null;
    }

    public void start() {
        for (Cook cook : cooks) {
            new Thread(cook).start();
        }
    }

    public int getCount() {
        return cooks.size();
    }

    public void cookCompletedOrder(Cook cook) {
        cook.orderCompleted();
        waitForCook.notifyAll();;
    }
}
