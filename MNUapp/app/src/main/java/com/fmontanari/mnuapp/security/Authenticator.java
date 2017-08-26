package com.fmontanari.mnuapp.security;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Franco Montanari on 24/11/2016.
 */
public class Authenticator {
    private String AuthServerURL = "http://fmontanari.ml/";
    public String AuthKey = "";
    public String SvrID = "";

    public String toJSON() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("AuthServerURL", AuthServerURL);
            jsonObject.put("AuthKey", AuthKey);
            jsonObject.put("SvrID", SvrID);

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static Authenticator fromJson(String jsonString)
    {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Authenticator.fromJson(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Authenticator fromJson(JSONObject jsonObject)
    {
        Authenticator auth = new Authenticator();
        try {
            auth.AuthServerURL = (String)jsonObject.get("AuthServerURL");
            auth.AuthKey = (String)jsonObject.get("AuthKey");
            auth.SvrID = (String)jsonObject.get("SvrID");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return  auth;
    }
}
