package xyd.com.xiayuandongtest.manualbinder;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

import xyd.com.xiayuandongtest.entity.Book;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/18 20:56
 */

public class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this,DESCRIPTOR);
    }

    @Override
    public List<Book> getBookList() throws RemoteException {
        return null;
    }

    @Override
    public void addBook(Book book) throws RemoteException {

    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                break;
            case TRANSACTION_getBookList :
                data.enforceInterface(DESCRIPTOR);
                List<Book> bookList = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(bookList);
                return true;
            case TRANSACTION_addBook :
                data.enforceInterface(DESCRIPTOR);
                Book book0;
                if(data.readInt() != 0){
                    book0 = Book.CREATOR.createFromParcel(data);
                }else {
                    book0 = null;
                }
                this.addBook(book0);
                reply.writeNoException();
                return true;
        }


        return super.onTransact(code, data, reply, flags);
    }
}
