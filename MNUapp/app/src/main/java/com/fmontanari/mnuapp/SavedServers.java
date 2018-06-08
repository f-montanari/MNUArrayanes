package com.fmontanari.mnuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.*;

import java.util.ArrayList;

/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class SavedServers extends AppCompatActivity {

    ArrayList<SavedServerInformation> datos = new ArrayList<SavedServerInformation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_devices);

        // We need a way to know if we had some servers stored before.
        // We'll get that by using default connections. If there're any, then it means that
        // we have at least ONE connection, and we can safely add it to the list.
        SharedPreferences prefs = getSharedPreferences("SavedConnections", Context.MODE_PRIVATE);
        if(prefs.contains("DefaultConnection") && prefs.contains("DefaultPort"))
        {
            // We have at least 1 connection.
            LinearLayout img = (LinearLayout) findViewById(R.id.imgLayout);
            img.setVisibility(View.INVISIBLE);
            LoadConnections(prefs);
        }
        RefreshLists();
    }

    /**
     * Sets adapter for the ListView in this activity, and populates it with the saved data.
     */
    private void RefreshLists() {
        ListView lista = (ListView) findViewById(R.id.savedDevicesListView);

        // Populate ListView
        lista.setAdapter(new SavedServerInformationAdapter(this, R.layout.saved_devices, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                texto_superior_entrada.setText(((SavedServerInformation) entrada).getName());

                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior);
                texto_inferior_entrada.setText(((SavedServerInformation) entrada).getIPAddress() + ":" + ((SavedServerInformation) entrada).getPort());
            }
        });

        // Set OnClickListener to select an item as the default server info
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                SavedServerInformation elegido = (SavedServerInformation) pariente.getItemAtPosition(posicion);
                Intent extraData = new Intent();
                extraData.putExtra("IP",elegido.getIPAddress());
                extraData.putExtra("Port",elegido.getPort());
                setResult(RESULT_OK,extraData);
                finish();
            }
        });
    }

    /**
     * Gets all saved connections in the SharedPrefs, and populates the data array.
     * @param prefs SharedPreferences of this activity.
     */
    private void LoadConnections(SharedPreferences prefs)
    {
        String connections = prefs.getString("Connections", null);
        if(connections != null)
        {
            try {
                // Get all connections...
                JSONArray connArray = new JSONArray(connections);
                for(int i = 0; i< connArray.length(); i++)
                {
                    // ... and add them to the data array.
                    JSONObject connection = connArray.getJSONObject(i);
                    String Name = connection.getString("Name");
                    String IP = connection.getString("IP");
                    int Port = connection.getInt("Port");

                    datos.add(new SavedServerInformation(Name,IP,Port));
                }
            } catch (JSONException e) {
                Log.e("JSON", "Corrupt data");
                LinearLayout img = (LinearLayout) findViewById(R.id.imgLayout);
                img.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * onClickListener for the "Add server" button.
     * Shows a dialog asking for the new server IP and port.
     * If the result's OK, we add it to the server list. Else we just don't do anything.
     * @param view The View that called this sub.
     */
    public void addServerToList(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.new_device_dialog, null))
                // Add action buttons
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText IPText = (EditText)((AlertDialog)dialog).findViewById(R.id.txtIP);
                        EditText PortText = (EditText)((AlertDialog)dialog).findViewById(R.id.txtPort);
                        EditText NameText = (EditText)((AlertDialog)dialog).findViewById(R.id.txtName);

                        String IP = IPText.getText().toString();
                        int Port = Integer.parseInt(PortText.getText().toString());
                        String Name = NameText.getText().toString();

                        datos.add(new SavedServerInformation(Name,IP,Port));
                        RefreshLists();
                        LinearLayout img = (LinearLayout) findViewById(R.id.imgLayout);
                        img.setVisibility(View.INVISIBLE);
                        SaveData();
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.show();
    }

    /**
     * Helper function to save server information for later use.
     */
    private void SaveData() {
        String jsonString;
        JSONArray connectionArray = new JSONArray();

        // Convert data array to a JSON format
        for (int i=0;i<datos.toArray().length;i++)
        {
            JSONObject connectionObj = new JSONObject();
            try {
                connectionObj.put("Name", ((SavedServerInformation) datos.toArray()[i]).getName());
                connectionObj.put("IP",((SavedServerInformation) datos.toArray()[i]).getIPAddress());
                connectionObj.put("Port",((SavedServerInformation) datos.toArray()[i]).getPort());
                connectionArray.put(connectionObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // And save it as a string in the SharedPreferences.
        jsonString = connectionArray.toString();
        SharedPreferences prefs = getSharedPreferences("SavedConnections",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString("Connections",jsonString);
        prefsEdit.commit();
    }
}
