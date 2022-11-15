package com.example.fm_client_22;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    public DataCache dataCache = DataCache.getInstance();

    @RequiresApi(api = 33)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        // GET ALL Needed data, save to DataCache
        ServerProxy proxy = new ServerProxy(dataCache.serverHost, dataCache.port);
        proxy.getAllPersonData();
        setContentView(R.layout.main_map);

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
                //Toast.makeText(this, "SETTINGS BUTTON CLICKED", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}

// HANDLE BACK BUTTON TO LOGIN PAGE