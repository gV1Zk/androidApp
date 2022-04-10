package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    private String room = "null";
    private String bufStrLat = "55.648100", bufStrLon = "37.652216", bufStr = null;

    private Button changeRoomBut;
    private TextView roomText;
    private TextView cordsText;

    private MapView mapView;
    private MapObjectCollection mapObjects;

    private Socket mSocket;


    private final String APP_PREFERENCES = "usersettings";
    private SharedPreferences mSettings;
    private final String APP_PREFERENCES_ROOM = "room";
    private final String APP_PREFERENCES_LATITUDE = "latitude";
    private final String APP_PREFERENCES_LONGITUDE = "longitude";


    private Emitter.Listener onNewMessage = args -> MainActivity.this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            JSONObject data = (JSONObject) args[0];
            Log.wtf("AvisActivity", "?????");
            String id;
            String button_address;
            String created_at = null;
            String status = null;
            try {
                id = data.getString("id");
                Log.wtf("id", id);
                status = data.getString("status");
                Log.wtf("mts_id", status);
                button_address = data.getString("button_address");
                Log.wtf("type", button_address);
            } catch (JSONException e) {

                return;
            }

            // add the message to view

            findPlace(id, button_address, created_at, status, "1", "1");
        }
    });


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        MapKitFactory.setApiKey("0f07d937-a358-4269-836d-33d9285feea5");
        MapKitFactory.initialize(this);
        setContentView(R.layout.activity_main);

        //SearchFactory.initialize(this);


        changeRoomBut = findViewById(R.id.buttonChangeRoom);
        roomText = findViewById(R.id.TextCurRoom);
        cordsText = findViewById(R.id.TextCoords);
        cordsText.setText("Everything all right");

        mapView = findViewById(R.id.mapView_);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(mSettings.contains(APP_PREFERENCES_ROOM)) {
            room = mSettings.getString(APP_PREFERENCES_ROOM, "ws://5.101.50.195/");
        }

        if(room.length() < 10) room = "ws://5.101.50.195/";


        try {
            mSocket = IO.socket(room);
            Log.wtf("AvisActivity", "AAAsocket");
        }
        catch (URISyntaxException ignored) {
            Log.wtf("AvisActivity", "error connecting to socket");
        }

        if(room.equals("null")) {
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        }

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


    }

    @Override
    public void onStart() {
        super.onStart();
        mSocket.disconnect();
        mSocket.off("click", onNewMessage);
        if(room.length() < 2) room = "ws://5.101.50.195/";
        try {
            mSocket = IO.socket(room);
            Log.wtf("AvisActivity", "AAAsocket");
        }
        catch (URISyntaxException ignored) {
            Log.wtf("AvisActivity", "error connecting to socket");
        }

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

        mSocket.disconnect();
        mSocket.off("click", onNewMessage);
        if(room.length() < 2) room = "ws://5.101.50.195/";
        try {
            mSocket = IO.socket(room);
            Log.wtf("AvisActivity", "AAAsocket");
        }
        catch (URISyntaxException ignored) {
            Log.wtf("AvisActivity", "error connecting to socket");
        }

        mSocket.on("click", onNewMessage);
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

        mSocket.disconnect();

    }

    private void findPlace(String id, String type, String created_at,
                            String mts_id, String lat, String lon) {

        mapObjects = mapView.getMap().getMapObjects();
        mapObjects.clear();

        mapObjects.addPlacemark(new Point(Float.parseFloat(lat), Float.parseFloat(lon)),
                ImageProvider.fromResource(MainActivity.this, R.drawable.search_result));
        if(type.equals("click")) {
            cordsText.setText(id + " trouble");
        }
        else {
            cordsText.setText("idk");
        }

        mapView.getMap()
                .move(new CameraPosition(new Point(Float.parseFloat(lat),
                                Float.parseFloat(lon)), 15.0f, 0.0f, 0.0f),
                        new Animation(Animation.Type.SMOOTH, 0), null);

    }

}