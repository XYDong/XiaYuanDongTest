package xyd.com.xiayuandongtest.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/11 22:03
 */

public class User implements Parcelable {
    private int id;
    private String name;
    private  boolean isMale;

//    private Book book;

    public User(int id, String name, boolean isMale) {
        this.id = id;
        this.name = name;
        this.isMale = isMale;
    }

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        isMale = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeByte((byte) (isMale ? 1 : 0));
//        dest.writeParcelable(book,0);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isMale=" + isMale +
                '}';
    }
}
