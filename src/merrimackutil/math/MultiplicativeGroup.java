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
package merrimackutil.math;

import java.security.SecureRandom ;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Arrays;
import merrimackutil.util.Tuple;

/**
 * This class createsa Modular multiplicative group built from a finite field.
 * @author Zach Kissel
 */
 public class MultiplicativeGroup implements Group<BigInteger>
 {
   BigInteger g;     // The generator.
   BigInteger p;     // The prime for the parent group, a safe prime.
   BigInteger q;     // The order of the sub group.

   /**
    * Constructs a new Z_p group of primer order q under multiplication.
    * @param g the generator of the group.
    * @param q the order of the group.
    * @param p the modulus used for mathematical opearations.
    */
   public MultiplicativeGroup(BigInteger g, BigInteger p, BigInteger q)
   {
     this.g = g;
     this.p = p;
     this.q = q;
   }
   /**
    * Determines the order of the group.
    * @return the number of elements in the group.
    */
   public BigInteger getOrder()
   {
     return q;
   }

   /**
    * Gets the generator of the group.
    * @return the generator of the group.
    */
    public BigInteger getGenerator()
    {
      return g;
    }

   /**
    * Finds the  inverse of element {@code element} in the
    * group.
    * @param element the element to find the inverse of.
    * @return the inverse of element {@code element}.
    */
   public BigInteger findInverse(BigInteger element)
   {
     return element.modInverse(p);
   }

   /**
    * Applies the group operation to the two elements returning a
    * new element.
    * @param left the left element.
    * @param right the right element
    * @return the result of applying the operation.
    */
    public BigInteger applyOperation(BigInteger left, BigInteger right)
    {
      return left.multiply(right).mod(p);
    }

    /**
     * Applies the group operation {@code n} times.
     * @param element the element to apply the operation to.
     * @param n the number of times to apply the group operation
     * to the specified element of the group.
     * @return the result of applyting the operation to {@code n} copies of
     * {@code element}.
     */
     public BigInteger iterateOperation(BigInteger element, BigInteger n)
     {
       return element.modPow(n, p);
     }

    /**
     * Sample a random element from the group.
     * @return a uniformly random element from the group.
     */
     public BigInteger sampleElement()
     {
      return sampleElementWithDlog().getFirst();
     }

     /**
      * Samples an element together with discrete log of the element.
      * @return a uniformly random element of the group along with its
      * discrete log.
      */
      public Tuple<BigInteger, BigInteger> sampleElementWithDlog()
      {
        BigInteger exp = new BigInteger(
            (int)(Math.log(q.bitLength())/Math.log(2)),
             new SecureRandom());
        return new Tuple<BigInteger, BigInteger>(g.modPow(exp, p), exp);
      }

     /**
      * Hashes a binary string to a group element.
      * @param msg the binary string to hash to a group element.
      * @return an element of the group.
      */
      public BigInteger hashToGroup(byte[] msg)
      {
        MessageDigest md = null;
        try
        {
          md = MessageDigest.getInstance("SHA-512");
        }
        catch (NoSuchAlgorithmException ex)
        {
          System.err.println(
              "Internal Error: Provider does not support Message" +
              "digest mechanism with SHA-2 512 Algorithm.");
          System.exit(1);
        }

        // Hash the message once with counter = 1.
        md.update(msg);
        byte[] part = md.digest(BigInteger.ONE.toByteArray());

        // Keep iterating the hash until we have sufficient bits.
        for (int i = 1; i < Math.ceil(Math.log(q.bitLength())/ Math.log(2)); i++)
        {
          md.update(msg);
          part = joinBytes(part, md.digest(new BigInteger(String.valueOf(i + 1)).toByteArray()));
        }

        return g.modPow(new BigInteger(1, part).mod(q), p);
      }

      /**
       * Joins array {@code first} and {@code second} into a new array
       * of bytes that contains elements of {@code first} followed by
       * elements of {@code second}.
       * @param first the first half of the bytes.
       * @param second the second half of the bytes.
       * @return a byte array consisting of {@code first} followed by {@code second}.
       */
      private byte[] joinBytes(byte[] first, byte[] second)
      {
        byte[] res = Arrays.copyOf(first, first.length + second.length);
        for (int i = first.length; i < second.length; i++)
            res[i] = second[i - first.length];
        return res;
      }

}
