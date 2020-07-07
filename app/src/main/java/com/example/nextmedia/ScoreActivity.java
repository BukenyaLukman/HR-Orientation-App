package com.example.nextmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    private TextView Scored, Total;
    private Button doneBtn;
    private String CategoryName,Mark,TotalMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        Scored = findViewById(R.id.scored);
        Total = findViewById(R.id.total);
        doneBtn = findViewById(R.id.done_btn);

        Mark = String.valueOf(getIntent().getIntExtra("score",0));

        Scored.setText(Mark);
        TotalMark = String.valueOf(getIntent().getIntExtra("total",0));
        Total.setText("OUT OF "+ TotalMark);
        CategoryName = getIntent().getStringExtra("category");


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToTestReportActivity();

            }
        });
    }

    private void GoToTestReportActivity() {
        Intent reportIntent = new Intent(ScoreActivity.this,TestReportActivity.class);
        reportIntent.putExtra("mark",getIntent().getIntExtra("score",0));
        reportIntent.putExtra("total",getIntent().getIntExtra("total",0));
        reportIntent.putExtra("category",CategoryName);
        startActivity(reportIntent);
        finish();
    }
}