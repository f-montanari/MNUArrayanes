package com.fmontanari.mnuapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.fmontanari.mnuapp.data.DeviceInfo;
import com.fmontanari.mnuapp.data.Votacion;
import com.fmontanari.mnuapp.first_run.FirstRunActivity;
import com.fmontanari.mnuapp.interfaces.ClientEvents;
import com.fmontanari.mnuapp.models.Client;


/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class MainActivity extends AppCompatActivity implements ClientEvents{

    public enum State{
        DISCONNECTED,
        CONNECTED,
        PAIRED,
        GET_INFO
    }

    static final int GET_SERVER_ADDRESS = 1;
    static final int SEND_DATA = 2;
    static final int FIRST_RUN = 3;
    static final int START_VOTES = 4;

    State serverState = State.DISCONNECTED;
    Client myClient;
    DeviceInfo myInfo;
    int numVotantes = 0;
    Votacion currVotacion;
    Context myContext;
    boolean isConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myContext = this;
        myInfo = new DeviceInfo();

        SharedPreferences devPrefs = getSharedPreferences("DevicePrefs",Context.MODE_PRIVATE);
        if(!devPrefs.contains("DeviceInfo"))
        {
            FirstRun();
            return;
        }
        else
        {
            myInfo = DeviceInfo.fromJSON(devPrefs.getString("DeviceInfo",""));
        }

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

        //noinspection SimplifiableIfStatement
        switch(id)
        {
            case R.id.action_settings:
                Intent theIntent = new Intent(this, SavedDevices.class);
                startActivityForResult(theIntent, GET_SERVER_ADDRESS);
                return true;
            case R.id.erase:
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
                SharedPreferences devPrefs = getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = devPrefs.edit();
                editor2.clear();
                editor2.commit();
                ShowWarningMessage("Se borraron los datos satisfactoriamente", Toast.LENGTH_SHORT);
                break;
        }


        return super.onOptionsItemSelected(item);
    }


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
            Intent theIntent = new Intent(this, SavedDevices.class);
            startActivityForResult(theIntent, GET_SERVER_ADDRESS);
        }
    }

    public void ConnectToServer(String srvIP, int port)
    {
        if( !isConnected )
        {
            Log.i("Client","Starting connection");
            myClient = new Client(srvIP,port,this);
            myClient.addEventListener(this);
            myClient.execute();
            isConnected = true;
        }
    }

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
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                String IP = data.getStringExtra("IP");
                int port = data.getIntExtra("Port", 0000);

                SharedPreferences connPrefs = getSharedPreferences("SavedConnections",Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = connPrefs.edit();
                editor.putString("DefaultConnection", IP);
                editor.putInt("DefaultPort", port);
                editor.commit();

                if ( !isConnected )
                {
                    ConnectToServer(IP,port);
                }else
                {
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
                }
            }
        }
        if (requestCode == START_VOTES)
        {
            if (resultCode == RESULT_OK)
            {
                currVotacion = Votacion.fromJSON(data.getStringExtra("VoteResults"));
                new Thread(new Runnable() {
                    public void run() {
                        myClient.SendMessage("get_data");
                    }
                }).start();
            }
        }
    }

    public void sendAccessData()
    {
        String jsonString = myInfo.toJSON();
        String newString = jsonString.replace("\\","");
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //setContentView(R.layout.activity_my);
    }

    private void processCommands(String commands)
    {
        Log.i("Client","Processing: " + commands);

        if( commands.contains("Error"))
        {
            ShowWarningMessage("Error conectando al servidor.",Toast.LENGTH_LONG);
            serverState = State.DISCONNECTED;
            SetDisconnectedMessage(true);
            return;
        }
        // TODO: Implement State Machine here
        switch(serverState)
        {
            case CONNECTED:
                if(commands.contains("device_paired"))
                {
                    serverState = State.PAIRED;
                }

                break;
            case PAIRED:

                // We'll get this when asked for votation results.
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

                // We'll get this when we update votation info.
                if(commands.contains("get_info"))
                {
                    serverState = State.GET_INFO;
                    myClient.SendMessage("send_info");
                }
                break;

            case GET_INFO:
                // We'll receive info about how many voting members are.

                // Wait a second... WE KNOW HOW MANY WE ARE!!
                // There's no need for us to receive any info about that.

                // TODO: Remove hardcoded value
                //numVotantes = Integer.parseInt(commands.substring(0,2));

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
