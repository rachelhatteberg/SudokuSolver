package sudokuSolver.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tile class to create and manipulate tiles within the
 * Sudoku board. Tiles will have an i and j location, an
 * associated list of possibilities, and a value.
 *
 * @author Rachel Hatteberg, S02633540
 * @version 1.0, 04/27/2020 CSC-241 Student Project
 */
public class Tile implements Cloneable {

	/**
	 * holds the numerical value associated with a particular tile.
	 */
    private int value;

    /**
     * holds the x locator information in the x-y sudoku grid.
     */
	private int x;

	/**
     * holds the y locator information in the x-y sudoku grid.
     */
	private int y;

	/**
     * holds the local group locator information in the x-y sudoku grid.
     */
	private int localGroup;

	/**
	 * holds a list of possible values a particular tile may possess.
	 */
	private List<Integer> possibilities;

	/**
	 * parameter assigned to any tile with no value.
	 */
	private static Integer MISSING_VALUE = -1;

	/**
	 * constructor to create a tile at a specified location.
	 *
	 * @param x location in the x direction.
	 * @param y location in the y direction.
	 * @param localGroup position of the individual 3x3 grid this tile is a part of.
	 */
	public Tile(int x, int y, int localGroup) {
		this.value = MISSING_VALUE;
		this.x = x;
		this.y = y;
		this.localGroup = localGroup;
		this.possibilities = new ArrayList<Integer>();
	}

	/**
	 * constructor to create a tile at a specified location and assign it
	 * a value.
	 *
	 * @param value value of tile at indicated location.
	 * @param x location in the x direction.
	 * @param y location in the y direction.
	 * @param localGroup position of the individual 3x3 grid this tile is a part of.
	 */
	public Tile(int value, int x, int y, int localGroup) {
		this.x = x;
		this.y = y;
		this.localGroup = localGroup;
		this.possibilities = new ArrayList<Integer>();
		this.value = value;

	}

	/**
	 * getter to return the x location of the tile.
	 *
	 * @return x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * getter to return the y location of the tile.
	 *
	 * @return y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * getter to return the local group location of the tile.
	 *
	 * @return localGroup
	 */
	public int getLocalGroup() {
		return this.localGroup;
	}

	/**
	 * getter to return the tile's value.
	 *
	 * @return value
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * set a value to a tile.
	 *
	 * @param value
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * set list of possibilities to a tile.
	 *
	 * @param pos
	 */
	public void setPossibilities(List<Integer> pos) {
		this.possibilities = pos;
	}

	/**
	 * getter to return the list of possibilities for a tile.
	 *
	 * @return possibilities
	 */
	public List<Integer> getPossibilities() {
		return this.possibilities;
	}

	/**
	 * overridden toString() method to return information about a tile.
	 */
	@Override
	public String toString() {
		String returnString = "";
		returnString += "\n------";
		returnString += "\nrow = " + x;
		returnString += "\ncol = " + y;
		returnString += "\ngrp = " + localGroup;
		returnString += "\npossible = " + possibilities;
		returnString += "\n------";

		return returnString;
	}

	/**
	 * overridden clone() method to clone tile properties.
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		Tile clonedTile = (Tile) super.clone();
		return clonedTile;
	}

}
