package com.example.fm_client_22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private EditText host;
    private EditText port;
    private EditText username;
    private EditText password;
    private EditText FName;
    private EditText LName;
    private EditText email;
    private RadioButton maleGender;
    private RadioButton femaleGender;
    private Button registerButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        ConstraintLayout layout = findViewById(R.id.login_page_layout);
        host = findViewById(R.id.inputHost);
        port = findViewById(R.id.inputPort);
        username = findViewById(R.id.inputUsername);
        password = findViewById(R.id.inputPass);
        FName = findViewById(R.id.inputFName);
        LName = findViewById(R.id.inputLName);
        email = findViewById(R.id.inputEmail);
        maleGender = findViewById(R.id.radio_male);
        femaleGender = findViewById(R.id.radio_female);
        registerButton = findViewById(R.id.register);
        loginButton = findViewById(R.id.login);
        registerButton.setEnabled(false);
        loginButton.setEnabled(false);


        layout.setOnClickListener(view -> allFieldsValid());

        for (EditText editText : Arrays.asList(host, port, username, password, FName, LName, email)) {
            editText.setOnKeyListener((view, i, keyEvent) -> {
                allFieldsValid();
                return false;
            });
        }
        for (RadioButton radioButton : Arrays.asList(maleGender, femaleGender)) {
            radioButton.setOnClickListener(view -> allFieldsValid());
        }



        registerButton.setOnClickListener(view -> {
            String temp = "Please Select a Gender";
            if (maleGender.isChecked()) temp = "Male";
            else if (femaleGender.isChecked()) temp = "Female";
            if (host.getText().toString().equals("") || port.getText().toString().equals("") ||
                    username.getText().toString().equals("") || password.getText().toString().equals("") ||
                    FName.getText().toString().equals("") || LName.getText().toString().equals("") ||
                    email.getText().toString().equals("")) {
                temp = "Required field(s) empty.\nPlease try again.";
            }


            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
        });

        loginButton.setOnClickListener(view -> {
            String temp = "Logging in...";

            if (host.getText().toString().equals("") || port.getText().toString().equals("") ||
                    username.getText().toString().equals("") || password.getText().toString().equals("")) {
                temp = "Required field(s) empty.\nPlease try again.";
            }
            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
        });

    }

    public void allFieldsValid(){
        loginButton.setEnabled(!host.getText().toString().equals("") && !port.getText().toString().equals("") &&
                !username.getText().toString().equals("") && !password.getText().toString().equals(""));
        registerButton.setEnabled(!host.getText().toString().equals("") && !port.getText().toString().equals("") &&
                !username.getText().toString().equals("") && !password.getText().toString().equals("") &&
                !FName.getText().toString().equals("") && !LName.getText().toString().equals("") &&
                !email.getText().toString().equals("") && (maleGender.isChecked() || femaleGender.isChecked()));
    }
}