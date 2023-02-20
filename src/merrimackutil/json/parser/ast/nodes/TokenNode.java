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
package merrimackutil.json.parser.ast.nodes;

import merrimackutil.json.lexer.TokenType;
import merrimackutil.json.lexer.Token;

/**
 * This node represents the a token in the grammar.
 * @author Zach Kissel
 */
 public class TokenNode extends SyntaxNode
 {
   private Token token;   // The token type.

   /**
    * Constructs a new token node.
    * @param token the token to associate with the node.
    */
    public TokenNode(Token token)
    {
      this.token = token;
    }

    /**
     * Evaluate the node.
     * @return the object representing the result of the evaluation.
     */
     public Object evaluate()
     {
       switch(token.getType())
       {
         case NUMBER:
          return Double.valueOf(token.getValue());
         case TRUE:
          return Boolean.valueOf(true);
         case FALSE:
          return Boolean.valueOf(false);
         case STRING:
          return token.getValue();
         case NULL:
          return "null";
         default:
          return token;
        }
     }
 }
