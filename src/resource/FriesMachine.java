/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents the fries machine at the restaurant.                             */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package resource;

public class FriesMachine extends Machine{
    static final int PREP_TIME = 3;

    @Override
    int getPrepTime() {
        return PREP_TIME;
    }

    @Override
    String getMachineName() {
        return "Fries";
    }
}