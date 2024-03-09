package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.company.sw_team4.R;

public class IntroActivity extends AppCompatActivity {

    // 인트로 화면
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        int internet = Get_Internet(this);
        if(internet == 0){
            //인터넷 연결 안 될 시 종료시키기
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
        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1500); //1.5초 후 로그인 액티비티로 넘어감
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}