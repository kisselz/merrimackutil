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
package merrimackutil.cli;

/**
 * Represents a long option for the option parser.
 *
 * @author Zach Kissel.
 */
 public class LongOption
 {
   private String option;
   private boolean hasArg;
   private char rv;

   /**
    * Construct a new long option with option text {@code option} and
    * whether or not it has an argument (using {@code hasArg}).
    * @param option the long option name.
    * @param hasArg true if their is an optional argument, false otherwsie,
    * @param rv the return character associated with this long option.
    */
   public LongOption(String option, boolean hasArg, char rv)
   {
      this.option = option;
      this.hasArg = hasArg;
      this.rv = rv;
   }

   /**
    * Gets the option long name.
    * @return the long name of the option.
    */
   public String getOption()
   {
     return this.option;
   }

   /**
    * Gets whether or not the command needs an argument.
    * @return true if an argument is needed; otherwise, false.
    */
   public boolean needsArg()
   {
     return this.hasArg;
   }

   /**
    * Gets the return value.
    * @return the return value.
    */
   public char getReturnVal()
   {
     return this.rv;
   }
 }
