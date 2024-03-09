package kr.co.company.sw_team4.Fragment;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import java.io.File;

import kr.co.company.sw_team4.DB.WakDB;
import kr.co.company.sw_team4.R;

public class SettingsFragment extends PreferenceFragmentCompat{

    String email;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, @Nullable String rootKey) {
        // 화면 설정
        setPreferencesFromResource(R.xml.setting, rootKey);

        email = this.getArguments().getString("email");

        //차트 초기화
        Preference chart = (Preference) findPreference("chart_reset");
        chart.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(@NonNull Preference preference) {
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(getActivity());
                alert_ex.setMessage("입력했던 데이터를 초기화하시겠습니까?");

                alert_ex.setPositiveButton("초기화", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WakDB wakDB = new WakDB(getActivity());
                        wakDB.deleteData(email);
                    }
                });
                alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = alert_ex.create();
                alert.show();
                return false;
            }
        });
    }
}
