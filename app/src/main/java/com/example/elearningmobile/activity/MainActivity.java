package com.example.elearningmobile.activity;

import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.elearningmobile.R;
import com.example.elearningmobile.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment(this))
                .commit();
        setControl();
        setEvent() ;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int fragment = extras.getInt("fragment");
            if (fragment == R.id.nav_cart) {
                redirectCartFragment();
                bottom_navigation.setSelectedItemId(R.id.nav_cart);
            }else if (fragment == R.id.nav_profile){
                redirectProfileFragment();
                bottom_navigation.setSelectedItemId(R.id.nav_profile);
            }
        }

    }

    private void setEvent() {
        bottom_navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_home){
                    redirectHomeFragment();
                    return true;
                }

                if(item.getItemId() == R.id.nav_cart){
                    redirectCartFragment();
                    return true;
                }

                if(item.getItemId() == R.id.nav_profile) {
                    redirectProfileFragment();
                    return true;
                }
                return false;
            }
        });
    }

    private void setControl() {
        bottom_navigation = findViewById(R.id.bottom_navigation);
    }

    private void redirectHomeFragment() {
        Fragment fragment  = new HomeFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    private void redirectCartFragment() {
        Fragment fragment  = new HomeFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }

    private void redirectProfileFragment() {
        Fragment fragment  = new HomeFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
    }


}