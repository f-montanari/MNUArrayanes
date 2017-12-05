package com.fmontanari.mnuapp.models;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

/**
 * This Fragment manages a single background task and retains
 * itself across configuration changes.
 */
public class TaskFragment extends Fragment {

    /**
     * Callback interface through which the fragment will report the
     * task's progress and results back to the Activity.
     */
    public interface TaskCallbacks {
        void onClientCreated(Client c);
        void onConnected();
        void onIncomingData(String incomingData);
        void onDisconnected();
    }

    private TaskCallbacks mCallbacks;
    private Client mTask;

    /**
     * Hold a reference to the parent Activity so we can report the
     * task's current progress and results. The Android framework
     * will pass us a reference to the newly created Activity after
     * each configuration change.
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (TaskCallbacks) activity;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);

        // Create and execute the background task.
        mTask = new Client("192.168.0.30",1855,mCallbacks);
        mTask.execute();
        mCallbacks.onClientCreated(mTask);
    }

    /**
     * Set the callback to null so we don't accidentally leak the
     * Activity instance.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

}