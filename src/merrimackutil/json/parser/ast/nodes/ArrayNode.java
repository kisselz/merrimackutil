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

import merrimackutil.json.types.JSONArray;
import merrimackutil.json.lexer.Token;
import merrimackutil.json.lexer.TokenType;
import java.util.LinkedList;

/**
 * This node represents an array.
 * @author Zach Kissel
 */
 public class ArrayNode extends SyntaxNode
 {
   private LinkedList<SyntaxNode> vals;

   /**
    * Constructs a new key value syntax node.
    */
    public ArrayNode()
    {
      this.vals = new LinkedList<>();
    }

    /**
     * Adds a new value to the list of values for
     * this array.
     * @param newVal  the value node to add to the list.
     */
    public void addValue(SyntaxNode newVal)
    {
      vals.add(newVal);
    }

    /**
     * Evaluate the node.
     * @return the object representing the result of the evaluation.
     */
     public Object evaluate()
     {
       JSONArray array = new JSONArray();
       for (SyntaxNode ele : vals)
       {
         Object val = ele.evaluate();
         if (val == null)
         {
           System.out.println("Error: value can't be absent.");
           return null;
         }
         array.add(ele.evaluate());
       }

       return array;
     }

 }
