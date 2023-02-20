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

import merrimackutil.json.types.JSONObject;
import merrimackutil.util.Tuple;
import merrimackutil.json.lexer.Token;
import merrimackutil.json.lexer.TokenType;
import java.util.LinkedList;

/**
 * This node represents a class.
 * @author Zach Kissel
 */
 public class ClassNode extends SyntaxNode
 {
   private LinkedList<KeyValueNode> kvPairs;

   /**
    * Constructs a new key value syntax node.
    */
    public ClassNode()
    {
      this.kvPairs = new LinkedList<>();
    }

    /**
     * Adds a new KV pair to the list of kv pairs for
     * this class.
     * @param newPair the new KeyValueNode to add to the list of KV pairs.
     */
    public void addKVPair(KeyValueNode newPair)
    {
      kvPairs.add(newPair);
    }

    /**
     * Evaluate the node.
     * @return the object representing the result of the evaluation.
     */
     public Object evaluate()
     {
        JSONObject obj = new JSONObject();

        // Loop over the list and add all of the key value pairs.
        for (KeyValueNode kv : kvPairs)
        {
          @SuppressWarnings("unchecked")
          Tuple<String, Object> pair = (Tuple<String, Object>)kv.evaluate();
          obj.put(pair.getFirst(), pair.getSecond());
        }

        return obj;
     }

 }
