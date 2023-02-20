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


/**
 * Represents the node of a syntax tree. Each node is slightly
 * different therefore, the class is abstract each derived class
 * is responsible for implementing the evaluate method for that
 * node subtype.
 *
 * @author Zach Kissel
 */
public abstract class SyntaxNode
{

  /**
   * Evaluate the node.
   * @return the object representing the result of the evaluation.
   */
  public abstract Object evaluate();
}
