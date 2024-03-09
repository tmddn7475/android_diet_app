package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import kr.co.company.sw_team4.R;

public class GoalActivity extends AppCompatActivity {

    EditText goal_weightEdit, goal_yearEdit, goal_monthEdit, goal_dayEdit;
    Button save_btn;
    String weight, year, month, day;
    LinearLayout linearLayout;
    Dialog weight_dialog;
    DatePickerDialog datePickerDialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    int cYear, cMonth, cDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        linearLayout = findViewById(R.id.linearLayout);
        goal_weightEdit = findViewById(R.id.goal_weight_edit);
        goal_yearEdit = findViewById(R.id.goal_year_edit);
        goal_monthEdit = findViewById(R.id.goal_month_edit);
        goal_dayEdit = findViewById(R.id.goal_day_edit);
        save_btn = findViewById(R.id.save_btn);

        Calendar calender = Calendar.getInstance();
        cYear = calender.get(Calendar.YEAR);
        cMonth = calender.get(Calendar.MONTH);
        cDay = calender.get(Calendar.DAY_OF_MONTH);

        // 공유 프레퍼런스
        preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = preferences.edit();

        // 프레퍼런스 파일에서 값을 읽어와 텍스트뷰에 나오게 한다
        weight = preferences.getString("weight", null);
        year = preferences.getString("year", null);
        month = preferences.getString("month", null);
        day = preferences.getString("day", null);

        goal_weightEdit.setText(weight);
        goal_yearEdit.setText(year);
        goal_monthEdit.setText(month);
        goal_dayEdit.setText(day);

        weight_dialog = new Dialog(GoalActivity.this);
        weight_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 다이어로그에 타이틀 안 나오게 하기
        weight_dialog.setContentView(R.layout.weight_dialog);
        goal_weightEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weightClick();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getWeight = goal_weightEdit.getText().toString();
                String getYear = goal_yearEdit.getText().toString();
                String getMonth = goal_monthEdit.getText().toString();
                String getDay = goal_dayEdit.getText().toString();

                if(getWeight.isEmpty() || getYear.isEmpty()){
                    Toast.makeText(GoalActivity.this, "몸무게와 기간을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //저장 누른 후 메인 화면에 이메일, 이름등이 안 나오는 현상 발생, 이를 해결하기 위해 intent 활용
                    Intent account = getIntent();
                    String get_email = account.getStringExtra("email");

                    Intent intent = new Intent(GoalActivity.this, MainActivity.class);
                    // SharedPreferences 데이터를 설정하고 저장
                    editor.putString("weight", getWeight);
                    editor.putString("year", getYear);
                    editor.putString("month", getMonth);
                    editor.putString("day", getDay);
                    editor.apply(); // 에디터 저장

                    intent.putExtra("email", get_email);
                    Toast.makeText(GoalActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });

        calenderClicked();

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("목표 설정");
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 인터넷 연결 안 되면 종료시키기
        int internet = Get_Internet(this);
        if(internet == 0){
            AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
            alert_ex.setMessage("네트워크 연결을 해주시길 바랍니다");

            alert_ex.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                }
            }).setCancelable(false);//영역 밖을 클릭해도 창이 사라지지 않게 한다.
            AlertDialog alert = alert_ex.create();
            alert.show();
        }
    }

    // 몸무게 다이얼로그
    private void weightClick(){
        weight_dialog.show();

        NumberPicker numberPicker, numberPicker2;
        numberPicker = weight_dialog.findViewById(R.id.numberPicker);
        numberPicker2 = weight_dialog.findViewById(R.id.numberPicker2);

        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(0);
        numberPicker2.setMaxValue(9);
        numberPicker2.setMinValue(0);

        Button dialog_save_btn = weight_dialog.findViewById(R.id.save_btn);
        dialog_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gWeight = numberPicker.getValue() + "." + numberPicker2.getValue();
                goal_weightEdit.setText(gWeight);
                weight_dialog.dismiss();
            }
        });

        Button dialog_cancel_btn = weight_dialog.findViewById(R.id.cancel_btn);
        dialog_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight_dialog.dismiss();
            }
        });
    }

    private void calenderClicked(){
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        goal_yearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        goal_monthEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
        goal_dayEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });
    }

    // 달력 창 생성
    private void datePicker(){
        datePickerDialog = new DatePickerDialog(GoalActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                goal_yearEdit.setText(String.valueOf(year));
                goal_monthEdit.setText(String.valueOf(month));
                goal_dayEdit.setText(String.valueOf(day));
            }
        }, cYear, cMonth, cDay);
        datePickerDialog.show();
    }
}