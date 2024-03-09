package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import kr.co.company.sw_team4.R;
import kr.co.company.sw_team4.Fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Fragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_settings, settingsFragment).commit();

        Intent account = getIntent();
        String get_email = account.getStringExtra("email");

        Bundle bundle = new Bundle();
        bundle.putString("email", get_email);

        settingsFragment.setArguments(bundle);

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("설정");
        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish_setting();
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

    // 뒤로가기 버튼 누를 때
    @Override
    public void onBackPressed() {
        finish_setting();
    }

    public void finish_setting() {
        // 화면 전환 될 때 이메일, 이름이 안 나오는 현상 발생, 이를 고치기 위해 intent 활용
        Intent account = getIntent();
        String get_email = account.getStringExtra("email");

        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("email", get_email);
        startActivity(intent);
        finishAffinity();
    }
}