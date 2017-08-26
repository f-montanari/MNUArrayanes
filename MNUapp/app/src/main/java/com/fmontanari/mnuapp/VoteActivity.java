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

    public void AddDesaprobados(View view) {

        // TODO: check max voting members
        int intReprobados = Integer.parseInt(txtDesaprobados.getText().toString());
        int intAprobados = Integer.parseInt(txtAprobados.getText().toString());
        if(intAprobados + intReprobados == maxVotes)
        {
            return;
        }
        txtDesaprobados.setText(Integer.toString(intReprobados + 1 ));
    }

    public void RemoveDesaprobados(View view) {
        int intReprobados = Integer.parseInt(txtDesaprobados.getText().toString());
        if(intReprobados != 0)
        {
            txtDesaprobados.setText(Integer.toString(intReprobados - 1 ));
        }
    }

    public void addAFavor(View view) {
        int intReprobados = Integer.parseInt(txtDesaprobados.getText().toString());
        int intAprobados = Integer.parseInt(txtAprobados.getText().toString());
        if(intAprobados + intReprobados == maxVotes)
        {
            return;
        }
        txtAprobados.setText(Integer.toString(intAprobados + 1) );
    }

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

        Intent data = getIntent();
        maxVotes = data.getIntExtra("maxVotes",0);
        myVotes = new Votacion();

        txtAprobados = (EditText)findViewById(R.id.txtAprobados);
        txtDesaprobados = (EditText)findViewById(R.id.txtDesaprobados);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int intAfavor = 0;
                int intEnContra = 0;

                intAfavor = Integer.parseInt(txtAprobados.getText().toString());
                intEnContra = Integer.parseInt(txtDesaprobados.getText().toString());

                if (intAfavor + intEnContra != maxVotes)
                {
                    Toast.makeText(getApplicationContext(),"No ingresó una cantidad de votos acorde a su cantidad de votantes",Toast.LENGTH_LONG).show();
                    return;
                }

                myVotes.Abstenciones = 0;
                myVotes.AFavor = intAfavor;
                myVotes.EnContra = intEnContra;

                Intent i = new Intent();
                i.putExtra("VoteResults", myVotes.toJSON().toString());
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
