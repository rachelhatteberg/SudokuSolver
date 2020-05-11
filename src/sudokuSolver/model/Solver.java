package sudokuSolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Solver class which provides a multi-faceted sudoku solving mechanism for all
 * ranges of difficulty from easy to evil. This class can be broken into two
 * main parts: the heuristic solver and the recursive, brute force solver. For
 * easy puzzles, the heuristic solver is usually all that is needed to solve the
 * problem; however, difficult and evil puzzles can be started using the
 * heuristic, but will need to be completed using a brute-force methodology.
 *
 * A mixed approach was implemented for personal enjoyment in trying to apply
 * personal solving techniques into this algorithm, but with the understanding
 * that I lack understanding of how to humanly solve such a difficult problem,
 * brute force is needed to finish the job.
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */
public class Solver {

	/**
	 * int value for tiles without a value assigned to it.
	 */
	private static int MISSING_VALUE = -1;

	/**
	 * The sudoku board dimensions (9x9 grid).
	 */
	private static int BOARD_DIMENSIONS = 9;

	/**
	 * Message dialogue which will appear if there is no valid solution found.
	 */
	private static final String NO_SOLUTION_WARNING = "No valid solution found!";

	/**
	 * Message denoting unknown entity to evaluate.
	 */
	private static final String UNKNOWN_STRING = "Cannot determine what to evaluate";

	/**
	 * unique possibility in shared tiles' lists of possibilities.
	 */
	private static final int UNIQUE_POSSIBILITY = 1;

	/**
	 * there are no possibilities for the specified tile.
	 */
	private static final int NO_POSSIBILITIES = 0;

	/**
	 * range of values from 1 to 9
	 */
	private static final List<Integer> RANGE = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);

	/**
	 * String representation of ROW
	 */
	private static final String ROW = "ROW";

	/**
	 * String representation of ROW
	 */
	private static final String COLUMN = "COLUMN";

	/**
	 * String representation of LOCAL_GROUP
	 */
	private static final String LOCAL_GROUP = "LG";

	/**
	 * high level solver method to call the heuristic solver and, if needed, calls
	 * the brute force recursive solver.
	 *
	 * @param board
	 *            board being worked on
	 * @return board completed board
	 * @throws SolverException
	 */
	public static Board solve(Board board) throws SolverException {
		/* Record the system time when the solver starts. */
		Board finalBoard = null;
		try {
			Board resultBoard = heuristicSolver(board);
			if (resultBoard.isSolved())
				return resultBoard;
			Tile t = findNextTile(resultBoard);
			System.out.println(resultBoard);
			Integer nextPos = t.getPossibilities().remove(0);
			if (nextPos == null) {
				throw new SolverException(NO_SOLUTION_WARNING);
			}
			finalBoard = recursiveBruteForce(resultBoard, t, nextPos);
			if (finalBoard == null)
				throw new SolverException(NO_SOLUTION_WARNING);
		} catch (IndexOutOfBoundsException e) {
			throw new SolverException(NO_SOLUTION_WARNING);
		}

		return finalBoard;
	}

	/**
	 * method to evaluate a board and try to find as many correct values to insert
	 * into tiles as possible. This method uses human approaches to solving the
	 * sudoku puzzle.
	 *
	 * @param board
	 *            the initial board prior to being passed through the solver.
	 * @return board an instance of the board after passed through the solver.
	 * @throws SolverException
	 */
	public static Board heuristicSolver(Board board) throws SolverException {
		/*
		 * flag will hold status of whether the heuristic solver has been able to find
		 * any new answers to be put into the board. The solver will continue until the
		 * flag is set to false.
		 */
		boolean found = true;
		while (found) {
			found = false;
			for (int i = 0; i < BOARD_DIMENSIONS; i++) {
				for (int j = 0; j < BOARD_DIMENSIONS; j++) {
					Tile curTile = board.getTile(i, j);
					/* if curTile is empty, then we need to find a solution for it! */
					if (curTile.getValue() == MISSING_VALUE) {
						/* get all possibilities for the current tile */
						List<Integer> possible = getPossibilities(board, curTile);
						curTile.setPossibilities(possible);
						if (possible.size() == NO_POSSIBILITIES) {
							throw new SolverException(NO_SOLUTION_WARNING);
						}
						/* if there is only one unique possibility in the list,
						 * then set the value to the curTile and set the found flag
						 * to true since we found something
						 */
						if (possible.size() == UNIQUE_POSSIBILITY) {
							found = true;
							curTile.setValue((int) (possible.get(0)));
							curTile.getPossibilities().remove(0);
							break;
						}
					}
				}
			}

			try {

				/*
				 * make successive calls to evaluate tiles by groups (i.e. row, column,
				 * localgroup). These method calls are an attempt to heuristically solve the
				 * puzzle even further before moving on to the brute-force method.
				 */

				/*
				 * if evaluateTileGroupNumbers returns true, leave found as true, otherwise
				 * found remains original bool.
				 */
				found = evaluateTileGroupNumbers(board, ROW) ? true : found;
				found = evaluateTileGroupNumbers(board, COLUMN) ? true : found;
				found = evaluateTileGroupNumbers(board, LOCAL_GROUP) ? true : found;

			} catch (SolverException e) {
				e.printStackTrace();
			}
		}
		return board;
	}

	/**
	 * method to evaluate tiles within individual group (i.e. row, column, local
	 * group)
	 *
	 * @param board
	 *            current board
	 * @param str
	 *            representation of which group to evaluate
	 * @return true or false depending on whether a value was found for any number
	 *         of tiles.
	 * @throws SolverException
	 */
	public static boolean evaluateTileGroupNumbers(Board board, String str) throws SolverException {

		/*
		 * found flag will determine whether the heuristic solver will continue
		 * evaluating. first set to false, and will be set to true if a solution has
		 * been found for any tile.
		 */
		boolean found = false;

		/*
		 * set method call to func if matches string parameter so we only have to call
		 * the method we want.
		 */
		Function<Integer, List<Tile>> func;
		switch (str) {
		case ROW:
			func = (Integer) -> board.getRowTiles(Integer);
			break;
		case COLUMN:
			func = (Integer) -> board.getColTiles(Integer);
			break;
		case LOCAL_GROUP:
			func = (Integer) -> board.getLocalGroupTiles(Integer);
			break;
		default:
			throw new SolverException(UNKNOWN_STRING);
		}

		/*
		 * loop over each tile in group being evaluated and find all of the
		 * possibilities for each tile.
		 */
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			List<Tile> tileGroup = func.apply(i);
			ArrayList<Integer> allPos = new ArrayList<>();
			for (Tile t : tileGroup) {
				if (t.getValue() == MISSING_VALUE) {
					List<Integer> possible = getPossibilities(board, t);
					t.setPossibilities(possible);
					allPos.addAll(t.getPossibilities());
				}
			}
			/*
			 * using lists of possibilities for each tile, evaluate whether any tile has a
			 * unique possibility, if so, set the possibility/value to the tile. Last,
			 * remove the possibility from the other shared tiles' lists of possibilities.
			 */
			for (Tile t : tileGroup) {
				if (t.getValue() == MISSING_VALUE) {
					for (Integer poss : t.getPossibilities()) {
						int occurrences = Collections.frequency(allPos, poss);
						/*
						 * found a unique possibility amongst shared tiles' lists. set that value to the
						 * tile.
						 */
						if (occurrences == UNIQUE_POSSIBILITY) {
							t.setValue((int) (poss));
							t.setPossibilities(new ArrayList<>());
							/* set found flag to true since we found something */
							found = true;
							List<Tile> shared = getAllSharedTiles(board, t);
							/*
							 * remove the set value from the shared tiles' lists of possibilities.
							 */
							removePossibility(shared, (int) (poss));
							break;
						}
					}
				}
			}
		}
		/* did we find anything? */
		return found;
	}

	/**
	 * Recursive method to finish solving the sudoku puzzle at hand. The method
	 * accepts an instance of the current sudoku board, the current tile being
	 * worked on, and a single possibility of which the tile's value will try to be
	 * set with. The methodology of this solver is to set a possible value to a tile
	 * and pass the updated board back into the heuristic solver and see if the
	 * heuristic solver can find a solution. If not, then it backtracks to the
	 * original board and tries setting the next possibility to the tile and trying
	 * again until a solution is found.
	 *
	 * @param board
	 *            current instance of board
	 * @param tile
	 *            current tile being worked on
	 * @param poss
	 *            possible value which is being set to the tile
	 *
	 * @return an instance of the Board
	 * @throws SolverException
	 */
	public static Board recursiveBruteForce(Board board, Tile tile, Integer poss) throws SolverException {
		/* make clone of board */
		Board attemptedBoard = null;
		try {
			attemptedBoard = (Board) board.clone();
		} catch (CloneNotSupportedException e1) {
			e1.printStackTrace();
		}
		/*
		 * start attempting to solve the remaining puzzle by setting a possible value
		 * poss to attempted board and try passing newly updated board into the heuristic
		 * solver.
		 */
		attemptedBoard.setValue(tile.getX(), tile.getY(), poss);

		try {
			/*
			 * enter into heuristic solver with setting tile to be first element in poss
			 */
			attemptedBoard = heuristicSolver(attemptedBoard);

			/* heuristicSolver will return a SolverException if a dead end was found */
		} catch (SolverException e) {
			if (tile.getPossibilities().size() == NO_POSSIBILITIES) {
				throw new SolverException("test");
			} else {
				Integer nextPos = tile.getPossibilities().remove(0);
				return recursiveBruteForce(board, tile, nextPos);
			}
		}
		/* check whether the board has been solved or not before continuing */
		if (!attemptedBoard.isSolved()) {
			Tile newtile = findNextTile(attemptedBoard, tile);
			try {
				/* recursive call with next tile and a possibility to try */
				return recursiveBruteForce(attemptedBoard, newtile, newtile.getPossibilities().remove(0));
			} catch (SolverException e) {
				/*
				 * attempt recursive method call again by backtracking to original board and
				 * tile and try the next possibility in the list of possibilities.
				 */
				return recursiveBruteForce(board, tile, tile.getPossibilities().remove(0));
			}
		}
		return attemptedBoard;
	}

	/**
	 * method to retrieve a list of tiles connected to a specific tile. More
	 * specifically, it will return the row, column, and local group that the tile
	 * is associated with.
	 *
	 * @param board
	 *            current board being worked on
	 * @param tile
	 *            current tile being evaluated
	 * @return tile list list of shared tiles among the rows, columns, and local
	 *         groups
	 */
	public static List<Tile> getAllSharedTiles(Board board, Tile tile) {
		List<Tile> allSharedTiles = new ArrayList<>();

		/* get row, column, and local group tiles */
		List<Tile> rowTiles = board.getRowTiles(tile.getX());
		List<Tile> colTiles = board.getColTiles(tile.getY());
		List<Tile> lgTiles = board.getLocalGroupTiles(tile.getLocalGroup());

		/* add rows, columns, local group tiles into one shared list */
		allSharedTiles.addAll(rowTiles);
		allSharedTiles.addAll(colTiles);
		allSharedTiles.addAll(lgTiles);

		return allSharedTiles;
	}

	/**
	 * method to remove a possibility from a tile's list of possibilities. This
	 * prevents duplicate values from being added to a row, column, or local group.
	 *
	 * @param tiles
	 *            list of tiles of which a possibility is being removed from
	 * @param poss
	 *            value being removed from the tiles' lists of possibilities
	 */
	public static void removePossibility(List<Tile> tiles, int poss) {
		for (Tile t : tiles) {
			List<Integer> posses = t.getPossibilities();
			/*
			 * only remove a possibility from a tile's list of possibilities if the tile
			 * does not have a set value.
			 */
			if (posses.contains(poss) && t.getValue() == MISSING_VALUE) {
				t.getPossibilities().remove(new Integer(poss));
			}
		}
	}

	/**
	 * getter method to return a list of possibilities for a tile.
	 *
	 * @param board
	 *            board being worked on
	 * @param tile
	 *            current tile being evaluated
	 * @return list of Integers
	 */
	public static List<Integer> getPossibilities(Board board, Tile tile) {
		/* set initial possibilities to be all values from 1 -9 in board */
		List<Integer> initialPossibilities = new ArrayList<>(RANGE);
		List<Tile> rowTiles = board.getRowTiles(tile.getX());
		List<Tile> colTiles = board.getColTiles(tile.getY());
		/* combine all shared tiles into one list */
		List<Tile> allSharedTiles = new ArrayList<>(rowTiles);
		allSharedTiles.addAll(colTiles);
		allSharedTiles.addAll(board.getLocalGroupTiles(tile.getLocalGroup()));
		for (Tile t : allSharedTiles) {
			initialPossibilities.remove(new Integer(t.getValue()));
		}
		return initialPossibilities;
	}

	/**
	 * method to find the next tile which should be evaluated. called upon initial
	 * solving, and don't need to compare to current tile.
	 *
	 * @param board
	 *            board being evaluated
	 * @return tile next tile to evaluate
	 */
	public static Tile findNextTile(Board board) {
		return findNextTile(board, null);
	}

	/**
	 *
	 * overloaded findNextTile method accepting an additional parameter, tile, which
	 * will allow us to compare our "next" tile to the current tile and make sure we
	 * aren't going to attempt to process the same tile again.
	 *
	 * @param board
	 *            board being evaluated
	 * @param tile
	 *            tile being evaluated
	 * @return tile next tile to be evaluated
	 */
	public static Tile findNextTile(Board board, Tile tile) {

		// find tile that is empty and is not itself
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				Tile curTile = board.getTile(i, j);
				if (curTile.getValue() == MISSING_VALUE)
					if ((tile == null) || (tile != curTile)) {
						return curTile;
					}
			}
		}
		return null;
	}

}
