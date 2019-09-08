package com.example.practice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.text.SimpleDateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class Frag2 extends Fragment
{

    private View view;
    private FloatingActionButton fab;
    private CalendarView mCalendarView;
    private TextView whenDate;
    private SimpleDateFormat mFormat = new SimpleDateFormat("yyyy/M/d"); // 날짜 포맷


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.frag2, container, false);

        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        whenDate = (TextView) view.findViewById(R.id.whenDate);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        Date date = new Date();
        String time = mFormat.format(date);
        whenDate.setText(time); // 현재 날짜로 설정

        fab.setOnClickListener(new View.OnClickListener() // 일정 추가 버튼 클릭시
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), addschedule.class); // 일정 추가 액티비티 생성
                startActivity(intent);
            }
        });


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() // 날짜 선택 이벤트
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                String date = year + "/" + (month + 1) + "/" + dayOfMonth;
                whenDate.setText(date); // 선택한 날짜로 설정

            }
        });

        return view;
    }
}

