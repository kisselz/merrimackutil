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
 * A crude stopwatch class for rough performance measurements.
 * @author Zach Kissel
 */
public class StopWatch
{
  private long startNano;      // The current start nanosecond reading.

  /**
   * Starts the stopwatch.
   */
  public void start()
  {
    startNano = System.nanoTime();
  }

  /**
   * Stops the watch and returns the elapsed time.
   * @return the elapsed time in nano seconds.
   */
  public long stop()
  {
    return System.nanoTime() - startNano;
  }
}
