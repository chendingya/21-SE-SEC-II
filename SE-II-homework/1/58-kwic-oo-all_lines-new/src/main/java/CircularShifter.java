// -*- Java -*-
/*
 * <copyright>
 * 
 *  Copyright (c) 2002
 *  Institute for Information Processing and Computer Supported New Media (IICM),
 *  Graz University of Technology, Austria.
 * 
 * </copyright>
 * 
 * <file>
 * 
 *  Name:    CircularShifter.java
 * 
 *  Purpose: Holds circular shifts of input lines
 * 
 *  Created: 23 Sep 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    Holds circular shifts of input lines
 * </file>
*/



/*
 * $Log$
*/

import java.util.ArrayList;
import java.util.Arrays;

/**
 *  An object of the CircularShifter class produces and holds all circular shifts of
 *  a set of lines. In principle, the CircularShifter class provides a
 *  similar interface as the LineStorage class, thus allowing to manipulate 
 *  the lines that it holds. However, in the case of the CircularShifter
 *  class the lines are actually circular shifts of a particular set of original
 *  lines. Also, the CircularShifter class does not provide interface for
 *  updating of characters, words, and lines that it holds, but just an
 *  interface for reading characters, words, and lines.
 *  @author  dhelic
 *  @version $Id$
*/

public class CircularShifter{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

/**
 * LineStorage for circular shifts
 *
 */

  private LineStorage shifts_;

  private ArrayList<String> inputArrayList;
  private ArrayList<String> kWICArrayList;
//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Produces all circular shifts of lines in a given set. Circular shifts
 * are stored internally and can be queried by means of other methods. Note,
 * that for each line the first circular shift is same as the original line.
 * @param lines A set of lines
 * @see #getChar
 * @see #getCharCount
 * @see #getWord
 * @see #getWordCount
 * @see #getLine
 * @see #getLineCount
 */

  public void setup(LineStorage lines){
    shifts_ = new LineStorage();
  }

  public CircularShifter(ArrayList<String> arr) {
    inputArrayList = arr;
    kWICArrayList = new ArrayList<>();
  }

  public ArrayList<String> getKWICArrayList() {
    return kWICArrayList;
  }

  public void circularShift() {
    for (String line : inputArrayList) {
      String[] words = line.split(" ");
      ArrayList<String> line_word = new ArrayList<>(Arrays.asList(words));
      for (int i = 0; i < line_word.size(); i++) {
        String tem = line_word.get(0);
        line_word.remove(0);
        line_word.add(tem);
        StringBuilder res = new StringBuilder();
        for (int j = 0; j < line_word.size(); j++) {
          if (j != 0) {
            res.append(" " + line_word.get(j));
          } else {
            res.append(line_word.get(j));
          }
        }
        kWICArrayList.add(res.toString());
      }
    }
  }
//----------------------------------------------------------------------
/**
 * Gets the character from the specified position in the specified word 
 * in a particular line.
 * @param position character index in the word
 * @param word word index in the line
 * @param line line index
 * @return char
 */

  public char getChar(int position, int word, int line){
    return shifts_.getChar(position, word, line);
  }

/**
 * Gets the number of characters in this particular word.
 * @param word word index in the line
 * @param line line index
 * @return int
 */

  public int getCharCount(int word, int line){
    return shifts_.getCharCount(word, line);
  }

//----------------------------------------------------------------------
/**
 * Gets the word from the specified position in a particular line
 * String representing the word is returned.
 * @param word word index in the line
 * @param line line index
 * @return String
 */

  public String getWord(int word, int line){
    return shifts_.getWord(word, line);
  }

//----------------------------------------------------------------------
/**
 * Gets the number of words in this particular line.
 * @param line line index
 * @return int
 */

  public int getWordCount(int line){
    return shifts_.getWordCount(line);
  }

//----------------------------------------------------------------------
/**
 * Gets the line from the specified position.
 * String array representing the line is returned.
 * @param line line index
 * @return String[]
 * @see #getLineAsString
 */

  public String[] getLine(int line){
    return shifts_.getLine(line);
  }


//----------------------------------------------------------------------
/**
 * Gets the line from the specified position.
 * String representing the line is returned.
 * @param line line index
 * @return String
 * @see #getLine
 */

  public String getLineAsString(int line){
    return shifts_.getLineAsString(line);
  }

//----------------------------------------------------------------------
/**
 * Gets the number of lines.
 * @return int
 */

  public int getLineCount(){
    return shifts_.getLineCount();
  }


//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
