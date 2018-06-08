package com.fmontanari.mnuapp.interfaces;

/**
 * Created by Franco Montanari on 21/12/2016.
 * Classes implementing this interface will know when the Client is connected/disconnected and
 * will be able to handle incoming data.
 */
public interface ClientEvents {
    void onConnected();
    void onDisconnected();
    void onIncomingData(String data);
}
