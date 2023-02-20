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
package merrimackutil.json.parser.ast;

import merrimackutil.json.parser.ast.nodes.SyntaxNode;

/**
 * Represents a syntax tree for the language.
 * @author Zach Kissel
 */
public class SyntaxTree
{
  SyntaxNode root;  // The root of the syntax tree.

  /**
   * Constructs a new syntax tree with root {@code root}.
   * @param root the root node of the tree.
   */
  public SyntaxTree(SyntaxNode root)
  {
    this.root = root;
  }

  /**
   * Construct an empty syntax tree.
   */
  public SyntaxTree()
  {
    this(null);
  }

  /**
   * Sets the root node to {@code root}
   * @param root the object to set the root node to.
   */
   public void setRootNode(SyntaxNode root)
   {
     this.root = root;
   }

   /**
    * Gets the root node of the tree.
    * @return a reference to the root node of the tree.
    */
    public SyntaxNode getRootNode()
    {
      return this.root;
    }

   /**
    * Evaluate the syntax tree.
    * @return the object representing the result of the evaluation.
    */
    public Object evaluate()
    {
      return root.evaluate();
    }
}
