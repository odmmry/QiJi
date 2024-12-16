package com.example.myapplication.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.fragment.HomeFragment;
import com.example.myapplication.fragment.NotificationFragment;
import com.example.myapplication.fragment.ProfileFragment;
import com.example.myapplication.fragment.RideFragment;
import com.example.myapplication.fragment.ShoppingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TabbarActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_shopping) {
                selectedFragment = new ShoppingFragment();
            } else if (item.getItemId() == R.id.nav_ride) {
                selectedFragment =  new RideFragment();
            } else if (item.getItemId() == R.id.nav_notification) {
                selectedFragment =  new NotificationFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                selectedFragment =  new ProfileFragment();
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }

            return true;
        });

        replaceFragment(new HomeFragment());
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}
