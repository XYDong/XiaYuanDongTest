package xyd.com.xiayuandongtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/11 22:16
 */

public class Book implements Parcelable {
    private int id;
    private String bookName;
    private String author;


    protected Book(Parcel in) {
        id = in.readInt();
        bookName = in.readString();
        author = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(bookName);
        dest.writeString(author);
    }
}
