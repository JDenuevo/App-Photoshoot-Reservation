package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {
    TextView txtFname,txtLname,txtEmail;
    private ProgressDialog progressDialog;
    private StringRequest stringRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Button btnSaveProfile;
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Sending Information");

        txtFname = view.findViewById(R.id.inputEditFname);
        txtLname = view.findViewById(R.id.inputEditLname);
        txtEmail = view.findViewById(R.id.inputEditEmail);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext()); // Changed to requireContext()
        String userFirstname = preferences.getString("userFirstname", "");
        String userLastname = preferences.getString("userLastname", "");
        String userEmail = preferences.getString("userEmail", "");
        int userid = preferences.getInt("userid", -1);

        txtFname.setText(userFirstname);
        txtLname.setText(userLastname);
        txtEmail.setText(userEmail);

        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your button click listener code here
                String URL = "http://192.168.1.10/Photoshoot-Reservation/api/accounts/updateProfile.php";

                progressDialog.show();

                stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if ("true".equals(status)) {
                                // Handle successful response
                                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Use a handler to post a message to the main UI thread for showing toast
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireContext(), "Error parsing JSON response", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        // Replace userNameEditText and userPassEditText with your EditText fields
                        params.put("firstname", txtFname.getText().toString());
                        params.put("lastname", txtLname.getText().toString());
                        params.put("email", txtEmail.getText().toString());
                        params.put("userid", String.valueOf(userid));
                        return params;
                    }
                };

                // Add the request to the request queue
                requestQueue.add(stringRequest);
            }
        });
        return view;
    }
}