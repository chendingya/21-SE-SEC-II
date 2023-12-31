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
 *  Name:    KWIC.java
 * 
 *  Purpose: Demo system for practice in Software Architecture
 * 
 *  Created: 11 Sep 2002 
 * 
 *  $Id$
 * 
 *  Description:
 *    The basic KWIC system is defined as follows. The KWIC system accepts an ordered 
 *  set of lines, each line is an ordered set of words, and each word is an ordered set
 *  of characters. Any line may be "circularly shifted" by repeadetly removing the first
 *  word and appending it at the end of the line. The KWIC index system outputs a
 *  listing of all circular shifts of all lines in alphabetical order.
 * </file>
*/

//package kwic.ms;

/*
 * $Log$
*/

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.ArrayList;

/**
This class is an implementation of the main/subroutine architectural solution for the KWIC system.
 This solution is based on functional decomposition of the system.
 Thus, the system is decomposed into a number of modules, each module being a function.
 In this solution all functions share access to data, which is stored in the "core storage".
 The system is decomposed into the following modules (functions):
<ul>
<li>Master Control (main). This function controls the sequencing among the other four functions.
<li>Input. This function reads the data lines from the input medium (file) and stores them in the core for processing by the remaining modules. The characters are stored in a character array (char[]). The blank space is used to separate words in  a particular line. Another integer array (int[]) keeps the starting indices of  each line in the character array.
<li>Circular Shift. This function is called after the input function has completed its work. It prepares a two-dimensional integer array (int[][]) to keep track of all circular shifts. The array is organized as follows: for each circular  shift, both index of its original line, together with the index of the starting word of  that circular shift are stored as one column of the array.
<li>Alphabetizing. This function takes as input the arrays produced by the input  and circular shift functions. It produces an array in the same format (int[][])  as that produced by circular shift function. In this case, however, the circular   shifts are listed in another order (they are sorted alphabetically).
<li>Output. This function uses the arrays produced by input and alphabetizing  function. It produces a nicely formated output listing of all circular shifts.  </ul>
 该类是KWIC系统的主/子例程架构解决方案的实现。
 该解决方案基于系统的功能分解。
 因此，系统被分解为许多模块，每个模块是一个功能。
 在此解决方案中，所有函数共享对存储在“核心存储”中的数据的访问。
 系统分解为以下几个模块(功能):
 < ul >
 <li>主控(主)。这个函数控制其他四个函数之间的排序。
 <李>输入。该函数从输入介质(文件)中读取数据线，并将其存储在核心中，供其余模块处理。字符存储在字符数组(char[])中。
    空格用于分隔某一行中的单词。另一个整数数组(int[])保存字符数组中每行的起始索引。
 <李>循环转变。该函数在输入函数完成其工作后调用。
    它准备了一个二维整数数组(int[][])来跟踪所有的循环移位。
    数组的组织方式如下:对于每个循环移动，其原始行的索引以及该循环移动的起始字的索引都存储为数组的一列。
 <李>排序。这个函数将输入和圆移位函数产生的数组作为输入。
    它产生的数组格式(int[][])与循环移位函数产生的数组格式相同。
    然而，在这种情况下，循环移位以另一种顺序列出(它们按字母顺序排序)。
 <李>输出。这个函数使用输入和字母排序函数生成的数组。
    它生成所有循环移位的格式良好的输出列表。</ul >
 *  @author  dhelic
 *  @version $Id$
*/

public class KWIC{

//----------------------------------------------------------------------
/**
 * Fields
 *
 */
//----------------------------------------------------------------------

/*
 * Core storage for shared data
 *
 */

/**
 * Input characters
 *
 */

  private char[] chars_;

/**
 * Array that keeps line indices (line index is the index of the first character of a line)
 *保存行索引的数组(行索引是行第一个字符的索引)
 */

  private int[] line_index_;

/**
 * 2D array that keeps circular shift indices (each circular shift index is a column
 * in this 2D array, the first index is the index of the original line from line_index_ 
 * array, the second index is the index of the starting word from chars_ array of 
 * that circular shift)
保存循环移位索引的二维数组
 (每个循环移位索引都是这个二维数组中的一列，第一个索引是line_index_数组中原始行的索引，第二个索引是该循环移位的chars_数组中起始字的索引)
 */

  private int[][] circular_shifts_;

/**
 * 2D array that keeps circular shift indices, sorted alphabetically
 *二维数组，保持循环移位索引，按字母顺序排序
 */

  private int[][] alphabetized_;
  private ArrayList<String> lineText = new ArrayList<String>();
  private ArrayList<String> kWICArrayList = new ArrayList<>();
//----------------------------------------------------------------------
/**
 * Constructors
 *
 */
  public KWIC() {

  }
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
 * Methods
 *
 */
//----------------------------------------------------------------------

//----------------------------------------------------------------------
/**
   Input function reads the raw data from the specified file and stores it in the core storage.
   If some system I/O error occurs the program exits with an error message.
   The format of raw data is as follows.
  Lines are separated by the line separator character(s) (on Unix '\n', on Windows '\r\n').
  Each line consists of a number of words.
  Words are delimited by any number and combination of the space chracter (' ') and the horizontal tabulation chracter ('\t').
  The entered data is parsed in the following way.
  All line separators are removed from the data, all horizontal tabulation word delimiters are replaced by a single space character, and all multiple word delimiters are replaced by a single space character.
  Then the parsed data is represented  in the core as two arrays: chars_ array and line_index_ array.

 * @param file Name of input file
 */

  public void input(String file){
    /**
     * 输入函数从指定的文件中读取原始数据并将其存储在核心存储器中。
     *  如果发生系统I/O错误，程序将退出并显示一条错误消息。
     *  原始数据的格式如下:
     *  行之间由行分隔符分隔(在Unix上为'\n'，在Windows上为'\r\n')。
     *  每一行由若干个单词组成。
     *  单词由任意数字以及空格字符(' ')和水平制表字符('\t')的组合分隔。
     *  输入的数据按以下方式解析。
     *  从数据中删除所有行分隔符，将所有水平制表词分隔符替换为单个空格字符，并将所有多个词分隔符替换为单个空格字符。
     *  然后，解析后的数据在核心中表示为两个数组:chars_ array和line_index_ array。
     */
    try {
      BufferedReader bf = new BufferedReader(new FileReader(file));
      String line;
      while ((line = bf.readLine()) != null) {
        line = line.replace("\t", " ");
        this.lineText.add(line);
      }
    } catch(FileNotFoundException exc){
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not open " + file + "file.");
      System.exit(1);
    }catch(IOException exc){
      exc.printStackTrace();
      System.err.println("KWIC Error: Could not read " + file + "file.");
      System.exit(1);
    }



  }

//----------------------------------------------------------------------
/**
 This function processes arrays prepared by the input
 function and produces circular shifts of the stored lines.
 A circular shift is a line where the first word is removed from the beginning of a line and appended
 at the end of the line. To obtain all circular shifts of a line
 we repeat this process until we can't obtain any new lines.
 Circular shifts are represented as a 2D array that keeps circular shift indices
 (each circular shift index is a column in this 2D array,
 the first index is the index of the original line from line_index_ array,
 the second index is the index of the starting word from chars_ array of that circular shift)
 这个函数处理输入函数准备好的数组，并产生存储行的循环移位。
 循环移位是将第一个单词从行首移走并添加到行尾的行。
 为了得到一条线的所有圆周位移，我们重复这个过程，直到我们不能得到任何新的线。
 循环移位表示为一个保留循环移位索引的二维数组
 (每个循环移位索引都是这个二维数组中的一列，
 第一个索引是line_index_数组中原始行的索引，
 第二个索引是该循环移位的chars_数组中起始单词的索引)
 */

  public void circularShift(){
    for (String line : lineText) {
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
 * This function sorts circular shifts lines alphabetically. The sorted shifts
 * are represented in the same way as the unsorted shifts with the only difference
 * that now they are ordered alphabetically. This function implements binary search
 * to sort the shifts.
 */

  public void alphabetizing(){
    Collections.sort(kWICArrayList);
  }

//----------------------------------------------------------------------
/**
 * This function prints the sorted shifts at the standard output.
 */

  public void output(){
    for (int i = 0; i < this.kWICArrayList.size(); i++) {
      System.out.println(kWICArrayList.get(i));
    }
  }

//----------------------------------------------------------------------
/**
 * This function controls all other functions in the system. It implements
 * the sequence of calls to other functions to obtain the desired functionality
 * of the system. Before any other function is called, main function checks the 
 * command line arguments. The program expects exactly one command line argument
 * specifying the name of the file that contains the data. If the program have
 * not been started with proper command line arguments, main function exits
 * with an error message. Otherwise, the input function is called first to read the 
 * data from the file. After that the circularShift and alphabetizing 
 * functions are called to produce and sort the shifts respectivelly. Finally, the output
 * function prints the sorted shifts at the standard output.
 * @param args command line argumnets
 */

  public static void main(String[] args){
    KWIC kwic = new KWIC();
    kwic.input("Test_Case.txt");
    kwic.circularShift();
    kwic.alphabetizing();
    kwic.output();
  }

//----------------------------------------------------------------------
/**
 * Inner classes
 *
 */
//----------------------------------------------------------------------

}
