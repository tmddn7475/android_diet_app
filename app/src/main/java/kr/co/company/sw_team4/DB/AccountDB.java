package kr.co.company.sw_team4.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDB extends SQLiteOpenHelper {

    public AccountDB(Context context) {
        super(context, "account.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table account (email text not null primary key, id text not null, pwd text not null, " +
                "name text not null, age text not null);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account;");
        onCreate(db);
    }

    public void signUp(String email, String id, String pwd, String name, String age){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("email", email);
        contentValues.put("id", id);
        contentValues.put("pwd", pwd);
        contentValues.put("name", name);
        contentValues.put("age", age);
        sqLiteDatabase.insert("account", null, contentValues);
    }

    // 계정 삭제
    public void deleteAccount(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from account where email = '" + email + "';");
    }
}