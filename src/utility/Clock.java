/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents clock. Restaurant uses an instance of this class as clock.        */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package utility;

public class Clock {

    private int currentTime;
    
    public Clock() {
        this.currentTime = 0;
    }

    public void incrementTime() {
        currentTime++;
    }

    public int getTime() {
        return currentTime;
    }
}