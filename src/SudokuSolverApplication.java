
import sudokuSolver.view.View;
import javafx.application.Application;

/**
 * Contains the main entry point for this Sudoku Solver Application.
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */
public class SudokuSolverApplication {
	/**
	 * Prevent instantiation except from within the class.
	 */
	private SudokuSolverApplication() {
	}

	/**
	 * Main entry point: instantiate and run the SudokuSolverApplication
	 * application.
	 * <p>
	 * Execute:
	 * </p>
	 *
	 * <pre>
	 * java edu.frontrange.csc241.a1.javafx.SudokuSolverApplication
	 * </pre>
	 *
	 * @param args:
	 *            none
	 */
	public static void main(String... args) {
		Application.launch(View.class, args);
	}
}
