package com.example.nextmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookMarkActivity extends AppCompatActivity {

    private  static final String FILE_NAME = "TEST";
    private  static final String KEY_NAME = "QUESTIONS";
    private Toolbar toolbar;
    private RecyclerView recyclerView;

    private List<QuestionModel> bookmarksList;
    private int position = 0;
    private int score = 0;

    private int count = 0;


    private String category;
    private int setNo;
    private Gson gson;
    private  int matchedQuestionPosition;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("BookMarks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        preferences = getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        gson = new Gson();
        getBookMarks();
        recyclerView = findViewById(R.id.rv_bookmarks);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);




        BookMarkAdapter adapter = new BookMarkAdapter(bookmarksList);
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        storedBookmarks();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getBookMarks(){
        String json = preferences.getString(KEY_NAME,"");
        Type type = new TypeToken<List<QuestionModel>>(){}.getType();
        bookmarksList = gson.fromJson(json, type);
        if(bookmarksList == null){
            bookmarksList = new ArrayList<>();
        }
    }

    private boolean modeMatch(){
        boolean matched = false;
        int i = 0;

        for (QuestionModel model: bookmarksList){

            if(model.getQuestion().equals(bookmarksList.get(position).getQuestion())
                    && model.getCorrectAns().equals(bookmarksList.get(position).getCorrectAns())
                    && model.getSetNo() == bookmarksList.get(position).getSetNo()){
                matched = true;
                matchedQuestionPosition = i;
            }
            i++;
        }
        return matched;
    }

    private void storedBookmarks(){
        String json = gson.toJson(bookmarksList);
        editor.putString(KEY_NAME,json);
        editor.commit();
    }
}
