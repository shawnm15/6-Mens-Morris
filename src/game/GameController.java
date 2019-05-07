package game;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the User Interface. Communicates with the fxml files and the Main class
 *
 * @author Victor Velechovsky
 * @version 1.0
 */
public class GameController implements Initializable {

    //instance of Main used to communicate with the class
    private Main application;

    private URL location;
    private ResourceBundle resources;

    /* FXML INJECTIONS */

    @FXML
    private Button controlButton;

    @FXML
    private Button circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15, circle16;

    @FXML
    private Pane surfacePane;

    @FXML
    private Button newGameButton;

    @FXML
    private Button resumeGameButton;

    @FXML
    private Button newGameAI;

    @FXML
    private TextField statusBox;

    @FXML
    private Button pvpButton;

    @FXML
    private Button pvcButton;

    /**
     * Called when the controller is initialized
     *
     * @param location - local PATH of application
     * @param resources - not used
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;

    }

    /**
     * Sets the text of the primary button to a given string
     *
     * @param s - button text string
     */
    public void setButtonText(String s) {
        controlButton.setText(s);
    }

    /**
     * Sets the text of the status box to a given string
     *
     * @param s - The string to be set
     */
    public void setStatusText(String s) {
        statusBox.setText(s);
    }

    /**
     * Gets the application contenxt to communicate with the main class
     * Called by main during setup
     *
     * @param application - Main object
     */
    public void setApp(Application application) {
        this.application = (Main) application;
    }

    /**
     * Sets all of the buttons to be grey
     */
    public void drawBlanks() {
        Button [] buttons = {circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15, circle16};
        for (Button b : buttons) drawBlank(b);
    }

    /**
     * Displays an alert window to the user with a given message
     *
     * @param str - message to display
     */
    public void alert(String str) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(str);

        alert.showAndWait();
    }

    public Button getButton(int i) {

        Button b = null;

        switch(i) {
            case 1:
                b = circle1;
                break;

            case 2:
                b = circle2;
                break;

            case 3:
                b = circle3;
                break;

            case 4:
                b = circle4;
                break;

            case 5:
                b = circle5;
                break;

            case 6:
                b = circle6;
                break;

            case 7:
                b = circle7;
                break;
            case 8:
                b = circle8;
                break;

            case 9:
                b = circle9;
                break;

            case 10:
                b = circle10;
                break;

            case 11:
                b = circle11;
                break;

            case 12:
                b = circle12;
                break;

            case 13:
                b = circle13;
                break;

            case 14:
                b = circle14;
                break;

            case 15:
                b = circle15;
                break;

            case 16:
                b = circle16;
                break;

        }

        return b;

    }

    /**
     * Called whenever a 'circle' button is pressed
     *
     * @param e - The event instance
     * @throws Exception
     */
    @FXML
    protected void circlePressed(ActionEvent e) throws Exception {

        application.circlePressed(e);

    }

    /**
     * Called when the control button (bottom-left) is pressed
     *
     * @param e - event instance
     * @throws Exception
     */
    @FXML
    protected void controlButtonPressed(ActionEvent e) throws Exception {

        int gameMode = application.getGameMode();

        if (gameMode == Main.SETUP || gameMode == Main.SETUP_BLUE || gameMode == Main.SETUP_RED) {
            application.startGame();
        }
    }

    @FXML
    protected void pvpButtonClicked(ActionEvent e) throws Exception {
        application.setAI(false);
        application.showNewGameLayout();
    }

    @FXML
    protected void pvcButtonClicked(ActionEvent e) throws Exception {
        application.setAI(true);
        application.showNewGameLayout();
    }

    /**
     * Draw the button blank (grey)
     *
     * @param c - the button to be painted
     */
    public static void drawBlank(Button c) {
        c.setStyle("-fx-graphic: url('/res/grey.jpg')");
    }

    /**
     * Draw the button red
     *
     * @param c - the button to be painted
     */
    public static void drawRed(Button c) {
        c.setStyle("-fx-graphic: url('/res/red.jpg')");
    }

    /**
     * Draw the button blue
     *
     * @param c - the button to be painted
     */
    public static void drawBlue(Button c) {
        c.setStyle("-fx-graphic: url('/res/blue.jpg')");
    }

    /**
     * Draw the button yellow
     *
     * @param c - the button to be painted
     */
    public static void drawYellow(Button c) {
        c.setStyle("-fx-graphic: url('/res/yellow.jpg')");
    }

    /**
     * Called when the 'New Game' button is pressed. Tells main to start a new game
     *
     * @param e - event instance
     * @throws Exception
     */
    @FXML
    protected void handleNewGameButtonPressed(ActionEvent e) throws Exception {
        application.newGame();

    }

    /**
     * Called when the 'Resume Game' button is pressed. Tells main to setup the board
     *
     * @param e - event instance
     * @throws Exception
     */
    @FXML
    protected void handleResumeGameButtonPressed(ActionEvent e) throws Exception {
        application.resumeGame();
    }

    /**
     * Called when the red toggle is clicked. Used in setup mode
     *
     * @param e - event instance
     * @throws Exception
     */
    @FXML
    protected void onRedToggleClicked(ActionEvent e) throws Exception {
        application.redToggleClicked();
    }

    //Called when the 'Load Game' button is pressed
    @FXML
    protected void handleResumeFromFileButtonPressed(ActionEvent e) throws Exception {
        application.loadGame();
    }

    //Called when the 'Save Game' button is pressed
    @FXML
    protected void onStoreGameButtonPressed(ActionEvent e) throws Exception {

        application.storeGame();

    }

    /**
     * Draw all of the circle colors based on the states variable
     *
     * @param states - array containing the state (color) of each position
     */
    public void drawAll(int [] states) {

        //Loaded from FXML injector
        Button [] buttons = {circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15, circle16};

        for (int i = 0; i < states.length; i++) {
            switch(states[i]) {
                case 0:
                    drawBlank(buttons[i]);
                    break;
                case 1:
                    drawRed(buttons[i]);
                    break;
                case 2:
                    drawBlue(buttons[i]);
                    break;
                default:
                    System.out.println("ERROR"); //Invalid value
                    break;
            }
        }

    }

    /**
     * Called when the blue toggle is clicked. Used in setup mode
     *
     * @param e - event instance
     * @throws Exception
     */
    @FXML
    protected void onBlueToggleClicked(ActionEvent e) throws Exception {
        application.blueToggleClicked();
    }

}
