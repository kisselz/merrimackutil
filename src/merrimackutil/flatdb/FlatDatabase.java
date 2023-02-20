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
package merrimackutil.flatdb;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

import merrimackutil.flatdb.Record;

/**
 * Class to manage a flat file database.
 *
 * @author Zach Kissel
 */
public class FlatDatabase
{
private String dbFile;    // The backing store.
private boolean openSuccessful;
private String[] schema;
private LinkedList<Record> recordList = null;

/**
 * The default constructor. An empty database with no file is created.
 */
public FlatDatabase()
{
        openSuccessful = false; // Assume we can't open the database.
        dbFile = "";
        recordList = new LinkedList<Record>();
        schema = null;
}

/**
 * This is the finalize method it is responsible for saving the data to the
 * database.
 *
 * @return true if the save was successful; otherwise, false.
 */
public boolean saveDatabase()
{
        PrintWriter out = null;
        String line = "";

        if (dbFile.isEmpty())
                return false;

        // Try to open the file.
        try
        {
                // Don't append to the file, overwrite it.
                out = new PrintWriter(new FileWriter(dbFile));

                // Build the filed name string made up of
                for (int i = 0; i < schema.length; i++)
                        line += schema[i] + ";";
                line = line.substring(0, line.length() - 1);
                out.println(line);

                // Build the record list one record at a time.
                for (int i = 0; i < recordList.size(); i++)
                {
                        line = "";
                        for (int j = 0; j < schema.length; j++)
                                line += recordList.get(i).getFieldValue(schema[j]) + ";";
                        line = line.substring(0, line.length() - 1);
                        out.println(line);
                }
        }
        catch (IOException ioe)
        {
                ioe.printStackTrace();
        }
        finally
        {
                if (out != null)
                        out.close();
        }

        return true;
}

/**
 * This method creates a new database with the given field names. Can not be
 * called on an already open database.
 *
 * @param fname is non-empty string.
 * @param fieldNames an array of field names, must have length at least one.
 *
 * @return true is returned if a database is created and false otherwise.
 */
public boolean createDatabase(String fname, String[] fieldNames)
{
        assert (!openSuccessful);
        assert (!fname.isEmpty());
        assert (fieldNames.length > 0);

        PrintWriter out = null;
        String fieldNameLine = "";

        dbFile = fname; // Save the database file name.

        // Try to open the file.
        try
        {
                out = new PrintWriter(new FileWriter(fname));

                // Build the filed name string made up of
                for (int i = 0; i < fieldNames.length; i++)
                        fieldNameLine += fieldNames[i] + ";";
                fieldNameLine = fieldNameLine.substring(0, fieldNameLine.length() - 1);
                out.println(fieldNameLine);

                // Copy the field names.
                schema = new String[fieldNames.length];
                for (int i = 0; i < fieldNames.length; i++)
                        schema[i] = fieldNames[i];

        }
        catch (IOException ioe)
        {
                ioe.printStackTrace();
        }
        finally
        {
                if (out != null)
                        out.close();
        }

        openSuccessful = true;
        return true;
}

/**
 * This method loads an existing database.
 *
 * @param fname is a non-empty string representing a database file.
 *
 * @return true is returned if the database is opened sucessfully; otherwise,
 *       false is returned.
 */
public boolean openDatabase(String fname)
{
        assert (!fname.isEmpty());
        assert (!openSuccessful);

        Scanner in = null;
        String currLine = null;
        String [] recordValues;

        dbFile = fname;

        // Try to open the file.
        try
        {
                in = new Scanner(new File(dbFile));

                // Begin by reading in the field name line.
                currLine = in.nextLine();

                // Split the line up.
                schema = currLine.split(";");

                // Let's read in all the entries.
                while(in.hasNextLine())
                {
                        currLine = in.nextLine();
                        recordValues = currLine.split(";");

                        if (!recordList.add(new Record(schema, recordValues)))
                        {
                                in.close();
                                openSuccessful = false;
                                return false;
                        }
                }
        }
        catch (IOException ioe)
        {
                ioe.printStackTrace();
        }
        finally
        {
                if (in != null)
                        in.close();
        }

        openSuccessful = true;
        return true;

}

/**
 * Adds a new record to the database.
 *
 * @param r a non-null record to add.
 *
 * @return true if record was successfully added; false otherwise.
 */
public boolean insertRecord(Record r)
{
        assert (r != null);

        if (!recordList.add(r))
                return false;
        return true;
}

/**
 * This method removes a record from the database.
 *
 * @param key the key to search for.
 * @param val the value of the key to remove.
 *
 * @return true if successful, false otherwise.
 */
 public boolean removeRecord(String key, String val)
 {
   Record currRecord;

   for (int i = 0; i < recordList.size(); i++)
   {
      currRecord = recordList.get(i);
      if (currRecord.getFieldValue(key).equals(val))
      {
        recordList.remove(i);
        return true;
      }
   }
   return false;
 }

/**
 * Looksup a record in the database.
 *
 * @param key is non-empty string representing a field name.
 * @param val is a non-empty string representing the field value.
 *
 * @return A new object containing the contents of the record.
 */
public Record lookupRecord(String key, String val)
{
        assert (!key.isEmpty());
        assert (!val.isEmpty());
        Record r;

        // Find the record according to the key
        for (int i = 0; i < recordList.size(); i++)
        {
                r = recordList.get(i);
                if (r.getFieldValue(key).equals(val))
                        return r;
        }

        return null;
}


/**
 * This method retrieves the field list for the database.
 *
 * @return returns the field list or null on error.
 */
 public String[] getFields()
 {
   String [] retval;

   if (schema == null)
    return null;

  // Otherwise copy the schema to return.
  retval = new String[schema.length];
  for (int i = 0; i < schema.length; i++)
    retval[i] = schema[i];

  return retval;
 }
}
