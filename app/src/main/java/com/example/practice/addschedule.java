package com.example.practice;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class addschedule extends Activity


{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addschedule);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
    }
}
