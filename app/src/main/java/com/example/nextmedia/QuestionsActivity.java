package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class QuestionsActivity extends AppCompatActivity {


    private DatabaseReference myRef;



    private Toolbar toolbar;
    private TextView Question,NumberIndicator;
    private FloatingActionButton bookmarkBtn;
    private LinearLayout optionsContainer;
    private Button NextBtn;
    private List<QuestionModel> list;

    private int position = 0;
    private int score = 0;

    private Dialog loadingDialog;

    private int count = 0;


    private String category;
    private int setNo;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);



        myRef = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.questions_toolbar);
        setSupportActionBar(toolbar);

        loadingDialog = new Dialog(this);

        Question = findViewById(R.id.question);
        NumberIndicator = findViewById(R.id.no_indicator);
        bookmarkBtn = findViewById(R.id.bookmark_btn);
        optionsContainer = findViewById(R.id.options_container);
        NextBtn = findViewById(R.id.next_btn);


        category = getIntent().getStringExtra("category");
        //setNo = getIntent().getIntExtra("setNo",1);

        loadingDialog = new Dialog(this);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.rounded_corners));
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.setCancelable(false);

        //question = getIntent().getIntExtra("sets",0),getIntent().getStringExtra("title")

        list = new ArrayList<>();


        loadingDialog.show();
        myRef.child("SETS").child(category).child("questions")
                .orderByChild("setNo")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                            list.add(snapshot.getValue(QuestionModel.class));
                        }
                        if(list.size() > 0){
                            for(int i = 0; i < 4; i++){
                                optionsContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        checkAnswer((Button)v);
                                    }
                                });
                            }

                            playAnim(Question,0,list.get(position).getQuestion());
                            NextBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    NextBtn.setEnabled(false);
                                    NextBtn.setAlpha(0.7f);
                                    position++;
                                    if(position == list.size()){
                                        Intent scoreIntent = new Intent(QuestionsActivity.this,ScoreActivity.class);
                                        scoreIntent.putExtra("score",score);
                                        scoreIntent.putExtra("total",list.size());
                                        startActivity(scoreIntent);
                                        finish();
                                        return;
                                    }

                                    count = 0;
                                    playAnim(Question,0,list.get(position).getQuestion());
                                }
                            });
                        }else{
                            finish();
                            Toast.makeText(QuestionsActivity.this, "No More Questions", Toast.LENGTH_SHORT).show();

                        }
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(QuestionsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                        finish();
                    }
                });

        bookmarkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void playAnim(final View view, final int value, final String data){
        for(int i = 0; i < 4; i++){
            optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
        }
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if(value == 0 && count < 4 ){
                    String option = "";
                    if(count == 0){
                        option = list.get(position).getOptionA();
                    }else if(count == 1){
                        option = list.get(position).getOptionB();
                    }else if(count == 2){
                        option = list.get(position).getOptionC();
                    }else if(count == 3){
                        option = list.get(position).getOptionD();
                    }
                    playAnim(optionsContainer.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if(value == 0){
                    try {
                        ((TextView)view).setText(data);
                        NumberIndicator.setText(position+1+"/"+list.size());

                    }catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnim(view,1,data);
                }else{
                   enableOptions(true);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void checkAnswer(Button selectedOption){
        enableOptions(false);
        NextBtn.setEnabled(true);
        NextBtn.setAlpha(1);
        if(selectedOption.getText().toString().equals(list.get(position).getCorrectAns())){
            //Correct Answer
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }else{
            //Incorrect Answer
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
            Button correctOption =  optionsContainer.findViewWithTag(list.get(position).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }

    }
    private void enableOptions(boolean enable){
        for(int i = 0; i < 4; i++){
            optionsContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                optionsContainer.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#989898")));
            }
        }
    }


}
