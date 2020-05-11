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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import sudokuSolver.model.Board;
import sudokuSolver.model.Solver;
import sudokuSolver.model.SolverException;
import sudokuSolver.view.View;

/**
 * Controller class to work between the view and model classes (Board, Solver, and Tile).
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */
public class Controller implements Initializable {

	/**
	 * Title of GUI application.
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
	 * The board that the sudoku puzzle is build upon.
	 */
	private Board board;

	/**
	 * The container which holds the "Solve" and "Clear" buttons.
	 */
	private FlowPane flowPane;

	/**
	 * String value of the solve button.
	 */
	private static final String SOLVE_BUTTON_STRING = "Solve";

	/**
	 * The solve button which when clicked, will kick off the solver algorithms.
	 */
	private Button solveButton = new Button(SOLVE_BUTTON_STRING);

	/**
	 * String value of the clear button.
	 */
	private static final String CLEAR_BUTTON_STRING = "Clear";

	/**
	 * The solve button tooltip which gives the user guidance on the button's
	 * purpose.
	 */
	private String SOLVE_BUTTON_TOOLTIP = "Select this 'solve' button once you have"
			+ " input all numbers into the sudoku grid.";

	/**
	 * The clear button tooltip which gives the user guidance on the button's
	 * purpose.
	 */
	private String CLEAR_BUTTON_TOOLTIP = "Select this 'clear' button if you want to "
			+ "clear all input values in this sudoku grid.";

	/**
	 * Representation of a tile grid cell with a missing, non-solved value.
	 */
	private static final int MISSING_VALUE = -1;

	/**
	 * The sudoku board dimensions (9x9 grid).
	 */
	private static final int BOARD_DIMENSIONS = 9;

	/**
	 * adjusted inset value for "Solve" button.
	 */
	private static final int SOLVE_RIGHT_INSET = 40;

	/**
	 * adjusted inset value for "Solve" button.
	 */
	private static final int SOLVE_BOTTOM_INSET = 20;

	/**
	 * adjusted inset value for "Solve" button.
	 */
	private static final int SOLVE_LEFT_INSET = 90;

	/**
	 * adjusted inset value for "Clear" button.
	 */
	private static final int CLEAR_RIGHT_INSET = 70;

	/**
	 * adjusted inset value for "Clear" button.
	 */
	private static final int CLEAR_BOTTOM_INSET = 20;

	/**
	 * adjusted inset value for "Clear" button.
	 */
	private static final int CLEAR_LEFT_INSET = 75;

	/**
	 * The controller that mediates the interaction of user actions with the model.
	 *
	 * @param view
	 *            the view to be controlled
	 */
	public Controller(View view) {
		this.board = new Board();
	}

	/**
	 * Get the title for the window.
	 *
	 * @return the title
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

		solveButton.setTooltip(new Tooltip(SOLVE_BUTTON_TOOLTIP));
		solveButton.setDisable(false);

		/* tooltips for the clear and solve buttons */
		Button clearButton = new Button(CLEAR_BUTTON_STRING);
		clearButton.setTooltip(new Tooltip(CLEAR_BUTTON_TOOLTIP));

		/* handle event when the solve button is pressed */
		EventHandler<ActionEvent> solveEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					board = Solver.solve(board);
				} catch (SolverException e2) {
					/* display an alert saying there is no valid solution! */
					Alert a = new Alert(AlertType.INFORMATION);
					a.setContentText(e2.getMessage());
					a.show();
				}

				/* loop over textField nodes and display sudoku solution onto the gridpane */
				TextField[][] boxes = new TextField[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
				/* each column */
				for (int i = 0; i < BOARD_DIMENSIONS; i++) {
					/* each row */
					for (int j = 0; j < BOARD_DIMENSIONS; j++) {
						TextField curField = new TextField();
						/* don't add the tile value back into grid if missing */
						if (board.getValue(j, i) != MISSING_VALUE)
							curField.setText(String.valueOf(board.getValue(j, i)));
						boxes[i][j] = curField;
						gridPane.add(curField, i, j);
					}
				}
				/* disable solve button after puzzle has been solved */
				disableSubmitButton();
			}
		};
		solveButton.setOnAction(solveEvent);

		/* handle event when clear button is pressed */
		EventHandler<ActionEvent> clearEvent = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				gridPane.clearAll();

				/* re-enable solve button */
				solveButton.setDisable(false);
			}
		};
		clearButton.setOnAction(clearEvent);

		/* set location of buttons and add to flow pane */
		flowPane.setMargin(solveButton, new Insets(0, SOLVE_RIGHT_INSET, SOLVE_BOTTOM_INSET, SOLVE_LEFT_INSET));
		flowPane.setMargin(clearButton, new Insets(0, CLEAR_RIGHT_INSET, CLEAR_BOTTOM_INSET, CLEAR_LEFT_INSET));
		flowPane.getChildren().addAll(solveButton, clearButton);

		/* The title to use on the window. */
		title = GUI_APPLICATION_TITLE;
	}

	/**
	 * method to disable the submit button.
	 */
	public void disableSubmitButton() {
		solveButton.setDisable(true);
	}



	/**
	 * Inner GridPane class extends javafx's GridPane to provide a more specialized set of
	 * capabilities.
	 *
	 * @author Rachel Hatteberg, S02633540
	 * @version 1.0, 04/27/2020 CSC-241 Student Project
	 */
	private class GridPane extends javafx.scene.layout.GridPane {

		/**
		 * The sudoku board dimensions (9x9 grid).
		 */
		private final int BOARD_DIMENSIONS = 9;

		/**
		 * pre-set width of a GridPane text box for best appearance.
		 */
		private final int TEXT_BOX_WIDTH = 50;

		/**
		 * pre-set height of a GridPane text box for best appearance.
		 */
		private final int TEXT_BOX_HEIGHT = 30;

		/**
		 * maximum value a user is allowed to enter for the sudoku puzzle.
		 */
		private final int USER_INPUT_MAX = 9;

		/**
		 * minimum value a user is allowed to enter for sudoku puzzle.
		 */
		private final int USER_INPUT_MIN = 1;

		/**
		 * number of columns per local group.
		 */
		private static final int COL_PER_GROUP = 3;

		/**
		 * String representation of an error when a user inputs an invalid value into a
		 * text box.
		 */
		private final String USER_INPUT_ERROR = "Input into sudoku must be a " + "number from 1-9. Try again!";

		/**
		 * thin border line thickness.
		 */
		private static final String THIN_BORDER_LINE = "0.5 ";

		/**
		 * thick border line thickness.
		 */
		private static final String THICK_BORDER_LINE = "2 ";

		/**
		 * end column or row marker.
		 */
		private static final int END_COLUMN_ROW = 8;

		/**
		 * first column or row marker.
		 */
		private static final int FIRST_COLUMN_ROW = 0;

		/**
		 * default constructor.
		 */
		public GridPane() {
		}

		/**
		 * method to initialize the GridPane and listen for modified text fields. A
		 * simple value validation is performed to ensure correct values are entered.
		 */
		public void initialize() {

			gridPane.setAlignment(Pos.CENTER);

			TextField[][] boxes = new TextField[BOARD_DIMENSIONS][BOARD_DIMENSIONS];

			// create an alert
			Alert a = new Alert(AlertType.NONE);

			// each column
			for (int i = 0; i < BOARD_DIMENSIONS; i++) {
				// each row
				for (int j = 0; j < BOARD_DIMENSIONS; j++) {
					TextField curField = new TextField();
					setTextBoxStyle(curField, i, j);
					boxes[i][j] = curField;
					curField.setPrefWidth(TEXT_BOX_WIDTH);
					curField.setPrefHeight(TEXT_BOX_HEIGHT);
					gridPane.add(curField, i, j);

					/* assign location information to curField object */
					curField.setId(i + "" + j);

					curField.textProperty().addListener((observable, oldValue, newValue) -> {
						StringProperty textProperty = (StringProperty) observable;
						TextField textField = (TextField) textProperty.getBean();

						/* check that user input is within valid range of 1-9 */
						try {
							if (!isEntryValid(Integer.parseInt(newValue))) {
								handleUserInputException(curField, a);
							}
							/* parse string into int array to pass into board */
							Integer[] tempArr = new Integer[2];
							tempArr = parseIdString(textField.getId());
							/* set textField to correct place in board */
							board.setValue(tempArr[1], tempArr[0], Integer.parseInt(newValue));
						} catch (NumberFormatException e) {
							handleUserInputException(curField, a);
						} catch (IllegalArgumentException e) {
							handleUserInputException(curField, a);
						}
					});
				}
			}
		}

		/**
		 * handleUserInputException method to handle displaying an informational alert
		 * box to the user when the inputed value is incorrect. The incorrect input
		 * value will be cleared from the sudoku grid.
		 *
		 * @param curField
		 * @param a
		 */
		public void handleUserInputException(TextField curField, Alert a) {
			/* only show information alert if curField is non-empty */
			if (!curField.getText().isEmpty()) {
				/* set alert type */
				a.setContentText(USER_INPUT_ERROR);
				a.setAlertType(AlertType.INFORMATION);
				a.show();
			}
			/* clear invalid input */
			Platform.runLater(() -> {
				curField.clear();
			});
		}

		/**
		 * method clearAll() clears the sudoku board as well as clears the grid pane in
		 * the gui.
		 *
		 * @param void
		 * @return void
		 */
		public void clearAll() {

			/* first, clear the board */
			board = new Board();

			/* clear the GridPane and re-initialize */
			gridPane.getChildren().clear();
			initialize();
		}

		/**
		 * method parseIdString() accepts a String and parses it into an array of
		 * Integers and returns such Integer array.
		 *
		 * @param str
		 * @return Integer[] array
		 */
		private Integer[] parseIdString(String str) {
			/* create temporary strings for the substrings */
			String tmpStr = str.substring(0, 1);
			String tmpStr1 = str.substring(1);

			return new Integer[] { Integer.parseInt(tmpStr), Integer.parseInt(tmpStr1) };
		}

		/**
		 * Method isEntryValid() to accept an Integer and check whether it is within a
		 * range of valid values.
		 *
		 * @param enteredValue
		 * @return boolean
		 */
		private boolean isEntryValid(Integer enteredValue) {
			/* check whether entered value is valid */
			if ((enteredValue <= USER_INPUT_MAX) && (enteredValue >= USER_INPUT_MIN)) {
				return true;
			}
			/* invalid input */
			return false;
		}

		/**
		 * method to set the css border style for each text box to create the
		 * traditional sudoku board appearance.
		 *
		 * @param textbox
		 *            textbox performing action upon
		 * @param i
		 *            location of textbox
		 * @param j
		 *            location of textbox
		 */
		public void setTextBoxStyle(TextField textbox, int i, int j) {
			String border = "-fx-border-width: ";
			// top
			if (j % COL_PER_GROUP == 0 || j == FIRST_COLUMN_ROW)
				border += THICK_BORDER_LINE;
			else
				border += THIN_BORDER_LINE;
			// right
			if (i == END_COLUMN_ROW)
				border += THICK_BORDER_LINE;
			else
				border += THIN_BORDER_LINE;
			// bottom
			if (j == END_COLUMN_ROW)
				border += THICK_BORDER_LINE;
			else
				border += THIN_BORDER_LINE;
			// left
			if (i % COL_PER_GROUP == 0 || i == FIRST_COLUMN_ROW)
				border += THICK_BORDER_LINE;
			else
				border += THIN_BORDER_LINE;

			textbox.setStyle("-fx-border-style: solid; " + border);
		}

	}
}
