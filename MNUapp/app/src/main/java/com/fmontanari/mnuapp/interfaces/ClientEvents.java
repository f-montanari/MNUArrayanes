package com.fmontanari.mnuapp.interfaces;

/**
 * Created by Carlito on 21/12/2016.
 */

public interface ClientEvents {
    void onConnected();
    void onDisconnected();
    void onIncomingData(String data);
}
