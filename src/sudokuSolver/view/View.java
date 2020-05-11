package sudokuSolver.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import sudokuSolver.controller.Controller;

/**
 * View class to display details of the Sudoku Application GUI. Utilizes the
 * controller to initialize most aspects of the GUI.
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */
public class View extends Application {

	/**
	 * The controller for this view
	 */
	private Controller controller;

	/**
	 * x dimension of the frame to produce an ideal frame for the sudoku board.
	 */
	private static final int XSIZE = 400;

	/**
	 * y dimension of the frame to produce an ideal frame for the sudoku board.
	 *
	 */
	private static final int YSIZE = 500;

	public View() {
	}

	/**
	 * method to set up the gui view. This method calls the controller to initialize
	 * the gui.
	 */
	@Override
	public void start(Stage stage) throws Exception {

		/* Controller for this view. */
		this.controller = new Controller(this);
		controller.initialize(null, null);

		/* Set up the display window. */
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #92FC64 "); // make background festive green

		/* Create a scene, and put it into the application window. */
		Scene scene = new Scene(root);
		stage.setScene(scene);

		/* Add the flow pane to the application window. */
		root.setBottom(controller.getFlowPane());

		/* Get the gridPane from the controller and set to application window. */
		Region gridPane = controller.getGridPane();
		root.setCenter(gridPane);

		/* Retrieve the title from the controller */
		stage.setTitle(controller.getTitle());

		/* Finalize the details of the window (stage), and show the stage. */
		stage.setHeight(YSIZE);
		stage.setWidth(XSIZE);
		stage.setTitle(controller.getTitle());
		stage.setResizable(false);
		stage.show();
	}

}
