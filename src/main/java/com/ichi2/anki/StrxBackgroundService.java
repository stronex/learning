package com.ichi2.anki;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static org.acra.ACRA.log;


public class StrxBackgroundService extends Service {

    public StrxBackgroundService() {
    }

    private  int intervalTime = 60;//interwal czasowy w sekundach

    private StrxBackgroundService strxObj;
    private Toast toast;
    private String foo;
    private String flds[];
    private int cardState[];
    private int ease[];
    private int iteration = 0;
    private String sfld[];
    private int count = 0;
    private int notificationId = 0;

    private NotificationCompat.Builder mBuilder;
    private Intent intent;
    private Context context;
    private PendingIntent pIntent;
    private int maxOfNotes;
    private Timer timer;
    private TimerTask timerTask;
    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            context = getApplicationContext();

            /*
            if(count == 0) {
                    //close poprzednie notification - notification o aktywacji Learning offline
                    log.i("!!#@", context.toString());
                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                    nMgr.cancel(0);
            }
            */


            if(count == maxOfNotes){
                count = 0;
                iteration++;
            }


            //stop = 1
            //easy = 2
            //hard = 3




            Boolean flag = true;
            int n = count;
            for (int i = 0; i < maxOfNotes; i++) {
                if( cardState[n] == 3 ){
                    count = n;
                    flag = false;
                    break;
                }
                n++;
                if(n == maxOfNotes){
                    n = 0;
                }
            }
            Boolean flag2 = true;
            if(flag){ //nie ma kart state 3
                ;
                for (int i = 0; i < maxOfNotes; i++) {
                    //szuka kart state = 2
                    if( cardState[n] == 2 ){
                        count = n;
                        flag2 = false;
                        break;
                    }
                    n++;
                    if(n == maxOfNotes){
                        n = 0;
                    }
                }
            }
            if(flag && flag2){
                //nie ma kart o state = 3 ani state = 2
                stopSelf();
            }else{


                notificationId = count;

                String word;
                String word2;
                String arr = flds[count];
                String[] split = arr.split("\u001F");
                // System.out.println("Name = " + split[0]);
                // System.out.println("Password = " + split[1]);
                if (split[0] != null) word = split[0];
                else word = sfld[count];
                if (split[1] != null) word2 = split[1];
                else word2 = "-";
                //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(strxObj);
                String CHANNEL_ID = "my_channel_01";
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(strxObj);
                // Dodajemy podstawowe (wymagane) elementy
                mBuilder.setContentTitle(word);
                mBuilder.setContentText(null);
                mBuilder.setSmallIcon(R.drawable.ic_feedback_black_24dp);//ic_dialog_alert


                //hide poprzednie notification
                // Log.i("!!#@", context.toString());
                String ns = Context.NOTIFICATION_SERVICE;
                NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                nMgr.cancel(notificationId - 1);//notificationId = count


                // Set PendingIntent into Notification
                //mBuilder .setContentIntent(pIntent);

                //    mBuilder.setAutoCancel(true);

                //Uri alarmSound;
                //alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                //mBuilder.setSound(alarmSound);
                mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

                mBuilder.setVibrate(new long[]{100, 100});

                // mBuilder.setSound()


                // Tworzymy intent


                Intent mIntent = new Intent(context, Strx.class);
                mIntent.putExtra("note", sfld[count]);
                mIntent.putExtra("flds", flds[count]);
                mIntent.putExtra("ease", ease[count]);
                mIntent.putExtra("nId", count);

                /////
                // Intent startService = new Intent(context, SERVICE.class)
                //mIntent.putExtra("MESSENGER", new Messenger(messageHandler));
                //context.startService(startService);
                /////

                mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent mPendingIntent = PendingIntent.getActivity(context, notificationId, mIntent, 0);


                Intent intent3 = new Intent(context, StrxNotificationClickAction.class);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                intent3.putExtra("message", "Message From notification clicked button Off");
                intent3.putExtra("nId", count);
                intent3.putExtra("state", 1);
/*
            Intent intentBroadcast = new Intent("my-event");
            // add data
            intentBroadcast.putExtra("message", "Message From notification clicked button off");
            intentBroadcast.putExtra("nId", count);
            intentBroadcast.putExtra("nWord", "--");
            intentBroadcast.putExtra("state", 1);
            */
                PendingIntent pIntent3 = PendingIntent.getService(context, 11, intent3, PendingIntent.FLAG_UPDATE_CURRENT);//PendingIntent.FLAG_UPDATE_CURRENT
                //.getBroadcast(context, 11, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
                //.getService(context, notificationId, intent3, 0);//PendingIntent.FLAG_UPDATE_CURRENT

                // Add an Action Button below Notification
                mBuilder.addAction(R.drawable.av_stop, "Stop", pIntent3);


                // Open NotificationView.java Activity
                //Intent intent = new Intent(context, Strx.class);
                Intent intent2 = new Intent(context, StrxNotificationClickAction.class);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                intent2.putExtra("message", "Message From notification clicked button Easy");
                intent2.putExtra("nId", count);
                intent2.putExtra("state", 2);
                PendingIntent pIntent2 = PendingIntent.getService(context, 22, intent2, PendingIntent.FLAG_UPDATE_CURRENT);

                // Add an Action Button below Notification
                mBuilder.addAction(R.drawable.av_pause, "Easy", pIntent2);


                Intent intent = new Intent(context, StrxNotificationClickAction.class);
                //LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                intent.putExtra("message", "Message From notification clicked button Hard");
                intent.putExtra("nId", count);
                intent.putExtra("state", 3);
                PendingIntent pIntent = PendingIntent.getService(context, 33, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // Add an Action Button below Notification
                mBuilder.addAction(R.drawable.av_play, "Hard", pIntent);


                //mIntent.putExtra("note", "@#" + sfld[count]);
                // I go łączymy

                mBuilder.setContentIntent(mPendingIntent);

                //!!


                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationId, mBuilder.build());
                //mIntent.putExtra("note", "@#" + sfld[count]);

                //context.startService(GPSService);
                count++;
            }
        }
    }
/*
    public static Handler messageHandler = new MessageHandler();
    public static class MessageHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            int state = message.arg1;

            switch (state) {
                case HIDE:
                    //progressBar.setVisibility(View.GONE);
                    break;
                case SHOW:
                    //progressBar.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }
*/
    private void showToast(String text) {
        toast.setText(text);
        toast.show();
    }


    @Override
    public void onCreate() {
        super.onCreate();

       // writeToLogs("Background learning has been started");

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));

        Log.i("!@#", "Broadcast activated");

        timer = new Timer();
        //toast = Toast.makeText(this, "Background process was activated", Toast.LENGTH_SHORT);





    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeToLogs("Called onStartCommand() method");
        clearTimerSchedule();
        initTask();

        foo = intent.getStringExtra("foo").toString();
        flds = intent.getStringArrayExtra("flds");
        sfld = intent.getStringArrayExtra("sfld");
        ease = intent.getIntArrayExtra("ease");
/*
                -- which button you pushed to score your recall.
                -- review:  1(wrong), 2(hard), 3(ok), 4(easy)
                -- learn/relearn:   1(wrong), 2(ok), 3(easy)
*/
        cardState = new int[flds.length];
        for (int i = 0; i < sfld.length; i++) {
            Log.i("!@#", "cardState[" + i + "], flds.length=" + flds.length);
            if(ease[i] >= 3) cardState[i] = 2;
            else cardState[i] = 3;
        }
       // state[] = new int[flds.length];

        //showToast("Your service has been started " + foo );
        Toast.makeText(this, "Background learning has been started", Toast.LENGTH_SHORT).show();
        Log.i("!@#", "Start StrxBackground onStartCommand()");

        maxOfNotes = sfld.length;

        strxObj = this;
        mBuilder = new NotificationCompat.Builder(this);

        // Dodajemy podstawowe (wymagane) elementy
        mBuilder.setContentTitle("Learning offline");
        mBuilder.setContentText("There are " + maxOfNotes + " words to learning");
        mBuilder.setSmallIcon(R.drawable.ic_feedback_black_24dp);



        // Tworzymy intent
        ////  Intent mIntent = new Intent(this, MainActivity.class);
        // PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        // I go łączymy
        // mBuilder.setContentIntent(mPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());



        timer.scheduleAtFixedRate(timerTask, 1000 * intervalTime, 1000 * intervalTime);

        return super.onStartCommand(intent, flags, startId);
    }

    private void clearTimerSchedule() {
        if(timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
    }

    private void initTask() {

        timerTask = new MyTimerTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    private void writeToLogs(String message) {
        Log.d("HelloServices", message);
    }

    protected void stopStrxService(){


    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Background learning has been stopped", Toast.LENGTH_SHORT).show();
        clearTimerSchedule();
        stopSelf();
    }



////////////////////



    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            int nId = intent.getIntExtra("nId", 0);
            String nWord = intent.getStringExtra("nWord");
            int state = intent.getIntExtra("state", 0);
            Log.i("!@#", "Got message: " + message + ", nId=" + nId + ", note=" + nWord + ", state=" + state);

            //sfld[nId] += ", s=" + state;
            cardState[nId] = state;

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(nId);
        }
    };


}





