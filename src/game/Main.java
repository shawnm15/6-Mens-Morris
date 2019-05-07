package game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Random;

/**
 * Entry point of the application, handles the majority of the back-end logic
 *
 * @author Victor Velechovsky
 * @version 1.0
 */
public class Main extends Application {

    //List of all Mills that have already been 'dealt with', so as to avoid 'detecting' the same mill twice
    LinkedList<Mill> checkedMills;

    //Holds the state of each circle
    //0 = empty, 1 = red, 2 = blue, 3 = ERROR
    private int[] states;

    private int redPieces = 6, bluePieces = 6;

    private Stage primaryStage;

    //Controller
    private GameController gameController;


    //*******************Game Mode Constants****************//
    public static final int SETUP_BLUE = 500; //Setup - adding blue pieces
    public static final int SETUP_RED = 501; //Setup - adding red pieces
    public static final int RED_TURN = 502; //First part of game - red turn
    public static final int BLUE_TURN = 503; //First part of game - blue turn
    public static final int SETUP = 504; //Setup - initial
    public static final int REMOVE_MILL_BLUE = 505; //Red player forms a mill - can remove a blue piece
    public static final int REMOVE_MILL_RED = 506; //Blue player forms a mill - can remove a red piece
    public static final int GAME_RED = 507; //Second part of game - red turn
    public static final int GAME_BLUE = 508; //Second part of game - blue turn
    public static final int GAME_RED_CHOOSE = 509; //Choose where to place the red piece
    public static final int GAME_BLUE_CHOOSE = 510; //Choose where to place the blue piece
      
    private int gameMode = SETUP; //Determines what mode the game is currently in
    private boolean gameStarted = false; //Second part of the game active?
    private boolean AI; //Is the second player computer controlled?

    //If the game mode is player-vs-computer, RED = player, BLUE = computer

    private int previousLocation = -1; //index of a currently selected piece when making a 'move'
    private Button previousButton = null; //Button object of currently selected piece when making a 'move'

    private Random random;

    private Validator validator;

    public int[] getStates() {
        return states;
    }

    public void setStates(int[] states) {
        this.states = states;
    }

    public void setAI (boolean AI) { this.AI = AI; }

    /**
     * Called during the beginning of the application. Sets up the first stage
     *
     * @param primaryStage - the first stage of the application
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{

        random = new Random();
        checkedMills = new LinkedList<Mill>();

        validator = new Validator();

        states = new int[16];
        for (int i = 0; i < states.length; i++) states[i] = 0;

        this.primaryStage = primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ai_select.fxml"));
        VBox root = fxmlLoader.load();

        gameController = fxmlLoader.getController();
        gameController.setApp(this);

        primaryStage.setScene(new Scene(root, 300, 100));
        primaryStage.show();

    }

    /**
     * Sets the game board stage
     *
     * @throws Exception
     */
    public void initSetup() throws Exception {

        primaryStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layout.fxml"));
        Pane root = fxmlLoader.load();

        gameController = fxmlLoader.getController();
        gameController.setApp(this);

        primaryStage.setScene(new Scene(root, 700, 700));

        gameController.drawBlanks();

        primaryStage.show();

    }

    public void showNewGameLayout() throws Exception {

        primaryStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("new_game_layout.fxml"));
        SplitPane root = fxmlLoader.load();

        gameController = fxmlLoader.getController();
        gameController.setApp(this);

        primaryStage.setScene(new Scene(root, 300, 100));
        primaryStage.show();

    }

    /**
    * Check that a particular move IN THE SETUP PORTION is valid
    */
    public boolean validateMove() {

        if (gameMode == SETUP_BLUE || gameMode == BLUE_TURN) {

            //Out of pieces?
            if (bluePieces < 1 && !gameStarted) {

                gameController.alert("No more pieces left to place on the board");
                gameController.setButtonText("Blue: Choose a piece to move");

                gameMode = GAME_BLUE;
                gameStarted = true;

                return false;
            }

            //Decrement number of blue pieces left to place
            bluePieces--;
        } else if (gameMode == SETUP_RED || gameMode == RED_TURN) {

            //Out of pieces?
            if (redPieces < 1 && !gameStarted) {

                gameController.alert("No more pieces left to place on the board");
                gameController.setButtonText("Red: Choose a piece to move");

                gameMode = GAME_RED;
                gameStarted = true;

                return false;
            }

            redPieces--;
        }

        return true;

    }

    //-------------AI Methods-----------//

    public void computerPlaceAPiece() {

        //Randomly place a blue piece
        for (int i = 0; i < states.length; i++) {
            if (states[i] == 0) {
                states[i] = 2;
                gameController.drawBlue(gameController.getButton(i + 1));
                return;
            }

        }
    }

    public void computerMakeAMove() {

        System.out.println("Make a move");

        for (int i = 0; i < states.length; i++) {
            if (states[i] == 2) {
                for (int j : validator.neighbors(i)) {
                    if (states[j] == 0) {
                        states[j] = 2;
                        states[i] = 0;

                        gameController.drawBlue(gameController.getButton(j + 1));
                        gameController.drawBlank(gameController.getButton(i + 1));

                        previousLocation = i;
                        previousButton = gameController.getButton((i + 1));

                        Mill.movingFromMill(previousLocation, checkedMills);

                        return;
                    }
                }
            }

        }

    }

    public void computerRemoveARedPiece() {

        for (int i = 0; i < states.length; i++) {
            if (states[i] == 1) {
                gameController.drawBlank(gameController.getButton(i));
                states[i] = 0;

                previousLocation = i;
                previousButton = gameController.getButton(i + 1);

                Mill.movingFromMill(previousLocation, checkedMills);

                return;
            }
        }

    }

    /**
    * Main logic of the game function here. Based on what button was pressed and what the current state of the game is,
    * determines the next state of the game and performs whatever actions necessary
    * 
    **/
    public void circlePressed(ActionEvent e) {

        //Object of the particular button that was pressed
        Button b = (Button) e.getSource();
        //ID of the button that was pressed
        int ID = Integer.parseInt(b.getId().substring(6)) - 1;

        if (!validateMove()) return;

        switch (gameMode) {

            case REMOVE_MILL_BLUE:

                if (states[ID] == 2) {

                    gameController.drawBlank((Button) e.getSource());
                    states[ID] = 0;

                    Mill.movingFromMill(previousLocation, checkedMills);

                    if (AI) {
                        if (gameStarted) {
                            gameMode = GAME_RED;
                            gameController.setButtonText("Red: Chose a piece to move");
                        } else {
                            gameMode = RED_TURN;
                            gameController.setButtonText("Red Turn");
                        }
                        post();
                        return;
                    }

                    if (gameStarted) {
                        gameMode = GAME_BLUE;
                        gameController.setButtonText("Blue: Chose a piece to move");
                    } else {
                        gameMode = BLUE_TURN;
                        gameController.setButtonText("Blue Turn");
                    }

                    post();
                    return;
                } else {
                    gameController.alert("Please select a BLUE piece");
                }
                post();
                return;

            case REMOVE_MILL_RED:

                if (states[ID] == 1) {

                    gameController.drawBlank((Button) e.getSource());
                    states[ID] = 0;

                    Mill.movingFromMill(previousLocation, checkedMills);

                    if (gameStarted) {
                        gameMode = GAME_RED;
                        gameController.setButtonText("Red: Chose a piece to move");
                    } else {
                        gameMode = RED_TURN;
                        gameController.setButtonText("Red Turn");
                    }

                    post();
                    return;
                } else {
                    gameController.alert("Please select a RED piece");
                }
                post();
                return;

            case GAME_BLUE:

                if (states[ID] == 2) {
                    gameMode = GAME_BLUE_CHOOSE;

                    previousButton = b;
                    previousLocation = ID;
                } else {
                    gameController.alert("Choose a BLUE piece!");
                }
                post();
                return;

            case GAME_RED:

                if (states[ID] == 1) {
                    gameMode = GAME_RED_CHOOSE;

                    previousButton = b;
                    previousLocation = ID;
                } else {
                    gameController.alert("Choose a RED piece!");
                }
                post();
                return;

            case GAME_RED_CHOOSE:

                if (!validator.adjacentMove(previousLocation, ID)) {
                    gameController.alert("Must move with adjacency!");
                    gameMode = GAME_RED;
                    previousButton = null;
                    previousLocation = -1;
                    post();
                    return;
                }
                if (states[ID] == 0) {

                    states[ID] = 1;
                    states[previousLocation] = 0;


                    gameController.drawRed(b);
                    gameController.drawBlank(previousButton);
                    gameController.setButtonText("Blue: Choose a piece to move");

                    if (AI) {
                        computerMakeAMove();

                        gameMode = GAME_RED;
                        gameController.setButtonText("Red: Chose a piece to move");

                        Mill.movingFromMill(previousLocation, checkedMills);

                        post();
                        return;
                    }

                    gameMode = GAME_BLUE;
                    Mill.movingFromMill(previousLocation, checkedMills);

                    previousLocation = -1;
                } else {
                    gameController.alert("Choose a BLANK piece to move to");
                }
                post();
                return;

            case GAME_BLUE_CHOOSE:

                if (!validator.adjacentMove(previousLocation, ID)) {
                    gameController.alert("Must move with adjacency");
                    gameMode = GAME_BLUE;
                    previousButton = null;
                    previousLocation = -1;
                    post();
                    return;
                }
                if (states[ID] == 0) {

                    states[ID] = 2;
                    states[previousLocation] = 0;

                    gameController.drawBlue(b);
                    gameController.drawBlank(previousButton);
                    gameController.setButtonText("Red: Choose a piece to move");

                    gameMode = GAME_RED;
                    Mill.movingFromMill(previousLocation, checkedMills);

                    previousLocation = -1;
                } else {
                    gameController.alert("Choose a BLANK piece to move to");
                }
                post();
                return;

            case SETUP_BLUE:

                gameController.drawBlue((Button) e.getSource());

                states[ID] = 2;
                post();
                return;

            case SETUP_RED:

                gameController.drawRed((Button) e.getSource());

                states[ID] = 1;
                post();
                return;

            case RED_TURN:

                if (states[ID] != 0 || states[ID] == 1) return;

                gameController.drawRed((Button) e.getSource());
                gameController.setButtonText("Blue Turn");

                states[ID] = 1;

                if (AI) {
                    computerPlaceAPiece();

                    gameMode = RED_TURN;
                    post();
                    return;
                }

                gameMode = BLUE_TURN;
                post();
                return;

            case BLUE_TURN:

                if (states[ID] != 0 || states[ID] == 2) return;

                gameController.drawBlue((Button) e.getSource());
                gameController.setButtonText("Red Turn");

                states[ID] = 2;

                gameMode = RED_TURN;
                post();
                return;

        }
    }

    private void post() {

        System.out.println("post()");

        Mill.movingFromMill(previousLocation, checkedMills);

        //Check if any NEW mills have been formed
        Mill m = Mill.checkForMills(states, checkedMills);

        //If a new mill has been formed
        if (m.a != -1) {

            System.out.println(m.a + " " + m.b + " " + m.c);

            if (AI) {
                if (states[m.a] == 1) {
                    gameController.setButtonText("Select a blue piece to remove");
                    gameMode = REMOVE_MILL_BLUE;
                } if (states[m.a] == 2) {
                    computerRemoveARedPiece();
                    if (gameStarted) {
                        gameMode = GAME_RED;
                    } else {
                        gameMode = RED_TURN;
                    }
                }
            } else {
                if (states[m.a] == 1) {
                    gameController.setButtonText("Select a blue piece to remove");
                    gameMode = REMOVE_MILL_BLUE;
                } if (states[m.a] == 2) {
                    gameController.setButtonText("Select a red piece to remove");
                    gameMode = REMOVE_MILL_RED;
                }
            }

            checkedMills.add(m);

        }

        //Depending on the game mode, skip the following code
        if (!(gameMode == GAME_RED || gameMode == GAME_BLUE || gameMode == GAME_RED_CHOOSE || gameMode == GAME_BLUE_CHOOSE)) return;

        //Check the status of the game (blue win? red win? tie?)
        int v = validator.getGameStatus(states);

        switch (v) {

            case Validator.GAME_STATUS_DRAW:

                gameController.alert("It's a DRAW!");
                gameController.setStatusText("It's a DRAW!");
                break;

            case Validator.GAME_STATUS_BLUE_WON:

                gameController.alert("Blue won!");
                gameController.setStatusText("Blue won!");
                break;

            case Validator.GAME_STATUS_RED_WON:

                gameController.alert("Red won!");
                gameController.setStatusText("Red won!");
                break;

        }

    }

    /**
     * Called when the 'new game' button is selected
     * Start the game by setting the game mode
     *
     * @throws Exception
     */
    public void newGame() throws Exception {

        initSetup();

        //Randomly decide who goes first
        int rand = random.nextInt(2);

        if (rand == 1) {
            gameController.setButtonText("Red Turn");
            gameMode = RED_TURN;
        } else {
            gameController.setButtonText("Blue Turn");
            gameMode = BLUE_TURN;
        }

    }

    /**
     * Called whern the 'resume game' button is selected
     * Start the setup mode by setting the game mode variable
     *
     * @throws Exception
     */
    public void resumeGame() throws Exception {

        initSetup();
        gameController.setButtonText("Finish Setup");
        gameMode = SETUP;

    }

    /**
     * Store the game state
    */
    public void storeGame() {

        FileUtil f = new FileUtil();

        f.storeGame(states, checkedMills);

    }

    /**
     * Load the game state, then start the game
    */
    public void loadGame() throws Exception {

        initSetup();

        FileUtil f = new FileUtil();

        states = f.getStates();
        checkedMills = f.getMills();

        System.out.println(f.getBluePieces() + " " + f.getRedPieces());

        redPieces -= f.getRedPieces();
        bluePieces -= f.getBluePieces();

        gameController.drawAll(states);

        gameMode = GAME_RED;
        gameController.setButtonText("Red: Choose a piece to move");
        gameStarted = true;

    }

    /**
     * After the user has ended the seutp mode, this sets the game into regular play mode
     *
     * @throws Exception
     */
    public void startGame() throws Exception {

        gameController.setButtonText("Red Turn");
        gameMode = RED_TURN;
    }

    /**
     * Called whenever the red button is clicked
     */
    public void redToggleClicked() {
        if (gameMode == SETUP || gameMode == SETUP_BLUE) {
            gameMode = SETUP_RED;
        }
    }

    /**
     * Called whenever the blue button is clicked
     */
    public void blueToggleClicked() {
        if (gameMode == SETUP || gameMode == SETUP_RED) {
            gameMode = SETUP_BLUE;
        }
    }

    /**
     * @return the game mode
     */
    public int getGameMode() { return gameMode; }

    /**
     * Entry point of the application
     * @param args - not used
     */
    public static void main(String[] args) {
        launch(args);
    }

}
