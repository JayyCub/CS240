package com.example.fm_client_22;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if(fragment == null) {
            MapFragment mapFragment = new MapFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragmentContainer, mapFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.menuSearch:
                //Toast.makeText(this, "SEARCH BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

}