package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BookMarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.rv_bookmarks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<QuestionModel> list = new ArrayList<>();
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));
        list.add(new QuestionModel("What is your name? ","","","","","Bukenya Lukman",0));


        BookMarkAdapter adapter = new BookMarkAdapter(list);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
