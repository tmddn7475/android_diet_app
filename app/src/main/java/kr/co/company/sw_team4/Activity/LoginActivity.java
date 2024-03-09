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
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import kr.co.company.sw_team4.DB.AccountDB;
import kr.co.company.sw_team4.R;


public class LoginActivity extends AppCompatActivity {

    AccountDB accountDB;
    SQLiteDatabase db;
    EditText emailEt, pwdEt;
    Button loginButton, registerButton;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accountDB = new AccountDB(this);
        db = accountDB.getWritableDatabase();

        emailEt = findViewById(R.id.editTextEmailAddress);
        pwdEt = findViewById(R.id.editTextPassword);

        // 로그인
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_email = emailEt.getText().toString();
                String get_pwd = pwdEt.getText().toString();

                String sql = "select * from account where id = '"+ get_email +"' and pwd = '"+ get_pwd +"'";
                Cursor cursor = db.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    email = cursor.getString(0);
                }
                // 데이터베이스에 해당 이메일과 비밀번호가 있으면 1개의 행을 가져온다
                if(cursor.getCount() == 1) {
                    Intent sign = new Intent(LoginActivity.this, MainActivity.class);
                    sign.putExtra("email", email);
                    startActivity(sign);
                    Toast.makeText(LoginActivity.this, "로그인되었습니다", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                } else {
                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 틀렸습니다", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

        //회원가입 화면으로 이동
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_up = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(sign_up);
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

    //뒤로 가기 누를 때
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert_ex = new AlertDialog.Builder(this);
        alert_ex.setMessage("종료하시겠습니까?");

        alert_ex.setPositiveButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
}