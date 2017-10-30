package com.ichi2.anki;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class StrxNotificationClickAction extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.ichi2.anki.action.FOO";
    private static final String ACTION_BAZ = "com.ichi2.anki.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.ichi2.anki.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.ichi2.anki.extra.PARAM2";

    public StrxNotificationClickAction() {

        super("StrxNotificationClickAction");
        Log.i("!@#", "Start StrxNotificationClickAction StrxNotificationClickAction()");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, StrxNotificationClickAction.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {

        //Log.d("!@#", "IntentService Stopping! startActionBaz");
       // this.stopSelf();
    }

    @Override
    public void onDestroy(){
        Log.i("!@#", "IntentService Stopping! onDestroy()");
        // this.stopSelf();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //Log.d("!@#", "IntentService Stopping!");
       // this.stopSelf();
    }

    private int nId;
    private String noteWord;
    private String flds;
    private String message;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();




        Bundle extras = intent.getExtras();
        String word = "?";
        int state = 0;
        if(extras != null){
            // Log.i( "dd","Extra:" + extras.getString("note") );
            noteWord = extras.getString("note");
            flds = extras.getString("flds");
            message = extras.getString("message");
            nId = extras.getInt("nId");
            state = extras.getInt("state");
        }
        Log.i("!@#", "Start StrxNotificationClickAction onStartCommand(), startId=" + startId + ", message=" + message);

        sendMessage(state);

        //ComponentName componentName = intent.getParentActivity();


       // Log.d("!@#", "StrxNotificationClickAction onStartCommand() flags=" + flags);
        //this.stopSelf();

        return super.onStartCommand(intent,flags,startId);
    }


    private void sendMessage(int state) {
        Intent intent = new Intent("my-event");
        // add data
        intent.putExtra("message", message);
        intent.putExtra("nId", nId);
        intent.putExtra("nWord", noteWord);
        intent.putExtra("state", state);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
