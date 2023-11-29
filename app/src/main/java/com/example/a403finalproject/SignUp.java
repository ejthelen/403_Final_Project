package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    //Regex expression to validation password. Must have atleast 1 lowercase letter, 1 uppercase, and atleast 1 digit
    // and must be a atleast 8 chraacters long
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d).{8,}$");
    EditText etFirstName, etLastName, etEmail, etPhone, etCity, etState, etAddress, etPassword;
    Button btnSignUpForeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Call the findById method for each EditText
        etFirstName = findViewById(R.id.etFirst);
        etLastName = findViewById(R.id.etLast);
        etEmail = findViewById(R.id.etSignUpEmail);
        etPhone = findViewById(R.id.etSignUpPhoneNumber);
        etCity = findViewById(R.id.etCity);
        etState = findViewById(R.id.etState);
        etAddress = findViewById(R.id.etStreetAddy);
        etPassword = findViewById(R.id.etPasswordSignUp);
        btnSignUpForeal = findViewById(R.id.btnSignUpForeal);


        btnSignUpForeal.setOnClickListener((e -> {
            validateEmail();
            validatePassword();
        }));


    }

    //validates email address based off the regex expression in the Patterns class
    public boolean validateEmail() {
        String emailInput = etEmail.getText().toString().trim();
        if (emailInput.isEmpty()) {
            etEmail.setError("Field cant be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            etEmail.setError("Enter a valid email address");
            return false;
        } else {
            etEmail.setError(null);
            return false;
        }
    }

    //validates password based off PASSWORD_PATTERN regex
    public boolean validatePassword() {
        String password = etPassword.getText().toString().trim();
        System.out.println("Password: " + password); // Check the value

        if (password.isEmpty()) {
            etPassword.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            etPassword.setError("Password is too weak");
            return false;
        } else {
            etPassword.setError(null);
            return true;
        }
    }
//    public boolean validateAddress(){
//
//    }
//    public boolean validateCity(){
//
//    }
//    public boolean validateState (){
//
//    }

}