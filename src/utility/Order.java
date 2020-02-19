/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents an order placed by a diner.                                       */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package utility;

public class Order {

    public static final String BURGER = "Burger";
    public static final String FRIES = "Fries";
    public static final String SODA = "Soda";

    public final int numBurgersOrdered;
    public final int numFriessOrdered;
    public final boolean cokeOrdered;
    
    private int numBurgersPrepared;
    private int numFriesPrepared;
    private boolean cokePrepared;

    public Order(int numBurgersOrdered, int numFriesOrdered, boolean isCokeOrdered) {
        this.cokeOrdered = isCokeOrdered;
        this.numBurgersOrdered = numBurgersOrdered;
        this.numFriessOrdered = numFriesOrdered;

        this.numFriesPrepared = 0;
        this.numBurgersPrepared = 0;
        this.cokePrepared = false;
    }

    public boolean isComplete() {
        if (numBurgersOrdered == numBurgersPrepared && numFriessOrdered == numFriesPrepared && cokeOrdered == cokePrepared)
            return true;
        
        return false;
    }

    public void prepared(String item){
        switch (item) {
            case BURGER: numBurgersPrepared++; break;
            case FRIES: numFriesPrepared++; break;
            case SODA : cokePrepared = true; break;
            default: OutputHandler.getInstance().logError("Invalid item specified. There is no machine to prepare" + item); break;
        }
    }

    public boolean shouldPrepare(String item) throws Exception {
        switch (item) {
            case BURGER: return numBurgersOrdered - numBurgersPrepared > 0;
            case FRIES: return numFriessOrdered - numFriesPrepared > 0;
            case SODA : return cokeOrdered && cokeOrdered != cokePrepared;
            default: 
                throw new Exception("Invalid item specified. There is no machine to prepare" + item); 
                
        }
    }
}