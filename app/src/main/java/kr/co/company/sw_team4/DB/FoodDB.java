package kr.co.company.sw_team4.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FoodDB extends SQLiteOpenHelper {
    // database 의 파일 경로
    private static String DB_PATH = "/data/data/kr.co.company.sw_team4/databases/";
    public static final String DATABASE_NAME = "food.db";
    private Context mContext;

    public FoodDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
        dataBaseCheck();
    }

    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        // DB가 있는지 확인
        // 파일 경로 안에 DB가 없으면 DB 복사
        if (!dbFile.exists()) {
            dbCopy();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    // 안드로이드 assets 파일 내에 db 복사
    private void dbCopy() {
        try {
            File folder = new File(DB_PATH);
            //folder가 없으면 folder 생성
            if (!folder.exists()) {
                folder.mkdir();
            }
            InputStream inputStream = mContext.getAssets().open(DATABASE_NAME);
            String out_filename = DB_PATH + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer,0, mLength);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

