package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.company.sw_team4.DB.MemoDB;
import kr.co.company.sw_team4.R;

public class DiaryActivity extends AppCompatActivity {
    MemoDB memoDB;
    SQLiteDatabase db;

    EditText edit_memo;
    Button memo_save_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Intent intent = getIntent();
        String date = intent.getStringExtra("date");;

        edit_memo = findViewById(R.id.edit_memo);
        memo_save_btn = findViewById(R.id.memo_save_btn);

        memoDB = new MemoDB(this);
        String str = readDiary(date);
        edit_memo.setText(str);

        memo_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = memoDB.getWritableDatabase();
                db.execSQL("update myDiary set content = '"
                        + edit_memo.getText().toString()
                        + "'where diaryDate = '" + date + "';");
                db.close();
                Toast.makeText(DiaryActivity.this, "저장되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(date);
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

    String readDiary(String date){
        String diaryDate = null;
        String diaryContent = null;
        try {
            db = memoDB.getReadableDatabase();
            Cursor cursor;
            String sql = "select * from myDiary where diaryDate ='" + date + "'";
            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()){
                diaryDate = cursor.getString(0);
                diaryContent = cursor.getString(1);
            }
            if (diaryContent == null){
                edit_memo.setHint("메모를 입력하세요.");
                // 날짜가 데이터베이스에 없을 경우 해당 날짜를 데이터베이스에 추가
                if(diaryDate != date){
                    db.execSQL("INSERT INTO myDiary VALUES ('"+date+"',"+ null + ");");
                }
            }
            cursor.close();
            db.close();
        }catch (SQLException e){
            // 예외 발생 시
            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_SHORT).show();
        }
        return diaryContent;
    }
}
