package com.fmontanari.mnuapp.data;

import android.bluetooth.BluetoothClass;
import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import com.fmontanari.mnuapp.security.Authenticator;

import java.io.Reader;

public class DeviceInfo {
    public String DeviceID;
    public int maxVotes;
    private String clientVersion = "v0.1";
    private Authenticator Auth;
    public DeviceInfo()
    {
        Auth = new Authenticator();
    }

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DeviceID", DeviceID);
            jsonObject.put("ClientVersion", clientVersion);
            jsonObject.put("Auth", new JSONObject(Auth.toJSON()));
            jsonObject.put("maxVotes", maxVotes);
            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }
    public static DeviceInfo fromJSON(String jsonString)
    {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return DeviceInfo.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DeviceInfo fromJSON(JSONObject jsonObject)
    {
        DeviceInfo d = new DeviceInfo();
        try {
            d.DeviceID = (String)jsonObject.get("DeviceID");
            d.clientVersion = (String)jsonObject.get("ClientVersion");
            d.Auth = Authenticator.fromJson((JSONObject)jsonObject.get("Auth"));;
            d.maxVotes = (int)jsonObject.get("maxVotes");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return d;
    }
}
