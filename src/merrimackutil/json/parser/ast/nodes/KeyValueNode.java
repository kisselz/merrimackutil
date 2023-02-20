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

import merrimackutil.util.Tuple;
import merrimackutil.json.lexer.Token;
import merrimackutil.json.lexer.TokenType;
import java.util.LinkedList;

/**
 * This node represents a key-value pair.
 * @author Zach Kissel
 */
 public class KeyValueNode extends SyntaxNode
 {
   private SyntaxNode key;
   private SyntaxNode value;

   /**
    * Constructs a new key value syntax node.
    * @param key the key for the key-value pair.
    * @param value the value for the key-value pair.
    */
    public KeyValueNode(SyntaxNode key, SyntaxNode value)
    {
      this.key = key;
      this.value = value;
    }

    /**
     * Evaluate the node.
     * @return the object representing the result of the evaluation.
     */
     public Object evaluate()
     {
        Object keystr = key.evaluate();

        if (!(keystr instanceof String))
        {
          System.out.println("Error: key must be of type string.");
          return null;
        }
        return new Tuple<String, Object>((String)keystr, value.evaluate());
     }

 }
