package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Forgotpasspage extends AppCompatActivity {

    Button btnBackLogin, btnSendcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgotpasspage);

        btnBackLogin = findViewById(R.id.btnBackLogin);
        btnSendcode = findViewById(R.id.btnSendcode);

        btnBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgotpasspage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });

        btnSendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgotpasspage.this, Recoverypage.class);
                startActivity(intent);
                finish();
            }
        });
    }
}