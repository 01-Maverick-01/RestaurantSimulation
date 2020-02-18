/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It is the base class for  the all types of machines at the restaurant.          */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

import utility.*;
import main.*;

abstract public class Machine {

    public synchronized void useMachine(Cook cook) {
        Restaurant restaurant = Restaurant.getInstance();
        int time = restaurant.getClock().getTime();
        OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Cook %d uses the %s machine.", time/60,
            time%60, cook.getId(), getMachineName()));
        
        while (restaurant.getClock().getTime() - time != getPrepTime()) {
            synchronized (restaurant.getClock()) {
                try {
                    restaurant.getClock().wait();
                } catch (InterruptedException e) {
                    OutputHandler.getInstance().logError(e);
                }
            }
        }
    }

    abstract int getPrepTime();
    abstract String getMachineName();
}