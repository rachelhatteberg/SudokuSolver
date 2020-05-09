package sudokuSolver.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board implements Cloneable {

	private static int BOARD_DIMENSIONS = 9;
	private static int COL_PER_GROUP = 3;

	private Tile tiles[][] = new Tile[BOARD_DIMENSIONS][BOARD_DIMENSIONS];
	private Map<Integer, List<Tile>> localGroupMap = new HashMap<Integer, List<Tile>>();
	private Map<Integer, List<Tile>> colMap = new HashMap<Integer, List<Tile>>();
	private Map<Integer, List<Tile>> rowMap = new HashMap<Integer, List<Tile>>();

	public Board() {
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			localGroupMap.put(new Integer(i), new ArrayList<Tile>());
			rowMap.put(new Integer(i), new ArrayList<Tile>());
			colMap.put(new Integer(i), new ArrayList<Tile>());
		}
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				int localGroup = ((i / COL_PER_GROUP) * COL_PER_GROUP) + (j / COL_PER_GROUP);
				Tile curTile = new Tile(i, j, localGroup);
				tiles[i][j] = curTile;
				localGroupMap.get(localGroup).add(curTile);
				rowMap.get(i).add(curTile);
				colMap.get(j).add(curTile);
			}
		}
	}

	public void setValue(int i, int j, int value) {
		tiles[i][j].setValue(value);
	}

	public int getValue(int i, int j) {
		return tiles[i][j].getValue();
	}

	public Tile getTile(int i, int j) {
		return tiles[i][j];
	}

	public List<Tile> getLocalGroupTiles(int localGroup) {
		return localGroupMap.get(localGroup);
	}

	public void setLocalGroupTiles(Map<Integer, List<Tile>> localGroupMap) {
		this.localGroupMap = localGroupMap;
	}

	public List<Tile> getRowTiles(int row) {
		return rowMap.get(row);
	}

	public void setRowTiles(Map<Integer, List<Tile>> rowMap) {
		this.rowMap = rowMap;
	}

	public List<Tile> getColTiles(int column) {
		return colMap.get(column);
	}

	public void setColTiles(Map<Integer, List<Tile>> colMap) {
		this.colMap = colMap;
	}

	public boolean isSolved() {

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (this.getValue(i, j) == -1) {
					return false;
				}
			}
		}
		return true;
	}

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

	public Object clone() throws CloneNotSupportedException {
		/*
		 * Board clonedBoard = (Board) super.clone();
		 * clonedBoard.setColTiles(colMap.clone()); return clonedBoard;
		 */
		Board clonedBoard = new Board();
		for (int i = 0; i < BOARD_DIMENSIONS; i++) {
			for (int j = 0; j < BOARD_DIMENSIONS; j++) {
				clonedBoard.setValue(i, j, this.tiles[i][j].getValue());
				List<Integer> t = this.tiles[i][j].getPossibilities();
				List<Integer> ayy = new ArrayList<Integer>(Collections.nCopies(t.size(), 0));

				Collections.copy(ayy, t);
                clonedBoard.getTile(i, j).setPossibilities(ayy);
			}
		}
		return clonedBoard;
	}

}
