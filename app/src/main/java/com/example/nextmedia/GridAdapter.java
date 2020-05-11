package com.example.nextmedia;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter{

    private int introduction = 0;
    private String category;

    public GridAdapter(int introduction,String category) {
        this.introduction = introduction;
        this.category = category;
    }

    @Override
    public int getCount() {
        return introduction;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.introduction_item,parent,false);

        }else{
            view = convertView;
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent questionIntent = new Intent(parent.getContext(),QuestionsActivity.class);
                questionIntent.putExtra("category",category);
                questionIntent.putExtra("setNo",position+1);
                parent.getContext().startActivity(questionIntent);
                //Flag off Kill Activity
            }
        });
        ((TextView)view.findViewById(R.id.text_view)).setText(String.valueOf(position+1));

        return view;
    }
}
