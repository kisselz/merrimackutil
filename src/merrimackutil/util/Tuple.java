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
package merrimackutil.util;

/**
 * A tuple type where the first element and second element
 * may have different types.
 *
 * @author Zach Kissel
 */
public class Tuple<F, S>
{
  F first;  // The first element of the tuple.
  S second; // The second element of the tuple.

  /**
   * Constructs a new tuple {@code (first, second)}.
   * @param first the first element of the tuple.
   * @param second the second elemen of the tuple.
   */
  public Tuple(F first, S second)
  {
      this.first = first;
      this.second = second;
  }

  /**
   * Gets the first element of the tuple.
   * @return the first element of the tuple.
   */
  public F getFirst()
  {
    return first;
  }

  /**
   * Gets the second element of the tuple.
   * @return the second element.
   */
  public S getSecond()
  {
    return second;
  }

  /**
   * Constructs a string representation of the tuple.
   * @return the string representation of the tuple.
   */
  public String toString()
  {
    return "(" + first +", " + second + ")";
  }
}
