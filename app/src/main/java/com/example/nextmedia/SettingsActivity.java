package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private Button SendBtn,GoToTest;
    private CircleImageView ProfileImage;
    private EditText FullName,Department;
    private TextView ChangeImage;
    private String InputName,DepartmentName;
    private Toolbar mToolbar;
    private Uri ImageUri;
    private String downloadUrl;

    private static int GalleryPick = 1;
    private ProgressDialog loadingBar;

    private StorageReference userProfileImageRef;
    private DatabaseReference userRef;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        mToolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SendBtn = findViewById(R.id.send_btn);
        ProfileImage = findViewById(R.id.profile_image);
        FullName = findViewById(R.id.user_full_name);
        Department = findViewById(R.id.department);
        ChangeImage = findViewById(R.id.change_profile);
        GoToTest = findViewById(R.id.skip_to_test);
        loadingBar = new ProgressDialog(this);

        ChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GalleryPick);
            }
        });

        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUserData();
            }
        });

        retrieveUserInfo();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  GalleryPick && resultCode==RESULT_OK && data != null){
                ImageUri = data.getData();
                ProfileImage.setImageURI(ImageUri);
        }
    }

    private void SaveUserData(){
        InputName = FullName.getText().toString();
        DepartmentName = Department.getText().toString();

        if(ImageUri == null){
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")){
                        saveInfoOnlyWithoutImage();
                    }else{
                        Toast.makeText(SettingsActivity.this, "Please select image First", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else if(InputName.equals("")){
            Toast.makeText(this, "Full Name is Required", Toast.LENGTH_SHORT).show();
        }else if(DepartmentName.equals("")){
            Toast.makeText(this, "Department Name is required", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Account Settings");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            final StorageReference filepath = userProfileImageRef
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filepath.putFile(ImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    downloadUrl = filepath.getDownloadUrl().toString();
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        downloadUrl = task.getResult().toString();
                        HashMap<String,Object> profileMap = new HashMap<>();
                        profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("name",InputName);
                        profileMap.put("department",DepartmentName);
                        profileMap.put("image",downloadUrl);


                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(SettingsActivity.this,SettingsActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loadingBar.dismiss();
                                    Toast.makeText(SettingsActivity.this, "Profile Settings Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void saveInfoOnlyWithoutImage(){
        InputName = FullName.getText().toString();
        DepartmentName = Department.getText().toString();

        if(InputName.equals("")){
            Toast.makeText(this, "Full Name is required", Toast.LENGTH_SHORT).show();
        }else if(DepartmentName.equals("")){
            Toast.makeText(this, "Department Name is Required", Toast.LENGTH_SHORT).show();
        }else{
            loadingBar.setTitle("Account Settings");
            loadingBar.setMessage("Please wait...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            HashMap<String,Object> profileMap = new HashMap<>();
            profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name",InputName);
            profileMap.put("department",DepartmentName);

            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(SettingsActivity.this,SettingsActivity.class);
                        startActivity(intent);
                        finish();
                        loadingBar.dismiss();
                        Toast.makeText(SettingsActivity.this, "Profile Settings Updated", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void retrieveUserInfo(){
        loadingDialog.show();
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String imageDb = dataSnapshot.child("image").getValue().toString();
                            String nameBb = dataSnapshot.child("name").getValue().toString();
                            String department = dataSnapshot.child("department").getValue().toString();

                            FullName.setText(nameBb);
                            Department.setText(department);
                            Picasso.get().load(imageDb).placeholder(R.drawable.profile_image).into(ProfileImage);
                            loadingDialog.dismiss();

                            GoToTest.setVisibility(View.VISIBLE);
                            GoToTest.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent goToTest = new Intent(SettingsActivity.this, CategoryActivity.class);
                                    startActivity(goToTest);
                                    finish();

                                }
                            });

                        }else{
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }
}