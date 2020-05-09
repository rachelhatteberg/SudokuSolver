package sudokuSolver.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import sudokuSolver.model.Board;
import sudokuSolver.model.Solver;
import sudokuSolver.model.SolverException;
import sudokuSolver.view.View;

public class Controller implements Initializable {

	/**
	 * Title of GUI application/
	 */
	private static final String GUI_APPLICATION_TITLE = "Sudoku Puzzle Solver";

	/**
	 * (GUI) The title to appear on the window.
	 */
	private String title;

	/**
	 * The GridPane container to hold the Sudoku grid.
	 */
	private GridPane gridPane;

	/**
	 * The view for which this is to be the controller.
	 */
	private final View view;

	/**
	 * The board that the sudoku puzzle is build upon
	 */
	private Board board;

	/**
	 * The container which holds the Solve and Clear buttons.
	 */
	private FlowPane flowPane;

	/**
	 * The solve button which when clicked, will kick off the solver algorithms
	 */
	private Button solveButton = new Button("Solve");

	/**
	 * Representation of a tile grid cell with a missing, non-solved value
	 */
     private Integer MISSING_VALUE = -1;

	/**
	 * The controller that mediates the interaction of user actions with the model.
	 *
	 * @param view		the view to be controlled
	 */
	public Controller(View view)
	{
		this.view = view;
		this.board = new Board();
		//this.model = new Model();
	}

	/**
	 * Get the title for the window.
	 *
	 * @return			the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the GridPane container to hold the Sudoku grid
	 *
	 * @return the GridPane
	 */
	public GridPane getGridPane() {
		return gridPane;
	}

	/**
	 * Get the FlowPane container to hold the Solve and Clear buttons
	 *
	 * @return the FlowPane
	 */
	public FlowPane getFlowPane() {
		return flowPane;
	}

	/**
	 * Create and initialize the containers for the View.
	 *
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		gridPane = new GridPane();
		gridPane.initialize();
		flowPane = new FlowPane();

        solveButton.setDisable(false);

		Button clearButton = new Button("Clear");

		EventHandler<ActionEvent> solveEvent = new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e)
	        {
				//Solver.huristicSolver(board);
				board = Solver.solve(board);

	            System.out.println(board);

	            // display sudoku solution on gridpane
	            System.out.println("displaying solved puzzle on grid");

	            TextField[][] boxes = new TextField[9][9];
	            // each column
	            for (int i = 0; i < 9; i++) {
	                // each row
	                for (int j = 0; j < 9; j++) {
	                	TextField curField = new TextField();
	                	// don't add the tile value back into grid if missing
	                	if ( board.getValue(j, i) != MISSING_VALUE )
	                		curField.setText(String.valueOf(board.getValue(j, i)));
	                	boxes[i][j] = curField;
	                    gridPane.add(curField, i, j);
	                }
	            }

	            // disable solve button after puzzle has been solved
	            disableSubmitButton();

	        }
	    };

	   // when solve button is pressed
	    solveButton.setOnAction(solveEvent);

	    EventHandler<ActionEvent> clearEvent = new EventHandler<ActionEvent>() {
	        public void handle(ActionEvent e)
	        {
	            gridPane.clearAll();

	            // re-enable solve button
	            solveButton.setDisable(false);
	        }
	    };

	    // when clear button is pressed
	    clearButton.setOnAction(clearEvent);

	    // set location of buttons and add to flow pane
		flowPane.setMargin(solveButton, new Insets(0, 40, 20, 90));
		flowPane.setMargin(clearButton, new Insets(0, 70, 20, 75));
		flowPane.getChildren().addAll(solveButton, clearButton);

		/* The title to use on the window. */
	    title = GUI_APPLICATION_TITLE;

	}

    public void disableSubmitButton() {
        solveButton.setDisable(true);
    }



private class GridPane extends javafx.scene.layout.GridPane{
	// TODO:
	//      * fix grid population
	//      * add sudoku grid image
	//      * add multithreading in brute force solver
	//      * popup if no solution exists - check inputs
	//      * code cleanup - comments and parameters

	private final Integer NUMBER_COLUMNS = 9;
	private final Integer NUMBER_ROWS = 9;

	private final Integer USER_INPUT_MAX = 9;
	private final Integer USER_INPUT_MIN = 1;

	private final String USER_INPUT_ERROR = "Input into sudoku must be a number from 1-9. Try again!";

	public GridPane() {}

	public void initialize() {

		gridPane.setAlignment(Pos.CENTER);

		gridPane.setPadding(new Insets(25, 25, 25, 25));
		gridPane.setHgap(5);
		gridPane.setVgap(5);
		// TODO remove once done debugging
		gridPane.setGridLinesVisible(false);

		TextField[][] boxes = new TextField[NUMBER_COLUMNS][NUMBER_ROWS];

		// each column
        for (int i = 0; i < NUMBER_COLUMNS; i++) {
            // each row
            for (int j = 0; j < NUMBER_ROWS; j++) {
            	TextField curField = new TextField();
            	boxes[i][j] = curField;
                gridPane.add(curField, i, j);

                /* assign location information to curField object */
                curField.setId( i + "" + j);

                curField.textProperty().addListener((observable, oldValue, newValue) -> {
        		    System.out.println("textfield changed from " + oldValue + " to " + newValue);
        		    StringProperty textProperty = (StringProperty)observable;
        		    TextField textField = (TextField) textProperty.getBean();
        		    System.out.println(textField.toString());
        		    System.out.println("id: " +textField.getId());

        		    /* check that user input is within valid range of 1-9 */
        		    try {
        		        if (!isEntryValid(Integer.parseInt(newValue))) {
        		        	System.out.println(USER_INPUT_ERROR);
        		        	Platform.runLater(() -> {
        		                curField.clear();
        		            });
        		        }
        		        // parse string into int array to pass into board
        		        Integer[] tempArr = new Integer[2];
        		        tempArr = parseIdString(textField.getId());
        		        // Set textField to correct place in board
        		        board.setValue(tempArr[1], tempArr[0], Integer.parseInt(newValue));
        		    }
        		    catch (NumberFormatException e){
        		    	 System.out.println (USER_INPUT_ERROR);
        		    	 Platform.runLater(() -> {
     		                curField.clear();
     		            });
        		    }
        		    catch (IllegalArgumentException e) {
        		    	System.out.println(USER_INPUT_ERROR);
        		    	Platform.runLater(() -> {
    		                curField.clear();
    		            });
        		    }
        		});


            }
        }

    }

	/**
	 * method clearAll() clears the sudoku board as well
	 * as clears the grid pane in the gui.
	 *
	 * @param void
	 * @return void
	 */
	public void clearAll() {

		// first, clear the board
		board = new Board();

		// next, clear the GridPane and re-initialize
		gridPane.getChildren().clear();
    	initialize();
	}

	/**
	 * method parseIdString() accepts a String and parses it into
	 * an array of Integers and returns such Integer array.
	 *
	 * @param str
	 * @return Integer[] array
	 */
	private Integer[] parseIdString(String str) {
		// create temporary strings for the substrings
        String tmpStr = str.substring(0,1);
        String tmpStr1 = str.substring(1);

		return new Integer[] {Integer.parseInt(tmpStr), Integer.parseInt(tmpStr1)};
	}


	/**
	 * Method isEntryValid() to accept an Integer and check whether it
	 * is within a range of valid values.
	 *
	 * @param enteredValue
	 * @return boolean
	 */
	private boolean isEntryValid(Integer enteredValue){
		if (( enteredValue <= USER_INPUT_MAX ) && ( enteredValue >= USER_INPUT_MIN )) return true;
		else return false;
	}

}
}
