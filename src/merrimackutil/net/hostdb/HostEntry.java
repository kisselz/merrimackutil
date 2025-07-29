/* 
 * Copyright (C) 2025  Zachary A. Kissel 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package merrimackutil.net.hostdb;

import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONType;
import merrimackutil.json.types.JSONObject;
import java.io.InvalidObjectException;

/**
 * This class represents a host entry in the hosts collection.
 * @author Zach Kissel
 */
public class HostEntry implements JSONSerializable
{
  private String hostName;  // Name of the host.
  private String address;   // Address of the host.
  private int port;         // Port number of the host.

  /**
   * Construct a host entry from the corresponding JSON object.
   * @param obj a JSON object representing a host entry.
   * @throws InvalidObjectException if {@code obj} is not a valid JSON object.
   */
  public HostEntry(JSONObject obj) throws InvalidObjectException
  {
    deserialize(obj);
  }

  /**
   * Gets the host name.
   * @return the host name as a string.
   */
   public String getHostName()
   {
     return hostName;
   }

 /**
  * Gets the address.
  * @return the address as a string.
  */
  public String getAddress()
  {
    return address;
  }

  /**
   * Gets the port number.
   * @return the port number.
   */
   public int getPort()
   {
     return port;
   }

  /**
   * Coverts json data to an object of this type.
   * @param obj a JSON type to deserialize.
   * @throws InvalidObjectException the type does not match this object.
   */
  public void deserialize(JSONType obj) throws InvalidObjectException
  {
    JSONObject entry;
    String[] keys = {"host-name", "address", "port"};
    if (obj.isObject())
    {
      entry = (JSONObject) obj;

      entry.checkValidity(keys);

      hostName = entry.getString("host-name");
      address = entry.getString("address");
      port = entry.getInt("port");
    }
    else
      throw new InvalidObjectException(
         "Host Entry -- recieved array, expected Object.");
  }

  /**
   * Converts the object to a JSON type.
   * @return a JSON type either JSONObject or JSONArray.
   */
  public JSONType toJSONType()
  {
    JSONObject obj = new JSONObject();

    obj.put("host-name", hostName);
    obj.put("address", address);
    obj.put("port", port);

    return obj;
  }
}
