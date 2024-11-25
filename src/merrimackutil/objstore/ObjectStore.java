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

import java.io.File;
import java.io.InvalidObjectException;

import merrimackutil.json.JsonIO;

/**
 * Implements a rudimentary object storage system. 
 */
public class ObjectStore 
{
    private String location;            // The path to the object store.
    private String name;                // The name of the object store.
    private StoreIndex index;          // The index for the data store.

    /**
     * Creates or loads and object store at a given location.
     * @param location the location of the object store. 
     * @param name the name of the object store.
     * @throws InvalidObjectException if object store index is invalid.
     */
    public ObjectStore(String location, String name) throws InvalidObjectException
    {
        this.location = location;
        this.name = name;
        File path = new File(location + File.pathSeparator + name);

        // Check if the path exists. If it does, load up the necessary information. 
        if (path.exists())
            index = new StoreIndex(JsonIO.readObject(location + File.pathSeparator + name + File.pathSeparator + "index.json"));
        else 
        {
            path.mkdir();                       // Try to create the directory.
            index = new StoreIndex(name);       // Create a brand new store index.
        }
    }

    /**
     * Save the object store to disk.
     */
    public void save()
    {
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

     /**
     * Get the item from the store based on the given URI.
     * @param uri the URI for the item.
     * @return the item at the given {@code uri}.
     */
    public Item get(String uri)
    {
        return index.get(uri);
    }

    /**
     * Add a new object to the object store.
     * @param uri the URI of the object.
     * @param meta the object's metadata.
     */
    public void add(String uri, Metadata meta)
    {
        File path = new File(buildDirPath(uri));
        path.mkdirs();
        index.add(uri, meta);
    }

    /**
     * Converts the URI to the path to a file in the object store.
     * @param uri the uri of object.
     * @return the path to the object itself.
     */
    public String uriToPath(String uri)
    {
        return buildDirPath(uri) + File.pathSeparator +  uri.substring(uri.lastIndexOf("/") + 1, uri.length());
    }

    /**
     * Given a uri construct the corresponding directory path.
     * @param uri the uri to use in building the path.
     * @return the path on the host machine.
     */
    private String buildDirPath(String uri)
    {
        String[] uriParts = uri.split("/");
        String path = location + File.pathSeparator + name;

        // Skip the last part of the uri as it's a file. 
        for (int i = 0; i < uriParts.length - 1; i++)
            path = path + File.pathSeparator + uriParts[i];
        
        return path;
    }
}
