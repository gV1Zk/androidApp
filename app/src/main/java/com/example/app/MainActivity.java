package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {
    private static String APP_PREFERENCES_ROOM = "room";
    private SharedPreferences mSettings;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://chat.socket.io");
        }
        catch (URISyntaxException e) {}
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.connect();
    }
}