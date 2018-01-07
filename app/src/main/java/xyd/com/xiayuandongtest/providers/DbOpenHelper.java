package xyd.com.xiayuandongtest.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/1/6.
 */

public class DbOpenHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "book_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    public static final int DB_VERSION = 2;

    //图书和用户信息表
    private String CREATE_BOOK_TABLE = "CREATE TABLE IF NOT EXISTS " + BOOK_TABLE_NAME + " ( _id INTEGER PRIMARY KEY," +
            "bookName TEXT , author TEXT)";

    private String CREATE_TUSER_ABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE_NAME + " (_id INTEGER PRIMARY KEY, " +
            "name TEXT," + "sex INTEGER)";

    public DbOpenHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TUSER_ABLE);
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
