package com.example.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class RegistrationActivity extends AppCompatActivity {
    private Button subBut;
    private EditText roomEdit;
    private TextView infoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg);
        subBut = findViewById(R.id.submit_reg);
        roomEdit = findViewById(R.id.editRoom);
        infoText = findViewById(R.id.textView);

    }
}
