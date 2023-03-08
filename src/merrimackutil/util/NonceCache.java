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


import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Base64;

/**
 * This class implements a basic nonce cache.
 * @author Zach Kissel
 */
public class NonceCache
{
  private int nonceSize;      // The size of the nonce in bytes.
  private long ageLimit;       // How long a nonce is valid in the cache.
  private SecureRandom rand;    // A secure random bit generator.
  private HashMap<String, Long> cache;

  /**
   * Constructs a new nonce cache with nonces of size {@code nonceSize} and
   * validity period of {@code ageLimit}.
   * @param nonceSize how big the nonce must be in bytes.
   * @param ageLimit how long the nonce is valid in seconds.
   * @throws IllegalArgumentException if {@code nonceSize} is less than or
   * equal to zero or {@code ageLimit} is less than or equal to zero.
   */
  public NonceCache(int nonceSize, int ageLimit) throws IllegalArgumentException
  {
    // Make sure our cache has sane properties.
    if (nonceSize <= 0)
      throw new IllegalArgumentException("Invalid nonce size.");
    if (ageLimit <= 0)
      throw new IllegalArgumentException("Invalid age limit.");

    this.nonceSize = nonceSize;
    this.ageLimit = ageLimit * 1000;  // The clock resolution is in ms.
    this.rand = new SecureRandom();
    this.cache = new HashMap<>();
  }

  /**
   * Determines if the cache contains the given nonce.
   * @param nonce a nonce of size {@code nonceSize}.
   * @return true if the cache contains the nonce; otherwise false.
   * @throws IllegalArgumentException if the nonce is not of the correct length.
   */
  public boolean containsNonce(byte[] nonce) throws IllegalArgumentException
  {
    if (nonce == null)
      throw new IllegalArgumentException("Nonce is null.");

    if (nonce.length != nonceSize)
      throw new IllegalArgumentException("Nonce not of correct length.");

    // If the cache contains the key, verify if the duration
    // is still valid.
    if (cache.containsKey(Base64.getEncoder().encodeToString(nonce)))
    {
      if ((System.currentTimeMillis() - cache.get(
        Base64.getEncoder().encodeToString(nonce))) < ageLimit)
        return true;
    }

    return false;
  }

  /**
   * Get a fresh nonce and adds it to the cache.
   * @return a fresh nonce.
   */
  public byte[] getNonce()
  {
    byte[] nonce = new byte[nonceSize];
    boolean done = false;

    while (!done)
    {
      rand.nextBytes(nonce);

      // If the cache contains the key, verify if the duration
      // is still
      if (cache.containsKey(Base64.getEncoder().encodeToString(nonce)))
      {
        if ((System.currentTimeMillis() - cache.get(
            Base64.getEncoder().encodeToString(nonce))) > ageLimit)
          done = true;
      }
      else
        done = true;
    }

    // Add the nonce to the cache with the current time.
    addNonce(nonce);

    return nonce;
  }

  /**
   * Adds a nonce to the cache.
   * @param nonce the nonce to add.
   * @throws IllegalArgumentException if the nonce is not of the correct length.
   */
   public void addNonce(byte[] nonce) throws IllegalArgumentException
   {
     if (nonce.length != nonceSize)
      throw new IllegalArgumentException("Nonce is not of the correct size.");

    // If the cache contains the key, verify if the duration
    // is still valid.
    if (cache.containsKey(Base64.getEncoder().encodeToString(nonce)))
    {
      if ((System.currentTimeMillis() - cache.get(
        Base64.getEncoder().encodeToString(nonce))) < ageLimit)
        return;
    }

    // Add the nonce.
    cache.put(Base64.getEncoder().encodeToString(nonce), System.currentTimeMillis());
   }
}
