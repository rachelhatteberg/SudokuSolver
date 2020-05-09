package sudokuSolver.model;

import java.util.*;
public class Tile implements Cloneable
{
   private int value;
   private int x;
   private int y;
   private int localGroup;
   private List<Integer> possibilities;
   private static Integer MISSING_VALUE = -1;


   public Tile(int x,int y, int localGroup)
   {
      this.value = MISSING_VALUE;
      this.x = x;
      this.y = y;
      this.localGroup = localGroup;
      this.possibilities = new ArrayList<Integer>();
   }
   public Tile(int value, int x,int y, int localGroup)
   {
      this.x = x;
      this.y = y;
      this.localGroup = localGroup;
      this.possibilities = new ArrayList<Integer>();
      this.value = value;

   }


   public int getX()
   {
      return this.x;
   }
   public int getY()
   {
      return this.y;
   }
   public int getLocalGroup()
   {
      return this.localGroup;
   }
   public int getValue()
   {
      return this.value;
   }
   public void setValue(int value)
   {
      this.value = value;
   }
   public void setPossibilities(List<Integer> pos)
   {
      this.possibilities = pos;
   }
   public List<Integer> getPossibilities()
   {
      return this.possibilities;
   }
   public String toString()
   {
      String returnString = "";
      returnString += "\n------";
      returnString += "\nrow = " + x;
      returnString += "\ncol = " + y;
      returnString += "\ngrp = " + localGroup;
      returnString += "\npossible = " + possibilities;
      returnString += "\n------";

      return returnString;
   }
   public Object clone() throws CloneNotSupportedException
   {
       Tile clonedTile = (Tile)super.clone();
       return clonedTile;
   }

}
