/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents the burger machine at the restaurant.                             */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

public class BurgerMachine extends Machine{
    static final int PREP_TIME = 5;

    @Override
    int getPrepTime() {
        return PREP_TIME;
    }

    @Override
    String getMachineName() {
        return "Burger";
    }
}