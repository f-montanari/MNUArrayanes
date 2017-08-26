package com.fmontanari.mnuapp.data;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Currency;

public class Votacion {
    public int AFavor;
    public int EnContra;
    public int Abstenciones;

    public JSONObject toJSON()
    {
        JSONObject obj = new JSONObject();
        try{
            obj.put("AFavor", AFavor);
            obj.put("EnContra", EnContra);
            obj.put("Abstenciones", Abstenciones);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return obj;
    }

    public static Votacion fromJSON(String jsonString)
    {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return Votacion.fromJSON(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Votacion fromJSON(JSONObject jsonObject)
    {
        Votacion v = new Votacion();
        try {
            v.Abstenciones = jsonObject.getInt("Abstenciones");
            v.AFavor = jsonObject.getInt("AFavor");
            v.EnContra = jsonObject.getInt("EnContra");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return v;
    }
}
