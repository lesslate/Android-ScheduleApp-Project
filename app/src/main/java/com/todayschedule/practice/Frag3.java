package com.todayschedule.practice;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;
import android.app.ActionBar;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;


public class Frag3 extends PreferenceFragmentCompat
{
    private MemoDBHelper dbHelper;
    private View view;
    private Preference resetPreference;
    private Preference sendFeedback;


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.settings_preference);


        dbHelper = MemoDBHelper.getInstance(getActivity());
        resetPreference =(Preference) findPreference("reset");
        sendFeedback =(Preference) findPreference("feedback");

        androidx.appcompat.app.ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("설정");


        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle("초기화");
                builder.setMessage("모든 일정을 삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        dbHelper.DropTable(db);
                        Toast.makeText(getActivity(), "모든 일정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
                return false;
            }
        });

        sendFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");

                String[] address = {"lesslate9@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT,"");
                email.putExtra(Intent.EXTRA_TEXT,"");
                startActivity(email);

                return false;
            }
        });

    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
//    {
//       view = inflater.inflate(R.layout.frag3,container,false);
//
//       return view;
//    }
}
