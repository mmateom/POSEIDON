package com.mikel.poseidon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;



import static com.mikel.poseidon.SetGraphLimits.sharedPrefs;

public class ActivityTracker extends AppCompatActivity {


    public static final String ACTIVITY = "ACTIVITY";

    SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        //callback to home button
        ImageButton home_butt = (ImageButton) findViewById(R.id.homebutton);
        home_butt.setOnClickListener(view -> {

            Intent home_intent4 = new Intent(ActivityTracker.this, MainActivity.class);
            startActivity(home_intent4);
        });

        //

        preferences = getSharedPreferences(sharedPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        //

        // WALK
        TextView walkText = (TextView) findViewById(R.id.walk);
        walkText.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent walkIntent = new Intent(ActivityTracker.this, Steps.class);
                                            editor.putString(ACTIVITY, "walk");
                                            editor.apply();
                                            startActivity(walkIntent);
                                        }
                                    });



        // RUN
        TextView runText = (TextView) findViewById(R.id.run);
        runText.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent runIntent = new Intent(ActivityTracker.this, Steps.class);
                                           editor.putString(ACTIVITY, "run");
                                           editor.commit();
                                           startActivity(runIntent);
                                       }
                                   }

        );

        // SWIM
        TextView swimText = (TextView) findViewById(R.id.swim);
        swimText.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent swimIntent = new Intent(ActivityTracker.this, Steps.class);
                                            editor.putString(ACTIVITY, "swim");
                                            editor.commit();
                                            startActivity(swimIntent);
                                        }
                                    }

        );

        // DANCE
        TextView danceText = (TextView) findViewById(R.id.dance);
        danceText.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent danceIntent = new Intent(ActivityTracker.this, Steps.class);
                                             editor.putString(ACTIVITY, "dance");
                                             editor.commit();
                                             startActivity(danceIntent);
                                         }
                                     }

        );

        // D

        TextView current = (TextView) findViewById(R.id.current_activity);
        current.setVisibility(View.INVISIBLE);





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
