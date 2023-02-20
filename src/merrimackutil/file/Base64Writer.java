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

 import java.util.Base64;
 import java.io.PrintWriter;
 import java.util.Arrays;

 /**
  * This class implements a stream writer for a Base64 streams.
  *
  * @author Zach Kissel
  */
 public class Base64Writer
 {
   private byte[] partialBuff;
   private int numPartialBytes;
   private PrintWriter writer;
   /**
    * The constructor takes a constructed PrintWriter
    * and will write to this PrintWriter the stream of
    * data in Base 64.
    *
    * @param writer the PrintWriter to write the Base64 data to.
    */
   public Base64Writer(PrintWriter writer)
   {
     // Remember the print writers name.
     this.writer = writer;

     // Create the space for the partialBuff and mark
     // that ther are no current partial bytes.
     partialBuff = new byte[2];
     numPartialBytes = 0;
   }

   /**
    * This method writes the base 64 bytes to the output
    * stream. It will only write units of 3 bytes to the
    * output stream. If there is partial data, it will be
    * stored for a later write.
    *
    * @param data the bytes of data to write to the stream.
    */
   public void writeUpdate(byte[] data)
   {
     int numBlks;
     int partialBytes;
     byte[] tmp;

     if (data == null)
      return;

    // If there is residual data, copy it to the front of a new data buffer.
    if (numPartialBytes > 0)
    {
        tmp = Arrays.copyOfRange(data, 0, data.length);
        data = new byte[tmp.length + numPartialBytes];

        for (int i = 0; i < numPartialBytes; i++)
          data[i] = partialBuff[i];
        for (int i = numPartialBytes; i < data.length; i++)
          data[i] = tmp[i - numPartialBytes];
        numPartialBytes = 0;

    }

     numBlks = data.length / 3;
     partialBytes = data.length % 3;

     // Write all of the full blocks to the data stream.
     for (int i = 0; i < numBlks; i++)
         writer.print(Base64.getEncoder().encodeToString(
            Arrays.copyOfRange(data, i * 3, (i * 3) + 3)));

     if (partialBytes > 0)  // Just partial bytes.
      for (int i = data.length - partialBytes; i < data.length; i++)
        partialBuff[numPartialBytes++] = data[i];
   }

   /**
    * This method completes the writing of the Base64 data stream.
    *
    * @param data the data to write.
    */
   public void writeFinal(byte[] data)
   {
     if (data == null)
      return;

     if (numPartialBytes != 0)
     {
       byte[] buff = new byte[data.length + numPartialBytes];

       // copy in the partial bytes.
       for (int i = 0; i < numPartialBytes; i++)
        buff[i] = partialBuff[i];

      // copy the remaining bytes.
      for (int i = 0; i < data.length; i++)
        buff[numPartialBytes + i] = data[i];

      // Write the encoded data.
      writer.print(Base64.getEncoder().encodeToString(buff));

      // There is no partial data left.
      numPartialBytes = 0;
     }
     else
      writer.print(Base64.getEncoder().encodeToString(data));

   }

   /**
    * This method just writes what is left in the buffer to
    * the print writer.
    */
    public void writeFinal()
    {
      if (numPartialBytes > 0)
      {
        byte[] tmp = Arrays.copyOfRange(partialBuff, 0, numPartialBytes);
        writer.print(Base64.getEncoder().encodeToString(tmp));
      }
    }
 }
