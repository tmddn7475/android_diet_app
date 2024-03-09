package kr.co.company.sw_team4.Activity;

import static kr.co.company.sw_team4.Internet.Get_Internet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import kr.co.company.sw_team4.DB.AccountDB;
import kr.co.company.sw_team4.Fragment.CalenderFragment;
import kr.co.company.sw_team4.Fragment.ExerciseFragment;
import kr.co.company.sw_team4.Fragment.FoodFragment;
import kr.co.company.sw_team4.Fragment.HomeFragment;
import kr.co.company.sw_team4.R;

public class MainActivity extends AppCompatActivity {

    AccountDB accountDB;
    SQLiteDatabase db;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    HomeFragment homeFragment;
    ExerciseFragment exerciseFragment;
    CalenderFragment calenderFragment;
    FoodFragment foodFragment;

    DrawerLayout drawerLayout;
    NavigationView sideNavigationView;
    ActionBarDrawerToggle drawerToggle;
    NavigationBarView bottomNavigationBarView;

    TextView header_name, header_email;
    String get_name, get_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // 툴바 설정

        // 프래그먼트 선언
        homeFragment = new HomeFragment();
        exerciseFragment = new ExerciseFragment();
        calenderFragment = new CalenderFragment();
        foodFragment = new FoodFragment();

        // 사이드 메뉴 바
        drawerLayout = findViewById(R.id.layout_draw);
        sideNavigationView = findViewById(R.id.sideNavigationView);
        drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerToggle.syncState(); //툴바에 사이드 메뉴 바 버튼 생성

        // 헤더 - 이름, 메일 선언
        View headerView = sideNavigationView.getHeaderView(0);
        header_name = headerView.findViewById(R.id.header_name);
        header_email = headerView.findViewById(R.id.header_mail);
        // 이메일 데이터 받기
        Intent intent = getIntent();
        get_email = intent.getStringExtra("email");

        accountDB = new AccountDB(this);
        db = accountDB.getWritableDatabase();
        // 데이터베이스에서 이메일과 같은 데이터가 담긴 행을 가져옴
        String sql = "select * from account where email = '" + get_email + "'";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            get_name = cursor.getString(3); }

        header_name.setText(get_name);
        header_email.setText(get_email);

        getSupportActionBar().setDisplayShowTitleEnabled(false); //액션바에 제목 표시 여부
            sideNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.side_account: {
                        Intent account = new Intent(MainActivity.this, ProfileActivity.class);
                        account.putExtra("email", get_email);
                        startActivity(account);
                        return false;
                    }
                    case R.id.side_logout: {
                        Intent logOut = new Intent(MainActivity.this, LoginActivity.class);
                        Toast.makeText(MainActivity.this, "로그아웃되었습니다", Toast.LENGTH_SHORT).show();
                        startActivity(logOut);
                        finishAffinity();
                        return false;
                    }
                    case R.id.side_calender:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, calenderFragment).commit();
                        bottomNavigationBarView.setSelectedItemId(R.id.bottom_calender);
                        drawerLayout.closeDrawer(sideNavigationView); //사이드 메뉴바 닫기
                        return false;
                    }
                    case R.id.side_food: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, foodFragment).commit();
                        bottomNavigationBarView.setSelectedItemId(R.id.bottom_food);
                        drawerLayout.closeDrawer(sideNavigationView);
                        return false;
                    }
                    case R.id.side_exercise: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, exerciseFragment).commit();
                        bottomNavigationBarView.setSelectedItemId(R.id.bottom_exercise);
                        drawerLayout.closeDrawer(sideNavigationView);
                        return false;
                    }
                    case R.id.side_goal: {
                        Intent goal = new Intent(MainActivity.this, GoalActivity.class);
                        goal.putExtra("email", get_email);
                        startActivity(goal);
                        return false;
                    }
                    case R.id.side_chart: {
                        Intent chart = new Intent(MainActivity.this, ChartActivity.class);
                        chart.putExtra("email", get_email);
                        startActivity(chart);
                        return false;
                    }
                    case R.id.side_setting: {
                        Intent setting2 = new Intent(MainActivity.this, SettingsActivity.class);
                        setting2.putExtra("email", get_email);
                        startActivity(setting2);
                        return false;
                    }
                }
                return false;
            }
        });

        preferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = preferences.edit();
        // preferences 초기값
        String weight = preferences.getString("weight", "0");
        String year = preferences.getString("year", "2023");
        String month = preferences.getString("month", "3");
        String day = preferences.getString("day", "13");

        editor.putString("weight", weight);
        editor.putString("year", year);
        editor.putString("month", month);
        editor.putString("day", day);
        editor.apply(); // 저장

        // 번들에 데이터를 저장
        Bundle bundle = new Bundle();
        bundle.putString("weight", weight);
        bundle.putString("year", year);
        bundle.putString("month", month);
        bundle.putString("day", day);
        bundle.putString("email", get_email);


        // 번들을 홈프래그먼트에 전달
        homeFragment.setArguments(bundle);

        // 메인 액티비티 처음 실행 시 기본 화면 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        // 하단 메뉴 바
        bottomNavigationBarView = findViewById(R.id.bottomNavigationView);
        bottomNavigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_home:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;}
                    case R.id.bottom_calender:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, calenderFragment).commit();
                        return true;}
                    case R.id.bottom_exercise:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, exerciseFragment).commit();
                        return true;}
                    case R.id.bottom_food:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, foodFragment).commit();
                        return true;}
                    }
                    return false;
                }
            });

        // 설정 들어가기
        ImageView settingImage = findViewById(R.id.setting_icon);
        settingImage.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                Intent setting = new Intent(MainActivity.this, SettingsActivity.class);
                setting.putExtra("email", get_email);
                startActivity(setting);
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

    //뒤로 가기 누를 때
    @Override
    public void onBackPressed() {
        // 사이드 메뉴바가 열려있을 때 닫히게 한다.
        if(drawerLayout.isDrawerOpen(sideNavigationView)){
            drawerLayout.closeDrawer(sideNavigationView);
        } else {
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
}

