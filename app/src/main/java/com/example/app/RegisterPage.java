package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity {
    TextView txtFname,txtLname,txtEmail,txtPass,txtPass1;
    private ProgressDialog progressDialog;
    private StringRequest stringRequest;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registerpage);
        Button btnRegister;
        txtFname = findViewById(R.id.inputFirstname);
        txtLname = findViewById(R.id.inputLastname);
        txtEmail = findViewById(R.id.inputEmail);
        txtPass = findViewById(R.id.inputPass);
        txtPass1 = findViewById(R.id.inputConfirmPassword);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Information");

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateFields()){
                    return;
                }

                // Your button click listener code here
                String URL = "http://192.168.1.10/Photoshoot-Reservation/api/accounts/registration.php";

                progressDialog.show();

                stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("true".equals(status)) {
                                // Handle successful response
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Use a handler to post a message to the main UI thread for showing toast
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        // Replace userNameEditText and userPassEditText with your EditText fields
                        params.put("fname", txtFname.getText().toString());
                        params.put("lname", txtLname.getText().toString());
                        params.put("email", txtEmail.getText().toString());
                        params.put("pass", txtPass.getText().toString());
                        return params;
                    }
                };

                // Add the request to the request queue
                requestQueue.add(stringRequest);
            }
        });


        TextView textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean validateFields() {
        // Check if first name field is empty
        String pass1 = txtPass.getText().toString().trim();
        String pass2 = txtPass1.getText().toString().trim();

        if (!pass2.equals(pass1)) {
            txtPass1.setError("Password does not match");
            txtPass1.requestFocus();
            return false;
        }

        if (txtFname.getText().toString().trim().isEmpty()) {
            txtFname.setError("First name is required");
            txtFname.requestFocus();
            return false;
        }

        // Check if password field is empty
        if (txtLname.getText().toString().trim().isEmpty()) {
            txtPass.setError("Password is required");
            txtPass.requestFocus();
            return false;
        }
        if (txtEmail.getText().toString().trim().isEmpty()) {
            txtPass.setError("Password is required");
            txtPass.requestFocus();
            return false;
        }
        if (txtPass.getText().toString().trim().isEmpty()) {
            txtPass.setError("Password is required");
            txtPass.requestFocus();
            return false;
        }

        return true;
    }


    private void navigateToLoginPage() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
        finish();
    }
}