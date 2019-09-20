package com.example.practice;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Frag1 extends Fragment
{

    private View view;
    private String mTime;
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/M/d");
    private MemoDBHelper dbHelper;
    private ArrayList<String> list;
    private RecyclerView recyclerView;
    private TextAdapter textAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag1, container, false);

        Date date = new Date();
        mTime = mFormat.format(date);

        list = new ArrayList<>();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler0);

        // dbHelper 인스턴스 저장
        dbHelper = MemoDBHelper.getInstance(getActivity());

        // 리사이클러뷰 LinearLayoutManager 객체 지정
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // 어댑터 객체 생성
        textAdapter = new TextAdapter(list);

        // DB 에서 list 값저장
        getMemoCursor();

        // recyclerView 어댑터 객체 지정
        recyclerView.setAdapter(textAdapter);

        return view;

    }

    // 커서의 데이터를 Arraylist에 저장하는 메서드
    private void getMemoCursor()
    {
        String[] params = {mTime};

        list.clear();

        Cursor cursor = dbHelper.getReadableDatabase().query(MemoContract.MemoEntry.TABLE_NAME, null, "date=?", params, null, null, null);

        while (cursor.moveToNext())
        {
            list.add(cursor.getString(cursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_TITLE)));
        }

        textAdapter.notifyDataSetChanged();
    }
}
