package sudokuSolver.view;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collections;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sudokuSolver.controller.Controller;
import sudokuSolver.model.Solver;

public class View extends Application {

/**
 * The controller for this view
 */
private Controller controller;

/**
 * x dimension of the frame (chosen to match the default frame produced by
 * Swing--this size is not really critical).
 */
private static final int XSIZE = 400;

/**
 * y dimension of the frame (chosen to match the default frame produced by
 * Swing--this size is not really critical).

 */
private static final int YSIZE = 500;

public View() {}


@Override
public void start(Stage stage) throws Exception {

	/* Controller for this view. */
	this.controller = new Controller(this);
	controller.initialize(null, null);

	/* Set up the display window. */
	BorderPane root = new BorderPane();
	root.setStyle("-fx-background-color: #92FC64 ");	// make background grey

    /* Create a scene, and put it into the Application window */
	Scene scene = new Scene(root);
	stage.setScene(scene);


	root.setBottom(controller.getFlowPane());

	Region gridPane = controller.getGridPane();
	root.setCenter(gridPane);

	stage.setTitle(controller.getTitle());

	/*final ImageView sudokuImage = new ImageView();
	Image image1 = new Image(new FileInputStream("C:\\CSC241\\testsudoku.JPG"), 300, 400, false, true);
	sudokuImage.setImage(image1);
	root.setCenter(sudokuImage);*/

	scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());

	/*File file = new File("C:\\CSC241\\testsudoku.JPG");
	String url = file.toURL().toString();
    root.setBackground(new Background(
            Collections.singletonList(new BackgroundFill(
                    Color.TRANSPARENT,
                    new CornerRadii(500),
                    new Insets(10))),
            Collections.singletonList(new BackgroundImage(
                    new Image(url, 100, 100, false, true),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT))));
*/
	/* Finalize the details of the window (stage), and show the stage. */
	stage.setHeight(YSIZE);
	stage.setWidth(XSIZE);
	stage.setTitle(controller.getTitle());
	stage.setResizable(false);
	stage.show();
}

}
