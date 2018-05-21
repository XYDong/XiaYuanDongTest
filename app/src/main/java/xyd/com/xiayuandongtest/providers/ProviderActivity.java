package xyd.com.xiayuandongtest.providers;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;

import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.entity.Book;
import xyd.com.xiayuandongtest.entity.User;

public class ProviderActivity extends Activity {
    public static final String AUTHORITY = "xyd.com.xiayuandongtest.providers";
//    public static final  Uri bookUri = Uri.parse("content://" + AUTHORITY + "/book");
//    public static final  Uri userUri = Uri.parse("content://" + AUTHORITY + "/user");

    Uri bookUri = Uri.parse("content://xyd.com.xiayuandongtest.providers/book");
    Uri userUri = Uri.parse("content://xyd.com.xiayuandongtest.providers/user");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
//        Uri parse = Uri.parse("content://xyd.com.xiayuandongtest.providers");
//        getContentResolver().query(parse, null, null, null,null);
//        getContentResolver().query(parse, null, null, null,null);
//        getContentResolver().query(parse, null, null, null,null);

        ContentValues bookContentValues = new ContentValues();
        bookContentValues.put("_id",6);
        bookContentValues.put("bookName","人性的优点");
        bookContentValues.put("author","卡耐基");
        getContentResolver().insert(bookUri,bookContentValues);
        Cursor bookCousor = getContentResolver().query(bookUri, new String[]{"_id", "bookName","author"},
                null, null, null);
        while (bookCousor.moveToNext()){
            int bookCousorInt = bookCousor.getInt(0);
            String bookCousorString = bookCousor.getString(1);
            String bookCousorString2 = bookCousor.getString(2);
            Book book = new Book(bookCousorInt,bookCousorString,bookCousorString2);
            LogUtils.i("providerActivity query book :" + book.toString());
        }

        bookCousor.close();

        Cursor userCousor = getContentResolver().query(userUri, new String[]{"_id", "name"},
                null, null, null);
        while (userCousor.moveToNext()){
            int userCousorInt = userCousor.getInt(0);
            String userCousorString = userCousor.getString(1);
            User user = new User(userCousorInt,userCousorString,false);
            LogUtils.i("providerActivity query user :" + user.toString());
        }
        userCousor.close();

        int delete = getContentResolver().delete(bookUri, "_id=6", null);
        Cursor bookCousor2 = getContentResolver().query(bookUri, new String[]{"_id", "bookName","author"},
                null, null, null);
        while (bookCousor2.moveToNext()){
            int bookCousorInt = bookCousor2.getInt(0);
            String bookCousorString = bookCousor2.getString(1);
            String bookCousorString2 = bookCousor2.getString(2);
            Book book = new Book(bookCousorInt,bookCousorString,bookCousorString2);
            LogUtils.i("删除后===providerActivity query book :" + book.toString());
        }
        bookCousor2.close();

    }
}
