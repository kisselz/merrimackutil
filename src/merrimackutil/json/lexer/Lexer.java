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
package merrimackutil.json.lexer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.File;
import java.util.HashMap;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * This file implements a basic lexical analyzer.
 * @author Zach Kissel
 */
 public class Lexer
 {
   private BufferedReader input;      // The input to the lexer.
   private char nextChar;             // The next character read.
   private boolean skipRead;          // Whether or not to skip the next char
                                      // read.
   private long currentLineNumber;    // The current line number being processed.

   // The dictionary of language keywords
   private HashMap<String, TokenType> keywords;

   private enum CharacterClass {LETTER, DIGIT, WHITE_SPACE, OTHER, END, MINUS, QUOTE};
   CharacterClass nextClass; // The character class of the nextChar.

   /**
    * Constructs a new lexical analyzer whose source
    * input is a file.
    * @param file the file to open for lexical analysis.
    * @throws FileNotFoundException if the file can not be opened.
    */
   public Lexer(File file) throws FileNotFoundException
   {
     input = new BufferedReader(new FileReader(file));
     currentLineNumber = 1;
     loadKeywords();
   }

   /**
    * Constructs a new lexical analyzer whose source is a string.
    * @param input the input to lexically analyze.
    */
    public Lexer(String input)
    {
      this.input = new BufferedReader(new StringReader(input));
      currentLineNumber = 1;
      loadKeywords();
    }

    /**
     * Gets the next token from the stream.
     * @return the next token.
     */
    public Token nextToken()
    {
      String value = "";   // The value to be associated with the token.

      getNonBlank();
      switch (nextClass)
      {
        // The state where we are recognizing identifiers.
        // Regex: [A-Za-Z][0-9a-zA-z]*
        case QUOTE:
          getChar();

          // Read the rest of the identifier.
          while (nextClass != CharacterClass.QUOTE )
          {
            value += nextChar;
            getChar();
          }
          return new Token(TokenType.STRING, value);
        case LETTER:
          value += nextChar;
          getChar();

          // Read the rest of the identifier.
          while (nextClass == CharacterClass.DIGIT ||
              nextClass == CharacterClass.LETTER)
          {
            value += nextChar;
            getChar();
          }
          unread(); // The symbol just read is part of the next token.

          // Check the keyword dictionary.
          if (keywords.containsKey(value))
            return new Token(keywords.get(value), "");
          return new Token(TokenType.UNKNOWN, value);

        // The state where we are recognizing digits.
        // Regex: [0-9]+
        case MINUS:
        case DIGIT:
          value += nextChar;
          getChar();

          while(nextClass == CharacterClass.DIGIT)
          {
            value += nextChar;
            getChar();
          }

          if(nextChar == '.') // Decimal point.
          {
            value += nextChar;
            getChar();
            while (nextClass == CharacterClass.DIGIT)
            {
              value += nextChar;
              getChar();
            }
          }
          unread(); // The symbol just read is part of the next token.

          return new Token(TokenType.NUMBER, value);

        // Handles all special character symbols.
        case OTHER:
          return lookup();

        // We reached the end of our input.
        case END:
          return new Token(TokenType.EOF, "");

        // This should never be reached.
        default:
          return new Token(TokenType.UNKNOWN, "");
      }
    }

    /**
     * Get the current line number being processed.
     * @return the current line number being processed.
     */
    public long getLineNumber()
    {
      return currentLineNumber;
    }

    /************
     * Private Methods
     ************/

     /**
      * Processes the {@code nextChar} and returns the resulting token.
      * @return the new token.
      */
     private Token lookup()
     {
       String value = "";

       switch(nextChar)
       {
         case '-':
          value += "-";
         case '.':      // A double with just a leading dot.
          value += ".";
          getChar();
          if (nextClass != CharacterClass.DIGIT)
          {
            unread();
            return new Token(TokenType.UNKNOWN, "." + String.valueOf(nextChar));
          }
          while (nextClass == CharacterClass.DIGIT)
          {
            value += nextChar;
            getChar();
          }
          unread();
          return new Token(TokenType.NUMBER, value);
         case '{':
          return new Token(TokenType.LBRACE, "{");
         case '}':
          return new Token(TokenType.RBRACE, "}");
         case '[':
           return new Token(TokenType.LBRACKET, "[");
         case ']':
           return new Token(TokenType.RBRACKET, "]");
         case ':':
           return new Token(TokenType.COLON, ":");
         case ',':
          return new Token(TokenType.COMMA, ",");
         default:
          return new Token(TokenType.UNKNOWN, String.valueOf(nextChar));
       }
     }

     /**
      * Gets the next character from the buffered reader. This updates
      * potentially both {@code nextChar} and {@code nextClass}.
      */
     private void getChar()
     {
       int c = -1;

       // Handle the unread operation.
       if (skipRead)
       {
         skipRead = false;
         return;
       }

       try {
         c = input.read();
       }
       catch(IOException ioe)
       {
         System.err.println("Internal error (getChar()): " + ioe);
         nextChar = '\0';
         nextClass = CharacterClass.END;
       }

       if (c == -1) // If there is no character to read, we've reached the end.
       {
        nextChar = '\0';
        nextClass = CharacterClass.END;
        return;
       }

       // Set the character and determine it's class.
       nextChar = (char)c;
       if (Character.isLetter(nextChar))
        nextClass = CharacterClass.LETTER;
       else if (Character.isDigit(nextChar))
        nextClass = CharacterClass.DIGIT;
       else if (Character.isWhitespace(nextChar))
        nextClass = CharacterClass.WHITE_SPACE;
       else if (nextChar == '-')
        nextClass = CharacterClass.MINUS;
       else if (nextChar == '\"')
        nextClass = CharacterClass.QUOTE;
       else
          nextClass = CharacterClass.OTHER;

       // Update the line counter for error checking.
       if (nextChar == '\n')
        currentLineNumber++;
     }

     /**
      * Gets the next non-blank character.  This updates
      * potentially both {@code nextChar} and {@code nextClass}.
      */
     private void getNonBlank()
     {
       getChar();

       while (nextClass != CharacterClass.END &&
            Character.isWhitespace(nextChar))
            getChar();
     }

     /**
      * Save the previous character for a future read operation.
      */
     private void unread()
     {
        skipRead = true;
     }

     /**
      * Sets up the dictionary with all of the keywords.
      */
      private void loadKeywords()
      {
        keywords = new HashMap<String, TokenType>();
        keywords.put("null", TokenType.NULL);
        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);
      }


 }
