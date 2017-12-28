// IOnNewBookArrivedListener.aidl
package xyd.com.xiayuandongtest;
import xyd.com.xiayuandongtest.entity.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {

    void onNewBookArrived(in Book newBook);

}
