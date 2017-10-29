package com.ichi2.anki;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class Strx extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //!! setContentView(R.layout.activity_strx);

        /*Toast.makeText(getApplicationContext(), "Service Started in" +
                " a Different " +
                "Process", Toast.LENGTH_LONG).show();*/
        //savedInstanceState.getI
        //Intent intent = getIntent();

        //Bundle bundle = intent.getExtras();
        Bundle extras = getIntent().getExtras();
        String word = "?";
        if(extras != null){
           // Log.i( "dd","Extra:" + extras.getString("note") );
            word = extras.getString("note");
        }

        //word +=  bundle.getString("note");
        int nId = extras.getInt("nId");

        showToast(word);

        showDialog(word);
        //finish();
    }

    public void showDialog( String word ) {
        AlertDialog alertDialog = new AlertDialog.Builder(Strx.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Word: " + word);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //finish(); //zamyka okno Strx.action (to okno w tle)
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Off", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                finish();
            }
        });

                /*
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        finish();
                    }
                });
                */
        alertDialog.show();
    }

    private void showToast(String text) {
        //toast.setText(text);
        //toast.show();
    }

}
