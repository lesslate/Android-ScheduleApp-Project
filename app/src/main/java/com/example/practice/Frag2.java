package com.example.practice;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.database.Cursor;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Frag2 extends Fragment
{

    public static final int REQUEST_CODE_INSERT = 1000;
    private View view;
    private FloatingActionButton fab;
    private CalendarView mCalendarView;
    private TextView mTextDate;
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/M/d"); // 날짜 포맷
    private String mTime;
    private RecyclerView recyclerView;
    private TextAdapter textAdapter;
    private MemoDBHelper dbHelper;
    private ArrayList<String> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag2, container, false);
        list = new ArrayList<>();

        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        mTextDate = (TextView) view.findViewById(R.id.whenDate);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler1);


        Date date = new Date();
        mTime = mFormat.format(date);
        mTextDate.setText(mTime); // 현재 날짜로 설정

        fab.setOnClickListener(new View.OnClickListener() // 일정 추가 버튼 클릭시
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), addschedule.class); // 일정 추가 액티비티 생성
                intent.putExtra("SelectedDate", mTime);
                startActivityForResult(intent, REQUEST_CODE_INSERT);
            }
        });


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                mTime = year + "/" + (month + 1) + "/" + dayOfMonth;
                getMemoCursor(); // 선택한 날짜의 메모 가져옴
                mTextDate.setText(mTime); // 선택한 날짜로 설정

            }
        });

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
//        Cursor testCursor = dbHelper.getReadableDatabase().query(MemoContract.MemoEntry.TABLE_NAME, null, null, null, null, null, null);
//        while (testCusor.moveToNext())
//        {
//            String id = testCusor.getString(testCusor.getColumnIndex(MemoContract.MemoEntry._ID));
//            String Date = testCusor.getString(testCusor.getColumnIndex(MemoContract.MemoEntry.DATE));
//            String Title = testCusor.getString(testCusor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_TITLE));
//            String Content = testCusor.getString(testCusor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_CONTENTS));
//            Log.e("Frag2",id + Date + Title + Content);
//        }
        list.clear();

        Cursor cursor = dbHelper.getReadableDatabase().query(MemoContract.MemoEntry.TABLE_NAME, null, "date=?", params, null, null, null);

        while (cursor.moveToNext())
        {
            list.add(cursor.getString(cursor.getColumnIndex(MemoContract.MemoEntry.COLUMN_NAME_TITLE)));
        }

        textAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_INSERT)
        {
            getMemoCursor();
        }
    }
}


