package com.fmontanari.mnuapp;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.fmontanari.mnuapp.data.DeviceInfo;
import com.fmontanari.mnuapp.data.Votacion;
import com.fmontanari.mnuapp.first_run.FirstRunActivity;
import com.fmontanari.mnuapp.interfaces.ClientEvents;
import com.fmontanari.mnuapp.models.Client;
import com.fmontanari.mnuapp.models.TaskFragment;

/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class MainActivity extends AppCompatActivity implements ClientEvents, TaskFragment.TaskCallbacks
{

    // Device state synced with the server.
    public enum State{
        DISCONNECTED,
        CONNECTED,
        PAIRED,
        GET_INFO
    }

    // Intent request codes
    static final int GET_SERVER_ADDRESS = 1;
    static final int SEND_DATA = 2;
    static final int FIRST_RUN = 3;
    static final int START_VOTES = 4;
    private static final String TAG_TASK_FRAGMENT = "TASK_FRAGMENT";


    State serverState = State.DISCONNECTED;
    Client myClient;
    DeviceInfo myInfo;
    int numVotantes = 0;
    Votacion currVotacion;
    Context myContext;
    boolean isConnected = false;
    TaskFragment mTaskFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myContext = this;
        myInfo = new DeviceInfo();
        SharedPreferences devPrefs = getSharedPreferences("DevicePrefs",Context.MODE_PRIVATE);

        // Is this our first run? If we have no device info present, then we have to create it first
        if(!devPrefs.contains("DeviceInfo"))
        {
            FirstRun();
            return;
        }
        else
        {
            myInfo = DeviceInfo.fromJSON(devPrefs.getString("DeviceInfo",""));
        }

        // Now that we have our info, start the connection
        setupConnection();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id)
        {
            case R.id.action_settings:
                // Connection settings menu
                Intent theIntent = new Intent(this, SavedServers.class);
                startActivityForResult(theIntent, GET_SERVER_ADDRESS);
                return true;

            case R.id.erase:
                // Erase connection data
                CharSequence text = "Borrando datos...";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
                SharedPreferences connPrefs = getSharedPreferences("SavedConnections", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = connPrefs.edit();
                editor.clear();
                editor.commit();

                toast.setText("Se borraron los datos satisfactoriamente");
                toast.show();
                break;

            case R.id.erase2:
                // Erase device data (such as device ID, etc)
                SharedPreferences devPrefs = getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = devPrefs.edit();
                editor2.clear();
                editor2.commit();
                ShowWarningMessage("Se borraron los datos satisfactoriamente", Toast.LENGTH_SHORT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Checks to see if there's any connection info saved,
     * gets the preferred connection info (aka: IP and port).
     * If there's any, it starts the connection with the server.
     * If not, starts the activity that setups the default connection.
     */
    public void setupConnection()
    {
        SharedPreferences connPrefs = getSharedPreferences("SavedConnections", Context.MODE_PRIVATE);
        if(connPrefs.contains("DefaultConnection") && connPrefs.contains("DefaultPort"))
        {
            String IP = connPrefs.getString("DefaultConnection","");
            int port = connPrefs.getInt("DefaultPort",0000);
            if( !isConnected )
                ConnectToServer(IP,port);
        }
        else
        {
            ShowWarningMessage("Por favor, especifique un servidor", Toast.LENGTH_SHORT);
            Intent theIntent = new Intent(this, SavedServers.class);
            startActivityForResult(theIntent, GET_SERVER_ADDRESS);
        }
    }


    /**
     * Creates a new TaskFragment that will connect to the srvIP and port given.
     * @param srvIP IP of the server that we'll try to connect to.
     * @param port Port of the server that we'll try to connect to.
     */
    public void ConnectToServer(String srvIP, int port)
    {
        // Don't need to do this if we're already connected.
        if( !isConnected )
        {
            Log.i("Client","Starting connection");
            FragmentManager fm = getFragmentManager();
            mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

            // If the Fragment is non-null, then it is currently being
            // retained across a configuration change.
            if (mTaskFragment == null) {
                mTaskFragment = new TaskFragment();
                mTaskFragment.IP = srvIP;
                mTaskFragment.port = port;
                fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
            }
            isConnected = true;
        }
    }


    /**
     * Helper function to display a Toast with the given message and duration.
     * @param message The message to be displayed.
     * @param duration Duration of the message. Has to be Toast.LENGTH_SHORT or Toast.LENGTH_LONG.
     */
    public void ShowWarningMessage(String message, int duration)
    {
        final String myMessage = message;
        final int myDuration = duration;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CharSequence text = myMessage;
                Toast toast = Toast.makeText(getApplicationContext(),text,myDuration);
                toast.show();
            }
        });
    }

    /**
     * Starts the FirstRun Activity to setup starting data.
     */
    private void FirstRun()
    {
        ShowWarningMessage("Setting up for first run...", Toast.LENGTH_SHORT);

        Intent theIntent = new Intent(this, FirstRunActivity.class);
        theIntent.putExtra("info",myInfo.toJSON());
        startActivityForResult(theIntent, FIRST_RUN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Check which request we're responding to
        if (requestCode == GET_SERVER_ADDRESS) {
            // This is from the connection settings activity. If the request was successful,
            // then we've got the IP and Port that we will connect to when we start the app.
            if (resultCode == RESULT_OK) {

                // Get data from the intent.
                String IP = data.getStringExtra("IP");
                int port = data.getIntExtra("Port", 0000);

                // Save them for use later.
                SharedPreferences connPrefs = getSharedPreferences("SavedConnections",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = connPrefs.edit();
                editor.putString("DefaultConnection", IP);
                editor.putInt("DefaultPort", port);
                editor.commit();

                // Have we tried to connect already?
                if ( !isConnected )
                {
                    ConnectToServer(IP,port);
                }else
                {
                    // TODO: disconnect and reconnect.
                    ShowWarningMessage("Reinicie la aplicación para que los cambios surjan efecto.",Toast.LENGTH_LONG);
                }
            }
        }

        if (requestCode == SEND_DATA)
        {
            if (resultCode == RESULT_OK)
            {
                myClient.SendMessage("get_data");
            }
        }

        if (requestCode == FIRST_RUN)
        {
            // This is from the FirstRun activity. If the request was successful, then we've got
            // the device info that we need in the first place.
            if (resultCode == RESULT_OK)
            {
                if(DeviceInfo.fromJSON(data.getStringExtra("info")) != null)
                {
                    myInfo = DeviceInfo.fromJSON(data.getStringExtra("info"));
                    Log.i("Info", myInfo.toJSON());
                    setupConnection();
                }
                else
                {
                    Log.e("Info","Null info!");
                    ShowWarningMessage("Ha habido un error obteniendo la información. Por favor intente de nuevo.",Toast.LENGTH_LONG);
                }
            }
        }
        if (requestCode == START_VOTES)
        {
            // This is from the Voting activity. Here we get the results of this device's votes.
            // If the request was successful, then we can send it to the server.
            if (resultCode == RESULT_OK)
            {
                currVotacion = Votacion.fromJSON(data.getStringExtra("VoteResults"));
                // Can't run network methods in main thread. Have to make a new thread for this.
                // TODO: Do I? if the client is on a separate fragment, we might not need to do this.
                new Thread(new Runnable() {
                    public void run() {
                        myClient.SendMessage("get_data");
                    }
                }).start();
            }
            // TODO: What if the request wasn't successful?
        }
    }


    /**
     * Callback method implementing the TaskCallback interface.
     * @param c The client created in the fragment. It will hold it's data in any configuration change
     *          I.E: device rotation, locking the device, etc.
     */
    @Override
    public void onClientCreated(Client c)
    {
        myClient = c;
    }


    /**
     * Sends our device information to the server.
     */
    public void sendAccessData()
    {
        String jsonString = myInfo.toJSON();
        String newString = jsonString.replace("\\","");
        if(newString == null)
            Log.e("Client","Null device info string");
        else
            myClient.SendMessage(newString);
    }

    // Client Interface implementation.

    @Override
    public void onConnected()
    {
        serverState = State.CONNECTED;
        SetDisconnectedMessage(false);
        sendAccessData();
    }

    @Override
    public void onDisconnected() {
        serverState = State.DISCONNECTED;
        SetDisconnectedMessage(true);
    }

    @Override
    public void onIncomingData(String data) {
        Log.i("Client", "Received: " + data);
        data = data.replace("\n","");
        processCommands(data);
    }


    /**
     *  Handles the incoming data from the server.
     *  WARNING: Doesn't run on UI Thread.
     * @param commands Data sent by the server.
     */
    private void processCommands(String commands)
    {
        Log.i("Client","Processing: " + commands);

        // We'll get this probably by an authentication error, or because we were forced to disconnect.
        if( commands.contains("Error"))
        {
            ShowWarningMessage("Error conectando al servidor.",Toast.LENGTH_LONG);
            serverState = State.DISCONNECTED;
            SetDisconnectedMessage(true);
            return;
        }

        // According to the device's state, we won't want to execute any other command that doesn't belong to it.
        switch(serverState)
        {

            // We're connected to the server, but we haven't authenticated yet.
            // If the command contains "device_paired" keyword, the device has been successfully registered by the server,
            // and can handle the other commands that the server will send. So the devices is paired with the server.
            case CONNECTED:
                if(commands.contains("device_paired"))
                {
                    serverState = State.PAIRED;
                }

                break;

            // We're paired with the server, so there're two chances: either we receive information from the current voting scheme
            // or the server is ready to receive data from the device, and we can send it.
            case PAIRED:

                // We'll get here when asked for voting results.
                if(commands.contains("send_data"))
                {
                    try{
                        myClient.SendMessage(currVotacion.toJSON().toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(myContext, "La votación se envió correctamente.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        currVotacion = null;
                    }catch(Exception ex)
                    {
                        ex.printStackTrace();
                    }
                }

                // We'll get this when we have to update voting info.
                if(commands.contains("get_info"))
                {
                    serverState = State.GET_INFO;
                    myClient.SendMessage("send_info");
                }
                break;

            // We are ready to get info about our voting scheme.
            case GET_INFO:

                /*
                * Currently we don't get any information from the server, but we might get some information
                * in the future.
                */
                // Update voting scheme
                numVotantes = myInfo.maxVotes;
                currVotacion = new Votacion();
                Log.i("main","New Votation with " + numVotantes + " votes");
                Intent i = new Intent(this, VoteActivity.class);
                i.putExtra("maxVotes", myInfo.maxVotes);
                startActivityForResult(i, START_VOTES);
                serverState = State.PAIRED;
                break;
        }

        Log.i("Client", "State of device: " + serverState);
    }


    /**
     * Helper function to set the main UI information to the device's state.
     * @param disconnected Do we set it to disconnected or not?
     */
    private void SetDisconnectedMessage(boolean disconnected)
    {
        Log.i("main","Setting message to:" + disconnected);
        if(!disconnected)
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView imgStatus = (ImageView)findViewById(R.id.imgStatus);
                    imgStatus.setImageResource(R.drawable.ic_connected);

                    TextView txtStatus = (TextView)findViewById(R.id.txtStatus);
                    txtStatus.setText("Conectado\n Espere votación");
                }
            });
        }else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView imgStatus = (ImageView)findViewById(R.id.imgStatus);
                    imgStatus.setImageResource(R.drawable.ic_disconnected);

                    TextView txtStatus = (TextView)findViewById(R.id.txtStatus);
                    txtStatus.setText("No Conectado");
                }
            });
        }
    }




}
