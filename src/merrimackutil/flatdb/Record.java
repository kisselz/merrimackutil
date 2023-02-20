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

import java.util.Map;
import java.util.HashMap;

/**
 * This class implements a record for the flat database.
 *
 * @author Zach Kissel
 */
public class Record
{
  private HashMap<String, String> recordMap;

  /**
   * The constructor builds the record field names and
   * allocates space for the fieldValues.
   *
   * @param fields the array of field names.
   */
  public Record(String[] fields)
  {
    recordMap = new HashMap<>();

    // Populate the map with the keys.
    for (int i = 0; i < fields.length; i++)
      recordMap.put(fields[i], "");
  }

  /**
   * The constructor builds the record field names and values.
   *
   * @param fields an array of field names.
   * @param fieldValues an array of values one for each field.
   */
  public Record(String[] fields, String[] fieldValues)
  {
      assert(fields.length == fieldValues.length);
    recordMap = new HashMap<>();

    // Populate the map with the keys.
    for (int i = 0; i < fields.length; i++)
      recordMap.put(fields[i], fieldValues[i]);
  }

  /**
   * This method sets the value of the named field.
   *
   * @param fieldName is non-empty string representing a field name.
   * @param fieldVal is a non-empty string representing the value for the
   *        fieldName
   *
   * @return If field exists, the value is set to fieldVal and true is returned;
   *       otherwise, false is returned.
   */
   public boolean setField(String fieldName, String fieldVal)
   {
     assert(!fieldName.isEmpty());

     // Try to modify the filed default value if the key exists.
     if (!recordMap.containsKey(fieldName))
      return false;

    // Update the field.
    recordMap.replace(fieldName, fieldVal);

    return true;
   }


   /**
    * This method sets all named fields at once.
    *
    * @param fieldNames  an array of fied names to set.
    * @param fieldVals an array of field values the same size as fieldNames.
    *
    * @return true is returned if all entries were set; otherewise return false.
    */
    public boolean setFields(String[] fieldNames, String[] fieldVals)
    {
      if (fieldVals.length != recordMap.size() ||
          fieldNames.length != recordMap.size())
        return false;

      // Set all values appropriately.
      for (int i = 0; i < fieldNames.length; i++)
        if (!setField(fieldNames[i], fieldVals[i]))
          return false;

     return true;
    }

    /**
     * This emthod gets a named field.
     *
     * @param fieldName a non-empty string representing the field name.
     *
     * @return A string containing the filed value or null if the field does not
     *       exist.
     */
     public String getFieldValue(String fieldName)
     {
       assert(!fieldName.isEmpty());

       return recordMap.get(fieldName);
     }
}
