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

 /**
  * This class represents a generic JSON type.
  */
  public interface JSONType
  {
    /**
     * Converts a JSON type to a string.
     * @return a string representing the type in JSON.
     */
     public String toJSON();

   /**
    * Gets a formatted JSON string representing the array.
    * @return a formatted JSON string.
    */
   public String getFormattedJSON();

   /**
    * Returns {@code true} if the JSON type is an array; otherwise, {@code false}
    * @return {@code true} if {@code this} is a JSON array.
    */
   public boolean isArray();

   /**
    * Returns {@code true} if the JSON type is an object; otherwise, {@code false}
    * @return {@code true} if {@code this} is a JSON object.
    */
   public boolean isObject();
  }
