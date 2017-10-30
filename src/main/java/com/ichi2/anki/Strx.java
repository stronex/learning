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
    private String flds;
    private int ease;
    private String[] easeNames= {"wrong", "hard", "easy", "easy"};
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
            flds = extras.getString("flds");
            nId = extras.getInt("nId");
            ease = extras.getInt("ease");

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



        String word;
        String word2;
        String arr = flds;
        String[] split = arr.split("\u001F");
        // System.out.println("Name = " + split[0]);
        // System.out.println("Password = " + split[1]);
        if(split[0] != null) word = split[0];
        else word = noteWord;
        if(split[1] != null) word2 = split[1];
        else word2 = "-";

        String komunikat = "";
        if( ease >= 0 && ease <= 4){
            komunikat = "\n\nlast state: " + easeNames[ease];
        }
        alertDialog.setTitle(word);
        alertDialog.setMessage( word2 +  komunikat ); //+ " \n" + flds

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Hard", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                sendMessage(3);
                dialog.dismiss();
                finish(); //zamyka okno Strx.action (to okno w tle)
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Stop", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                sendMessage(1);
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Easy", new DialogInterface.OnClickListener() {
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
