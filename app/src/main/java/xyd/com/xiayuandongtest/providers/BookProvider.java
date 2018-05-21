package xyd.com.xiayuandongtest.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2018/1/3 10:58
 */

public class BookProvider extends ContentProvider {
    public static final String TAG = "bookprovider";

    public static final String AUTHORITY = "xyd.com.xiayuandongtest.providers";
    public static final Uri BOOK_CINTENT_URI = Uri.parse("content://" + AUTHORITY + "/book");
    public static final Uri USER_CINTENT_URI = Uri.parse("content://" + AUTHORITY + "/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private Context context;
    private SQLiteDatabase dbOpenHelper;

    @Override
    public boolean onCreate() {
        LogUtils.i(TAG,"onCreate, current thread:"+Thread.currentThread().getName());
        context = getContext();
        initProviderData();

        return true;
    }

    private void initProviderData() {
        dbOpenHelper = new DbOpenHelper(context).getWritableDatabase();
        dbOpenHelper.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME);
        dbOpenHelper.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME);
        dbOpenHelper.execSQL("insert into book values(3,'亲热堂','卡卡西')");
        dbOpenHelper.execSQL("insert into book values(4,'mysql从删库到跑路','切格瓦拉')");
        dbOpenHelper.execSQL("insert into book values(5,'演员的自我修养','周星星')");
        dbOpenHelper.execSQL("insert into user values(1,'卡卡西',0)");
        dbOpenHelper.execSQL("insert into user values(2,'娜美',1)");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LogUtils.i("query, current thread:"+Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("UnSupported URI:" + uri);
        }
        return dbOpenHelper.query(tableName,projection,selection,selectionArgs,null,null,sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        LogUtils.i("getType, current thread:"+Thread.currentThread().getName());

        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        LogUtils.i("insert, current thread:"+Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URL:" + uri);
        }

        dbOpenHelper.insert(tableName, null, values);
        context.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        LogUtils.i("delete, current thread:"+Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URL:" + uri);
        }
        int delete = dbOpenHelper.delete(tableName, selection, selectionArgs);
        if(delete > 0){
            context.getContentResolver().notifyChange(uri,null);
        }
        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        LogUtils.i("update, current thread:"+Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if(tableName == null){
            throw new IllegalArgumentException("Unsupported URL:" + uri);
        }
        int update = dbOpenHelper.update(tableName, values, null, null);
        if (update > 0)
            context.getContentResolver().notifyChange(uri,null);
        return update;
    }

    public String getTableName(Uri uri){
        String tabName = null;
        switch (sUriMatcher.match(uri)) {
            case USER_URI_CODE:
                tabName = DbOpenHelper.USER_TABLE_NAME;
                break;
            case BOOK_URI_CODE:
                tabName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            default: break;
        }
        return tabName;
    }

}
