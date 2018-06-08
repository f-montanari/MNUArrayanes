package com.fmontanari.mnuapp.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import android.os.AsyncTask;
import android.util.Log;
import com.fmontanari.mnuapp.interfaces.ClientEvents;

/**
 * Created by Franco Montanari on 24/11/2016.
 * This class is meant to handle the connection to the server.
 * Most of the networking code is handled here.
 */

public class Client extends AsyncTask<Void,Integer,Void> {

    private String dstAddress;
    private int dstPort;
    private String response = "";
    private ByteArrayOutputStream byteArrayOutputStream = null;
    private OutputStream outputStream = null;
    private Socket socket = null;

    // Handles callbacks for the MainActivity
    private TaskFragment.TaskCallbacks mCallbacks;

    public Client(String addr, int port, TaskFragment.TaskCallbacks mCallbacks) {
        dstAddress = addr;
        dstPort = port;
        this.mCallbacks = mCallbacks;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {

            // Open socket at desired address and port.
            socket = new Socket(dstAddress, dstPort);
            InputStream inputStream = socket.getInputStream();

            // TODO: Hardcoded Buffer... Should optimize this some way.
            byte[] buffer = new byte[1024];
            int bytesRead;
            String incomingData = "";
            outputStream = socket.getOutputStream();

            // If we got the output stream, we're connected successfully.
            Log.i("Client","Connected to server");

            // Let everyone know we're connected.
            mCallbacks.onConnected();

            // Read input stream
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // TODO: Hardcoded Buffer... Should optimize this some way.
                byteArrayOutputStream = new ByteArrayOutputStream(1024);
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                // TODO: ASCII? Might want to change to other format.
                incomingData += byteArrayOutputStream.toString("ASCII");
                byteArrayOutputStream.flush();
                mCallbacks.onIncomingData(incomingData);
                incomingData = "";
            }

        }
        catch (UnknownHostException e) {

            e.printStackTrace();
            Log.e("Client","UnknownHostException: " + e.toString());
        }
        catch (java.net.ConnectException e)
        {
            Log.e("Client","Couldn't connect to server");
        }
        catch (IOException e) {

            e.printStackTrace();
            if(e.getMessage().contains("ECONNRESET"))
            {
                Log.e( "Client", "Connection closed from the server." );
            }else if(e.getMessage().contains("ECONNREFUSED"))
            {
                Log.e( "Client", "Couldn't find the server." );
            }else
            {
                Log.e( "Client", "IOException: " + e.toString() );
            }
        }
        // Close the socket when we're done.
        finally {
            if (socket != null) {
                try {
                    socket.close();
                    Log.e( "Client", "Successfully closed the connection" );
                    mCallbacks.onDisconnected();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }

        publishProgress();
        return null;
    }


    /**
     * Sends a message to the server
     * @param message The message to be sent.
     * @return Response that confirms if the message was sent successfully or not.
     */
    public String SendMessage(String message)
    {
        Log.i("Client", "Sending message: " + message);
        byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] buffer;

        int bytesWrite;
        bytesWrite = message.length();
        try
        {
            buffer = message.getBytes("UTF-8");
        }catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
            buffer = null;
        }
        try {

            outputStream.write(buffer,0,bytesWrite);
            response = "Sent message: " + message + "\n";
        }catch (IOException e)
        {
            response = "Error sending message: " + e.getMessage();
            e.printStackTrace();
        }
        return response;
    }

    // TODO: Implement feature.
    public Void Disconnect()
    {
        try
        {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();
            Log.e( "Client", "Successfully closed the connection" );
            mCallbacks.onDisconnected();
        }catch (IOException e)
        {
            Log.e("Client","Socket disconnected");
            mCallbacks.onDisconnected();
        }
        return null;
    }

    private void LogActivity(String message) { Log.i("Client", "Received message: " + message); }

}
