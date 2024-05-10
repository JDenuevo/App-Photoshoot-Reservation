package com.example.app;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {
    private GoogleSignInClient mGoogleSignInClient;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private EditText userNameEditText, userPassEditText;
    private ProgressDialog progressDialog;
    TextView textView;
    private static final int RC_SIGN_IN = 9001;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginpage);
        requestQueue = Volley.newRequestQueue(this);
            progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending Information");

        userNameEditText = findViewById(R.id.inputEmail);
        userPassEditText = findViewById(R.id.inputPass);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1005147137971-rvgetd9gacvg7ahsc7ru484l3g01c5rn.apps.googleusercontent.com")
                .requestEmail()
                .build();

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://192.168.1.12/Photoshoot-Reservation/api/accounts/login.php";

                progressDialog.show();

                stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        // Assuming the response is JSON, you can parse it here
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("true".equals(status)) { // Use .equals() for string comparison

                                int userid = jsonResponse.getInt("userid");
                                String userFirstname = jsonResponse.getString("userFirstname");
                                String userLastname = jsonResponse.getString("userLastname");
                                String userEmail = jsonResponse.getString("email");
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("userid", userid);
                                editor.putString("userFirstname", userFirstname);
                                editor.putString("userLastname", userLastname);
                                editor.putString("userEmail", userEmail);
                                editor.apply();

                                Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Error parsing JSON response", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", userNameEditText.getText().toString());
                        params.put("password", userPassEditText.getText().toString());
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
            }

        });


        TextView textViewRegister = findViewById(R.id.textViewRegister);
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(intent);
                finish();
            }
        });

        TextView textforgotPass = findViewById(R.id.textforgotPass);
        textforgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, Forgotpasspage.class);
                startActivity(intent);
                finish();
            }
        });

        LinearLayout btnGoogle = findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize Google Sign-In options
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso
                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

                // Get the sign-in intent
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();

                // Start the sign-in flow
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with your backend server
                GoogleSignInAccount account = task.getResult(ApiException.class);
                 if (account != null) {
                     // Signed in successfully, show authenticated UI.
                     String email = account.getEmail();
                     String displayName = account.getDisplayName();
                     String[] names = displayName.split(" "); // Split the display name by space
                     String firstName = "";
                     String lastName = "";

                     if (names.length >= 2) {
                         firstName = names[0];
                         lastName = names[names.length - 1];
                     } else if (names.length == 1) {
                         firstName = names[0];
                     }

                     //success
                     String URL = "http://192.168.1.12/Photoshoot-Reservation/api/accounts/loginGoogle.php";

                     progressDialog.show();


                     String finalFirstName = firstName;
                     String finalLastName = lastName;
                     stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             progressDialog.dismiss();
                             // Assuming the response is JSON, you can parse it here
                             try {
                                 JSONObject jsonResponse = new JSONObject(response);

                                 String status = jsonResponse.getString("status");
                                 String message = jsonResponse.getString("message");

                                 if ("true".equals(status)) { // Use .equals() for string comparison

                                     int userid = jsonResponse.getInt("userid");

                                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                     SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                     SharedPreferences.Editor editor = preferences.edit();
                                     editor.putInt("userid", userid);
                                     editor.putString("userFirstname", finalFirstName);
                                     editor.putString("userLastname", finalLastName);
                                     editor.putString("userEmail", email);
                                     editor.apply();

                                     Intent intent = new Intent(LoginPage.this, MainActivity.class);
                                     startActivity(intent);
                                     finish();
                                 }else{
                                     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                 }

                             } catch (JSONException e) {
                                 e.printStackTrace();
                                 Toast.makeText(getApplicationContext(), "Error parsing JSON response", Toast.LENGTH_LONG).show();
                             }
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             progressDialog.dismiss();
                             Toast.makeText(getApplicationContext(),  error.getMessage(), Toast.LENGTH_LONG).show();
                         }
                     }) {
                         @Override
                         protected Map<String, String> getParams() {
                             Map<String, String> params = new HashMap<>();
                             params.put("email", email);
                             params.put("firstname", finalFirstName);
                             params.put("lastname", finalLastName);
                             return params;
                         }
                     };

                     requestQueue.add(stringRequest);

                 } else {
                    // Signed out, show unauthenticated UI.
                    Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                }
            }catch (ApiException e) {
                // Google Sign-In failed, handle the error
                int statusCode = e.getStatusCode();
                Log.e(TAG, "Google sign in failed with status code: " + statusCode);
                Toast.makeText(this, "Google sign in failed: " + statusCode, Toast.LENGTH_SHORT).show();
            }
        }
    }



}
