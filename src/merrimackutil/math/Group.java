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

import java.math.BigInteger;
import merrimackutil.util.Tuple;

/**
 * This interface defines the basic notion of a group.
 * @param <T> the type of the group element.
 * @author Zach Kissel
 */
 public interface Group<T>
 {
   /**
    * Determines the order of the group.
    * @return the number of elements in the group.
    */
   public BigInteger getOrder();

   /**
    * Gets the generator of the group.
    * @return the generator of the group.
    */
    public T getGenerator();

   /**
    * Finds the  inverse of element {@code element} in the
    * group.
    * @param element the element to find the inverse of.
    * @return the inverse of element {@code element}.
    */
   public T findInverse(T element);

   /**
    * Applies the group operation to the two elements returning a
    * new element.
    * @param left the left element.
    * @param right the right element
    * @return the result of applying the operation.
    */
    public T applyOperation(T left, T right);

    /**
     * Applies the group operation {@code n} times.
     * @param element the element to apply the operation to.
     * @param n the number of times to apply the group operation
     * to the specified element of the group.
     * @return the result of applyting the operation to {@code n} copies of
     * {@code element}.
     */
     public T iterateOperation(T element, BigInteger n);

    /**
     * Sample a random element from the group.
     * @return a uniformly random element from the group.
     */
     public T sampleElement();

     /**
      * Samples an element together with discrete log of the element.
      * @return a uniformly random element of the group along with its
      * discrete log.
      */
      public Tuple<T, BigInteger> sampleElementWithDlog();

     /**
      * Hashes a binary string to a group element.
      * @param msg the binary string to hash to a group element.
      * @return an element of the group.
      */
      public T hashToGroup(byte[] msg);
 }
