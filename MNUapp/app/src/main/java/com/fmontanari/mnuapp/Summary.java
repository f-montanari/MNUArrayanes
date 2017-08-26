package com.fmontanari.mnuapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Franco Montanari on 24/11/2016.
 */

public class Summary extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent i = getIntent();
        String pedidoDetalle = i.getStringExtra("pedidoActual");
        TextView txtSummary = (TextView) findViewById(R.id.txtSummary);

        txtSummary.setText(pedidoDetalle);
    }

    public void cancelPedido(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void sendPedido(View view) {
        setResult(RESULT_OK);
        finish();
    }
}
