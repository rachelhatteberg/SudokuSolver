package sudokuSolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class Solver {

	public static void main(String[] args) {

		/*Tile ti = new Tile(0,0,0,0);
		List<Integer> initialPossibilities = new ArrayList<Integer>(
	            Arrays.asList(1,2,6,7,8,9));
		ti.setPossibilities(initialPossibilities );
		System.out.println(ti);
		try
		{
		Tile mmk = (Tile)ti.clone();
		List<Integer> ayyy = new ArrayList<Integer>(
	            Arrays.asList(1,2));
		mmk.setPossibilities(ayyy);
		System.out.println(ti);
		}
		catch(Exception e)
		{
		}

		System.exit(1);*/


		/*
		 * int[] testi =
		 * {0,0,1,1,1,1,1,2,2,3,3,3,3,4,4,4,4,4,5,5,5,5,6,6,7,7,7,7,7,8,8}; int[] testj
		 * = {1,4,2,4,6,7,8,2,5,1,3,6,8,0,3,4,5,8,0,2,5,7,3,6,0,1,2,4,6,4,7}; int[]
		 * testv = {1,5,3,1,6,5,9,6,4,6,1,5,8,9,3,8,5,6,1,5,6,2,8,3,6,2,7,3,8,4,6};
		 */

		// evil below
		int[] testi = { 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 3, 3, 3, 5, 5, 5, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8 };
		int[] testj = { 2, 3, 5, 0, 2, 3, 4, 5, 0, 6, 0, 1, 6, 2, 7, 8, 2, 8, 3, 4, 5, 6, 8, 3, 5, 6 };
		int[] testv = { 8, 5, 1, 7, 5, 6, 8, 9, 2, 5, 9, 5, 1, 7, 8, 4, 4, 1, 1, 9, 2, 3, 7, 7, 4, 8 };

		// int[] testj = {7,5,6,7,8,3,4,7,8,0,2,7,3,5,1,6,8,0,1,4,5,0,1,2,3,1};
		// int[] testi = {0,1,1,1,1,2,2,2,2,3,3,3,4,4,5,5,5,6,6,6,6,7,7,7,7,8};
		// int[] testv = {4,6,1,8,9,9,5,3,7,3,2,6,6,2,4,3,8,4,8,9,7,9,7,3,1,1};

		Board board = new Board();
		for (int i = 0; i < testi.length; i++) {
			board.setValue(testi[i], testj[i], testv[i]);
		}
		System.out.println(board);

		try {
			Board resultBoard = huristicSolver(board);
			Tile t = findNextTile(resultBoard);
			System.out.println(resultBoard);
			Board finalBoard = recursiveBruteForce(resultBoard, t, t.getPossibilities().remove(0));
			System.out.println(finalBoard);
		} catch (SolverException e) {
			e.printStackTrace();
		}
	}

	private static int MISSING_VALUE = -1;
	private static int BOARD_DIMENSIONS = 9;

	public static boolean evaluateTileGroupNumbers(Board board, String str) throws SolverException {

		boolean found = false;
		Function<Integer, List<Tile>> func;
		switch (str) {
		case "ROW":
			func = (Integer) -> board.getRowTiles(Integer);
			break;
		case "COLUMN":
			func = (Integer) -> board.getColTiles(Integer);
			break;
		case "LG":
			func = (Integer) -> board.getLocalGroupTiles(Integer);
			break;
		default:
			throw new SolverException("Cannot determine what to evaluate");
		}

		for (int i = 0; i < 9; i++) {
			List<Tile> tileGroup = func.apply(i);
			ArrayList<Integer> allPos = new ArrayList<Integer>();
			for (Tile t : tileGroup) {
				if (t.getValue() == MISSING_VALUE) {
					List<Integer> possible = getPossibilities(board, t);
					t.setPossibilities(possible);
					allPos.addAll(t.getPossibilities());
				}
			}
			for (Tile t : tileGroup) {
				if (t.getValue() == MISSING_VALUE) {
					for (Integer poss : t.getPossibilities()) {
						int occurrences = Collections.frequency(allPos, poss);
						/* found something */
						if (occurrences == 1) {
							//System.out.println("found something " + t);
							t.setValue((int) (poss));
							found = true;
							List<Tile> shared = getAllSharedTiles(board, t);
							removePossibility(shared, (int) (poss));
							//System.out.println(board);
							break;
						}
					}
				}
			}
		}
		return found;
	}

	public static Board huristicSolver(Board board) throws SolverException {
		boolean found = true;
		while (found) {
			found = false;
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					Tile curTile = board.getTile(i, j);
					if (curTile.getValue() == MISSING_VALUE) {
						List<Integer> possible = getPossibilities(board, curTile);
						curTile.setPossibilities(possible);
						if (possible.size() == 0) {
							//System.out.println(curTile);
							throw new SolverException("no solution!");
						}
						if (possible.size() == 1) {
							//System.out.println("found something" + i + " " + j);
							found = true;
							curTile.setValue((int) (possible.get(0)));
							//System.out.println(board);
							break;
						}
					}
				}
			}

			try {

				// if evaluateTileGroupNumbers() returns true, leave found as true,
				// otherwise found remains original value
				found = evaluateTileGroupNumbers(board, "ROW") ? true : found;
				found = evaluateTileGroupNumbers(board, "COLUMN") ? true : found;
				found = evaluateTileGroupNumbers(board, "LG") ? true : found;

			} catch (SolverException e) {
				e.printStackTrace();
			}

		}
		return board;
	}

	public static List<Tile> getAllSharedTiles(Board board, Tile tile) {
		List<Tile> allSharedTiles = new ArrayList();

		List<Tile> rowTiles = board.getRowTiles(tile.getX());
		List<Tile> colTiles = board.getColTiles(tile.getY());
		List<Tile> lgTiles = board.getLocalGroupTiles(tile.getLocalGroup());

		allSharedTiles.addAll(rowTiles);
		allSharedTiles.addAll(colTiles);
		allSharedTiles.addAll(lgTiles);

		return allSharedTiles;
	}

	public static void removePossibility(List<Tile> tiles, int poss) {
		for (Tile t : tiles) {
			List<Integer> posses = t.getPossibilities();
			if (posses.contains(poss)) {
				t.getPossibilities().remove(new Integer(poss));
			}
		}
	}

	public static List<Integer> getPossibilities(Board board, Tile tile) {
		List<Integer> initialPossibilities = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		List<Tile> rowTiles = board.getRowTiles(tile.getX());
		List<Tile> colTiles = board.getColTiles(tile.getY());
		List<Tile> allSharedTiles = new ArrayList<Tile>(rowTiles);
		allSharedTiles.addAll(colTiles);
		allSharedTiles.addAll(board.getLocalGroupTiles(tile.getLocalGroup()));
		for (Tile t : allSharedTiles) {
			initialPossibilities.remove(new Integer(t.getValue()));
		}

		// board.getGetColTiles(tile.getY())
		return initialPossibilities;
	}

	public static Board solve(Board board)
	{
		Board finalBoard = null;
		try {
			Board resultBoard = huristicSolver(board);
			Tile t = findNextTile(resultBoard);
			System.out.println(resultBoard);
			finalBoard = recursiveBruteForce(resultBoard, t, t.getPossibilities().remove(0));
			System.out.println(finalBoard);
		} catch (SolverException e) {
			e.printStackTrace();
		}

		return finalBoard;
	}

	/**
	 * Recursive method to finish solving the sudoku puzzle at hand. The method
	 * accepts an instance of the current sudoku board, an Integer ..
	 *
	 * @param board,
	 *            i
	 * @return an instance of the Board
	 */
	// public Board recursiveBruteForce(Board board, Tile tile, List<Integer> poss)
	public static Board recursiveBruteForce(Board board, Tile tile, Integer poss) {
		// at a specific tile, get possibilities, and try solving with first possibility
		// in list.
		// if no solution, then remove possibility from list and try solving with the
		// next

		// try with this tile and poss, and continue trying to solve until get a div/0
		// exception

		// make clone of board
		Board attemptedBoard = null;
		try {
			attemptedBoard = (Board) board.clone();
		} catch (CloneNotSupportedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		attemptedBoard.setValue(tile.getX(), tile.getY(), poss);
		System.out.println("tile : " + tile);
		/* first check if the sudoku has been solved */
		// while (!attemptedBoard.isSolved()) {
		try {
			/* call huristic solver with setting tile to be first element in poss */
			System.out.println("calling Huristic");
			attemptedBoard = huristicSolver(attemptedBoard);

		} catch (SolverException e) {
			// call recursiveBruteForce(board, tile, poss.remove(0)); with new poss, and new
			// tile if no more poss
			// if no solution, make recursive call with original board, same tile and one
			// less poss in list
			// tile.getPossibilities().remove(0) returns the first element in the
			// possibilities List
			System.out.println("divide by 0");
			System.out.println("original** board ");
			System.out.println(board);
			System.out.println(tile);
			Integer nextPos = tile.getPossibilities().remove(0);
			System.out.println("nextPos: "+nextPos);
			return recursiveBruteForce(board, tile, nextPos);

		}
		if (!attemptedBoard.isSolved()) {
			System.out.println("Something worked");
			System.out.println(board);
			tile = findNextTile(board);
			return recursiveBruteForce(board, tile, tile.getPossibilities().remove(0));
		}

		/* return completed board */
		return attemptedBoard;

	}

	public boolean hasPossibilities(Tile tile) {
		List<Integer> poss = new ArrayList<>();
		// correct? or do i need to loop and add?
		poss = tile.getPossibilities();
		if (poss.size() == 0)
			return false;
		return false;
	}

	public static Tile findNextTile(Board board) {

		// TODO find tile that is not -1 and is not itself
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				if (board.getTile(i, j).getValue() == MISSING_VALUE)
					return board.getTile(i, j);
			}
		}
		return null;

	}
}
