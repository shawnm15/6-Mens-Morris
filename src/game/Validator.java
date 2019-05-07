package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Contains functions that analyze the current state of the game
 *
 * @author Victor Velechovsky
 * @version 1.0
 */
public class Validator {

    //************Game Progress Constants****************//
    public static final int GAME_IN_PROGRESS = 6000;
    public static final int GAME_STATUS_BLUE_WON = 6001;
    public static final int GAME_STATUS_RED_WON = 6002;
    public static final int GAME_STATUS_DRAW = 6003;

    //Map contains the valid 'neighbors' (adjacent positions) for each index position
    //ex. the upper-left spot (0) has a neightbor to the right (1) and a neighbor to the bottom (3)
    //so map[0] = {1, 6}
    private int [][] map;

    /**
     * Constructor reads in mapping data from map.txt
    */
    public Validator() {

        map = new int[16][];
        int i = 0;

        try {
            Scanner scanner = new Scanner(new File("map.txt"));

            while(scanner.hasNextLine()) {
                String next = scanner.nextLine();

                String values[] = next.split(",");

                map[i] = new int[values.length];
                for (int j = 0; j < values.length; j++) map[i][j] = Integer.parseInt(values[j]);

                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int[] neighbors(int i ) {

        return map[i];

    }

    /**
     * Determines if a proposed move is valid (adjacent)
     *
     * @param oldPos - the initial position of the piece
     * @param newPos - the proposed new position of the piece
    */
    public boolean adjacentMove(int oldPos, int newPos) {

        for (int i : map[oldPos]) {
            if (i == newPos) return true;
        }

        return false;
    }

    /**
     * Analyses the game to see if a player has won, or if there is a draw
     *
     * @param states - the state of the pieces
     * @return integer - game progress constant
    */
    public int getGameStatus(int [] states) {

        boolean blueHasMoves = false;
        boolean redHasMoves = false;

        //For each index
        for (int i = 0; i < states.length; i++) {
            //Red
            if (states[i] == 1) {
                //If there is a move available for red
                if (checkForMoves(states, i)) redHasMoves = true;
            }
            if (states[i] == 2) {
                //If there is a move available for blue
                if (checkForMoves(states, i)) blueHasMoves = true;
            }
        }

        //If one player runs out of pieces, this is the end of the game
        if (numberOfBluePieces(states) < 3) return GAME_STATUS_RED_WON;
        if (numberOfRedPieces(states) < 3) return GAME_STATUS_BLUE_WON;

        if (blueHasMoves && redHasMoves) return GAME_IN_PROGRESS;
        else if (blueHasMoves) return GAME_STATUS_BLUE_WON;
        else if (redHasMoves) return GAME_STATUS_RED_WON;

        return GAME_STATUS_DRAW;

    }

    /**
     * For a given index position, check if there are any adjacent empty pieces
     *
     * @param states - states of the pieces
     * @param i - index of the position to check
    */
    public boolean checkForMoves(int [] states, int i) {

        int [] neighbors = map[i];

        for (int j : neighbors) {
            //If a neighbor piece is blank, return true
            if (states[j] == 0) return true;
        }

        return false;
    }

    /**
     * Determine the number of red pieces currently on the board
     *
     * @return - number of red pieces on the board
    */
    public int numberOfRedPieces (int [] states) {

        int c = 0;

        for (int i : states) if (i == 1) c++;
        return c;
    }

    /**
      * Determine the number of blue pieces currently on the board
      *
      * @return - number of blue pieces on the board
    */
    public int numberOfBluePieces (int [] states) {

        int c = 0;

        for (int i : states) if (i == 2) c++;
        return c;
    }

}
