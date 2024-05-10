package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout; // Update import statement
import androidx.constraintlayout.widget.ConstraintLayout; // Update import statement

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConstraintLayout mainContainer = findViewById(R.id.main_container); // Changed to main_container
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();





        // Add this code to replace the fragment with HomeFragment by default
        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            Bundle args = new Bundle();
            homeFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.navigation_home); // Optional: Set the checked item in the navigation drawer
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (!item.isChecked()) {
            item.setChecked(true);
        }

        Bundle args;

        if (id == R.id.navigation_home) {
            HomeFragment homeFragment = new HomeFragment();
            args = new Bundle();
            homeFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        } else if (id == R.id.navigation_payment) {
            PaymentFragment paymentFragment = new PaymentFragment();
            args = new Bundle();
            paymentFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, paymentFragment).commit();
        } else if (id == R.id.navigation_reserve) {
            ReserveFragment reserveFragment = new ReserveFragment();
            args = new Bundle();
            reserveFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, reserveFragment).commit();
        } else if (id == R.id.navigation_rate) {
            RateFragment rateFragment = new RateFragment();
            args = new Bundle();
            rateFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, rateFragment).commit();
        } else if (id == R.id.navigation_profile) {
            ProfileFragment profileFragment = new ProfileFragment();
            args = new Bundle();
            profileFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginPage.class); // Replace LoginActivity.class with the actual class name for your login activity
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
