package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneRegistrationActivity extends AppCompatActivity {
    private EditText PhoneInput,CodeInput;
    private Button SendCodeBtn,VerifyCodeBtn;
    private ProgressDialog loadingBar;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_phone_registration);

        mAuth = FirebaseAuth.getInstance();

        PhoneInput = findViewById(R.id.admin_register_phone);
        CodeInput = findViewById(R.id.admin_phone_verification);
        SendCodeBtn = findViewById(R.id.send_verification_code_btn);
        VerifyCodeBtn = findViewById(R.id.verify_btn);
        loadingBar = new ProgressDialog(this);

        SendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = PhoneInput.getText().toString();
                if(TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(PhoneRegistrationActivity.this, "Please Enter your phone number First..", Toast.LENGTH_SHORT).show();

                }else{
                    loadingBar.setTitle("Phone Verification");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,
                            60,
                            TimeUnit.SECONDS,
                            PhoneRegistrationActivity.this,
                            mCallbacks);
                }
            }
        });

        VerifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendCodeBtn.setVisibility(View.INVISIBLE);
                PhoneInput.setVisibility(View.INVISIBLE);

                String verificationCode = CodeInput.getText().toString();
                if(TextUtils.isEmpty(verificationCode)){
                    Toast.makeText(PhoneRegistrationActivity.this, "Please Enter code Sent", Toast.LENGTH_SHORT).show();
                }else{

                    loadingBar.setTitle("Code Verification");
                    loadingBar.setMessage("Please wait...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                loadingBar.dismiss();
                Toast.makeText(PhoneRegistrationActivity.this, "Invalid Phone Number.. Please Start Number with Country Code", Toast.LENGTH_SHORT).show();

                SendCodeBtn.setVisibility(View.VISIBLE);
                PhoneInput.setVisibility(View.VISIBLE);

                VerifyCodeBtn.setVisibility(View.INVISIBLE);
                CodeInput.setVisibility(View.INVISIBLE);

            }

            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                loadingBar.dismiss();
                Toast.makeText(PhoneRegistrationActivity.this, "Code has been  Sent", Toast.LENGTH_SHORT).show();
                SendCodeBtn.setVisibility(View.INVISIBLE);
                PhoneInput.setVisibility(View.INVISIBLE);

                VerifyCodeBtn.setVisibility(View.VISIBLE);
                CodeInput.setVisibility(View.VISIBLE);

            }

        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(PhoneRegistrationActivity.this, "Congratulations, Logged in successfully...", Toast.LENGTH_SHORT).show();
                            SendUserToMainActivity();
                        } else {
                            String error = task.getException().toString();
                            Toast.makeText(PhoneRegistrationActivity.this, "Error: "+ error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(PhoneRegistrationActivity.this,CategoryActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
