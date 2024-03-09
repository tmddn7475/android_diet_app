package kr.co.company.sw_team4.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "memo.db";

    public MemoDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table myDiary(diaryDate text, content text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS myDiary;");
        onCreate(db);
    }
}
