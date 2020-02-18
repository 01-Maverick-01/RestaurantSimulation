/****************************************************************************************/
/* CSE 6431 - Lab (Restaurant Simulation)                                               */
/*      This is the main entry point for the lab. It represents the restaurant for which*/
/*      we need to implement simulatuin and this class is a singleton                   */
/*                                                                                      */
/* Name: Ishan Deep                                                                     */
/* Lname.#: deep.24                                                                     */
/*                                                                                      */
/****************************************************************************************/

package main;

import java.io.*;
import java.util.*;

import utility.*;
import resource.*;

public class Restaurant {

    public static final String BURGER_MACHINE = "BurgerMachine";
    public static final String FRIES_MACHINE = "FriesMachine";
    public static final String SODA_MACHINE = "SodaMachine";
    
    static final String DEFAULT_INPUT = "../testfiles/input.txt";
    static final int CLOSING_TIME = 120;

    private Clock clock;                                        // represents the restaurant clock
    private static Restaurant instance = null;

    private DinersQueue diners;                                 // handler for incoming diners
    private Cooks cooks;                                        // represents a pool of cook at the restaurant
    private Tables tables;                                      // represents a pool of tables at the restaurant
    private HashMap<String, Machine> machines;                  // represents the machines available at the restaurant
    private ArrayList<Diner> declinedReservations;

    private Restaurant() {                                      // private constructor
        declinedReservations = new ArrayList<Diner>();
        diners = new DinersQueue();
        clock = new Clock();
        machines = new HashMap<String, Machine>();
        machines.put(BURGER_MACHINE, new BurgerMachine());
        machines.put(FRIES_MACHINE, new FriesMachine());
        machines.put(SODA_MACHINE, new SodaMachine());
    }

    public static synchronized Restaurant getInstance() {
        if (instance == null)
            instance = new Restaurant();
        
        return instance;
    }

    public Machine getMachine(String machineName) {             // method to get an instance of a specific machine
        if (machines.containsKey(machineName))
            return machines.get(machineName);
        
        return null;
    }

    public void releaseTable(Table table) {                     // method to release a occupied table
        synchronized(tables.waitForTable) {
            tables.releaseTable(table.getId());
        }    
    }

    public void orderCompleted(Cook cook) {                     // method to notify order is complete
        synchronized(cooks.waitForCook) {
            cooks.cookCompletedOrder(cook);
        }    
    }

    public boolean isDinerInWaitingForFood() {                  // method to check if diner is waiting for his food
        return diners.isAnyDinerInWaitingForFood();
    }

    public void assignTable(Diner diner) {                      // method to assign a table to a diner
        synchronized(tables.waitForTable) {
            while (tables.getFreeTable() == null) {
                try {  
                    tables.waitForTable.wait();
                } catch (Exception e) {
                    OutputHandler.getInstance().logError(e);
                }
            }
            int time = clock.getTime();
            Table freeTable = tables.getFreeTable();
            diner.assignTable(freeTable);
            OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Diner %d is seated at table %d.", time/60,
                time%60, diner.getId(), freeTable.getId()));
        }    
    }

    public void assignCook(Table table) {                       // method to assign a cook to a table
        synchronized (cooks.waitForCook) {
            while (cooks.getFree() == null) {
                try {  
                    cooks.waitForCook.wait();
                } catch (Exception e) {
                    OutputHandler.getInstance().logError(e);
                }
            }
            Cook freeCook = cooks.getFree();
            int time = clock.getTime();
            OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Cook %d processes Diner %d's order.", time/60,
                time%60, freeCook.getId(), table.getDinerId()));
            freeCook.assignTable(table);
        }
    }

    public Clock getClock() {                                   // method to get restaurant clock
        return clock;
    }

    public void initialize(final String path) {
        try {
            OutputHandler.getInstance().displayMsg("\nParsing intput file '" + path + "'. Please wait...\n");
            File input = new File(path);
            // parse input file
            try (BufferedReader buf = new BufferedReader(new FileReader(input))){ 
                int numDiners = Integer.parseInt(buf.readLine().trim());
                int numTables = Integer.parseInt(buf.readLine().trim());
                int numCooks = Integer.parseInt(buf.readLine().trim());
                
                cooks = new Cooks(numCooks);
                tables = new Tables(numTables);
                
                for(int i = 0; i < numDiners; i++)
                {
                    final StringTokenizer items = new StringTokenizer(buf.readLine(),",");
                    //check if there are 4 items on each input line
                    if(items.countTokens() == 4) {
                        int arrivalTime = Integer.parseInt(items.nextToken().trim());
                        int burgersOrdered = Integer.parseInt(items.nextToken().trim());
                        int friesOrdered = Integer.parseInt(items.nextToken().trim());
                        int cokeOrdered = Integer.parseInt(items.nextToken().trim());
                        if (arrivalTime >= 0 && arrivalTime <= CLOSING_TIME && burgersOrdered >= 1 && friesOrdered >= 0 
                                && (cokeOrdered == 0 || cokeOrdered == 1)) {
                            diners.addNewDiner(i, arrivalTime, burgersOrdered, friesOrdered, cokeOrdered);
                        }
                        else {
                            if (arrivalTime > CLOSING_TIME)
                                declinedReservations.add(new Diner(i, arrivalTime, -1, -1, -1));
                            else
                                System.out.println("Error: invalid input");
                        }
                    }
                    else {
                        System.out.println("Error: Invalid number of items found on line number:" + i+3);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();       // if exception occurred, then terminate execution
            System.exit(0);
        }
    }

    private void open() {
        OutputHandler.getInstance().displayMsg("\t*************************************************");
        OutputHandler.getInstance().displayMsg("\t*\t\tRestuarant is now open.\t\t*");
        OutputHandler.getInstance().displayMsg("\t*\t\tNumber of cooks : "+ cooks.getCount() +"\t\t*");
        OutputHandler.getInstance().displayMsg("\t*\t\tNumber of diners: "+ diners.getCount() +"\t\t*");
        OutputHandler.getInstance().displayMsg("\t*\t\tNumber of tables: "+ tables.getCount() +"\t\t*");
        OutputHandler.getInstance().displayMsg("\t*************************************************\n");
        diners.start();                 // start diner threads
        cooks.start();                  // start cook threads
    }

    // This is the main entry point
    public static void main(final String[] args) {
        final Restaurant restaurant = Restaurant.getInstance();
        final String inputFilePath = args.length > 0 ? args[0] : DEFAULT_INPUT;
        restaurant.initialize(inputFilePath);
        restaurant.open();
        while(restaurant.diners.isAnyDinerInRestaurant()) {
            restaurant.clock.incrementTime();
            synchronized(restaurant.clock) {
                handleDeclinedReservation(restaurant.clock.getTime());
                restaurant.clock.notifyAll();
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                OutputHandler.getInstance().logError(e);
            }
        }

        int time = restaurant.clock.getTime();
        OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - The last diner leaves the restaurant.", time/60, time%60));
    }

    private static void handleDeclinedReservation(int time) {
        for (Diner diner : Restaurant.getInstance().declinedReservations) {
            if (diner.getArrivalTime() == time && time > CLOSING_TIME) {
                OutputHandler.getInstance().displayMsg(String.format("%02d:%02d - Diner %d reservation declined as restaurant is closed.", 
                    time/60, time%60, diner.getId()));
            }
        }
    }
}