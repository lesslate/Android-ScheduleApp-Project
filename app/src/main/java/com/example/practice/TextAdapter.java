package com.example.practice;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.ViewHolder>
{
    private ArrayList<String> mData = null;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView1;

        ViewHolder(View itemView)
        {
            super(itemView);

            textView1 = itemView.findViewById(R.id.text1);
        }
    }

    // 생성자에서 데이터 리스트 객체 전달받음
    TextAdapter(ArrayList<String> list)
    {
        mData = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.recyclerview_item, parent,false);
        TextAdapter.ViewHolder vh = new TextAdapter.ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        String text = mData.get(position);
        holder.textView1.setText(text);
    }


    @Override
    public int getItemCount()
    {
        return mData.size();
    }
}
