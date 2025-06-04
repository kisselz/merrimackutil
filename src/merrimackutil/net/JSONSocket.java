package merrimackutil.net;

import merrimackutil.json.JsonIO;
import merrimackutil.json.types.JSONArray;
import merrimackutil.json.types.JSONObject;
import merrimackutil.net.hostdb.HostEntry;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class provides extends Socket to provide convenient methods for sending and receiving JSONTypes
 */
public class JSONSocket extends Socket {
    private Scanner recv; //input stream
    private PrintWriter send; //output stream

    /**
     * Constructs a new JSONSocket stream and connects to {@code host}
     * @param host HostEntry to connect to
     * @throws IOException throws if there is an I/O error creating, connecting or establishing I/O streams of Socket
     */
    public JSONSocket(HostEntry host) throws IOException {
        super(host.getAddress(), host.getPort());

        this.recv = new Scanner(getInputStream());
        this.send = new PrintWriter(getOutputStream(), true);
    }

    /**
     * Constructs a new JSONSocket stream and connects to port {@code port} on named host {@code host}
     * @param host Named host, or null for loopback address
     * @param port the port number
     * @throws IOException throws if there is an I/O error creating, connecting or establishing I/O Streams of Socket
     */
    public JSONSocket(String host, int port) throws IOException {
        super(host,port);

        this.recv = new Scanner(getInputStream());
        this.send = new PrintWriter(getOutputStream(), true);
    }

    /**
     * Constructs a new JSONSocket stream and connects it to port {@code port} and IP address {@code address}
     * @param address the IP address
     * @param port the port number
     * @throws IOException throws if there is an I/O error creating, connecting or establishing I/O Streams of Socket
     */
    public JSONSocket(InetAddress address, int port) throws IOException {
        super(address, port);

        this.recv = new Scanner(getInputStream());
        this.send = new PrintWriter(getOutputStream(), true);
    }

    /**
     * Constructs a new JSONSocket stream which wraps an existing connected Socket Stream {@code sock}
     * @apiNote Useful to wrap a socket given by {@code ServerSocket.accept()};
     * @param sock Connected Socket Stream to wrap
     * @throws IOException throws if there is an I/O error creating, connecting or establishing I/O Streams of Socket
     */
    public JSONSocket(Socket sock) throws IOException{
        super();

        this.recv = new Scanner(sock.getInputStream());
        this.send = new PrintWriter(sock.getOutputStream(), true);
    }

    /**
     * Sends a JSONObject on this Socket's output stream
     * @param obj JSONObject to send
     */
    public void sendObject(JSONObject obj) {
        send.println(obj.toJSON());
    }

    /**
     * Sends a JSONArray on this Socket's output stream
     * @param array JSONArray to send
     */
    public void sendArray(JSONArray array) {
        send.println(array.toJSON());
    }

    /**
     * Receives JSONObject on this Socket's input stream
     * @return JSONObject received by Socket
     * @throws InvalidObjectException throws if received string cannot be parsed or is of wrong type
     */
    public JSONObject recvObject() throws InvalidObjectException {
        try {
            return JsonIO.readObject(recv.nextLine());
        }
        catch (NullPointerException e) {
            throw new InvalidObjectException("Could not parse JSON: " + e.getMessage());
        }
        catch(ClassCastException e) {
            throw new InvalidObjectException("Could not cast to JSONObject: " + e.getMessage());
        }
    }

    /**
     * Receives JSONArray on this Socket's input stream
     * @return JSONArray received by Socket
     * @throws InvalidObjectException throws if received string cannot be parsed or is of wrong type
     */
    public JSONArray recvArray() throws InvalidObjectException {
        try {
            return JsonIO.readArray(recv.nextLine());
        }
        catch (NullPointerException e) {
            throw new InvalidObjectException("Could not parse JSON: " + e.getMessage());
        }
        catch(ClassCastException e) {
            throw new InvalidObjectException("Could not cast to JSONArray: " + e.getMessage());
        }
    }
}
