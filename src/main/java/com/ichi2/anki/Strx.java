package com.ichi2.anki;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static org.acra.ACRA.log;

public class Strx extends Activity {

    private int nId;
    private String noteWord;
    //private int state = 0;

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
            noteWord = extras.getString("note");
            nId = extras.getInt("nId");

            /////
           // messageHandler = (Messenger) extras.get("MESSENGER");

            //log.i("!@#", messageHandler.toString());
            //Message message = Message.obtain();
            //message.arg1 = Home.SHOW;
            //messageHandler.send(message);

        }

        //word +=  bundle.getString("note");

        //showToast();

        showDialog();
        //finish();
    }
    //////
   // private Messenger messageHandler;
////////////////////////////
    // Send an Intent with an action named "my-event".
    private void sendMessage(int state) {
        Intent intent = new Intent("my-event");
        // add data
        intent.putExtra("message", "Message From Strx Activity to StrxBackground service");
        intent.putExtra("nId", nId);
        intent.putExtra("nWord", noteWord);
        intent.putExtra("state", state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
    public void showDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Strx.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Word: " + noteWord);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                sendMessage(3);
                dialog.dismiss();
                //finish(); //zamyka okno Strx.action (to okno w tle)
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendMessage(1);
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Off", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendMessage(2);
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
