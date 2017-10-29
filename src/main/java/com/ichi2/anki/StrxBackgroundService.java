package com.ichi2.anki;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import static org.acra.ACRA.log;


public class StrxBackgroundService extends Service {

    public StrxBackgroundService() {
    }

    private StrxBackgroundService strxObj;
    private Toast toast;
    private String foo;
    private long notes[];
    private String sfld[];
    private int count = 0;

    private NotificationCompat.Builder mBuilder;

    private Timer timer;
    private TimerTask timerTask;
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {

           // String sfldd = sfld[1];
            //showToast("Service working " + foo + " :: " + sfldd.toString() );
            Context context = getApplicationContext();

            if(count == 99){
                count = 0;
            }else{
                if(count == 0) {
                    //close poprzednie notification - notification o aktywacji Learning offline
                    log.i("!!#@", context.toString());
                    String ns = Context.NOTIFICATION_SERVICE;
                    NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
                    nMgr.cancel(0);
                }
                count++;


            }
            //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(strxObj);
            String CHANNEL_ID = "my_channel_01";
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(strxObj);
            // Dodajemy podstawowe (wymagane) elementy
            mBuilder.setContentTitle("Word: " + sfld[count] );
            mBuilder.setContentText("..");
            mBuilder.setSmallIcon(R.drawable.ic_dialog_alert);//ic_dialog_alert



            //close poprzednie notification
            log.i("!!#@", context.toString());
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
            nMgr.cancel(count-1);



                    // Set PendingIntent into Notification
            //mBuilder .setContentIntent(pIntent);

        //    mBuilder.setAutoCancel(true);

            //Uri alarmSound;
            //alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            //mBuilder.setSound(alarmSound);
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

            mBuilder.setVibrate(new long[] { 1000, 1000});

           // mBuilder.setSound()


            // Tworzymy intent


            Intent mIntent = new Intent(context, Strx.class);
            mIntent.putExtra("note", sfld[count]);
            mIntent.putExtra("nId", count);
            mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent mPendingIntent = PendingIntent.getActivity(context, count, mIntent, 0);

            // Open NotificationView.java Activity
            //Intent intent = new Intent(context, Strx.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, count, mIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            // Add an Action Button below Notification
            mBuilder.addAction(R.drawable.ic_action_cancel, "OK", pIntent);
            // Add an Action Button below Notification
            mBuilder.addAction(R.drawable.ic_action_cancel, "Close", pIntent);

            //mIntent.putExtra("note", "@#" + sfld[count]);
            // I go łączymy

             mBuilder.setContentIntent(mPendingIntent);

            //!!



            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(count, mBuilder.build());
            //mIntent.putExtra("note", "@#" + sfld[count]);

        }
    }

    private void showToast(String text) {
        toast.setText(text);
        toast.show();
    }


    @Override
    public void onCreate() {
        super.onCreate();

        writeToLogs("Called onCreate() method.");
        timer = new Timer();
        //toast = Toast.makeText(this, "Background process was activated", Toast.LENGTH_SHORT);

        strxObj = this;

        mBuilder = new NotificationCompat.Builder(this);

        // Dodajemy podstawowe (wymagane) elementy
        mBuilder.setContentTitle("Learning offline");
        mBuilder.setContentText("Background learning has been activated");
        mBuilder.setSmallIcon(R.drawable.ic_feedback_black_24dp);

        // Tworzymy intent
        ////  Intent mIntent = new Intent(this, MainActivity.class);
        // PendingIntent mPendingIntent = PendingIntent.getActivity(this, 0, mIntent, 0);

        // I go łączymy
        // mBuilder.setContentIntent(mPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());



    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        writeToLogs("Called onStartCommand() method");
        clearTimerSchedule();
        initTask();

        foo = intent.getStringExtra("foo").toString();
        notes = intent.getLongArrayExtra("notes");
        sfld = intent.getStringArrayExtra("sfld");

        //showToast("Your service has been started " + foo );
        Toast.makeText(this, "service onStartCommand()", Toast.LENGTH_SHORT).show();

        timer.scheduleAtFixedRate(timerTask, 10 * 1000, 10 * 1000);

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
        Toast.makeText(this, "service onDestroy()", Toast.LENGTH_SHORT).show();
        clearTimerSchedule();
        stopSelf();
    }






}





