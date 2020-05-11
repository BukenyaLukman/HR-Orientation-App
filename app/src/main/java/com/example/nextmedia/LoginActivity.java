package com.example.nextmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private EditText EditEmail,Editpassword;
    private Button LoginBtn,UsePhoneBtn;
    private TextView NeedNewAccount,ForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditEmail = findViewById(R.id.admin_email);
        Editpassword = findViewById(R.id.admin_login_password);
        LoginBtn = findViewById(R.id.login_btn);
        UsePhoneBtn = findViewById(R.id.login_phone_btn);
        NeedNewAccount = findViewById(R.id.need_new_account);
        ForgetPassword = findViewById(R.id.forget_password_link);

        UsePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPhoneLoginActivity();
            }
        });

    }

    private void SendUserToPhoneLoginActivity() {
        Intent phoneLoginIntent = new Intent(LoginActivity.this,PhoneRegistrationActivity.class);
        startActivity(phoneLoginIntent);
    }
}
