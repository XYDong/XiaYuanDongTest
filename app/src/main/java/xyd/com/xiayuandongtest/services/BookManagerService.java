package xyd.com.xiayuandongtest.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import xyd.com.xiayuandongtest.IBookManager;
import xyd.com.xiayuandongtest.IOnNewBookArrivedListener;
import xyd.com.xiayuandongtest.entity.Book;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BookManagerService extends Service {

    public static final String TAG = "BMS";

    RemoteCallbackList<IOnNewBookArrivedListener> listeners = new RemoteCallbackList<>();

    private   CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

    private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
//    private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getListBook() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
        }

        @SuppressLint("NewApi")
        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            listeners.register(listener);
            LogUtils.d("ungisterListener, current size:"+listeners.getRegisteredCallbackCount());


          /*  if(!listeners.contains(listener)){
                listeners.add(listener);
            }else
                LogUtils.i("registerListener,size:"+listeners.size());

            LogUtils.d("ungisterListener, current size:"+listeners.size());*/

        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            if(listeners.unregister(listener)){
//                listeners.remove(listener);
                LogUtils.i("unregister listener succesed.");
            }else
                LogUtils.i("not found , can not unregister");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1,"Android","xyd"));
        mBookList.add(new Book(2,"ios","xyd"));
        new Thread(new ServiceWorkThread()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class ServiceWorkThread implements Runnable{

        @Override
        public void run() {
            while (!mIsServiceDestoryed.get()){
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int bookId = mBookList.size() + 1;
                Book book = new Book(bookId,"亲热天堂"+bookId,"自来也"+bookId);
                try {
                    onNewBookArrived(book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 有新书添加
     * @param book
     */
    @SuppressLint("NewApi")
    private void onNewBookArrived(Book book) throws Exception{
        mBookList.add(book);
        LogUtils.i("onNewBookArrived,notify listener:"+listeners.getRegisteredCallbackCount());
        for (int i = 0; i < listeners.getRegisteredCallbackCount(); i++) {
            IOnNewBookArrivedListener listener = listeners.getBroadcastItem(i);
            LogUtils.d("onNewBookArrived, notify listener:"+listener);
            listener.onNewBookArrived(book);
        }

    }


}
