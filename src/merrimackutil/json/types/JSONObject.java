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
package merrimackutil.json.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import merrimackutil.json.lexer.Lexer;
import merrimackutil.json.lexer.Token;
import merrimackutil.json.lexer.TokenType;

/**
 * This class represents a JSON object.
 */
public final class JSONObject extends HashMap<String, Object> implements JSONType
{
  /**
   * Constructs a new JSON class.
   */
  public JSONObject()
  {
    super();
  }

  /**
   * Constructs a new JSON class from this map.
   * WARNING: there is no checking that this map is a valid JSON
   * object.
   * @param map the map to instantiate the object with.
   */
  public JSONObject(final Map<String, ?> map)
  {
    super(map);
  }

  /**
   * Gets a string associated with the key. If the
   * key is not associated with a string, null is
   * returned.
   * @param key the key to find the associated value of.
   * @return the associated value or null.
   */
  public String getString(String key)
  {
    Object val = get(key);

    if (val instanceof String)
      return (String)val;
    return null;
  }

  /**
   * Gets an integer associated with the key. If the
   * key is not associated with an Integer, null is
   * returned.
   * @param key the key to find the associated value of.
   * @return the associated value or null.
   */
  public Integer getInt(String key)
  {
    Object val = get(key);

    if (val instanceof Double)
    {
      // Check if this is an integer.
      if (Math.floor((Double)val) == (Double) val)
        return ((Double) val).intValue();
    }
    return null;
  }

  /**
   * Gets double associated with the key. If the
   * key is not associated with a Double, null is
   * returned.
   * @param key the key to find the associated value of.
   * @return the associated value or null.
   */
  public Double getDouble(String key)
  {
    Object val = get(key);

    if (val instanceof Double)
      return (Double)val;
    return null;
  }

  /**
   * Gets Boolean associated with the key. If the
   * key is not associated with a Boolean, null is
   * returned.
   * @param key the key to find the associated value of.
   * @return the associated value or null.
   */
  public Boolean getBoolean(String key)
  {
    Object val = get(key);

    if (val instanceof Boolean)
      return (Boolean) val;
    return null;
  }

  /**
   * Gets the JSONArray associated with the key. If the
   * key is not associated with a JSONArray, null is
   * returned.
   * @param key the key to find the associated value of.
   * @return the associated value or null.
   */
  public JSONArray getArray(String key)
  {
    Object val = get(key);

    if (val instanceof JSONArray)
      return (JSONArray)val;
    return null;
  }

  /**
   * Gets the JSONObject associated with the key. If the
   * key is not associated with a JSONObject, null is
   * returned.
   * @param key the key to find the associated value of.
   * @return the associated value or null.
   */
   public JSONObject getObject(String key)
   {
     Object val = get(key);

     if (val instanceof JSONObject)
      return (JSONObject) val;
     return null;
   }

   /**
    * Converts the JSON object to a string suitable for
    * storing in a file.
    * @return A string reprsentation of the object.
    */
   public String toJSON()
   {

     Set<String> keySet = keySet();
     String output = "{";
     for (String key : keySet)
     {
       Object val = get(key);
       output += "\"" + key + "\":";
       if (val instanceof String)
        output += "\"" + val + "\",";
       else if (val instanceof JSONArray)
        output += ((JSONArray)val).toJSON() + ",";
       else if (val instanceof JSONObject)
        output += ((JSONObject)val).toJSON()+ ",";
       else
        output += val + ",";
     }
     output = output.substring(0, output.length() - 1);
     output += "}";
     return output;
   }

   /**
    * Gets a formatted JSON string representing the object.
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
       }
       tok = lex.nextToken();
     }
     return formattedJSON;
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
