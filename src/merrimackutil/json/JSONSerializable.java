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

import merrimackutil.json.types.JSONType;
import java.io.InvalidObjectException;

/**
 * An interface for classes that can be serialized into JSON.
 * @author Zach Kissel
 */
 public interface JSONSerializable
 {
   /**
    * Serializes the object into a JSON encoded string.
    * @return a string representing the JSON form of the object.
    */
   public String serialize();

   /**
    * Coverts json data to an object of this type.
    * @param obj a JSON type to deserialize.
    * @throws InvalidObjectException the type does not match this object.
    */
   public void deserialize(JSONType obj) throws InvalidObjectException;

   /**
    * Converts the object to a JSON type. 
    * @return a JSON type either JSONObject or JSONArray.
    */
   public JSONType toJSONType();

 }
