package utility;

import java.io.*;

public class OutputHandler {

    private static OutputHandler instance;
    private static BufferedWriter outputFileWriter;
    private static BufferedWriter errLogWriter;
    
    private OutputHandler() {
        instance = this;
        File f1 = new File("../out/output.txt");
        File f2 = new File("../out/error.txt");
        if(f1.exists())
            f1.delete();
        
        if(f2.exists())
            f2.delete();

        try {
            FileWriter fw = new FileWriter(f1);
            outputFileWriter = new BufferedWriter(fw);
            fw = new FileWriter(f2);
            errLogWriter = new BufferedWriter(fw);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized OutputHandler getInstance() {
        if (instance == null)
            instance = new OutputHandler();
        
        return instance;
    }

    public synchronized void displayMsg(String msg) {
        System.out.println(msg);
        try {
            outputFileWriter.write(msg + "\n");
            outputFileWriter.flush();
        } catch (IOException e) {
            logError(e);
        }
    }

    public synchronized void logError(String msg) {
        try {
            errLogWriter.write(msg);
            errLogWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void logError(Exception ex) {
        try {
            errLogWriter.write(ex.getMessage());
            errLogWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}