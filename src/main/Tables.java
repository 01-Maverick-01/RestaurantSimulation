/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      It represents all the tables that there in the restaurant.                      */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package main;

import resource.*;
import java.util.*;

public class Tables {
    private ArrayList<Table> tables;                                        // list of all the tables
    Integer waitForTable;                                                   // monitor to request a new table for diner
    
    public Tables(int numTables) {
        this.waitForTable = 1;
        tables = new ArrayList<Table>(numTables);
        for (int i = 0; i < numTables; i++) {
            tables.add(new Table(i));
        }
    }

    public Table getFreeTable() {
        for (int i = 0; i < tables.size(); i++) {
            if (tables.get(i).isFree())
                return tables.get(i);
        }

        return null;
    }

    public int getCount() {
        return tables.size();
    }

    public void releaseTable(int tableId) {
        tables.get(tableId-1).dinerFinishedEating();
        waitForTable.notifyAll();
    }
}