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
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText EmailTextField,PasswordTextField;
    private Button CreateAccountBtn,RegisterWithPhone;
    private TextView AlreadyHaveAccount;


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_register_activity);


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        initializeFields();

        AlreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToLoginActivity();
            }
        });

        RegisterWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToPhoneRegisterActivity();
            }
        });

        CreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }
        });
    }
    private void initializeFields(){
        EmailTextField = findViewById(R.id.admin_register_email);
        PasswordTextField = findViewById(R.id.admin_register_password);
        AlreadyHaveAccount = findViewById(R.id.already_have_account_link);
        CreateAccountBtn = findViewById(R.id.register_btn);
        RegisterWithPhone = findViewById(R.id.admin_option_phone);
        loadingBar = new ProgressDialog(this);
    }

    private void CreateNewAccount() {
        String email = EmailTextField.getText().toString();
        String password = PasswordTextField.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter an Email...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter an Password...", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Creating Account...");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        SendUserToLoginActivity();
                        Toast.makeText(RegisterActivity.this, "Account Created successfully", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }else{
                        String message = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser != null){
            SendUSerToMainActivity();
        }
    }

    private void SendUSerToMainActivity() {
        Intent catIntent = new Intent(RegisterActivity.this,MainActivity.class);
        catIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        catIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(catIntent);
    }

    private void SendUserToPhoneRegisterActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this,PhoneRegistrationActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }

    private void SendUserToLoginActivity() {
        Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }
}
