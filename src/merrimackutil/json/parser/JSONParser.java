/*
 *   Copyright (C) 2017 -- 2022  Zachary A. Kissel
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package merrimackutil.json.parser;

import java.util.LinkedList;
import merrimackutil.json.lexer.Lexer;
import merrimackutil.json.lexer.TokenType;
import merrimackutil.json.lexer.Token;
import merrimackutil.json.parser.ast.SyntaxTree;
import merrimackutil.json.parser.ast.nodes.*;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Implements a  class for parsing JSON files.
 * @author Zach Kissel
 */
public class JSONParser
{
  private Lexer lex;            // The lexer for the parser.
  private boolean errorFound;   // TokenType.TRUE if ther was a parser error.
  private boolean doTracing;    // TokenType.TRUE if we should run parser tracing.
  private Token nextTok;        // The current token being analyzed.

  /**
   * Constructs a new JSON parser for the file {@code source} by
   * setting up lexer.
   * @param jsonFile the JSON file to parse.
   * @throws FileNotFoundException if the file can not be found.
   */
  public JSONParser(File jsonFile) throws FileNotFoundException
  {
    lex = new Lexer(jsonFile);
    errorFound = false;
    doTracing = false;
  }

  /**
   * Construct a parser that parses the string {@code str}.
   * @param str the JSON to evaluate.
   */
  public JSONParser(String str)
  {
    lex = new Lexer(str);
    errorFound = false;
    doTracing = false;
  }

  /**
   * Turns tracing on an off.
   */
  public void toggleTracing()
  {
    doTracing = !doTracing;
  }

  /**
   * Determines if the program has any errors that would prevent
   * evaluation.
   * @return TokenType.TRUE if the program has syntax errors; otherwise, false.
   */
  public boolean hasError()
  {
    return errorFound;
  }

  /**
   * Parses the file according to the grammar.
   * @return the abstract syntax tree representing the parsed program.
   */
  public SyntaxTree parse()
  {
    SyntaxTree ast;

    nextToken();    // Get the first token.
    ast = new SyntaxTree(parseJSON());   // Start processing at the root of the tree.

    if (nextTok.getType() != TokenType.EOF)
      logError("Parse error, unexpected token " + nextTok);
    return ast;
  }


  /************
   * Private Methods.
   *
   * It is important to remember that all of our non-terminal processing methods
   * maintain the invariant that each method leaves the next unprocessed token
   * in {@code nextTok}. This means each method can assume the value of
   * {@code nextTok} has not yet been processed when the method begins.
   ***********/

   /**
    * Method to handle the JSON file non-terminal.
    *
    * <JSON> -> <class> | <array>
    * @return a SyntaxNode representing a class or an array.
    */
    private SyntaxNode parseJSON()
    {
      SyntaxNode res = null;
      trace("Enter <JSON>");
      if (nextTok.getType() == TokenType.LBRACE)
      {
        nextToken();
        res = parseClass();
      }
      else if (nextTok.getType() == TokenType.LBRACKET)
      {
        nextToken();
        res = parseArray();
      }

      if (nextTok.getType() != TokenType.EOF)
      {
        logError("Invalid JSON file, EOF expected.");
        return null;
      }
      trace("Exit <JSON>");
      return res;
    }

   /**
    * Method to handle evaluating a class.
    * @return A syntax node representing a class.
    */
    private SyntaxNode parseClass()
    {
      ClassNode cnode = new ClassNode();
      KeyValueNode pair;
      trace("Enter <class>");

      // Check to see if we have an empty class.
      if (nextTok.getType() == TokenType.RBRACE)
        return cnode;

      // Add the first key-value pair to the class.
      pair = (KeyValueNode)parseKVPair();
      if (pair == null)
        return null;
      cnode.addKVPair(pair);

      // Handle the remaining key-value pairs.
      while (nextTok.getType() == TokenType.COMMA)
      {
        nextToken();
        pair = (KeyValueNode)parseKVPair();
        if (pair == null)
          return null;
        cnode.addKVPair(pair);
      }

      // Make sure we have closed the class definition.
      if (nextTok.getType() != TokenType.RBRACE)
      {
        logError("Bad class definition -- expected }.");
        return null;
      }
      nextToken();
      trace("Exit <class>");
      return cnode;
    }

    /**
     * Builds the subtree corresponding to an array.
     * @return a SyntaxNode representing the array.
     */
     private SyntaxNode parseArray()
     {
       ArrayNode anode = new ArrayNode();
       SyntaxNode val;

       trace("Enter <array>");
       if (nextTok.getType() == TokenType.RBRACKET)
       {
        nextToken();
        return anode;
       }

       // Add the first value to the array.
       val = parseValue();
       if (val == null)
        return null;
       anode.addValue(val);
       while (nextTok.getType() == TokenType.COMMA)
       {
         nextToken();
         val = parseValue();
         if (val == null)
          return null;
         anode.addValue(val);
       }

       if (nextTok.getType() != TokenType.RBRACKET)
       {
         logError("Invalid array definition -- missing ]");
         return null;
       }
       nextToken();
       trace("Exit <array>");
       return anode;
     }

     /**
      * Builds the subtree corresponding to a key-value pair.
      * @return a SyntaxNode representing the key-value pair.
      */
     private SyntaxNode parseKVPair()
     {
       TokenNode key;
       SyntaxNode value;
       trace("Enter <kvpair>");
       if (nextTok.getType() != TokenType.STRING)
       {
         logError("Invalid key");
         return null;
       }
       key = new TokenNode(nextTok);
       nextToken();
       if (nextTok.getType() != TokenType.COLON)
       {
         logError("Colon expected");
         return null;
       }
       nextToken();
       value = parseValue();
       if (value == null)
        return null;
       trace("Exit <kvpair>");
       return new KeyValueNode(key, value);
     }

     /**
      * Produces a syntax node representing a JSON value.
      * @return a SyntaxNode representing a JSON value.
      */
     private SyntaxNode parseValue()
     {
       SyntaxNode rv = null;

       trace("Enter <value>");
       if (nextTok.getType() == TokenType.NULL || nextTok.getType() == TokenType.TRUE ||
           nextTok.getType() == TokenType.FALSE || nextTok.getType() == TokenType.NUMBER ||
           nextTok.getType() == TokenType.STRING)
       {
            rv = new TokenNode(nextTok);
            nextToken();
       }
       else if (nextTok.getType() == TokenType.LBRACE)
        {
          nextToken();
          rv = parseClass();
        }
        else if (nextTok.getType() == TokenType.LBRACKET)
        {
          nextToken();
          rv = parseArray();
        }
        else
        {
          logError("Invalid value.");
          return null;
        }
       trace("exit <value>");
       return rv;
     }


  /**
   * Logs an error to the console.
   * @param msg the error message to dispaly.
   */
   private void logError(String msg)
   {
     System.err.println("Error (" + lex.getLineNumber() + "): " + msg);
     errorFound = true;
   }

   /**
    * This prints a message to the screen on if {@code doTracing} is
    * TokenType.TRUE.
    * @param msg the message to display to the screen.
    */
    private void trace(String msg)
    {
      if (doTracing)
        System.out.println(msg);
    }

    /**
     * Gets the next token from the lexer potentially logging that
     * token to the screen.
     */
    private void nextToken()
    {
      nextTok = lex.nextToken();

      if (doTracing)
        System.out.println("nextToken: " + nextTok);

    }

}
