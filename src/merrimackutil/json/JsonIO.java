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
package merrimackutil.json;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONArray;
import merrimackutil.json.parser.JSONParser;
import merrimackutil.json.parser.ast.SyntaxTree;

/**
 * This class provides convience methods for reading and writing JSON
 * objects and arrays from files or from strings.
 * @author Zach Kissel
 */
public class JsonIO
{
  /**
   * Reads the object stored in file {@code objFile}
   * @param objFile the file to read the object from.
   * @return a JSONObject or null in the case of error.
   * @throws FileNotFoundException if the file is not found.
   * @throws InvalidJSONException if the JSON is invalid.
   */
  public static JSONObject readObject(File objFile) throws FileNotFoundException, InvalidJSONException
  {
    JSONParser parser = new JSONParser(objFile);

    SyntaxTree ast = parser.parse();

    if (parser.hasError())
      throw new InvalidJSONException("readObject failed.\n" + parser.getErrorLog().trim());
      
    return (JSONObject) ast.evaluate();
  }

  /**
   * Reads the JSON object from string {@code objString}
   * @param objString the JSON string to read the object from.
   * @return a JSONObject or null in the case of error.
   * @throws InvalidJSONException  if the JSON is invalid.
   */
  public static JSONObject readObject(String objString) throws InvalidJSONException
  {
    JSONParser parser = new JSONParser(objString);

    SyntaxTree ast = parser.parse();
    if (parser.hasError())
      throw new InvalidJSONException("readObject failed.\n" + parser.getErrorLog().trim());
      
    return (JSONObject) ast.evaluate();
  }

  /**
   * Reads the array stored in file {@code arrayFile}
   * @param arrayFile the the file to read the array from.
   * @return a JSONArray or null in the case of error.
   * @throws FileNotFoundException if the file is not found.
   * @throws InvalidJSONException if the JSON is invalid.
   */
  public static JSONArray readArray(File arrayFile) throws FileNotFoundException, InvalidJSONException
  {
    JSONParser parser = new JSONParser(arrayFile);

    SyntaxTree ast = parser.parse();
    if (parser.hasError())
      throw new InvalidJSONException("readArray failed.\n" + parser.getErrorLog().trim());
      
    return (JSONArray) ast.evaluate();
  }

  /**
   * Builds the JSON array from string {@code arrayString}
   * @param arrayString the JSON string to read the array from.
   * @return a JSONArray or null in the case of error.
   * @throws InvalidJSONException if the JSON is invalid.
   */
  public static JSONArray readArray(String arrayString) throws InvalidJSONException
  {
    JSONParser parser = new JSONParser(arrayString);

    SyntaxTree ast = parser.parse();

    if (parser.hasError())
      throw new InvalidJSONException("readObject failed.\n" + parser.getErrorLog().trim());
      
    return (JSONArray) ast.evaluate();
  }

  /**
   * Serializes the object {@code obj} into JSON and stores
   * the result in file {@code jsonFile}.
   * @param obj a JSON serializable object.
   * @param jsonFile a file to store the serialized results to.
   * @throws FileNotFoundException if the file is not found.
   */
  public static void writeSerializedObject(JSONSerializable obj, File jsonFile) throws
     FileNotFoundException
  {
    PrintWriter out = new PrintWriter(jsonFile);
    out.println(obj.serialize());
    out.close();
  }

  /**
   * Serializes the object {@code obj} into JSON and writes it 
   * to the named PrinterWriter
   * 
   * @param obj a JSON serializable object.
   * @param out the stream to write to..
   */
  public static void writeSerializedObject(JSONSerializable obj, PrintWriter out)
  {
    out.println(obj.serialize());
  }

  /**
   * Stores a formatted JSON representation of {@code obj} in the 
   * file {@code jsonFile}.
   * 
   * @param obj      a JSON serializable object.
   * @param jsonFile a file to store the serialized results to.
   * @throws FileNotFoundException if the file is not found.
   */
  public static void writeFormattedObject(JSONSerializable obj, File jsonFile) throws FileNotFoundException {
    PrintWriter out = new PrintWriter(jsonFile);
    out.println(obj.toJSONType().getFormattedJSON());
    out.close();
  }
}
