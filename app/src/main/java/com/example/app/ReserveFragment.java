package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReserveFragment extends Fragment {
    private ProgressDialog progressDialog;
    private Spinner typepackage_spinner;
    private StringRequest stringRequest;
    private RequestQueue requestQueue;
    private int packageid ;
    public class Package {
        private int packageID;
        private String packageName;

        public Package(int packageID, String packageName) {
            this.packageID = packageID;
            this.packageName = packageName;
        }

        public int getPackageID() {
            return packageID;
        }

        public String getPackageName() {
            return packageName;
        }

        @Override
        public String toString() {
            return packageName; // Display the packageName in the spinner
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserve, container, false);

        // Initialize ProgressDialog and RequestQueue
        progressDialog = new ProgressDialog(requireContext());
        requestQueue = Volley.newRequestQueue(requireContext());

        typepackage_spinner = view.findViewById(R.id.typepackage_spinner);

        // Setup spinners and listeners
        setupMainPackageSpinner();
        getPackage();
        setupAddTimeSpinner();



        // Set up the OnItemSelectedListener for typepackage_spinner
        typepackage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Get the selected Package object from the spinner adapter
                Package selectedPackage = (Package) adapterView.getItemAtPosition(position);


                packageid = selectedPackage.getPackageID();
                // Display the selected PackageName in a Toast
                Toast.makeText(requireContext(), "Selected Package: " + packageid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case where nothing is selected (if needed)
            }
        });

        return view;
    }

    private void setupAddTimeSpinner() {
        String[] addTime = {
                "0 minutes", "10 minutes", "20 minutes",
                "30 minutes", "40 minutes",
                "50 minutes", "60 minutes/1 hour"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                addTime
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //addtionaltime_spinner.setAdapter(adapter);
    }

    private void setupMainPackageSpinner() {
        String[] mainPackage = {
                "Digital Package", "Digital and Print Package"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                mainPackage
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //mainpackage_spinner.setAdapter(adapter);
    }

    private void getPackage() {
        String URL = "http://192.168.1.6/Photoshoot-Reservation/api/packages/get.php";

        progressDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                parsePackageResponse(response);
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
                params.put("packageID", "");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void parsePackageResponse(String response) {
        ArrayList<Package> packages = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            boolean status = jsonResponse.getBoolean("status");

            if (status) {
                JSONArray packagesArray = jsonResponse.getJSONArray("packages");
                for (int i = 0; i < packagesArray.length(); i++) {
                    JSONObject packageObject = packagesArray.getJSONObject(i);
                    int packageID = packageObject.getInt("PackageID");
                    String packageName = packageObject.getString("PackageName");
                    packages.add(new Package(packageID, packageName));
                }

                ArrayAdapter<Package> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        packages
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                typepackage_spinner.setAdapter(adapter);
            } else {
                // Handle the case where there are no packages available
                Toast.makeText(requireContext(), "No packages available", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error parsing JSON response", Toast.LENGTH_LONG).show();
        }
    }
}