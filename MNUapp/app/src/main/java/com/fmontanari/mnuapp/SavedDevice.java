package com.fmontanari.mnuapp;

/**
 * Created by Franco Montanari on 18/11/2016.
 */
public class SavedDevice {
    private String Name;
    private String IPAddress;
    private int Port;

    public SavedDevice(String Name, String IPAddress, int Port)
    {
        this.Name = Name;
        this.IPAddress = IPAddress;
        this.Port = Port;
    }

    public String getName()
    {
        return Name;
    }

    public String getIPAddress()
    {
        return IPAddress;
    }

    public int getPort() { return Port; }
}
