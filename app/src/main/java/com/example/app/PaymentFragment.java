package com.example.app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PaymentFragment extends Fragment {

    private Spinner paypackage_spinner, payoptions_spinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        paypackage_spinner = view.findViewById(R.id.paypackage_spinner);
        payoptions_spinner = view.findViewById(R.id.payoptions_spinner);

        setupPaypack();
        setupPayoption();

        return view;
    }

    private void setupPayoption() {
        String[] payoptions = {
                "Down payment", "Full payment"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                payoptions
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        payoptions_spinner.setAdapter(adapter);
    }

    private void setupPaypack() {
        String[] paypack = {
                "Package A", "Package B", "Package C", "Package D"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                paypack
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paypackage_spinner.setAdapter(adapter);
    }
}