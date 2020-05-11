package sudokuSolver.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Board class to create and use a sudoku board.
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */
public class Board implements Cloneable {

	/**
	 * dimensions of the sudoku board.
	 */
	private static int BOARD_DIMENSIONS = 9;

	/**
	 * number of columns per local group.
	 */
	private static int COL_PER_GROUP = 3;

	/**
	 * tile has no value.
	 */
	private static final int MISSING_VALUE = -1;

	/**
	 * array of tiles with size board dimensions x board dimensions.
	 */
	private Tile tiles[][] = new Tile[BOARD_DIMENSIONS][BOARD_DIMENSIONS];

	/**
	 * map of tiles in local group.
	 */
	private Map<Integer, List<Tile>> localGroupMap = new HashMap<Integer, List<Tile>>();

	/**
	 * map of tiles in a column.
	 */
	private Map<Integer, List<Tile>> colMap = new HashMap<Integer, List<Tile>>();

	/**
	 * map of tiles in a row.
	 */
	private Map<Integer, List<Tile>> rowMap = new HashMap<Integer, List<Tile>>();

	/**
	 * construct new sudoku board.
	 */
	public Board() {
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			localGroupMap.put(new Integer(i), new ArrayList<Tile>());
			rowMap.put(new Integer(i), new ArrayList<Tile>());
			colMap.put(new Integer(i), new ArrayList<Tile>());
		}
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				/* define local groups */
				int localGroup = ((i / COL_PER_GROUP) * COL_PER_GROUP) + (j / COL_PER_GROUP);
				Tile curTile = new Tile(i, j, localGroup);
				tiles[i][j] = curTile;
				/* add tiles to associated maps (row, column, localgroup) */
				localGroupMap.get(localGroup).add(curTile);
				rowMap.get(i).add(curTile);
				colMap.get(j).add(curTile);
			}
		}
	}

	/**
	 * set value to tile at specified i,j location.
	 *
	 * @param i
	 *            i location
	 * @param j
	 *            j location
	 * @param value
	 *            value at i, j location
	 */
	public void setValue(int i, int j, int value) {
		tiles[i][j].setValue(value);
	}

	/**
	 * getter to return tile value at specified i, j location.
	 *
	 * @param i
	 *            i location
	 * @param j
	 *            j location
	 * @return value at i, j location
	 */
	public int getValue(int i, int j) {
		return tiles[i][j].getValue();
	}

	/**
	 * getter to return a tile at a specified i, j location.
	 *
	 * @param i
	 *            i location
	 * @param j
	 *            j location
	 * @return tile
	 */
	public Tile getTile(int i, int j) {
		return tiles[i][j];
	}

	/**
	 * getter to return the local group of tiles.
	 *
	 * @param localGroup
	 * @return localGroup
	 */
	public List<Tile> getLocalGroupTiles(int localGroup) {
		return localGroupMap.get(localGroup);
	}

	/**
	 * set map of tiles for localGroup.
	 *
	 * @param localGroupMap
	 */
	public void setLocalGroupTiles(Map<Integer, List<Tile>> localGroupMap) {
		this.localGroupMap = localGroupMap;
	}

	/**
	 * getter to return a row (list) of tiles.
	 *
	 * @param row
	 * @return row
	 */
	public List<Tile> getRowTiles(int row) {
		return rowMap.get(row);
	}

	/**
	 * set map for row of tiles.
	 *
	 * @param rowMap
	 */
	public void setRowTiles(Map<Integer, List<Tile>> rowMap) {
		this.rowMap = rowMap;
	}

	/**
	 *
	 * getter to return a column (list) of tiles.
	 *
	 * @param column
	 * @return column
	 */
	public List<Tile> getColTiles(int column) {
		return colMap.get(column);
	}

	/**
	 * set map for column of tiles.
	 *
	 * @param colMap
	 */
	public void setColTiles(Map<Integer, List<Tile>> colMap) {
		this.colMap = colMap;
	}

	/**
	 * method to determine whether the sudoku board has been solved or not.
	 *
	 * @return true or false
	 */
	public boolean isSolved() {

		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				if (this.getValue(i, j) == MISSING_VALUE) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * overridden toString() method to return a board to the console for debugging.
	 */
	@Override
	public String toString() {
		String boardString = "";
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			if (i % COL_PER_GROUP == 0)
				boardString += "===================\n";

			boardString += "|";
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				Tile curTile = tiles[i][j];
				int tileValue = curTile.getValue();
				if (tileValue != -1) {
					boardString += tileValue;
				} else {
					boardString += " ";
				}
				if ((j + 1) % COL_PER_GROUP == 0)
					boardString += "|";
				else
					boardString += " ";
			}
			boardString += "\n";

		}
		boardString += "===================\n";
		return boardString;
	}

	/**
	 * overridden clone() method to clone a sudoku board.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Board clonedBoard = new Board();
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				clonedBoard.setValue(i, j, this.tiles[i][j].getValue());
				List<Integer> t = this.tiles[i][j].getPossibilities();
				List<Integer> ayy = new ArrayList<>(Collections.nCopies(t.size(), 0));
				Collections.copy(ayy, t);
				clonedBoard.getTile(i, j).setPossibilities(ayy);
			}
		}
		return clonedBoard;
	}

}
