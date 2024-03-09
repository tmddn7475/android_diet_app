package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import kr.co.company.sw_team4.DB.AccountDB;
import kr.co.company.sw_team4.DB.WakDB;
import kr.co.company.sw_team4.R;

public class ProfileActivity extends AppCompatActivity {
    AccountDB accountDB;
    SQLiteDatabase db;
    TextView profile_name, profile_email, profile_age;
    String get_email, get_name, get_age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profile_name = findViewById(R.id.profile_name);
        profile_email = findViewById(R.id.profile_email);
        profile_age = findViewById(R.id.profile_age);

        accountDB = new AccountDB(this);
        db = accountDB.getWritableDatabase();

        Intent intent = getIntent();
        get_email = intent.getStringExtra("email");

        //데이터베이스에서 이메일과 같은 데이터가 담긴 행을 가져옴
        Cursor cursor = db.rawQuery("select * from account where email = '" + get_email + "'", null);
        while (cursor.moveToNext()) {
            get_name = cursor.getString(3);
            get_age = cursor.getString(4);
        }

        profile_name.setText(get_name);
        profile_email.setText(get_email);
        profile_age.setText(get_age + "세");

        // 계정 삭제
        Button delete_account = findViewById(R.id.account_delete);
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_ex = new AlertDialog.Builder(ProfileActivity.this);
                alert_ex.setMessage("계정을 삭제하시겠습니까?");

                alert_ex.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WakDB wakDB = new WakDB(ProfileActivity.this);
                        wakDB.deleteData(get_email);

                        accountDB.deleteAccount(get_email);
                        Intent delete = new Intent(ProfileActivity.this, LoginActivity.class);
                        startActivity(delete);
                        Toast.makeText(ProfileActivity.this, "계정이 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        finishAffinity();
                    }
                });
                alert_ex.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = alert_ex.create();
                alert.show();
            }
        });

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("회원 정보");
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
            }).setCancelable(false); //영역 밖을 클릭해도 창이 사라지지 않게 한다.
            AlertDialog alert = alert_ex.create();
            alert.show();
        }
    }
}