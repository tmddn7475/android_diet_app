package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.company.sw_team4.DB.WakDB;
import kr.co.company.sw_team4.R;

public class ChartActivity extends AppCompatActivity {

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("MM/dd hh:mm");

    Dialog weight_dialog;
    EditText chart_weight, chart_kcal;
    Button chartSaveBtn;
    WakDB wakDB;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        wakDB = new WakDB(ChartActivity.this);
        sqLiteDatabase = wakDB.getWritableDatabase();

        chart_weight = findViewById(R.id.goal_weight_edit);
        chart_kcal = findViewById(R.id.goal_month_edit);
        chartSaveBtn = findViewById(R.id.chart_save_btn);

        weight_dialog = new Dialog(ChartActivity.this);
        weight_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 다이어로그에 타이틀 안 나오게 하기
        weight_dialog.setContentView(R.layout.weight_dialog);

        chart_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weight_dialog.show();

                NumberPicker numberPicker, numberPicker2;
                numberPicker = weight_dialog.findViewById(R.id.numberPicker);
                numberPicker2 = weight_dialog.findViewById(R.id.numberPicker2);

                numberPicker.setMaxValue(200);
                numberPicker.setMinValue(0);
                numberPicker2.setMaxValue(9);
                numberPicker2.setMinValue(0);

                Button dialog_save_btn = weight_dialog.findViewById(R.id.save_btn);
                dialog_save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String gWeight = numberPicker.getValue() + "." + numberPicker2.getValue();
                        chart_weight.setText(gWeight);
                        weight_dialog.dismiss(); // 다이얼로그 닫기
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
        });

        // 저장하기
        chartSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gWeight = chart_weight.getText().toString();
                String gKcal = chart_kcal.getText().toString();

                if(gWeight.isEmpty() || gKcal.isEmpty()){
                    Toast.makeText(ChartActivity.this, "전부 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    //저장 누른 후 메인 화면에 이메일, 이름등이 안 나오는 현상 발생, 이를 해결하기 위해 intent 활용
                    Intent account = getIntent();
                    String get_email = account.getStringExtra("email");

                    Float weight = Float.valueOf(gWeight);
                    Float kcal = Float.valueOf(gKcal);
                    wakDB.insertData(getTime(), weight, kcal, get_email);

                    Intent intent = new Intent(ChartActivity.this, MainActivity.class);
                    intent.putExtra("email", get_email);
                    Toast.makeText(ChartActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finishAffinity();
                }
            }
        });

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("차트 입력");

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

    // 현재 시간 구하기
    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }
}