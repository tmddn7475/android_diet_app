package kr.co.company.sw_team4.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import kr.co.company.sw_team4.Activity.DiaryActivity;
import kr.co.company.sw_team4.R;

public class CalenderFragment extends Fragment {

    CalendarView calendarView;
    TextView diaryTextView;
    Button diary_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_calender, container, false);

        calendarView = v.findViewById(R.id.calender);
        diaryTextView = v.findViewById(R.id.diaryText);
        diary_btn = v.findViewById(R.id.diary_btn);

        // 달력에서 날짜를 클릭 할때
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(View.VISIBLE);
                diary_btn.setVisibility(View.VISIBLE);
                String date = year + " / " + (month + 1) + " / " + dayOfMonth;

                diaryTextView.setText(date);

                diary_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent showDiary = new Intent(getActivity(), DiaryActivity.class);
                        showDiary.putExtra("date", date);
                        startActivity(showDiary);
                    }
                });
            }
        });

        return v;
    }
}