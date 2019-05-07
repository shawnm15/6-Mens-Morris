package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Saves and Loads the game in the SAVE.txt file
 *
 * @author Victor Velechovsky
 * @version 1.0
 */
public class FileUtil {

    Scanner scanner;

    int [] states;
    LinkedList<Mill> mills;

    int redPieces;
    int bluePieces;

    /**
     * Constructor that reads and parses game state information from the SAVE.txt file
    */
    public FileUtil() {

        redPieces = 0;
        bluePieces = 0;

        states = new int[16];
        mills = new LinkedList<>();

        try {

            File inputFile = new File("SAVE.txt");

            if (! inputFile.exists()) System.out.println("SAVE.txt file not found - MUST be present at all times!");

            scanner = new Scanner(inputFile);

            String [] stateValues = scanner.nextLine().split(",");
            //Color values
            for (int i = 0; i < stateValues.length; i++) {
                states[i] = Integer.parseInt(stateValues[i]);
                if (states[i] == 1) redPieces++;
                if (states[i] == 2) bluePieces++;
            }

            //Checked mills
            while(scanner.hasNextLine()) {
                String [] values = scanner.nextLine().split(",");

                int a = Integer.parseInt(values[0]);
                int b = Integer.parseInt(values[1]);
                int c = Integer.parseInt(values[2]);

                mills.add(new Mill(a, b, c));
            }

            scanner.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int [] getStates() {

        return states;
    }

    public int getBluePieces() { return bluePieces; }

    public int getRedPieces() { return redPieces; }

    public LinkedList<Mill> getMills() {

        return mills;
    }

    //Save the game state to the file
    public void storeGame(int [] states, LinkedList<Mill> checkedMills) {

        try {
            FileWriter writer = new FileWriter(new File("SAVE.txt"));

            //Store the states on the first line
            for (int i : states) writer.append(i + ",");

            writer.append("\n");

            for (Mill m : checkedMills) writer.append(m.a + "," + m.b + "," + m.c + "\n");

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
