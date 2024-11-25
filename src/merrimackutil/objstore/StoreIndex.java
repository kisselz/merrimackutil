/*
 *   Copyright (C) 2017 -- 2024  Zachary A. Kissel
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
package merrimackutil.objstore;

import java.io.InvalidObjectException;
import java.util.HashMap;

import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONArray;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;

/**
 * The index for the object store.
 */
public class StoreIndex implements JSONSerializable
{
    private String name;                            // The name of the object store this index represents.
    private HashMap<String, Item> store;            // The Stroe of the metadata.
    
    /**
     * Create a new empyt storage index. 
     * @param name the name of the storage index.
     */
    public StoreIndex(String name)
    {
        this.store = new HashMap<>();
        this.name = name;
    }

    /**
     * Constructs a new object store index 
     * @param obj the JSON object representing the store index.
     * @throws InvalidObjectException {@code obj} is not a valid store index object.
     */
    public StoreIndex(JSONObject obj) throws InvalidObjectException
    {
        deserialize(obj);
    }

    @Override
    public String serialize() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }

    /**
     * Use a JSON object to construct a new store index object. 
     */
    @Override
    public void deserialize(JSONType obj) throws InvalidObjectException 
    {
        this.store = new HashMap<>();
        JSONObject index;

        if (!(obj instanceof JSONObject))
            throw new InvalidObjectException("Not an object store index object.");

        index = (JSONObject) obj;

        // Load the name of the object store.
        if (index.containsKey("name"))
            this.name = index.getString("name");
        else
            throw new InvalidObjectException("Index should contain the name of the object store.");

        // Load the index (array of items)
        if (index.containsKey("objects"))
        {
            JSONArray objs = index.getArray("objects");
            
            for (int i = 0; i < objs.size(); i++)
            {
                Item tmp = new Item(objs.getObject(i));
                store.put(tmp.getURI(), tmp);
            }
        }
        else
            throw new InvalidObjectException("Index should contain the object meta data array.");

        
        // Make sure we don't have superfluous fields.
        if (index.size() != 2)
            throw new InvalidObjectException("Index contains unknown fields.");
        
    }

    @Override
    public JSONType toJSONType() 
    {
        throw new UnsupportedOperationException("Unimplemented method 'toJSONType'");
    }

    /**
     * Get the item from the store based on the given URI.
     * @param uri the URI for the item.
     * @return the item at the given {@code uri}.
     */
    public Item get(String uri)
    {
        throw new UnsupportedOperationException("Unimplmented method 'get'");
    }

    /**
     * Add a new object to the object store.
     * @param uri the URI of the object.
     * @param meta the object's metadata.
     */
    public void add(String uri, Metadata meta)
    {
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    /**
     * Converts the URI to the path to a file in the object store.
     * @param uri the uri of object.
     * @return the path to the object itself.
     */
    public String uriToPath(String uri)
    {
        return "";
    }
}
