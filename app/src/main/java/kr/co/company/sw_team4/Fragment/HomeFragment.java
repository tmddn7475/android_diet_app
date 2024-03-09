package kr.co.company.sw_team4.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.co.company.sw_team4.Activity.GoalActivity;
import kr.co.company.sw_team4.Activity.ProfileActivity;
import kr.co.company.sw_team4.Activity.ChartActivity;
import kr.co.company.sw_team4.R;
import kr.co.company.sw_team4.DB.WakDB;

public class HomeFragment extends Fragment {

    // Millisecond 형태의 하루
    private final int ONE_DAY = 24 * 60 * 60 * 1000;

    TextView goal_day_home, goal_weight_home;
    Button chart_btn, account_btn, goal_btn;

    LineChart weightChart, kcalChart;
    ArrayList<ILineDataSet> weightDataSets = new ArrayList<ILineDataSet>();
    ArrayList<ILineDataSet> kcalDataSets = new ArrayList<ILineDataSet>();
    LineData weightData, kcalData;
    LineDataSet weightDataSet, kcalDataSet;

    WakDB wakDB;
    SQLiteDatabase sqLiteDatabase;
    String email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        goal_day_home = v.findViewById(R.id.goal_day_home);
        goal_weight_home = v.findViewById(R.id.goal_weight_home);
        account_btn = v.findViewById(R.id.account_btn);
        chart_btn = v.findViewById(R.id.insert_btn);
        goal_btn = v.findViewById(R.id.show_goal_btn);

        // 번들에 담긴 데이터 가져오기
        String year = this.getArguments().getString("year");
        String month = this.getArguments().getString("month");
        String day = this.getArguments().getString("day");
        String weight = this.getArguments().getString("weight");
        email = this.getArguments().getString("email");

        int mYear = Integer.parseInt(year);
        int mMonth = Integer.parseInt(month);
        int mDay = Integer.parseInt(day);
        String dDay = get_dDay(mYear, mMonth, mDay);

        goal_day_home.setText(dDay);
        goal_weight_home.setText(weight);

        // 회원 정보
        account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
        // 차트 입력
        chart_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chart = new Intent(getActivity(), ChartActivity.class);
                chart.putExtra("email", email);
                startActivity(chart);
            }
        });
        // 목표 설정
        goal_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goal = new Intent(getActivity(), GoalActivity.class);
                goal.putExtra("email", email);
                startActivity(goal);
            }
        });

        // 차트
        wakDB = new WakDB(getActivity());
        sqLiteDatabase = wakDB.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM wak where EMAIL = '" + email + "'",null);
        // 차트 x축 값 리스트
        ArrayList<String> labels = new ArrayList<>();
        // x값이 좌표 0부터 설정 되므로 0 추가
        labels.add("0");
        while(cursor.moveToNext()){
            labels.add(cursor.getString(0));
        }

        // 차트 - 몸무게
        weightChart = v.findViewById(R.id.home_chart_weight);
        weightChart.setTouchEnabled(true);
        weightChart.setDragXEnabled(true); // x축 방향으로 드래그 가능 여부
        weightChart.setScaleEnabled(false); // 줌 가능 여부
        weightChart.setDrawGridBackground(false); // 격자 구조
        weightChart.getDescription().setEnabled(false); // 하단 description 표출 x
        weightChart.animateY(2000); // 애니메이션 설정

        weightDataSet = new LineDataSet(getWeightValues(), "weight");
        weightDataSet.setLineWidth(1); // 라인 두께
        weightDataSet.setCircleRadius(3); // 점 크기
        weightDataSet.setDrawCircleHole(true); // 원의 안 부분 칠할지 여부
        weightDataSet.setDrawCircles(true); // 원의 겉 부분 칠할지 여부
        weightDataSet.setCircleColor(Color.parseColor("#FF0099CC")); // 점 색깔
        weightDataSet.setColor(Color.parseColor("#FF0099CC")); // 라인 색깔
        weightDataSet.setDrawHorizontalHighlightIndicator(false); // 값 클릭시 선 출력 여부
        weightDataSet.setDrawHighlightIndicators(false);
        weightDataSet.setDrawValues(true); // 결과 값 출력 여부
        weightDataSet.setValueTypeface(Typeface.DEFAULT_BOLD); // 결과 값 볼드
        weightDataSet.setValueTextSize(13);
        weightDataSet.setValueTextColor(Color.BLACK);
        weightDataSet.setValueFormatter(new MyValueFormatter());

        weightDataSets.add(weightDataSet);
        weightData = new LineData(weightDataSets);

        weightChart.setData(weightData);
        //x축을 데이터의 오른쪽 끝으로 위치하게 한다
        weightChart.moveViewTo(weightData.getEntryCount(), 50f, YAxis.AxisDependency.RIGHT);
        weightChart.setVisibleXRangeMinimum(0);
        weightChart.setVisibleXRangeMaximum(6); // 볼 수 있는 최소, 최대 x축 범위

        Legend weight_legend = weightChart.getLegend();
        weight_legend.setEnabled(false); // 밑 설명 표시 여부

        XAxis weight_xAxis = weightChart.getXAxis(); //x축 설정
        weight_xAxis.setDrawLabels(true); // x축 그래프 값 출력
        weight_xAxis.setTextSize(8);
        weight_xAxis.setDrawGridLines(false);
        weight_xAxis.setTextColor(Color.GRAY);
        weight_xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축 데이터 표시 위치
        weight_xAxis.setGridColor(Color.GRAY); // 선 색깔
        weight_xAxis.setSpaceMin(0.3f); // x축 왼쪽에서 얼마나 떨어지게 하는지 설정
        weight_xAxis.setSpaceMax(0.3f); // x축 오른쪽에서 얼마나 떨어지게 하는지 설정
        weight_xAxis.setGranularity(1f);
        weight_xAxis.setXOffset(15f);
        weight_xAxis.setGranularityEnabled(true);
        weight_xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); // x축 설명 설정

        YAxis weight_yLAxis = weightChart.getAxisLeft();
        weight_yLAxis.setGranularity(1); // Y값 최소 간격
        weight_yLAxis.setTextColor(Color.GRAY);
        weight_yLAxis.setTextSize(2);
        weight_yLAxis.setDrawAxisLine(false); // Y값 세로 선
        weight_yLAxis.setDrawGridLines(false); // Y 가로 선

        YAxis weight_yRAxis = weightChart.getAxisRight();
        weight_yRAxis.setDrawLabels(false); // Y축 숫자 출력 여부
        weight_yRAxis.setDrawAxisLine(false);
        weight_yRAxis.setDrawGridLines(false);


        //차트 - 칼로리
        kcalChart = v.findViewById(R.id.home_chart_kcal);
        kcalChart.setTouchEnabled(true);
        kcalChart.setDragXEnabled(true);
        kcalChart.setScaleEnabled(false); // 줌 가능 여부
        kcalChart.setDrawGridBackground(false); // 격자 구조
        kcalChart.getDescription().setEnabled(false); // 하단 description 표출 x
        kcalChart.animateY(2000); // 애니메이션 설정

        kcalDataSet = new LineDataSet(getKcalValues(), "칼로리");
        kcalDataSet.setLineWidth(1); // 라인 두께
        kcalDataSet.setCircleRadius(3); // 점 크기
        kcalDataSet.setDrawCircleHole(true); // 원의 안 부분 칠할지 여부
        kcalDataSet.setDrawCircles(true); // 원의 겉 부분 칠할지 여부
        kcalDataSet.setCircleColor(Color.parseColor("#FF0099CC")); // 점 색깔
        kcalDataSet.setColor(Color.parseColor("#FF0099CC")); // 라인 색깔
        kcalDataSet.setDrawHorizontalHighlightIndicator(false); // 값 클릭시 선 출력 여부
        kcalDataSet.setDrawHighlightIndicators(false);
        kcalDataSet.setDrawValues(true); // 결과 값 출력 여부

        kcalDataSet.setValueTypeface(Typeface.DEFAULT_BOLD); // 결과 값
        kcalDataSet.setValueTextSize(13);
        kcalDataSet.setValueTextColor(Color.BLACK);
        kcalDataSet.setValueFormatter(new MyValueFormatter()); //소수점 첫자리까지만 나오게 하기

        kcalDataSets.add(kcalDataSet);
        kcalData = new LineData(kcalDataSets);

        kcalChart.setData(kcalData);
        //x축을 데이터의 오른쪽 끝으로 위치하게 한다
        kcalChart.moveViewTo(kcalData.getEntryCount(), 50f, YAxis.AxisDependency.RIGHT);
        kcalChart.setVisibleXRangeMinimum(0);
        kcalChart.setVisibleXRangeMaximum(6); // 볼 수 있는 최소, 최대 x축 범위

        Legend kcal_legend = kcalChart.getLegend();
        kcal_legend.setEnabled(false); // 밑 설명 표시 여부

        XAxis kcal_xAxis = kcalChart.getXAxis(); //x축 설정
        kcal_xAxis.setDrawLabels(true); // x축 그래프 값 출력
        kcal_xAxis.setTextSize(8);
        kcal_xAxis.setTextColor(Color.GRAY);
        kcal_xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x축 데이터 표시 위치
        kcal_xAxis.setGridColor(Color.GRAY); // 선 색깔
        kcal_xAxis.enableGridDashedLine(10, 24, 0); //수직 격자선
        kcal_xAxis.setSpaceMin(0.3f);
        kcal_xAxis.setSpaceMax(0.3f);
        kcal_xAxis.setGranularity(1f); // x축 값 간격 설정
        kcal_xAxis.setXOffset(15f); // x축 간격 설정
        kcal_xAxis.setGranularityEnabled(true);
        kcal_xAxis.setValueFormatter(new IndexAxisValueFormatter(labels)); //소수점 첫자리까지만 나오게 하기

        YAxis kcal_yLAxis = kcalChart.getAxisLeft();
        kcal_yLAxis.setGranularity(1); // Y값 최소 간격
        kcal_yLAxis.setTextColor(Color.GRAY);
        kcal_yLAxis.setDrawAxisLine(false); // Y값 세로 선
        kcal_yLAxis.setDrawGridLines(true); // Y 가로 선

        YAxis kcal_yRAxis = kcalChart.getAxisRight();
        kcal_yRAxis.setDrawLabels(false); // Y축 숫자 출력 여부
        kcal_yRAxis.setDrawAxisLine(false);
        kcal_yRAxis.setDrawGridLines(false);

        return v;
    }

    // 몸무게 값
    private ArrayList<Entry> getWeightValues(){
        WakDB weightDB = new WakDB(getActivity());
        SQLiteDatabase database = weightDB.getWritableDatabase();

        ArrayList<Entry> dataVal = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM wak where EMAIL = '" + email + "'",null);
        int i = 1;

        while(cursor.moveToNext()){
            String time = cursor.getString(1);
            // x, y값
            dataVal.add(new Entry(i++, cursor.getFloat(1)));

        }
        return dataVal;
    }

    // 칼로리 값
    private ArrayList<Entry> getKcalValues(){
        WakDB weightDB = new WakDB(getActivity());
        SQLiteDatabase database = weightDB.getWritableDatabase();

        ArrayList<Entry> dataVal = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM wak where EMAIL = '" + email + "'",null);
        int i = 1;

        while(cursor.moveToNext()){
            // x, y값
            dataVal.add(new Entry(i++, cursor.getFloat(2)));
        }
        return dataVal;
    }

    // 날짜를 계산하고 남은 날 구하기
    private String get_dDay(int a_year, int a_monthOfYear, int a_dayOfMonth) {

        final Calendar dDayCalendar = Calendar.getInstance();
        // 입력 받은 날짜로 설정한다
        dDayCalendar.set(a_year, a_monthOfYear - 1, a_dayOfMonth);

        // millisecond 으로 환산한 뒤 입력한 날짜에서 현재 날짜의 차를 구한다
        final long dDay = dDayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dDay - today;

        final String goalDate;
        if (result > 0) {
            goalDate = "D - " + result;
        } else if (result == 0) {
            goalDate = "D - Day";
        } else {
            result *= -1;
            goalDate = "D + " + result;
        }

        return goalDate;
    }

    //차트 값 소수점 한 자리까지만 보이게 하기
    public class MyValueFormatter extends ValueFormatter{
        private DecimalFormat mFormat;

        public MyValueFormatter() {
            mFormat = new DecimalFormat("###,###,##0.0");
        }
    }
}