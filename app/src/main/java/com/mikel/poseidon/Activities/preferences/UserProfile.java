package com.mikel.poseidon.Activities.preferences;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikel.poseidon.Model.Profile;
import com.mikel.poseidon.Model.DBHelper;
import com.mikel.poseidon.Activities.Menu;
import com.mikel.poseidon.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;
import static com.mikel.poseidon.R.id.weight1;

public class UserProfile extends AppCompatActivity  implements NumberPicker.OnValueChangeListener{

    private TextView age,height,gender;
    private EditText weight;
    private Button savebtn;
    Context mContext;
    Spinner spinner, spinnerUnits;

    SharedPreferences mSharedPrefs;
    public String age_key = "age_key";
    public String height_key = "height_key";
    public String gender_key = "gender_key";
    public String actLevelKey = "actLevelKey";
    public String levelint = "levelint";
    public String weightUnits = "weightUnits";
    TextView unit;

    int position;

    Profile profile;



    //numberpicker


    //TODO: NO VOY A GUARDAR EL PESO EN SHAREDPrefs porque para hacer cálculos siempre necesitaré
    //el último peso

    DBHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //callback to home button
        ImageButton home_button1 = (ImageButton) findViewById(R.id.homebutton);
        home_button1.setOnClickListener(view -> {
            Intent home_intent = new Intent(UserProfile.this, Menu.class);
            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(home_intent);
        });

        //mContext = getApplicationContext();

        //text views
        age = (TextView)findViewById(R.id.profile_age);
        height = (TextView)findViewById(R.id.profile_height);
        gender = (TextView)findViewById(R.id.profile_gender);
        weight = (EditText) findViewById(R.id.profile_weight);
        savebtn = (Button)findViewById(R.id.save);


        //set listeners
        age.setOnClickListener(view -> showAgeDialog());
        height.setOnClickListener(view -> showHeightDialog());
        gender.setOnClickListener(view -> showGenderDialog());
        savebtn.setOnClickListener(view -> onSaveButtonClick());

        //create DB instance
        myDB = new DBHelper(this);
        //profile instance
        profile = new Profile(this);

        int p = profile.getUnits();
        if(p == 1){
            unit = (TextView)findViewById(weight1);
            unit.setText("lbs");}

        //spinner
        spinner = (Spinner) findViewById(R.id.spinner);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_level, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new ActivityLevelSpinnerClass());

        /** asdfasdfasdfasdfasdfasdfasdfasdfasdf */
        //spinner units
        spinnerUnits = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.weight_unit, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerUnits.setAdapter(adapter2);
        spinnerUnits.setOnItemSelectedListener(new UnitsSpinnerClass());

    }




    //===========================
    // NUMBERPICKER METHOD
    //===========================

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        Log.i("value is",""+newVal);
//        age.setText(newVal);

    }



    public void showAgeDialog()
    {
        final Dialog d = new Dialog(UserProfile.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);

        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100); // max value 100
        np.setMinValue(5); // min value 0
        np.setValue(20);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);

       // v=np.getValue();

        set.setOnClickListener(v -> {
            age.setText(String.valueOf(np.getValue())); //set the value to textview
            d.dismiss();
        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();


    }
    //
    public void showHeightDialog()
    {

        final Dialog d = new Dialog(UserProfile.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);
        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);
        final NumberPicker np2 = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np2.setMaxValue(210); // max value 100
        np2.setMinValue(90); // min value 0
        np2.setValue(170);
        np2.setWrapSelectorWheel(false);
        np2.setOnValueChangedListener(this);

        set.setOnClickListener(v -> {
            height.setText(String.valueOf(np2.getValue())); //set the value to textview
            d.dismiss();
        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();


    }
    //
    public void showGenderDialog()
    {

        final Dialog d = new Dialog(UserProfile.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.np_dialog);
        Button set = (Button) d.findViewById(R.id.set_action);
        Button cancel = (Button) d.findViewById(R.id.cancel_action);
        final NumberPicker np3 = (NumberPicker) d.findViewById(R.id.numberPicker1);

        final String[] values= {"Male", "Female"};

        //Populate NumberPicker values from String array values
        //Set the minimum value of NumberPicker

        np3.setMinValue(0); //from array first value

        //Specify the maximum value/number of NumberPicker
        np3.setMaxValue(values.length-1); //to array last value

        //Specify the NumberPicker data source as array elements
        np3.setDisplayedValues(values);

        np3.setWrapSelectorWheel(false);
        np3.setOnValueChangedListener(this);

        set.setOnClickListener(v -> {
            gender.setText(String.valueOf(values[np3.getValue()])); //set the value to textview
            d.dismiss();
        });
        cancel.setOnClickListener(v -> {
            d.dismiss(); // dismiss the dialog
        });
        d.show();


    }

    //===========================
    // Save button
    //===========================

    public void onSaveButtonClick()
    {

        ////Saving values in sharedprefs

        //if x.getText is not empty then save it in sharedprefs
        if (!"".equals(age.getText().toString())) {

           profile.setAge(Integer.parseInt(age.getText().toString()));
        }

        if (!"".equals(height.getText().toString())) {

            profile.setHeight(Integer.parseInt(height.getText().toString()));
        }

        if (!"".equals(gender.getText().toString())) {

            profile.setGender(gender.getText().toString());
        }

        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();



        //Add weight to DB

        //---------------------------------------------
        //if weight is not null, parse it to double
        if (!"".equals(weight.getText().toString())) {
            double yourWeight = Double.parseDouble(weight.getText().toString());

            //if yourWeight is not empty (has not default value), save to DB
            if (yourWeight != 0.0d) {

                myDB.addData(age.getText().toString(), gender.getText().toString(),yourWeight, getCurrentDate());
            }
            else { Log.e(LOG_TAG, String.valueOf(yourWeight));}

        }
        //---------------------------------------------


    }


    //=========================================
    //             GET CURRENT TIME
    //=========================================
   public String getCurrentDate() {

        Calendar cal = Calendar.getInstance();

        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH) +1; //plus 1, because months start on 0 in java
        int year = cal.get(Calendar.YEAR);

        String date_final = null;


        String current_start_time = year + "-" + month + "-" + day;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {

            Date date_f = formatter.parse(current_start_time);
            date_final = formatter.format(date_f);

            //System.out.println(date_f);
            //System.out.println(date_final);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return date_final;
    }

    @Override
    protected void onResume() {
        super.onResume();



        int mAge = profile.getAge();
        age.setText(String.valueOf(mAge));

        int mHeight = profile.getHeight();
        height.setText(String.valueOf(mHeight));

        String mGender = profile.getGender();
        gender.setText(String.valueOf(mGender));


        int mPosition = profile.getLevel();
        spinner.setSelection(mPosition);

        int units = profile.getUnits();
        spinnerUnits.setSelection(units);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        /*mSharedPrefs = this.getSharedPreferences(SetGraphLimits.sharedPrefs, MODE_PRIVATE);

        int mAge = mSharedPrefs.getInt(age_key,0);
        age.setText(String.valueOf(mAge));

        int mHeight = mSharedPrefs.getInt(height_key,0);
        height.setText(String.valueOf(mHeight));

        String mGender = mSharedPrefs.getString(gender_key,"Male");
        gender.setText(String.valueOf(mGender));*/
    }


    //SPINNER CLASSES
    private class ActivityLevelSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String selection = parent.getItemAtPosition(position).toString();
            System.out.println(selection);

            profile.setLevel(position);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    public void showActivityInformation(View view){

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.activ_level_info, null);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.activity_user_profile);

        PopupWindow popupWindow = new PopupWindow(container, dpToPx(360), dpToPx(470), true); //true allows us to close window by tapping outside
        popupWindow.showAtLocation(relativeLayout, Gravity.NO_GRAVITY, dpToPx(0), dpToPx(120));

        //shut popup outside window
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                popupWindow.dismiss();
                return false;
            }
        });

    }
    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private class UnitsSpinnerClass implements AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
        {
            String selection = parent.getItemAtPosition(position).toString();
            profile.setUnits(position);
            System.out.println(selection);

            TextView unitasd = (TextView)findViewById(weight1);
            String value = (String) parent.getItemAtPosition(position);
            unitasd.setText(value);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }


}


