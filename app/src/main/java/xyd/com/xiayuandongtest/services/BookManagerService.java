package xyd.com.xiayuandongtest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import xyd.com.xiayuandongtest.IBookManager;
import xyd.com.xiayuandongtest.IOnNewBookArrivedListener;
import xyd.com.xiayuandongtest.entity.Book;
import xyd.com.xiayuandongtest.utils.LogUtils;

/**
 * Created by Administrator on 2017/12/25.
 */

public class BookManagerService extends Service {

    public static final String TAG = "BMS";

  private   CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<Book>();

  private AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);
  private CopyOnWriteArrayList<IOnNewBookArrivedListener> listeners = new CopyOnWriteArrayList<>();

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

      @Override
      public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
          if(!listeners.contains(listener)){
              listeners.add(listener);
          }else
              LogUtils.i("registerListener,size:"+listeners.size());

          LogUtils.d("ungisterListener, current size:"+listeners.size());

      }

      @Override
      public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
        if(listeners.contains(listener)){
            listeners.remove(listener);
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
    private void onNewBookArrived(Book book) throws Exception{
        mBookList.add(book);
        LogUtils.i("onNewBookArrived,notify listener:"+listeners.size());
        for (int i = 0; i < listeners.size(); i++) {
            IOnNewBookArrivedListener listener = listeners.get(i);
            LogUtils.d("onNewBookArrived, notify listener:"+listener);
            listener.onNewBookArrived(book);
        }

    }


}
