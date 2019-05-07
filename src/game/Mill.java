package game;

import java.util.LinkedList;

/**
 * Simple class that represent a Mill (3 pieces of the same color in a row). Also contains some logic functions
 *
 * @author Victor Velechovsky
 * @version 1.0
 */
public class Mill {

    int a, b, c;

    public Mill(int a, int b, int c) {
        this.a = a; this.b = b; this.c = c;
    }

    public boolean eq(Mill m) {
        if (a == m.a && b == m.b && c == m.c) return true;
        return false;
    }

    public String toString() {
        return (a + " " + b + " " + c);
    }

    /**
     * Bruteforce check each possible mill. If a NEW mill is found, add it to the list of already
     * checked mills, and return the mill object.
     *
     * @param states - state of all positions
     * @param checkedMills - list of all Mills that have already been 'dealt with', so as to avoid 'detecting' the same mill twice
    */
    public static Mill checkForMills(int [] states, LinkedList<Mill> checkedMills) {

        if (states[0] == states[6] && states[0] == states[13] && (states[0] == 1 || states[0] == 2)) {
            Mill m = new Mill(0, 6, 13);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }

        if (states[0] == states[1] && states[1] == states[2] && (states[0] == 1 || states[0] == 2)) {
            Mill m = new Mill(0, 1, 2);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }
        if (states[3] == states[4] && states[4] == states[5] && (states[3] == 1 || states[3] == 2)) {
            Mill m = new Mill(3, 4, 5);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }
        if (states[3] == states[7] && states[7] == states[10] && (states[3] == 1 || states[3] == 2)) {
            Mill m = new Mill(3, 7, 10);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }
        if (states[10] == states[11] && states[11] == states[12] && (states[10] == 1 || states[10] == 2)) {
            Mill m = new Mill(10, 11, 12);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }
        if (states[2] == states[9] && states[9] == states[15] && (states[2] == 1 || states[2] == 2)) {
            Mill m = new Mill(2, 11, 15);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }
        if (states[13] == states[14] && states[14] == states[15] && (states[13] == 1 || states[13] == 2)) {
            Mill m = new Mill(3, 4, 5);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }
        if (states[5] == states[8] && states[5] == states[12] && (states[5] == 1 || states[5] == 2)) {
            Mill m = new Mill(5, 8, 12);
            if (! checkedMillsContains(m, checkedMills)) {
                checkedMills.add(m);
                return m;
            }
        }

        return new Mill(-1, -1, -1);

    }

    //Searches the checkedMills list to see if an equivalent mill (same index positions) already exists
    private static boolean checkedMillsContains(Mill m, LinkedList<Mill> checkedMills) {

        for (Mill mill : checkedMills) {
            if (m.eq(mill)) return true;
        }

        return false;
    }

    /**
     * If a piece is being moved from a mill, the mill is 'deformed'.
     * The mill needs to be removed from the list of checked mills so that a new mill
     * can be formed in the same location in a later move
    */
    public static void movingFromMill( int pos, LinkedList<Mill> checkedMills) {

        for (int i = 0; i < checkedMills.size(); i++) {
            Mill m = checkedMills.get(i);

            if (m.a == pos || m.b == pos || m.c == pos) {
                checkedMills.remove(i);
            }
        }


    }

}