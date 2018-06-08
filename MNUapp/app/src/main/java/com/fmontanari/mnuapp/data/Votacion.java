package com.fmontanari.mnuapp.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class that holds the current voting data.
 */
public class Votacion {
    public int AFavor;
    public int EnContra;
    public int Abstenciones;

    /**
     * Converts this Votacion to a JSON formatted string.
     * @return A JSON object that holds the data of this Votacion.
     */
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

    /**
     * Return a Votacion from it's associated JSON string.
     * @param jsonString The JSON formatted string that holds the data for this Votacion.
     * @return The Votacion associated with the given JSON string.
     */
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

    /**
     * Return a Votacion from it's associated JSON string.
     * @param jsonObject The JSON object that holds the data for this Votacion.
     * @return The Votacion associated with the given JSON object.
     */
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
