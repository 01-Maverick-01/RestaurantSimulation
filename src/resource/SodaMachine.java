/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents the soda machine at the restaurant.                               */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

public class SodaMachine extends Machine {
    static final int PREP_TIME = 1;

    @Override
    int getPrepTime() {
        return PREP_TIME;
    }

    @Override
    String getMachineName() {
        return "Soda";
    }
}