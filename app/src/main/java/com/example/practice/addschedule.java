package com.example.practice;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class addschedule extends Activity
{
    private TextView mDateText;
    private EditText mTitleText;
    private EditText mContentText;
    private String SeletedDate;
    private long mMemoId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addschedule);

        mDateText = (TextView) findViewById(R.id.TextDate);
        mTitleText = (EditText) findViewById(R.id.Title_Edit);
        mContentText = (EditText) findViewById(R.id.Cotent_Edit);


        Intent intent = getIntent();
        SeletedDate = intent.getStringExtra("SelectedDate");
        mDateText.setText(SeletedDate);

//        if (intent != null)
//        {
//            mMemoId = intent.getLongExtra("id", -1);
//            String title = intent.getStringExtra("title");
//            String contents = intent.getStringExtra("contents");
//            mTitleText.setText(title);
//            mContentText.setText(contents);
//        }

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();


        String title = mTitleText.getText().toString();
        String contents = mContentText.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemoContract.MemoEntry.DATE,SeletedDate);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_CONTENTS, contents);


        SQLiteDatabase db = MemoDBHelper.getInstance(this).getWritableDatabase();


        if (mMemoId == -1)
        {
            long newRowId = db.insert(MemoContract.MemoEntry.TABLE_NAME, null, contentValues);

            if (newRowId == -1)
            {
                Toast.makeText(this, "저장 실패", Toast.LENGTH_SHORT).show();
            } else
            {
                Toast.makeText(this, "메모가 저장되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }


        } else
        {
            int count = db.update(MemoContract.MemoEntry.TABLE_NAME,contentValues,MemoContract.MemoEntry._ID+"="+mMemoId,null);

            if(count==0)
            {
                Toast.makeText(this,"수정에 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this,"수정 되었습니다.",Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }

        super.onBackPressed();
    }
}
