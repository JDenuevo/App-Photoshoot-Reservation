package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Recoverypage extends AppCompatActivity {

    Button btnBackForgot, btnRecoverycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recoverypage);

        btnBackForgot = findViewById(R.id.btnBackForgot);
        btnRecoverycode = findViewById(R.id.btnRecoverycode);

        btnBackForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recoverypage.this, Forgotpasspage.class);
                startActivity(intent);
                finish();
            }
        });

        btnRecoverycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recoverypage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });


    }
}