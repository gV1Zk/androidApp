package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import androidx.annotation.Nullable;

public class RegistrationActivity extends AppCompatActivity {
    private Button subBut;
    private EditText roomEdit;
    private String APP_PREFERENCES_NEW_ROOM = "room";
    private SharedPreferences mSettings;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_reg);

        String APP_PREFERENCES = "usersettings";
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        subBut = findViewById(R.id.submit_reg);
        roomEdit = findViewById(R.id.editRoom);
        TextView infoText = findViewById(R.id.infoText);
        infoText.setText("Введите имя комнаты");



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
