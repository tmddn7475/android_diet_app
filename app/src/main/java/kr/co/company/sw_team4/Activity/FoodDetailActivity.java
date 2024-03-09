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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import kr.co.company.sw_team4.DB.ExerciseDB;
import kr.co.company.sw_team4.DB.FoodDB;
import kr.co.company.sw_team4.R;

public class FoodDetailActivity extends AppCompatActivity {

    FoodDB foodDB;
    SQLiteDatabase db;
    String detail_name, detail_unit, detail_kcal, detail_carb,
            detail_protein, detail_fat, detail_sugars, detail_sodium, detail_unit2;
    TextView name, unit, kcal, carb, protein, fat, sugars, sodium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        Intent intent = getIntent();
        detail_name = intent.getStringExtra("name");

        foodDB = new FoodDB(this);
        db = foodDB.getWritableDatabase();
        // 데이터베이스에서 detail_name과 같은 이름이 담긴 행 가져오기
        Cursor cursor = db.rawQuery("select * from Food where NAME = '" + detail_name + "'", null);
        while (cursor.moveToNext()) {
            detail_unit = cursor.getString(2);
            detail_kcal = cursor.getString(3);
            detail_carb = cursor.getString(4);
            detail_protein = cursor.getString(5);
            detail_fat = cursor.getString(6);
            detail_sugars = cursor.getString(7);
            detail_sodium = cursor.getString(8);
            detail_unit2 = cursor.getString(9);
        }

        name = findViewById(R.id.food_name);
        name.setText(detail_name);

        unit = findViewById(R.id.food_unit);
        unit.setText(detail_unit + detail_unit2);

        kcal = findViewById(R.id.food_kcal);
        kcal.setText(detail_kcal);

        carb = findViewById(R.id.food_carb);
        carb.setText(detail_carb);

        protein = findViewById(R.id.food_protein);
        protein.setText(detail_protein);

        fat = findViewById(R.id.food_fat);
        fat.setText(detail_fat);

        sugars = findViewById(R.id.food_sugars);
        sugars.setText(detail_sugars);

        sodium = findViewById(R.id.food_sodium);
        sodium.setText(detail_sodium);

        // 뒤로 가기
        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("영양성분");

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