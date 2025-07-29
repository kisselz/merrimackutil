/*
 *   Copyright (C) 2017 -- 2025  Zachary A. Kissel
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
package merrimackutil.json.types;

import java.util.ArrayList;
import java.util.Collection;
import merrimackutil.json.lexer.Lexer;
import merrimackutil.json.lexer.Token;
import merrimackutil.json.lexer.TokenType;

/**
 * Represent a JSON array as a linked list.
 * @author Zach Kissel
 */
public final class JSONArray extends ArrayList<Object> implements JSONType
{
  /**
  * Default constructor builds an empty JSONArray.
  */
  public JSONArray()
  {
    super();
  }

  /**
   * Constructs a JSONArray from a give collection {@code collection}.
   * @param collection the collection to build the JSON array from.
   */
  public JSONArray(final Collection<?> collection)
  {
    super(collection);
  }

  /**
   * Gets a string associated with the idx. If the
   * idx is not associated with a string, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
  public String getString(int idx)
  {
    Object val = get(idx);

    if (val instanceof String)
      return (String)val;
    return null;
  }

  /**
   * Gets an integer associated with the idx. If the
   * idx is not associated with an Integer, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
  public Integer getInt(int idx)
  {
    Object val = get(idx);

    if (val instanceof Double)
    {
      // Check if this is an integer.
      if (Math.floor((Double)val) == (Double) val)
        return ((Double) val).intValue();
    }
    return null;
  }

  /**
   * Gets double associated with the idx. If the
   * idx is not associated with a Double, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
  public Double getDouble(int idx)
  {
    Object val = get(idx);

    if (val instanceof Double)
      return (Double)val;
    return null;
  }

  /**
   * Gets long associated with the idx. If the
   * idx is not associated with a Long, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
  public Long getLong(int idx) {
      Object val = get(idx);

      if (val instanceof Long)
          return (Long) val;
      return null;
  }

  /**
   * Gets Boolean associated with the idx. If the
   * idx is not associated with a Boolean, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
  public Boolean getBoolean(int idx)
  {
    Object val = get(idx);

    if (val instanceof Boolean)
      return (Boolean) val;
    return null;
  }

  /**
   * Gets the JSONArray associated with the idx. If the
   * idx is not associated with a JSONArray, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
  public JSONArray getArray(int idx)
  {
    Object val = get(idx);

    if (val instanceof JSONArray)
      return (JSONArray)val;
    return null;
  }

  /**
   * Gets the JSONObject associated with the idx. If the
   * idx is not associated with a JSONObject, null is
   * returned.
   * @param idx the idx to find the associated value of.
   * @return the associated value or null.
   */
   public JSONObject getObject(int idx)
   {
     Object val = get(idx);

     if (val instanceof JSONObject)
      return (JSONObject) val;
     return null;
   }
   
   /**
    * Determines if value at index {@code idx} is a null value.
    * @param idx the index to check.
    * @return {@code true} if the value at index {@code idx} is null; otherwise, {@code false}.
    */
   public boolean isNull(int idx)
   {
     return get(idx) == null;
   }

   /**
    * Gets the type of value associated with index {@code idx}.
    * @param idx the index to get the type of.
    * @return the value type associated with {@code key}.
    */
   public JSONValType getValueType(int idx)
   {
      Object val = get(idx);

      if (val instanceof Double)
      {
          // Check if this is an integer.
          if (Math.floor((Double)val) == (Double) val)
           return JSONValType.INT;
          else 
            return JSONValType.DOUBLE;
      }
      else if (val instanceof Long)
        return JSONValType.LONG;
      else if (val instanceof String)
        return JSONValType.STRING;
      else if (val instanceof Boolean)
        return JSONValType.BOOLEAN;
      else if (val instanceof JSONArray)
        return JSONValType.ARRAY;
      else if (val instanceof JSONObject)
        return JSONValType.OBJECT;
      else
        return JSONValType.NULL;
   }

   /**
    * Converts the JSON array to a string suitable for
    * storing in a file.
    * @return A string reprsentation of the array.
    */
   public String toJSON()
   {
     String output = "[";
     for (int i = 0; i < size(); i++)
     {
       Object val = get(i);
       if (val instanceof String)
        output += "\"" + val + "\",";
       else if (val instanceof JSONArray)
        output += ((JSONArray)val).toJSON() + ",";
       else if (val instanceof JSONObject)
        output += ((JSONObject)val).toJSON() + ",";
       else
        output += val + ",";
     }
     if (size() > 0)
      output = output.substring(0, output.length() - 1);
     output += "]";
     return output;
   }

   /**
    * Gets a formatted JSON string representing the array.
    * @return a formatted JSON string.
    */
   public String getFormattedJSON()
   {
     String rawJSON = toJSON();
     int indentCnt = 0;
     Token tok;
     Lexer lex = new Lexer(rawJSON);
     String formattedJSON = "";

     tok = lex.nextToken();
     while (tok.getType() != TokenType.EOF)
     {
       switch (tok.getType())
       {
         case NUMBER:
           if (formattedJSON.charAt(formattedJSON.length() -1) == '\n')
             formattedJSON += buildIndentString(indentCnt);
           formattedJSON += tok.getValue();
         break;
         case STRING:
          if (formattedJSON.charAt(formattedJSON.length() -1) == '\n')
            formattedJSON += buildIndentString(indentCnt);
          formattedJSON += "\"" + tok.getValue() + "\"";
         break;
         case LBRACE:
         case LBRACKET:
           if (formattedJSON == "" ||
               formattedJSON.charAt(formattedJSON.length() -1) == '\n')
               formattedJSON += buildIndentString(indentCnt);
           formattedJSON += tok.getValue() + "\n";
           indentCnt++;
         break;
         case RBRACE:
         case RBRACKET:
            if (indentCnt > 0)
              indentCnt--;
            if (formattedJSON.charAt(formattedJSON.length() -1) != '\n')
              formattedJSON += "\n";
            formattedJSON += buildIndentString(indentCnt) +
               tok.getValue() + "\n";
         break;
         case COMMA:
            if (formattedJSON.charAt(formattedJSON.length() - 2) == ']' ||
                formattedJSON.charAt(formattedJSON.length() - 2) == '}')
                formattedJSON = formattedJSON.stripTrailing();
            formattedJSON +=",\n";
         break;
         case COLON:
            formattedJSON += " : ";
         break;
         case TRUE:
           if (formattedJSON.charAt(formattedJSON.length() -1) == '\n')
             formattedJSON += buildIndentString(indentCnt);
           formattedJSON += "true";
         break;
         case FALSE:
           if (formattedJSON.charAt(formattedJSON.length() -1) == '\n')
             formattedJSON += buildIndentString(indentCnt);
           formattedJSON += "false";
         break;
         case NULL:
           if (formattedJSON.charAt(formattedJSON.length() -1) == '\n')
             formattedJSON += buildIndentString(indentCnt);
           formattedJSON += "null";
         break;
         case EOF:
         case UNKNOWN:
            /* This should never happen. */
         break;
       }
       tok = lex.nextToken();
     }
     return formattedJSON;
   }

   /**
    * Returns {@code true} if the JSON type is an array; otherwise, {@code false}
    * 
    * @return This method always returns {@code true}.
    */
   public boolean isArray() 
   {
     return true;
   }

   /**
    * Returns {@code true} if the JSON type is an object; otherwise, {@code false}
    * 
    * @return This method always returns {@code false}.
    */
   public boolean isObject() 
   {
     return false;
   }

   /**
    * Helper method to create an indent string for pretty printing.
    * @param cnt the number of indents to make.
    * @return a string representing the indent.
    */
   private String buildIndentString(int cnt)
   {
     String indent = "";
     for (int i = 0; i < cnt; i++)
      indent = indent + "   ";
     return indent;
   }

}
