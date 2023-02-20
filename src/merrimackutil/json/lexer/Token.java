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

/**
 * Implements a basic token class.
 *
 * @author Zach Kissel
 */
 public class Token
 {
    private String val;       // The value of the token.
    private TokenType type;   // The type of token represented.

    /**
     * This is the default constructor.
     */
    public Token()
    {
      val = "";
      type = TokenType.UNKNOWN;

    }

    /**
     * This is the overloaded constructor it sets the value and
     * the token type.
     *
     * @param type the type of the token.
     * @param val the value stored in the token.
     */
    public Token(TokenType type, String val)
    {
      this.type = type;
      this.val = val;
    }

    /**
     * Get the current value associated with the token.
     *
     * @return the string representing the value of the token.
     */
    public String getValue()
    {
      return val;
    }

    /**
     * Get the current type associated with the token.
     *
     * @return the type of token.
     */
    public TokenType getType()
    {
      return type;
    }

    /**
     * Set the value associated with the token.
     *
     * @param val the value of the token.
     */
    public void setValue(String val)
    {
      this.val = val;
    }

    /**
     * Sets the type of token.
     *
     * @param type the type of token.
     */
    public void setType(TokenType type)
    {
      this.type = type;
    }

    /**
     * Determines if two tokens are equal.
     * @return true if they are equal and false otherwise.
     */
     @Override
     public boolean equals(Object obj)
     {
       if (obj == this)
        return true;

       if (obj == null)
        return false;

       if (getClass() != obj.getClass())
        return false;

      Token tok = (Token) obj;
      return this.val.equals(tok.val);
     }

    /**
     * Return a String representation of the Token.
     *
     * @return a string representing the token.
     */
    @Override
    public String toString()
    {
      switch(type)
      {
        case UNKNOWN:
          return "UNKNOWN(" + val + ")";
        case NUMBER:
          return "NUMBER(" + val + ")";
        case LBRACE:
          return "LBRACE";
        case RBRACE:
          return "RBRACE";
        case LBRACKET:
          return "LBRACKET";
        case RBRACKET:
          return "RBRACKET";
        case COMMA:
          return "COMMA";
        case COLON:
          return "COLON";
        case TRUE:
          return "TRUE";
        case FALSE:
          return "FALSE";
        case NULL:
          return "NULL";
        case STRING:
          return "STRING(" + val + ")";
        case EOF:
          return "EOF";
      }
      return "";
    }
 }
