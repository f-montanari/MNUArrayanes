package com.fmontanari.mnuapp.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Data class to hold information of this device.
 */
public class DeviceInfo {
    public String DeviceID;
    public int maxVotes;
    private String clientVersion = "v0.1";

    /**
     * Converts this DeviceInfo to a JSON formatted string.
     * @return A JSON formatted string that holds the data of this DeviceInfo.
     */
    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("DeviceID", DeviceID);
            jsonObject.put("ClientVersion", clientVersion);
            jsonObject.put("maxVotes", maxVotes);
            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Return a DeviceInfo from it's associated JSON string.
     * @param jsonString The JSON formatted string that holds the data for this DeviceInfo.
     * @return The DeviceInfo associated with the given JSON string.
     */
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

    /**
     * Return a DeviceInfo from it's associated JSON string.
     * @param jsonObject The JSON object that holds the data for this DeviceInfo.
     * @return The DeviceInfo associated with the given JSON object.
     */
    public static DeviceInfo fromJSON(JSONObject jsonObject)
    {
        DeviceInfo d = new DeviceInfo();
        try {
            d.DeviceID = (String)jsonObject.get("DeviceID");
            d.clientVersion = (String)jsonObject.get("ClientVersion");
            d.maxVotes = (int)jsonObject.get("maxVotes");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return d;
    }
}
