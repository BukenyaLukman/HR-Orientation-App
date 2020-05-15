package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText EditEmail,EditPassword;
    private Button LoginBtn,UsePhoneBtn;
    private TextView NeedNewAccount,ForgetPassword;
    private FirebaseUser currentUser;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        initializeFields();

        UsePhoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPhoneLoginActivity();
            }
        });

        NeedNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToRegisterActivity();
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowUserToLogin();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser != null){
            SendUserToCategoryActivity();
        }
    }

    private void AllowUserToLogin() {
        String email = EditEmail.getText().toString();
        String password = EditPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter an Email...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter an Password...", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Sign In...");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful()){
                      SendUserToCategoryActivity();
                       Toast.makeText(LoginActivity.this, "Logged in Successfully..", Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                   }else{
                       String message = task.getException().toString();
                       Toast.makeText(LoginActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                       loadingBar.dismiss();
                   }
                }
            });
        }
    }

    private void SendUserToCategoryActivity() {
        Intent catIntent = new Intent(LoginActivity.this,CategoryActivity.class);
        catIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        catIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(catIntent);
    }


    private void initializeFields(){
        EditEmail = findViewById(R.id.admin_email);
        EditPassword = findViewById(R.id.admin_login_password);
        LoginBtn = findViewById(R.id.login_btn);
        UsePhoneBtn = findViewById(R.id.login_phone_btn);
        NeedNewAccount = findViewById(R.id.need_new_account);
        ForgetPassword = findViewById(R.id.forget_password_link);
        loadingBar = new ProgressDialog(this);
    }

    private void SendUserToPhoneLoginActivity() {
        Intent phoneLoginIntent = new Intent(LoginActivity.this,PhoneRegistrationActivity.class);
        phoneLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        phoneLoginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(phoneLoginIntent);
    }
    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        registerIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(registerIntent);
    }
}
