package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {
    private String room = "null";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://chat.socket.io");
        }
        catch (URISyntaxException ignored) {}
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String APP_PREFERENCES = "usersettings";
        SharedPreferences mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        String APP_PREFERENCES_ROOM = "room";
        if(mSettings.contains(APP_PREFERENCES_ROOM)) {
            room = mSettings.getString(APP_PREFERENCES_ROOM, "null");
        }

        if(room.equals("null")) {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

        mSocket.connect();


    }
}