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

import java.util.Arrays;
import java.util.HashMap;
import merrimackutil.util.Tuple;

/**
 * A getopt(3) style option parser for Java based command line
 * applications. The syntax mirrors the POSIX compliant getopt.
 *
 * The format string for an option string is a string of alphabetic chararcters
 * and colons. The string must begin with a non-colon character. A colon after
 * a character specifies that the option expects an argument. By way of an
 * example the option string "hvi:o:" means that an application takes the -h,
 * and -v options without options but the -i and -o option require an argument.
 * The single dash (i.e., - ) is used to denote the option on the command line.
 *
 * <br />
 * This class also supports the notion of long options a long option is specified
 * using an instance of the {@code LongOption} class. Long options are specified
 * at the command line using two dashes (i.e., --) before the multi-letter option.
 *
 * <br />
 * Any error messaging or notion of conflicting options must be handled by the
 * application make use of this class.
 *
 * @author Zach Kissel
 */
 public class OptionParser
 {
   private String[] args;
   private int optIdx;
   private HashMap<Character, Boolean> optMap;
   private HashMap<String, LongOption> loptMap;

    /**
     * The default constructor that sets up the
     * option parser.
     * @param args the argument array to parse.
     */
    public OptionParser(String[] args)
    {
      this.args = args;
      this.optIdx = 0;
      this.optMap = null;
      this.loptMap = null;
    }

    /**
     * Set the value of optIdx to any positive number less than
     * the length of the args array and greater than or equal to zero.
     * @param optIdx the opt index to use.
     */
     public void setOptIdx(int optIdx)
     {
       this.optIdx = optIdx;
     }

     /**
      * Gets the value of the option index.
      * @return an integer greater than or eqal to zero and less than
      * the length of the associated argument array.
      */
      public int getOptIdx()
      {
        return this.optIdx;
      }

    /**
     * Get remaining non-option arguments.
     * @return an array of strings.
     */
     public String[] getNonOpts()
     {
       if (optIdx < args.length)
        return Arrays.copyOfRange(args, optIdx, args.length);
       else
        return new String[0];
     }


     /**
      * Sets up the option string used by getOpt.
      * @param opts the option string.
      * @throws IllegalArgumentException if the options string starts with
      * a colon.
      */
      public void setOptString(String opts) throws IllegalArgumentException
      {
        optMap = new HashMap<>();

        if (opts.charAt(0) == ':')
          throw new IllegalArgumentException("Can't start with colon.");

        for (int i = 0; i < opts.length() - 1; i++)
        {
          if (Character.isLetter(opts.charAt(i)))
          {
            if (opts.charAt(i + 1) == ':')
              optMap.put(opts.charAt(i), true);
            else
              optMap.put(opts.charAt(i), false);
          }
        }

        // Handle the case when the last character of the option string 
        // is not a colon.
        if (opts.charAt(opts.length() - 1) != ':')
          optMap.put(opts.charAt(opts.length() - 1), false);
      }

    /**
     * Sets up the long options for getLongOpt.
     * @param lopts the array of long options.
     */
     public void setLongOpts(LongOption[] lopts)
     {
       loptMap = new HashMap<>();
       for (int i = 0; i < lopts.length; i++)
        loptMap.put(lopts[i].getOption(), lopts[i]);
     }

     /**
      * Sets up the long options together with the corresponding
      * short options. This will erase all existing short options.
      * @param lopts the array of long options.
      */
      public void setLongAndShortOpts(LongOption[] lopts)
      {
        String opt = "";
        setLongOpts(lopts);

        // Build out the option string.
        for (int i = 0; i < lopts.length; i++)
        {
          opt += lopts[i].getReturnVal();
          if (lopts[i].needsArg())
            opt += ":";
        }
        setOptString(opt);
      }


    /**
     * Responsible for parsing the args one by one and
     * returning the next option from the args according. This
     * requires the option string to already be set.
     * @return the next option character or {@code ?} if an invalid
     * option is discovered.
     */
    public Tuple<Character, String> getOpt()
    {
      Tuple<Character, String> rv = new Tuple<Character, String>('?', null);

      // If the option map is null, return null.
      if (optMap == null)
        return null;

      // If we've reached the end of the
      // argument list return null.
      if (optIdx == args.length)
        return null;

      // Handle the next option chractger.
      if (args[optIdx].charAt(0) == '-')
      {
        if (optMap.containsKey(args[optIdx].charAt(1)))
        {
            // Check for argument to option being in the same string
            // or the next string..
            if (optMap.get(args[optIdx].charAt(1)))
            {
              if (args[optIdx].length() > 2)
                rv = new Tuple<Character, String>(args[optIdx].charAt(1),
                   args[optIdx++].substring(2));
              else if (optIdx + 1 < args.length)
              {
                rv = new Tuple<Character, String>(args[optIdx].charAt(1), args[optIdx + 1]);
                optIdx += 2;
              }
            }
            else
            {
              // We don't have any arguments to our options but,
              // we may have several options together with one hyphen, and
              // we handle that here.
              rv = new Tuple<Character, String>(args[optIdx].charAt(1), null);

              // Handle compound options (e.g., -xy).
              if (args[optIdx].length() > 2)
              {
                args[optIdx] = "-" + args[optIdx].substring(2);
              }
              else  // There we'ver reached the end of the string.
                optIdx++;
            }
        }
      }
      return rv;
    }

    /**
     * Allows for long options as well as regular options. Long options
     * are guaranteed to always begin with {@code --} and have a space
     * between their arguements.
     * @param loptOnly set to true if only long options are supported.
     * @return the next option character or {@code ?} if an invalid
     * option is discovered.
     */
     public Tuple<Character, String> getLongOpt(boolean loptOnly)
     {
       Tuple<Character, String> rv = new Tuple<Character, String>('?', null);
       LongOption lopt;

       // Return null if there is no long option map set.
       if (loptMap == null)
        return null;

      // If we've reached the end of the
      // argument list return null.
      if (optIdx == args.length)
        return null;

      // Check for a long option.
      if (args[optIdx].charAt(0) == '-' && args[optIdx].charAt(1) == '-')
      {
        String cmd = args[optIdx].substring(2);
        if (loptMap.containsKey(cmd))
        {
          lopt = loptMap.get(cmd);
          if (!lopt.needsArg())
              rv = new Tuple<>(lopt.getReturnVal(), null);
          else if ((optIdx + 1) < args.length)
          {
            rv = new Tuple<>(lopt.getReturnVal(), args[optIdx + 1]);
            optIdx++;
          }
          optIdx++;
        }
      }
      else if (!loptOnly)
        return getOpt();
      return rv;
     }
 }
