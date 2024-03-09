package kr.co.company.sw_team4.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WakDB extends SQLiteOpenHelper {

    public WakDB(Context context) {
        super(context, "WAK.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table wak(DATE text, WEIGHT real, KCAL real, EMAIL text);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS wak;");
        onCreate(db);
    }

    public void insertData(String date, float weight, float kcal, String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("DATE", date);
        contentValues.put("WEIGHT", weight);
        contentValues.put("KCAL", kcal);
        contentValues.put("EMAIL", email);
        sqLiteDatabase.insert("wak", null, contentValues);
    }

    public void deleteData(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM wak WHERE EMAIL = '" + email + "';");
    }
}
