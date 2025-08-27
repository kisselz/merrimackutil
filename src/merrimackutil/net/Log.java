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
package merrimackutil.net;

import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * A simple thread-safe logging class designed for 
 * network services.
 * 
 * @author Zach Kissel
 */
public class Log {
    private boolean logActive;      // If the log is active or not.
    private PrintWriter out;        // The log file.
    private String serviceName;     // The name of the service. 
    private String timeZone;        // The time zone to use for the logger (default UTC).

    /**
     * Construct a new log with timestamps set to UTC time..
     * @param logName the name of the log file.
     * @param serviceName the name of the service for the log.
     * @throws FileNotFoundException if the log file can not be created.
     */
    public Log(String logName, String serviceName) throws FileNotFoundException
    {
        this(logName, serviceName, "UTC");
    }

    /**
     * Construct a new log with timestamps localized to your provided time zone.
     * 
     * @param logName     the name of the log file.
     * @param serviceName the name of the service for the log.
     * @param timeZone    the string describing the time zone
     *                    ({@see https://docs.oracle.com/cd/E72987_01/wcs/tag-ref/MISC/TimeZones.html})
     * @throws FileNotFoundException if the log file can not be created.
     */
    public Log(String logName, String serviceName, String timeZone) throws FileNotFoundException, IllegalAccessError
    {
        // Open the file for appending.
        this.out = new PrintWriter(new FileOutputStream(new File(logName), true));
        this.serviceName = serviceName;
        this.logActive = true;
        
        if (!timeZone.equals("UTC"))
        {
            // Check if we have a valid timezone.
            String[] ids = TimeZone.getAvailableIDs();
            boolean validZone = false;
            for (String id : ids)
            {
                if (id.equals(timeZone))
                    validZone = true;
            }
            if (validZone == false)
                throw new IllegalArgumentException("Unknown timezone: " + timeZone + " try one of " + Arrays.toString(ids));
            this.timeZone = timeZone;
        }
        this.timeZone = timeZone;

    }

    /**
     * Turn logging on.
     */
    public synchronized void loggingOn()
    {
        logActive = true;
    }

    /**
     * Turn logging off.
     */
    public synchronized void loggingOff()
    {
        logActive = false;
    }

    /**
     * Log message {@code msg} to the log.
     * @param msg the message to log.
     */
    public synchronized void log(String msg)
    {
        if (logActive)
        {
            DateTimeFormatter utc = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of(timeZone));
            out.println(utc.format(Instant.now()) + " " + serviceName + ": " + msg);
            out.flush();
        }
    }
    
    /**
     * Closes the log file.
     */
    public synchronized void closeLog()
    {
        out.close();
    }
}
