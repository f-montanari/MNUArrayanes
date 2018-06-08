package com.fmontanari.mnuapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fmontanari.mnuapp.data.Votacion;

/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class VoteActivity extends AppCompatActivity {

    int maxVotes = 0;

    EditText txtDesaprobados;
    EditText txtAprobados;

    Votacion myVotes;

    /**
     * onClickListener for the AddDesaprobados button.
     * Checks if we haven't passed the max possible votes for this device, and adds a vote against.
     * @param view The view that called the function.
     */
    public void AddDesaprobados(View view) {

        // TODO: cache data? Would need to add a listener for the EditText.
        int intReprobados = Integer.parseInt(txtDesaprobados.getText().toString());
        int intAprobados = Integer.parseInt(txtAprobados.getText().toString());
        if(intAprobados + intReprobados == maxVotes)
        {
            return;
        }
        txtDesaprobados.setText(Integer.toString(intReprobados + 1 ));
    }

    /**
     * onClickListener for the RemoveDesaprobados button.
     * Checks if we aren't at 0 votes, and removes a vote against.
     * @param view The view that called the function.
     */
    public void RemoveDesaprobados(View view) {
        int intReprobados = Integer.parseInt(txtDesaprobados.getText().toString());
        if(intReprobados != 0)
        {
            txtDesaprobados.setText(Integer.toString(intReprobados - 1 ));
        }
    }

    /**
     * onClickListener for the AddAFavor button.
     * Checks if we haven't passed the max possible votes for this device, and adds a vote on favor.
     * @param view The view that called the function.
     */
    public void addAFavor(View view) {
        int intReprobados = Integer.parseInt(txtDesaprobados.getText().toString());
        int intAprobados = Integer.parseInt(txtAprobados.getText().toString());
        if(intAprobados + intReprobados == maxVotes)
        {
            return;
        }
        txtAprobados.setText(Integer.toString(intAprobados + 1) );
    }

    /**
     * onClickListener for the RemoveAFavor button.
     * Checks if we aren't at 0 votes, and removes a vote on favor.
     * @param view The view that called the function.
     */
    public void removeAFavor(View view) {
        int intAprobados = Integer.parseInt(txtAprobados.getText().toString());
        if(intAprobados != 0)
        {
            txtAprobados.setText(Integer.toString(intAprobados - 1 ));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        // Get max votes for this device.
        Intent data = getIntent();
        maxVotes = data.getIntExtra("maxVotes",0);
        myVotes = new Votacion();

        txtAprobados = (EditText)findViewById(R.id.txtAprobados);
        txtDesaprobados = (EditText)findViewById(R.id.txtDesaprobados);

        // The FloatingActionButton confirms this vote result.
        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int intAfavor = Integer.parseInt(txtAprobados.getText().toString());
                int intEnContra = Integer.parseInt(txtDesaprobados.getText().toString());

                // Is there a legal amount of votes?
                if (intAfavor + intEnContra != maxVotes)
                {
                    Toast.makeText(getApplicationContext(),"No ingresó una cantidad de votos acorde a su cantidad de votantes",Toast.LENGTH_LONG).show();
                    return;
                }

                // Populate vote results
                myVotes.Abstenciones = 0;
                myVotes.AFavor = intAfavor;
                myVotes.EnContra = intEnContra;

                // Put it in the intent
                Intent i = new Intent();
                i.putExtra("VoteResults", myVotes.toJSON().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
