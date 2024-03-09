package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.company.sw_team4.DB.AccountDB;
import kr.co.company.sw_team4.DB.ExerciseDB;
import kr.co.company.sw_team4.R;

public class ExerciseDetailActivity extends AppCompatActivity {
    ExerciseDB exerciseDB;
    SQLiteDatabase db;
    String detail_name, detail_image, detail_part, detail_step1,
            detail_step2, detail_step3, detail_step4, detail_step5;
    TextView name, part, step1, step2, step3, step4, step5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        Intent intent = getIntent();
        detail_name = intent.getStringExtra("name");

        exerciseDB = new ExerciseDB(this);
        db = exerciseDB.getWritableDatabase();
        // 데이터베이스에서 detail_name과 같은 이름이 담긴 행 가져오기
        Cursor cursor = db.rawQuery("select * from Exercise where NAME = '" + detail_name + "'", null);
        while (cursor.moveToNext()) {
            detail_image = cursor.getString(2);
            detail_part = cursor.getString(3);
            detail_step1 = cursor.getString(4);
            detail_step2 = cursor.getString(5);
            detail_step3 = cursor.getString(6);
            detail_step4 = cursor.getString(7);
            detail_step5 = cursor.getString(8);
        }

        ImageView exercise_image = findViewById(R.id.exercise_image);
        //getResources().getIdentifier()를 통해 drawable 내에 해당 이름의 이미지 리소스를 가져온다
        int imageID = this.getResources().getIdentifier(detail_image, "drawable", this.getPackageName());
        exercise_image.setImageResource(imageID);

        name = findViewById(R.id.text_name);
        name.setText(detail_name);

        part = findViewById(R.id.text_part);
        part.setText(detail_part);

        step1 = findViewById(R.id.exercise_step1);
        step1.setText(detail_step1);

        step2 = findViewById(R.id.exercise_step2);
        step2.setText(detail_step2);

        step3 = findViewById(R.id.exercise_step3);
        step3.setText(detail_step3);

        step4 = findViewById(R.id.exercise_step4);
        step4.setText(detail_step4);

        step5 = findViewById(R.id.exercise_step5);
        step5.setText(detail_step5);

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("운동하기");
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
}