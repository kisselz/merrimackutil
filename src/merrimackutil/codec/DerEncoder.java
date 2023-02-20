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

/**
 * This class does some basic DER encoding of common Java objects.
 * @author Zach Kissel
 */
 public class DerEncoder
 {
   private static final byte INTEGER = (byte) 0x02;
   private static final byte SEQUENCE = (byte) 0x30;
   private static final byte OCTET_STRING = (byte) 0x04;
   private static final byte NULL = (byte) 0x05;
   private static final byte IA5STRING = (byte) 0x16;

   /**
    * DER encodes a big integer using DER type 0x02
    * @param n the number to encoe as an integer.
    * @return a byte array of the DER encoded integer.
    */
   public static byte[] encodeBigInteger(BigInteger n)
   {
     return encodeToType(INTEGER, n.toByteArray());
   }

   /**
    * DER encode a IA5String.
    * @param str the string to encode
    * @return returns the der encoded string.
    */
    public static byte[] encodeString(String str)
    {
      byte[] strb = null;

      try
      {
        strb = str.getBytes("US-ASCII");
      }
      catch(UnsupportedEncodingException ex)
      {
        System.out.println("Fatal Error: US-ASCII not supported.");
        System.exit(1);
      }
      return encodeToType(IA5STRING, strb);

    }

    /**
     * Returns an encoded null value.
     * @return the null value encoded.
     */
    public static byte[] encodeNull()
    {
      byte[] nullVal = new byte[2];
      nullVal[0] = NULL;
      nullVal[1] = (byte) 0x00;
      return nullVal;
    }

    /**
     * DER encode an octet string.
     * @param octets the octet string to encode
     * @return returns the der encoded string.
     */
     public static byte[] encodeOctets(byte[] octets)
     {
       return encodeToType(OCTET_STRING, octets);
     }

   /**
    * DER encode a sequence of encoded values.
    * @param lst the list of encoded values to add to the sequence.
    * @return a byte array of the DER encoded sequence.
    */
    public static byte[] encodeSequence(ArrayList<byte[]> lst)
    {
      byte[] payload;

      payload = lst.get(0);
      for (int i = 1; i < lst.size(); i++)
          payload = joinArrays(payload, lst.get(i));

      // Handle the case where the length can be encoded using
      // one byte.
      return encodeToType(SEQUENCE, payload);
    }

    /*************************************************
     *
     * Private Methods
     *
     *************************************************/

    /**
     * Encode a payload byte array using a short length
     * encoding.
     * @param type a byte representing the type.
     * @param payload the array of Bytes for the payload.
     */
    private static byte[] encodeShortLen(byte type, byte[] payload)
    {
      byte[] hdr = new byte[2];
      hdr[0] = type;
      hdr[1] = (byte) (payload.length & 0xFF);
      return joinArrays(hdr, payload);
    }

    /**
     * Encode a payload byte array using a short length
     * encoding.
     * @param type a byte representing the type.
     * @param payload the array of bytes for the payload.
     */
    private static byte[] encodeLongLen(byte type, byte[] payload)
    {
      int numLenBytes;

      // Compute the number of bytes necessary to hold the length.
      numLenBytes = (int) Math.ceil(Math.log(payload.length)/(8*Math.log(2))) + 1;

      byte[] hdr = new byte[2];
      hdr[0] = type;
      hdr[1] = (byte) ((0x01 << 7) | (numLenBytes & 0xFF));

      // Pack the length byte which is encoded in base 256.
      int convLen = payload.length;
      byte[] length = new byte[numLenBytes];

      int j = numLenBytes;
      while (convLen != 0)
      {
        length[j-1] = (byte) ((convLen % 256) & 0xFF);
        convLen /= 256;
        j--;
      }
      byte[] rv = joinArrays(hdr, length);
      return joinArrays(rv, payload);
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

    /**
     * Encodes to a type.
     * @param type the type of the payload.
     * @param payload the payload.
     * @return the DER encoding.
     */
     private static byte[] encodeToType(byte type, byte[] payload)
     {
       if (payload.length <= 127)
         return encodeShortLen(type, payload);
       return encodeLongLen(type, payload);
     }
 }
