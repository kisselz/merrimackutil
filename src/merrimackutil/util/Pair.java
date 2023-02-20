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
 * A simple pair object where each element of the pair
 * as the same type.
 *
 * @author Zach Kissel
 */
public class Pair<T> extends Tuple<T, T>
{
  /**
   * Constructs a new pair {@code (first, second)}.
   * @param first the first element of the pair.
   * @param second the second element of the pair.
   */
  public Pair(T first, T second)
  {
    super(first, second);
  }
}
