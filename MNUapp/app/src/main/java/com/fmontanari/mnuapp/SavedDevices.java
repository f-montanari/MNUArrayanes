package com.fmontanari.mnuapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.JsonReader;
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
import java.util.Set;

/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class SavedDevices extends AppCompatActivity {

    ArrayList<SavedDevice> datos = new ArrayList<SavedDevice>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_devices);

        // TODO: Implement settings loading here:
        // datos.add(new SavedDevice("Test","localhost",1855));
        // datos.add(new SavedDevice("Computer 1","192.168.0.4",1855));

        // We need a way to now if we had some servers stored before.
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

    private void RefreshLists() {
        ListView lista = (ListView) findViewById(R.id.savedDevicesListView);

        lista.setAdapter(new SavedDevicesAdapter(this, R.layout.saved_devices, datos){
            @Override
            public void onEntrada(Object entrada, View view) {
                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                texto_superior_entrada.setText(((SavedDevice) entrada).getName());

                TextView texto_inferior_entrada = (TextView) view.findViewById(R.id.textView_inferior);
                texto_inferior_entrada.setText(((SavedDevice) entrada).getIPAddress() + ":" + ((SavedDevice) entrada).getPort());
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                SavedDevice elegido = (SavedDevice) pariente.getItemAtPosition(posicion);
                Intent extraData = new Intent();
                extraData.putExtra("IP",elegido.getIPAddress());
                extraData.putExtra("Port",elegido.getPort());
                setResult(RESULT_OK,extraData);
                finish();
            }
        });
    }

    private void LoadConnections(SharedPreferences prefs)
    {
        String connections = prefs.getString("Connections", null);
        if(connections != null)
        {
            try {
                JSONArray connArray = new JSONArray(connections);
                for(int i = 0; i< connArray.length(); i++)
                {
                    JSONObject connection = connArray.getJSONObject(i);
                    String Name = connection.getString("Name");
                    String IP = connection.getString("IP");
                    int Port = connection.getInt("Port");

                    datos.add(new SavedDevice(Name,IP,Port));
                }
            } catch (JSONException e) {
                Log.e("JSON", "Corrupt data");
                LinearLayout img = (LinearLayout) findViewById(R.id.imgLayout);
                img.setVisibility(View.VISIBLE);
            }
        }
    }

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

                        datos.add(new SavedDevice(Name,IP,Port));
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

    private void SaveData() {
        String jsonString;
        JSONArray connectionArray = new JSONArray();
        for (int i=0;i<datos.toArray().length;i++)
        {
            JSONObject connectionObj = new JSONObject();
            try {
                connectionObj.put("Name", ((SavedDevice) datos.toArray()[i]).getName());
                connectionObj.put("IP",((SavedDevice) datos.toArray()[i]).getIPAddress());
                connectionObj.put("Port",((SavedDevice) datos.toArray()[i]).getPort());
                connectionArray.put(connectionObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        jsonString = connectionArray.toString();

        SharedPreferences prefs = getSharedPreferences("SavedConnections",Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.putString("Connections",jsonString);
        prefsEdit.commit();
    }
}
