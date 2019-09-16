package com.example.practice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
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
    //private MemoAdapter mAdapter;
    TextAdapter textAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag2, container, false);


        ArrayList<String> list = new ArrayList<>();

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
                mTextDate.setText(mTime); // 선택한 날짜로 설정

            }
        });


        Cursor cursor = getMemoCursor();

        //mAdapter = new MemoAdapter(getActivity(), cursor);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        textAdapter = new TextAdapter(list);
        recyclerView.setAdapter(textAdapter);

        return view;
    }

    private Cursor getMemoCursor()
    {
        MemoDBHelper dbHelper = MemoDBHelper.getInstance(getActivity());
        return dbHelper.getReadableDatabase().query(MemoContract.MemoEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_INSERT )
        {
            //mAdapter.swapCursor(getMemoCursor());
        }
    }

//    private static class MemoAdapter extends CursorAdapter
//    {
//
//        public MemoAdapter(Context context, Cursor c)
//        {
//            super(context, c, false);
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent)
//        {
//            return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor)
//        {
//            TextView titleText = view.findViewById(android.R.id.text1);
//            titleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.DATE)));
//        }
//    }
}

