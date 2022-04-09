package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class RegistrationActivity extends AppCompatActivity {
    private Button subBut;
    private EditText roomEdit;
    private TextView infoText;
    private String APP_PREFERENCES = "usersettings";
    private String APP_PREFERENCES_NEW_ROOM = "room";
    private SharedPreferences mSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        subBut = findViewById(R.id.submit_reg);
        roomEdit = findViewById(R.id.editRoom);
        infoText = findViewById(R.id.textView);



    }

    @Override
    public void onStart() {
        super.onStart();
        subBut.setOnClickListener(v -> {
            String bufStr = roomEdit.getText().toString();
            if(bufStr.length() < 5) {
                Toast.makeText(this, "input error",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_NEW_ROOM, bufStr);
                //Log.wtf("OMG ",bufStr);
                //Log.wtf("OMG ", mSettings.getString(APP_PREFERENCES_NEW_ROOM, "Null"));
                editor.apply();
                finish();
            }
        });
    }
}
