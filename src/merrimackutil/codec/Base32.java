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
package merrimackutil.codec;

/**
 * This class provides an encoder and decoder capabilities for Base-32 strings.
 * There are probably more efficent ways to encode but, this is sufficient
 * for our purposes.
 *
 * @author Zach Kissel
 */
 public class Base32
 {
   private static final String b32Tbl = "abcdefghijklmnopqrstuvwxyz234567";
   /**
    * This method encodes an array of bytes as a Base-32 string.
    * See RFC 4648.
    *
    * @param data the data to encode.
    * @param ignorePadding set to true if the padding should be ingored.
    *
    * @return a Base-32 encoded string.
    */
   public static String encodeToString(byte[] data, boolean ignorePadding)
   {
      String res = "";
      String bits = toBinaryString(data);
      int remainingBits = bits.length();

      for (int i = 0; i < bits.length()/5; i++)
      {
          res += toBase32Char(bits.substring(i * 5, i * 5 + 5));
          remainingBits -= 5;
      }

      // Append trailing zeros to this string
      if (remainingBits != 0)
        res += toBase32Char(bits.substring(bits.length() - remainingBits, bits.length()));

      // In some cases we should ignore padding like when generating
      // keys for TOTP soft tokens.
      if (!ignorePadding)
      {
        int paddingBits = bits.length() % 40;

        if (paddingBits == 32)
          res+= "=";
        if (paddingBits == 24)
          res += "===";
        if (paddingBits == 16)
          res += "====";
        if (paddingBits == 8)
          res += "======";
      }

      return res;
   }

   /**
    * This method decodes a base-32 encoded value to an array of bytes.
    *
    * @param b32str a base 32 encoded string.
    *
    * @return an array of bytes that correspond to the base-32 string.
    */
   public static byte[] decode(String b32str)
   {
     String binString = "";
     byte[] res;
     String tmp;

     // Convert it to a bit string.
     for (int i = 0; i < b32str.length(); i++)
     {
       if (b32str.charAt(i) == '=')
        break;

      binString += String.format("%5s", Integer.toBinaryString(
          b32Tbl.indexOf(String.valueOf(b32str.charAt(i))))).replace(" ", "0");
     }

    System.out.println(binString);

    res = new byte[binString.length()/8];
    for (int i = 0; i < binString.length()/8; i++)
      res[i] = (byte) (Integer.parseInt(
          binString.substring(i * 8, i * 8 + 8), 2) & 0xff);

    return res;
   }

   /**
    * Given a bit string pad it out to five bits and return the
    * correspond Base32 character.
    *
    * @param bitstr a bit string that must be padded to 5 bits.
    *
    * @return the base-32 character associated with the 5 bit string.
    */
   private static String toBase32Char(String bitstr)
   {
     assert (bitstr.length() <= 5);
     assert (bitstr.length() >= 0);

     // Padd on the right until you get a multiple of 5 bits.
     while (bitstr.length() != 5)
      bitstr = bitstr + "0";

     return String.valueOf(b32Tbl.charAt(Integer.parseInt(bitstr, 2)));
   }

   /**
    * Converts and array of bytes to a binary string.
    *
    * @param data the array of bytes to covert.
    *
    * @return a byte aligned binary string representation of data.
    */
   private static String toBinaryString(byte[] data)
   {
     String result = "";

     for (int i = 0; i < data.length; i++)
      result += String.format("%8s",
          Integer.toBinaryString((int)(data[i]) & 0xff)).replace(" ", "0");
     return result;
   }
 }
