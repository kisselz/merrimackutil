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

import merrimackutil.json.JSONSerializable;
import merrimackutil.json.types.JSONObject;
import merrimackutil.json.types.JSONType;

/**
 * The item in the object store.
 */
public class Item implements JSONSerializable
{
    private String uri;             // The URI for the item.
    private Metadata meta;          // The items metadata.

    /**
     * Construct a new item.
     */
    public Item(String uri, Metadata meta)
    {
        this.uri = uri;
        this.meta = meta;
    }

    /**
     * Load an item from a JSON object. 
     * @param obj the JSON object representing the item.
     * @throws InvalidObjectException if the object is not a valid JSON object.
     */
    public Item(JSONObject obj) throws InvalidObjectException
    {

    }

    /**
     * Get the URI associated with this item.
     * @return the URI as a string.
     */
    public String getURI()
    {
        return this.uri;
    }

    /**
     * Get the metadata associated with this item.
     * @return the metadata object associated with this item.
     */
    public Metadata getMetadata()
    {
        return this.meta;
    }
    
    @Override
    public String serialize() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'serialize'");
    }

    @Override
    public void deserialize(JSONType obj) throws InvalidObjectException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deserialize'");
    }

    @Override
    public JSONType toJSONType() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJSONType'");
    }
    
}
