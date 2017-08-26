package com.fmontanari.mnuapp.models;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.fmontanari.mnuapp.MainActivity;
import com.fmontanari.mnuapp.interfaces.ClientEvents;

/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class Client extends AsyncTask<Void,Void,Void> {


    private List<ClientEvents> eventListeners = new ArrayList<>();
    private String dstAddress;
    private int dstPort;
    private String response = "";
    private ByteArrayOutputStream byteArrayOutputStream = null;
    private OutputStream outputStream = null;
    private Socket socket = null;
    private MainActivity callbackActivity;

    public Client(String addr, int port, MainActivity callback) {
        dstAddress = addr;
        dstPort = port;
        callbackActivity = callback;
    }

    public void addEventListener(ClientEvents event)
    {
        eventListeners.add(event);
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        try {
            socket = new Socket(dstAddress, dstPort);
            //publishProgress();

            InputStream inputStream = socket.getInputStream();
            //BufferedReader breader = new BufferedReader(new InputStreamReader(inputStream));
            byte[] buffer = new byte[1024];
            int bytesRead;
            String incomingData = "";
            outputStream = socket.getOutputStream();

            Log.i("Client","Connected to server");
            for(ClientEvents in : eventListeners)
            {
                in.onConnected();
            }

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                // Hardcoded Buffer... Should optimize this some way.
                byteArrayOutputStream = new ByteArrayOutputStream(1024);
                //byteArrayOutputStream.reset();
                byteArrayOutputStream.write(buffer, 0, bytesRead);
                incomingData += byteArrayOutputStream.toString("ASCII");
                byteArrayOutputStream.flush();
                //publishProgress();
                for (ClientEvents in : eventListeners) {
                    in.onIncomingData(incomingData);
                }
                incomingData = "";
            }

        } catch (UnknownHostException e) {

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
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                    Log.e( "Client", "Successfully closed the connection" );
                    //publishProgress();
                    for(ClientEvents in : eventListeners)
                    {
                        in.onDisconnected();
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
        Log.i("Client", response);
    }

    public String SendMessage(String message)
    {
        Log.i("Client", message);
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



    public Void Disconnect()
    {
        try
        {
            socket.getOutputStream().close();
            socket.getInputStream().close();
            socket.close();
            Log.e( "Client", "Successfully closed the connection" );
            for(ClientEvents in : eventListeners)
            {
                in.onDisconnected();
            }
        }catch (IOException e)
        {
            Log.e("Client","Socket disconnected");
            for(ClientEvents in : eventListeners)
            {
                in.onDisconnected();
            }
        }
        return null;
    }

    private void LogActivity(String message) { Log.i("Client", "Received message: " + message); }
    @Override
    protected void onPostExecute(Void result) {
        LogActivity(response);
        super.onPostExecute(result);
    }

}
