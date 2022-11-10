package com.example.fm_client_22;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

import ReqRes.LoginRequest;
import ReqRes.RegisterRequest;

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
    public DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
            char gender = 0;
            if (maleGender.isChecked()) gender = 'm';
            else if (femaleGender.isChecked()) gender = 'f';

            ServerProxy proxy = new ServerProxy(host.getText().toString(), port.getText().toString(),
                    this.dataCache);
            RegisterRequest registerRequest = new RegisterRequest(username.getText().toString(),
                    password.getText().toString(), email.getText().toString(),
                    FName.getText().toString(), LName.getText().toString(), gender);

            try {
                boolean registered = proxy.register(registerRequest);
                if (!registered) {
                    if (dataCache.recentResult.getMessage().contains("Account already exists")) {
                        Toast.makeText(getApplicationContext(), "Username already in use",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Server connection error, could not register",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // REGISTERED, PASS TO NEXT PAGE
                    Toast.makeText(getApplicationContext(), "Registered! Welcome " +
                            FName.getText().toString() + " " +
                            LName.getText().toString() + "!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Server connection error",
                        Toast.LENGTH_SHORT).show();
            }
        });

        loginButton.setOnClickListener(view -> {
            ServerProxy proxy = new ServerProxy(host.getText().toString(), port.getText().toString(),
                    dataCache);
            LoginRequest loginRequest = new LoginRequest(username.getText().toString(),
                    password.getText().toString());

            try {
                boolean result = proxy.login(loginRequest);
                if (!result) {
                    // DID NOT LOG IN
                    String message = "Could not sign in";
                    if (dataCache.recentResult.getMessage().contains("No account found for")) {
                        message = "Account not found";
                    } else if (dataCache.recentResult.getMessage().contains("Incorrect password")) {
                        message = "Incorrect password";
                    }
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    // LOGGED IN, PASS TO NEXT PAGE
                    // GET USER'S FULL NAME
                    proxy.getPersonName();
                    Toast.makeText(getApplicationContext(), "Welcome " +
                            dataCache.currentPerson.getFirstName() + " " +
                            dataCache.currentPerson.getLastName() + "!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MapActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Server connection error",
                        Toast.LENGTH_SHORT).show();
            }
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