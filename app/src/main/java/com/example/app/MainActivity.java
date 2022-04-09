package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

import java.net.URISyntaxException;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {
    private String room = "null";
    private String bufStrLat = "55.648100", bufStrLon = "37.652216";

    private Button changeRoomBut;
    private TextView roomText;

    private MapView mapView;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://chat.socket.io");
        }
        catch (URISyntaxException ignored) {}
    }

    private final String APP_PREFERENCES = "usersettings";
    private SharedPreferences mSettings;
    private final String APP_PREFERENCES_ROOM = "room";
    private final String APP_PREFERENCES_LATITUDE = "latitude";
    private final String APP_PREFERENCES_LONGITUDE = "longitude";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        MapKitFactory.setApiKey("0f07d937-a358-4269-836d-33d9285feea5");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        //SearchFactory.initialize(this);


        changeRoomBut = findViewById(R.id.buttonChangeRoom);
        TextView coordsText = findViewById(R.id.TextCoords);
        roomText = findViewById(R.id.TextCurRoom);
        coordsText.setText("Everything all right");

        mapView = findViewById(R.id.mapView_);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        if(mSettings.contains(APP_PREFERENCES_LATITUDE)) {
            bufStrLat = mSettings.getString(APP_PREFERENCES_LATITUDE, "55.648100");
        }
        if(mSettings.contains(APP_PREFERENCES_LONGITUDE)) {
            bufStrLon = mSettings.getString(APP_PREFERENCES_LONGITUDE, "37.652216");
        }

        mapView.getMap()
                .move(new CameraPosition(new Point(Float.parseFloat(bufStrLat),
                                Float.parseFloat(bufStrLon)), 15.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0), null);

        //mapView.getMap().addCameraListener(Context);

        if(mSettings.contains(APP_PREFERENCES_ROOM)) {
            room = mSettings.getString(APP_PREFERENCES_ROOM, "null");
        }

        if(room.equals("null")) {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_ROOM)) {
            room = mSettings.getString(APP_PREFERENCES_ROOM, "null");
        }

        roomText.setText(room);

        changeRoomBut.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        mSocket.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        String latitude = String.valueOf(mapView.getMap().getCameraPosition()
                                                                        .getTarget().getLatitude()),
                longitude = String.valueOf(mapView.getMap().getCameraPosition()
                        .getTarget().getLongitude());

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_LATITUDE, latitude);
        editor.putString(APP_PREFERENCES_LONGITUDE, longitude);
        editor.apply();

    }
}