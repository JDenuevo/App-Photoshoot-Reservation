package com.example.app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ReserveFragment extends Fragment {
    private Button btnDatepicker;
    private TextView txtDateReserve;
    private ProgressDialog progressDialog;
    private Spinner time_spinner, typepackage_spinner;
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

        btnDatepicker = view.findViewById(R.id.btnDatepicker);
        txtDateReserve = view.findViewById(R.id.txtDateReserve);
        time_spinner = view.findViewById(R.id.time_spinner);
        typepackage_spinner = view.findViewById(R.id.typepackage_spinner);

        getPackage();
        setupTimeSpinner();

        btnDatepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(); // Open date picker dialog
            }
        });


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

    //fetch package from api
    private void getPackage() {
        String URL = "http://192.168.1.12/Photoshoot-Reservation/api/packages/get.php";

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
    private void openDatePicker() {
        // Get the current date using Calendar
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Show the picked value in the TextView
                String formattedDate = String.format(Locale.getDefault(), "%1$tB %1$td, %1$tY", new Date(year - 1900, month, day));
                txtDateReserve.setText(formattedDate);

            }
        }, currentYear, currentMonth, currentDay);

        datePickerDialog.show();
    }

    private void setupTimeSpinner() {
        String[] addTime = {
                "11:00 AM", "11:30 AM", "12:00 PM", "12:30 PM",
                "1:00 PM", "1:30 PM", "2:00 PM", "2:30 PM",
                "3:00 PM", "3:30 PM", "4:00 PM", "4:30 PM",
                "5:00 PM", "5:30 PM", "6:00 PM", "6:30 PM",
                "7:00 PM", "7:30 PM", "8:00 PM", "8:30 PM"
        };

        String[] addTimeValues = {
                "11:00:00", "11:30:00", "12:00:00", "12:30:00",
                "13:00:00", "13:30:00", "14:00:00", "14:30:00",
                "15:00:00", "15:30:00", "16:00:00", "16:30:00",
                "17:00:00", "17:30:00", "18:00:00", "18:30:00",
                "19:00:00", "19:30:00", "20:00:00", "20:30:00"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                addTime
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time_spinner.setAdapter(adapter);

        time_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedValue = addTimeValues[position];
                // Use the selectedValue (24-hour format) as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle when nothing is selected
            }
        });
    }

}