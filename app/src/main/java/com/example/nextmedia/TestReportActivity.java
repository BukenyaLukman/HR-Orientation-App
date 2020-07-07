package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TestReportActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private CircleImageView CandidateImage;
    private TextView CandidateName,CandidateMarks,TotalMark,CandidateDepartment;
    private RecyclerView recyclerView;
    private String Mark,Total,CategoryName;
    private FirebaseAuth mAuth;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_report);


        mAuth = FirebaseAuth.getInstance();



        CandidateImage = findViewById(R.id.candidate_profile_image);
        CandidateName = findViewById(R.id.candidate_name);
        CandidateMarks = findViewById(R.id.candidate_marks);
        CandidateDepartment =  findViewById(R.id.candidate_department);
        TotalMark = findViewById(R.id.total_markup);

        Mark = String.valueOf(getIntent().getIntExtra("mark",0));
        CandidateMarks.setText(Mark);
        Total = String.valueOf(getIntent().getIntExtra("total",0));
        TotalMark.setText(Total);
        CategoryName = getIntent().getStringExtra("category");

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);


        myRef = FirebaseDatabase.getInstance().getReference();

        retrieveInfo();

    }


    /// Write TextView For Category
    /// Upload the results of the category and candidates's details
    /// Retrieve them before setting up the print Preview
    private void retrieveInfo() {
        loadingDialog.show();
        myRef.child("Users").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String imageUrl = dataSnapshot.child("image").getValue().toString();
                    String department = dataSnapshot.child("department").getValue().toString();
                    String name = dataSnapshot.child("name").getValue().toString();

                    CandidateName.setText(name);
                    CandidateDepartment.setText(department);
                    Picasso.get().load(imageUrl).placeholder(R.drawable.profile_image).into(CandidateImage);
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}