package com.mikel.poseidon;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.mikel.poseidon.utility.ExplicitIntentGenerator;

import org.poseidon_project.context.IContextReasoner;

import java.util.Map;

import uk.ac.mdx.cs.ie.acontextlib.IContextReceiver;
import uk.ac.mdx.cs.ie.acontextlib.hardware.StepCounter;

import static com.mikel.poseidon.ActivityTracker.ACTIVITY;
import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

/**
 * Created by mikel on 02/12/2016.
 */

public class StepService extends Service {

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    StepCounter sCounter;
    private boolean mBound;
    private boolean mCollecting = false;

    //TextView textViewSteps;
    public static final String BROADCAST_INTENT = "com.mikel.poseidon.TOTAL_STEPS";
    private Context mContext;
    private PowerManager.WakeLock mWakeLock;
    private IContextReasoner mContextService;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        StepService getService() {
            // Return this instance of LocalService so clients can call public methods

            return StepService.this;
        }
    }

    long step;

    public long getSteps(){

        // We want to avoid people trying to start the "service" more than once
        if (mCollecting) {
            return step;
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getName());
        mWakeLock.acquire();

        mContext = getApplicationContext();
        sCounter = new StepCounter(mContext);
        sCounter.start();

        sCounter.addContextReceiver(new IContextReceiver() {
            @Override
            public void newContextValue(String name, long value) {
                step += value;
                //Value.add(step);
                //Value.add(step+1);*/
                broadcastSteps();


            }

            @Override
            public void newContextValue(String name, double value) {

            }
            @Override
            public void newContextValue(String name, boolean value) {

            }

            @Override
            public void newContextValue(String name, String value) {

            }

            @Override
            public void newContextValue(String name, Object value) {

            }

            @Override
            public void newContextValues(Map<String, String> values) {

            }
        });

        Intent serviceIntent = new Intent(IContextReasoner.class.getName());

        serviceIntent = ExplicitIntentGenerator
                .createExplicitFromImplicitIntent(mContext, serviceIntent);

        if (serviceIntent != null) {
            bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        } else {
            Log.e("POSEIDON", "Context Reasoner not installed!");
        }

        //createNotification();

        return 0;

    }
    /** method for clients */


    public void broadcastSteps() {

        Intent intent = new Intent();
        try {
            intent.setAction(BROADCAST_INTENT);
            intent.putExtra("steps",step);
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("StopService", e.getMessage());
        }

    }

   /* public long setSteps(){

        if (Value.size() > 0) {

            for (int i = 0; i < Value.size(); i++) {

                step = step + Value.get(i);


            }
        } else {
            step = 0;
        }

        return step;


    }*/

   public void stopCounting (){


       //Only allow stopping if its collecting.
       if (mCollecting) {
           //sCounter.stop(); this somehow stops counting but crushes the counter
           return;
       }

       try {
           if (sCounter.isRunning()) {
               sCounter.stop();
           }
       }catch (Exception e){
           Log.e("StepCounter", e.getMessage());
       }

       Intent intent = new Intent();
       try {
           intent.setAction("org.poseidon_project.context.EXTERNAL_CONTEXT_UPDATE");
           intent.putExtra("context_name", "Activity");
           intent.putExtra("context_value_type","long");
           intent.putExtra("context_value",step);
           mContext.sendBroadcast(intent);
       } catch (Exception e) {
           Log.e("StopService", e.getMessage());
       }

       if (mBound) {
           unbindService(mConnection);
           mBound = false;
       }


       //Autodismiss the notification
       //stopForeground(true);


       try{mWakeLock.release();}
       catch(Exception e){Log.e("StepCounter", e.getMessage());}

   }

    /*@Override
    public void onDestroy() {


        super.onDestroy();
    }*/

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mContextService  = IContextReasoner.Stub.asInterface(iBinder);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mContextService = null;
            mBound = false;
        }
    };








}
