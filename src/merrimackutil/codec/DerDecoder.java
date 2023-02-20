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

import java.math.BigInteger;
import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import merrimackutil.util.Tuple;

/**
 * This class does some basic DER encoding of common Java objects.
 * @author Zach Kissel
 */
 public class DerDecoder
 {
   private static final byte INTEGER = (byte) 0x02;
   private static final byte SEQUENCE = (byte) 0x30;
   private static final byte OCTET_STRING = (byte) 0x04;
   private static final byte NULL = (byte) 0x05;
   private static final byte IA5STRING = (byte) 0x16;

   /**
    * Decodes a der encoded number to a BigInteger.
    * @param num the DER encoded number.
    * @return the decoded big integer.
    * @throws IllegalArgumentException if the data is not a DER
    * encoded integer.
    */
   public static BigInteger decodeBigInteger(byte[] num)
      throws IllegalArgumentException
   {
     byte[] hdr = getHeader(num);
     byte[] payload = Arrays.copyOfRange(num, hdr.length, num.length);
     Tuple<Byte, Integer> hinfo = decodeHeader(hdr);

     if (hinfo.getFirst() == INTEGER && hinfo.getSecond() == payload.length)
      return new BigInteger(payload);
     else
      throw new IllegalArgumentException("Ivalid DER encoding of integer.");
   }

   /**
    * Decodes a DER encoded string.
    * @param str the DER encoded string.
    * @return the decoded string.
    * @throws IllegalArgumentException if the data is not a DER
    * encoded string.
    */
    public static String decodeString(byte[] str)
       throws IllegalArgumentException
    {
      byte[] hdr = getHeader(str);
      byte[] payload = Arrays.copyOfRange(str, hdr.length, str.length);
      Tuple<Byte, Integer> hinfo = decodeHeader(hdr);

      if (hinfo.getFirst() == IA5STRING && hinfo.getSecond() == payload.length)
      {
        try
         {
           return new String(payload, "US-ASCII");
         }
         catch (UnsupportedEncodingException ex)
         {
           System.out.println(
              "Fatal Error: US-ASCII character set not available.");
           System.exit(1);
         }
         return null; // Unreachable
      }
      else
       throw new IllegalArgumentException("Invalid DER encoding of string");
    }

    /**
     * Decode the octet string.
     * @param octets the encoded octet string.
     * @return returns the decoded octet string.
     * @throws IllegalArgumentException if the data is not an DER
     * encode octet string.
     */
     public static byte[] decodeOctets(byte[] octets)
       throws IllegalArgumentException
     {
       byte[] hdr = getHeader(octets);
       byte[] payload = Arrays.copyOfRange(octets, hdr.length, octets.length);
       Tuple<Byte, Integer> hinfo = decodeHeader(hdr);

       if (hinfo.getFirst() == OCTET_STRING &&
          hinfo.getSecond() == payload.length)
        return payload;
       else
        throw new IllegalArgumentException("Invalid DER encoding of string");
     }

   /**
    * Decodes a DER encoded sequence into its DER encoded components.
    * @param seq the encode sequence
    * @return a list of DER encoded entries.
    * @throws IllegalArgumentException if the sequence is not encoded
    * correctly.
    */
    public static ArrayList<byte[]> decodeSequence(byte[] seq)
       throws IllegalArgumentException
    {
      ArrayList<byte[]> dseq = new ArrayList<>();
      byte[] hdr = getHeader(seq);
      byte[] payload = Arrays.copyOfRange(seq, hdr.length, seq.length);
      Tuple<Byte, Integer> hinfo = decodeHeader(hdr);

      if (hinfo.getFirst() != SEQUENCE || hinfo.getSecond() != payload.length)
        throw new IllegalArgumentException("Invalid DER encoding of sequence.");

      // Break apart the entries to build the sequence.
      while (payload.length != 0)
      {
        hdr = getHeader(payload);
        hinfo = decodeHeader(hdr);
        dseq.add(Arrays.copyOfRange(payload, 0, hinfo.getSecond() + hdr.length));
        payload = Arrays.copyOfRange(payload, hinfo.getSecond() + hdr.length,
           payload.length);
     }
     return dseq;
    }

    /**
     * Determines if the encoded payload is a string.
     * @param encoded the DER encoded data.
     * @return true if {@code encoded} is a string; otherwise, false.
     */
    public static boolean isEncodedString(byte[] encoded)
    {
      return encoded[0] == IA5STRING;
    }

    /**
     * Determines if the encoded payload is a string.
     * @param encoded the DER encoded data.
     * @return true if {@code encoded} is an integer; otherwise, false.
     */
    public static boolean isEncodedInteger(byte[] encoded)
    {
      return encoded[0] == INTEGER;
    }

    /**
     * Determines if the encoded payload is a string.
     * @param encoded the DER encoded data.
     * @return true if {@code encoded} is a sequence; otherwise, false.
     */
    public static boolean isEncodedSequence(byte[] encoded)
    {
      return encoded[0] == SEQUENCE;
    }

    /**
     * Determines if the encoded payload is a string.
     * @param encoded the DER encoded data.
     * @return true if {@code encoded} is an octet string; otherwise, false.
     */
    public static boolean isEncodedOctet(byte[] encoded)
    {
      return encoded[0] == OCTET_STRING;
    }

    /*************************************************
     *
     * Private Methods
     *
     *************************************************/


    /**
     * Gets the header bytes of the encoded data.
     * @param encoded the encoded data.
     * @return a byte array containing the header.
     */
     private static byte[] getHeader(byte[] encoded)
     {
       byte[] hdr = null;
       int hdrSize = 2;

       // Check if we have a long length, in which case we need
       // to remove several bytes for the length field.
       if ((encoded[1] & 0xFF) > 127)
         hdrSize += (encoded[1] & 0x7F);

       hdr = new byte[hdrSize];
       for (int i = 0; i < hdrSize; i++)
        hdr[i] = encoded[i];

       return hdr;
     }

     /**
      * Decodes the header returning the type and length as a tuple.
      * @param hdr the hdr bytes.
      * @return a tuple of the type and length.
      */
      private static Tuple<Byte, Integer> decodeHeader(byte[] hdr)
      {
        int len = 0;
        int power = 1;

        if (hdr.length > 2)
          for (int i = hdr.length - 1; i >= 2; i--)
          {
            len += (hdr[i] & 0xFF) * power;
            power *= 256;
          }
        else
          len = hdr[1];
        return new Tuple<Byte, Integer>(hdr[0], len);
      }

    /**
     * Joins {@code array1} and {@code array2} together such
     * such that all elements of {@code array1} occur before
     * {@code array2}.
     * @param array1 the left half of the new array.
     * @param array2 the right half of the new array.
     * @return an array that consists of elements of {@code array1} followed by
     * the elements of{@code array2}.
     */
    private static byte[] joinArrays(byte[] array1, byte[] array2)
    {
      byte[] rv = new byte[array1.length + array2.length];

      for (int i = 0; i < array1.length; i++)
       rv[i] = array1[i];
      for (int i = 0; i < array2.length; i++)
       rv[i + array1.length] = array2[i];

      return rv;
    }
 }
