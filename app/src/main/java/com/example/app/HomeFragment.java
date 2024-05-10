package com.example.app;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView txtFullname = view.findViewById(R.id.txtFullname); // Corrected
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext()); // Changed to requireContext()
        int userId = preferences.getInt("userid", -1);
        String userFirstname = preferences.getString("userFirstname", "");
        String userLastname = preferences.getString("userLastname", "");
        txtFullname.setText(userFirstname + " " + userLastname);

        return view;
    }

}