package com.example.nextmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView Scored, Total;
    private Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Scored = findViewById(R.id.scored);
        Total = findViewById(R.id.total);
        doneBtn = findViewById(R.id.done_btn);

        Scored.setText(String.valueOf(getIntent().getIntExtra("score",0)));
        Total.setText("OUT OF "+ String.valueOf(getIntent().getIntExtra("total",0)));


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}