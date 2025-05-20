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
package merrimackutil.json;

/**
 * This exception represents an invalid JSON string or file.
 * @author Zach Kissel
 */
public class InvalidJSONException extends Exception {
    
    /**
     * Overloaded constructor that inncludes a message.
     * @param msg the reason for the exception.
     */
    public InvalidJSONException(String msg)
    {
        super(msg);
    }

    /**
     * Constructor that contains just the excepation name.
     */
    public InvalidJSONException()
    {
        super();
    }

    /**
     * Invalid JSON exception with throwable cause.
     * @param msg the message.
     * @param cause the cause of the exception.
     */
    public InvalidJSONException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    /**
     * Create a new Invalid JSON exception using the given cause.
     * @param cause the reason for the exception.
     */
    public InvalidJSONException(Throwable cause)
    {
        super(cause);
    }
    
}
