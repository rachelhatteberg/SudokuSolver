package sudokuSolver.model;

/**
 * Contains the primary exception thrown by the controller when there's a
 * critical point in solving the sudoku puzzle.
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */

public class SolverException extends Exception {
	public SolverException(String str) {
		super(str);
	}

}