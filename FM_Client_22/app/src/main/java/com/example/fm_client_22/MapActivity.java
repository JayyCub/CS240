package com.example.fm_client_22;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
    public DataCache dataCache = DataCache.getInstance();

    @RequiresApi(api = 33)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }
}