package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.company.sw_team4.DB.AccountDB;
import kr.co.company.sw_team4.R;

public class RegisterActivity extends AppCompatActivity {

    AccountDB accountDB;
    SQLiteDatabase db;
    EditText idEt, emailEt, pwdEt, nameEt, ageEt;
    Button sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        accountDB = new AccountDB(this);
        db = accountDB.getWritableDatabase();

        idEt = findViewById(R.id.registerId);
        emailEt = findViewById(R.id.registerEmailAddress);
        pwdEt = findViewById(R.id.registerPassword);
        nameEt = findViewById(R.id.registerPersonName);
        ageEt = findViewById(R.id.registerPersonAge);

        sign_up = findViewById(R.id.register_complete);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEt.getText().toString();
                String email = emailEt.getText().toString();
                String pwd = pwdEt.getText().toString();
                String name = nameEt.getText().toString();
                String age = ageEt.getText().toString();

                //데이터베이스에서 입력한 이메일과 같은 이메일이 담긴 행이 있는지 확인 한다
                String sql = "select * from account where email = '" + email + "'";
                Cursor cursor = db.rawQuery(sql, null);

                if (id.isEmpty() || email.isEmpty() || pwd.isEmpty() || name.isEmpty() || age.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "전부 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (pwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 6자리 이상 입력해주세요", Toast.LENGTH_SHORT).show();
                } else if (cursor.getCount() == 1) {
                    // 데이터베이스에 해당 이메일이 있으면 1개의 행를 가져온다
                    Toast.makeText(RegisterActivity.this, "이미 존재하는 이메일입니다", Toast.LENGTH_SHORT).show();
                } else {
                    // 없다면 아무 값도 가져오지 않으므로 0이 된다
                    // 데이터베이스에 해당 정보들을 추가한다
                    accountDB.signUp(email, id, pwd, name, age);
                    Toast.makeText(RegisterActivity.this, "회원가입이 되었습니다", Toast.LENGTH_SHORT).show();
                    finish();
                }
                cursor.close();
            }
        });

        //뒤로 가기
        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("회원가입");
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