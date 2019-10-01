package com.todayschedule.practice;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.Constants;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;


public class Frag3 extends PreferenceFragmentCompat implements BillingProcessor.IBillingHandler
{
    private MemoDBHelper dbHelper;
    private View view;
    private Preference resetPreference;
    private Preference sendFeedback;
    private Preference gotoStore;
    private SwitchPreferenceCompat switchdark;
    private AlertDialog.Builder builder;
    private SharedPref sharedPref;
    private BillingProcessor bp;
    private SwitchPreferenceCompat alarmSwitch;
    private SwitchPreferenceCompat fontSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey)
    {
        addPreferencesFromResource(R.xml.settings_preference);


        dbHelper = MemoDBHelper.getInstance(getActivity());
        resetPreference = (Preference) findPreference("reset");
        gotoStore = (Preference) findPreference("review");
        sendFeedback = (Preference) findPreference("feedback");
        switchdark = (SwitchPreferenceCompat) findPreference("theme_setting");
        sharedPref = new SharedPref(getActivity());
        alarmSwitch = (SwitchPreferenceCompat) findPreference("alarm_setting");
        fontSwitch = (SwitchPreferenceCompat) findPreference("font_setting");

        // TODO: 라이센스 코드 입력
        bp = new BillingProcessor(getActivity(), "라이센스 코드", this);
        bp.initialize();


        // 액션바 이름
        androidx.appcompat.app.ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("설정");

        // 스위치 상태, 다이얼로그
        if (sharedPref.loadNightModeState() && sharedPref.loadPuchasedTheme())
        {
            switchdark.setChecked(true);
            builder = new AlertDialog.Builder(getActivity(), R.style.Dialog);
        } else if (!sharedPref.loadNightModeState())
        {
            switchdark.setChecked(false);
            builder = new AlertDialog.Builder(getActivity());
        }
        gotoStore.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                return false;
            }
        });

        switchdark.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                if (!sharedPref.loadPuchasedTheme())
                {
                    bp.purchase(getActivity(), "black_theme");
                }
                return false;
            }
        });

        alarmSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if (alarmSwitch.isChecked())
                {
                    sharedPref.setAlarm(false);
                    alarmSwitch.setChecked(false);
                    ((MainActivity)MainActivity.mContext).hide();
                }
                else
                {
                    sharedPref.setAlarm(true);
                    alarmSwitch.setChecked(true);
                    ((MainActivity)MainActivity.mContext).show();
                }
                return false;
            }
        });

        fontSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if (fontSwitch.isChecked())
                {
                    sharedPref.setFontBold(false);
                    fontSwitch.setChecked(false);
                }
                else
                {
                    sharedPref.setFontBold(true);
                    fontSwitch.setChecked(true);
                }
                return false;
            }
        });

        // 앱 테마 변경
        switchdark.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                if (switchdark.isChecked() && sharedPref.loadPuchasedTheme())
                {
                    sharedPref.setNightModeState(false);
                    switchdark.setChecked(false);
                    restartApp();
                } else if (sharedPref.loadPuchasedTheme())
                {
                    sharedPref.setNightModeState(true);
                    switchdark.setChecked(true);

                    restartApp();
                }
                return false;
            }
        });


        // 일정 초기화
        resetPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {


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
                        ((MainActivity) MainActivity.mContext).show();
                    }
                });
                builder.setNegativeButton("취소", null);
                builder.show();

                return false;
            }
        });

        // 피드백 메일보내기
        sendFeedback.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/text");

                String[] address = {"lesslate9@naver.com"};
                email.putExtra(Intent.EXTRA_EMAIL, address);
                email.putExtra(Intent.EXTRA_SUBJECT, "");
                email.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(email);

                return false;
            }
        });

    }

    // 테마 변경시 앱 재시작
    public void restartApp()
    {
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details)
    {
        if (productId.equals("black_theme"))
        {
            sharedPref.setPurchasedTheme(true);
        }
    }

    @Override
    public void onPurchaseHistoryRestored()
    {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error)
    {
        if (errorCode != Constants.BILLING_RESPONSE_RESULT_USER_CANCELED)
        {
            Toast.makeText(getActivity(), "결제 실패", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBillingInitialized()
    {
//        sharedPref.setPurchasedTheme(bp.isPurchased("black_theme"));
//        SkuDetails mProduct = bp.getPurchaseListingDetails("black_theme");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy()
    {
        if (bp != null)
        {
            bp.release();
        }
        super.onDestroy();
    }
}


//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
//    {
//       view = inflater.inflate(R.layout.frag3,container,false);
//
//       return view;
//    }
