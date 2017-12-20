package xyd.com.xiayuandongtest.manualbinder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import java.util.List;

import xyd.com.xiayuandongtest.entity.Book;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/18 20:46
 */

public interface IBookManager extends IInterface {

    public static final String DESCRIPTOR ="xyd.com.xiayuandongdongtest.manualbinder.IBookManger";

    public static final int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    public static final int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;



    public List<Book> getBookList() throws RemoteException;
    public void addBook(Book book) throws RemoteException;




}
