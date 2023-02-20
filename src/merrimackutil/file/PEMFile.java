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
package merrimackutil.file;

import java.io.File;
import java.util.Scanner;

import java.io.IOException;
import java.io.FileNotFoundException;
import merrimackutil.exception.BadFileFormatException;

/**
 * This class represents a PEM encoded file.
 *
 * @author Zach Kissel
 */
public class PEMFile {

  /**
   * This method reads a PEM encoded PKCS8 file and returns the
   * base 64 encoded private key.
   *
   * @param fileName the name of the file to read the PEM data from.
   * @return the PEM data.
   *
   * @throws FileNotFoundException when the file is not found.
   * @throws BadFileFormatException when the file format is bad.
   */
  public static String readPCKS8PEM(String fileName) throws FileNotFoundException,
    BadFileFormatException
  {
    File inFile = new File(fileName);
    Scanner in = new Scanner(inFile);
    String b64Data = "";
    String line;

    // Make sure first line is the start of a private key.
    line = in.nextLine();
    if (!line.equals("-----BEGIN PRIVATE KEY-----"))
      throw new BadFileFormatException("Missing start of private key.");

    while (in.hasNextLine())
    {
      line = in.nextLine();
      if (!line.equals("-----END PRIVATE KEY-----"))
        b64Data += line;
    }

    // Make sure the key was ended correctly.
    if (!line.equals("-----END PRIVATE KEY-----"))
      throw new BadFileFormatException("Missing end of private key.");

    // Make sure we have private key data to return.
    if (b64Data.equals(""))
      throw new BadFileFormatException("No private key data found");

    return b64Data;
  }

  /**
   * This method reads a PEM encoded X509 public key file and returns the
   * base 64 encoded public key.
   *
   * @param fileName the name of the file to read the PEM data from.
   * @return the PEM data.
   *
   * @throws FileNotFoundException when the file is not found.
   * @throws BadFileFormatException when the file format is bad.
   */
  public static String readX509PEM(String fileName) throws FileNotFoundException,
    BadFileFormatException
  {
    File inFile = new File(fileName);
    Scanner in = new Scanner(inFile);
    String b64Data = "";
    String line;

    // Make sure first line is the start of a private key.
    line = in.nextLine();
    if (!line.equals("-----BEGIN PUBLIC KEY-----"))
      throw new BadFileFormatException("Missing start of private key.");

    while (in.hasNextLine())
    {
      line = in.nextLine();
      if (!line.equals("-----END PUBLIC KEY-----"))
        b64Data += line;
    }

    // Make sure the key was ended correctly.
    if (!line.equals("-----END PUBLIC KEY-----"))
      throw new BadFileFormatException("Missing end of private key.");

    // Make sure we have private key data to return.
    if (b64Data.equals(""))
      throw new BadFileFormatException("No private key data found");

    return b64Data;
  }


}
