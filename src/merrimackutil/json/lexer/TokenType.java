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
 * An enumeration of token types.
 */
public enum TokenType {

  /**
   * In JSON there is only one number type.
   */
   NUMBER,

   /**
    * A sequence of characters.
    */
  STRING,

  /**
   * A left brace.
   */
  LBRACE,

  /**
   * A right brace
   */
  RBRACE,

  /**
   * A left bracket.
   */
   LBRACKET,

   /**
    * A right bracket.
    */
    RBRACKET,


  /**
   * Comma
   */
   COMMA,

  /**
   * Colon
   */
   COLON,

  /**
   * An unknown token.
   */
  UNKNOWN,


  /**
   * True value.
   */
  TRUE,

  /**
   * False value.
   */
  FALSE,

  /**
   * Null Token
   */


    /**
     * A null token.
     */
    NULL,

  /**
   * The end of the file token.
   */
  EOF
}
