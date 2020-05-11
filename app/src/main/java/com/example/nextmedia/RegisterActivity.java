package com.example.nextmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

public class RegisterActivity extends AppCompatActivity {

    private EditText EmailTextField,PasswordTextField;
    private Button CreateAccountBtn;
    private TextView AlreadyHaveAccount,RegisterWithPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register_activity);

        EmailTextField = findViewById(R.id.admin_register_email);
        PasswordTextField = findViewById(R.id.admin_register_password);
        AlreadyHaveAccount = findViewById(R.id.already_have_account_link);
        CreateAccountBtn = findViewById(R.id.register_btn);
        RegisterWithPhone = findViewById(R.id.admin_option_phone);

        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAdminToLoginActivity();
            }
        });

        RegisterWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendAdminToPhoneRegisterActivity();
            }
        });
    }

    private void SendAdminToPhoneRegisterActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this,PhoneRegistrationActivity.class);
        startActivity(loginIntent);
    }

    private void SendAdminToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(loginIntent);
    }
}
