package com.mysankato.klasifikasikematanganbuahtin;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mysankato.klasifikasikematanganbuahtin.FragmentMenu.AboutFragment;
import com.mysankato.klasifikasikematanganbuahtin.FragmentMenu.HistoryFragment;
import com.mysankato.klasifikasikematanganbuahtin.FragmentMenu.HomeFragment;
import com.mysankato.klasifikasikematanganbuahtin.FragmentMenu.KlasifikasiFragment;

public class MainActivity extends AppCompatActivity{

    BottomNavigationView bottom_nav_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottom_nav_menu = findViewById(R.id.bottom_nav_menu);

        // Instance Fragment.
        HomeFragment homeFragment = new HomeFragment();
        KlasifikasiFragment klasifikasiFragment = new KlasifikasiFragment();
        HistoryFragment historyFragment = new HistoryFragment();
        AboutFragment aboutFragment = new AboutFragment();

        // Display first fragment when open app.
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        // Click Listener for bottom nav.
        bottom_nav_menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.klasifikasi:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, klasifikasiFragment).commit();
                        return true;
                    case R.id.history:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, historyFragment).commit();
                        return true;
                    case R.id.about:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, aboutFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
}