package ru.relastic.meet006;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.Random;


public class MyService extends Service {
    public final static String MSG_SERVICE_RNDVALUE = "rnd_value";
    public final static int MODE = Service.START_NOT_STICKY;
    private final static int INTERVAL = 250;
    private final Random  myRandom = new Random();
    private volatile boolean interrupted = false;

    public MyService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                while (!isInterrupted()) {
                    String messageInt = ((Integer)(myRandom.nextInt(899)+100)).toString();
                    sendBroadcastClients(messageInt);
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        Log.v("LOG:", "ERROR: "+e.toString());
                    }
                }
                Log.v("LOG:", "SERVICE MyService: Stopped");
                stopSelf();
            }
        });
        t.setName("WorkThread");
        t.start();
        return MODE;
    }
    private boolean isInterrupted(){
        return interrupted;
    }
    private void sendBroadcastClients(String message) {
        Intent broadcastIntent = new Intent("Meet006");
        broadcastIntent.putExtra(MSG_SERVICE_RNDVALUE, message);
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void interrupt(){
        interrupted = true;
    }
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context,MyService.class);
        return intent;
    }
}
