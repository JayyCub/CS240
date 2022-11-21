package com.example.fm_client_22;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import ReqRes.ResultMessage;

public class SettingsActivity extends AppCompatActivity {
    public DataCache dataCache = DataCache.getInstance();
    Button logoutButton;
    SwitchCompat lifeSwitch;
    SwitchCompat treeSwitch;
    SwitchCompat spouseSwitch;
    SwitchCompat fatherSwitch;
    SwitchCompat motherSwitch;
    SwitchCompat maleSwitch;
    SwitchCompat femaleSwitch;

    @RequiresApi(api = 33)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        logoutButton = findViewById(R.id.LogoutButton);

        lifeSwitch = findViewById(R.id.LifeSwitch);
        lifeSwitch.setChecked(dataCache.lifeSetting);

        treeSwitch = findViewById(R.id.TreeSwitch);
        treeSwitch.setChecked(dataCache.treeSetting);

        spouseSwitch = findViewById(R.id.SpouseSwitch);
        spouseSwitch.setChecked(dataCache.spouseSetting);

        fatherSwitch = findViewById(R.id.FatherSwitch);
        fatherSwitch.setChecked(dataCache.fatherSetting);

        motherSwitch = findViewById(R.id.MotherSwitch);
        motherSwitch.setChecked(dataCache.motherSetting);

        maleSwitch = findViewById(R.id.MaleSwitch);
        maleSwitch.setChecked(dataCache.maleSetting);

        femaleSwitch = findViewById(R.id.FemaleSwitch);
        femaleSwitch.setChecked(dataCache.femaleSetting);

        ConstraintLayout layout = findViewById(R.id.SettingsConstraint);

        logoutButton.setOnClickListener(view -> {
            Toast.makeText(this, "Goodbye!", Toast.LENGTH_SHORT).show();
            dataCache.recentResult = new ResultMessage(null, null, null,
                    null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, "null", false);

            dataCache.clearData();
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        lifeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.lifeSetting = isChecked;
        });
        treeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.treeSetting = isChecked;
        });
        spouseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.spouseSetting = isChecked;
        });
        fatherSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.fatherSetting = isChecked;
        });
        motherSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.motherSetting = isChecked;
        });
        maleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.maleSetting = isChecked;
        });
        femaleSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            dataCache.femaleSetting = isChecked;
        });
    }
}
