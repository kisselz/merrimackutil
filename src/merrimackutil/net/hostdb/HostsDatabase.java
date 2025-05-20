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

import merrimackutil.json.InvalidJSONException;
import merrimackutil.json.JSONSerializable;
import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONType;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;

/**
 * This class represents a simple hosts database to be used by
 * the client.
 * @author Zach Kissel
 */
 public class HostsDatabase implements JSONSerializable
 {
   private HashMap<String, HostEntry> hostMap;

   /**
    * Constructs a new hosts database from a given JSON file in the correct format.
    * @param file the file representing the JSON data.
    * @throws InvalidObjectException the JSON object is not a valid hosts database.
    * @throws FileNotFoundException the hosts database file could not be found.
    * @throws InvalidJSONException the JSON is invalid.
    */
   public HostsDatabase(File file) throws InvalidObjectException, FileNotFoundException, InvalidJSONException
   {
    hostMap = new HashMap<>();
    deserialize(JsonIO.readObject(file));
   }

   /**
    * Construct a new host from the given JSON object.
    * @param a JSON objec that represents a hosts file.
    * @throws InvalidObjectException if the JSON object does not represent
    * a host file.
    */
   public HostsDatabase(JSONObject obj) throws InvalidObjectException
   {
     hostMap = new HashMap<>();
     deserialize(obj);
   }

   /**
    * Determine if the host is in the hosts file.
    * @param hostName the name of the host.
    * @return true if the host is known; otherwise, false.
    */
   public boolean hostKnown(String hostName)
   {
     return hostMap.containsKey(hostName);
   }

   /**
    * Get the port number associated with the host.
    * @param hostName the name of the host.
    * @return the port number, or -1 of the host is not found.
    */
   public int getPort(String hostName)
   {
     if (!hostMap.containsKey(hostName))
      return -1;

     return hostMap.get(hostName).getPort();
   }

   /**
    * Get an array of all hosts in the database.
    * @return an array list of all known hosts in the database.
    */
   public ArrayList<String> getAllHosts()
   {
    ArrayList<String> res = new ArrayList<>();
    for (String host : hostMap.keySet())
      res.add(host);
    return res;
   }

   /**
    * Get the address associated with the host.
    * @param hostName the name of the host.
    * @return the address associated with the host, or null if there is
    * no such host.
    */
   public String getAddress(String hostName)
   {
     if (!hostMap.containsKey(hostName))
      return null;

     return hostMap.get(hostName).getAddress();
   }

   /**
    * Coverts json data to an object of this type.
    * @param obj a JSON type to deserialize.
    * @throws InvalidObjectException the type does not match this object.
    */
   public void deserialize(JSONType obj) throws InvalidObjectException
   {
     JSONObject hosts;
     String[] keys = {"hosts"};
     if (obj.isObject())
     {
       hosts = (JSONObject) obj;

       hosts.checkValidity(keys);

       JSONArray array = hosts.getArray("hosts");
       for (int i = 0; i < array.size(); i++)
       {
        HostEntry entry = new HostEntry(array.getObject(i));
        hostMap.put(entry.getHostName(), entry);
       }
     }
     else
       throw new InvalidObjectException(
          "Hosts -- recieved array, expected Object.");
   }

   /**
    * Converts the object to a JSON type.
    * @return a JSON type either JSONObject or JSONArray.
    */
   public JSONType toJSONType()
   {
     JSONObject obj = new JSONObject();
     JSONArray array = new JSONArray();

     for (String key : hostMap.keySet())
       array.add(hostMap.get(key).toJSONType());

     obj.put("hosts", array);
     return obj;
   }
 }
