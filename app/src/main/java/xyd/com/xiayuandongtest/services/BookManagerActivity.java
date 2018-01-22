package xyd.com.xiayuandongtest.services;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import java.util.List;

import xyd.com.xiayuandongtest.IBookManager;
import xyd.com.xiayuandongtest.IOnNewBookArrivedListener;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.entity.Book;
import xyd.com.xiayuandongtest.utils.LogUtils;

public class BookManagerActivity extends Activity {



    public static final int MESSAGE_NEW_BOOK_ARRIVED = 1;

    private IBookManager iBookManager;

    /**
     * binder 死亡代理
     */
    private IBinder.DeathRecipient deathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if(iBookManager == null){
                return;
            }else {
                iBookManager.asBinder().unlinkToDeath(deathRecipient,0);
                iBookManager = null;
                //重新绑定远程service
                LogUtils.i("DeathRecipient，service Disconnected");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    LogUtils.i("receive new book : " + msg.obj);

                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBookManager = IBookManager.Stub.asInterface(service);
            IBookManager.Stub.asInterface(service);
            try {
                service.linkToDeath(deathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            try {
                List<Book> listBook = iBookManager.getListBook();
                LogUtils.i("query book list, list type:"+listBook.getClass().getCanonicalName());

                LogUtils.i("query book list:"+listBook.toString());


                Book book = new Book(3,"亲热天堂","自来也");
                iBookManager.addBook(book);
                LogUtils.i("add book:"+book.toString());
                List<Book> listBook1 = iBookManager.getListBook();
                LogUtils.i("query listBook1 list:"+listBook1.toString());
                iBookManager.registerListener(myListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            iBookManager = null;
            LogUtils.e("binder died.");
        }
    };

    private IOnNewBookArrivedListener myListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book newBook) throws RemoteException {
            handler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,newBook).sendToTarget();
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_manager);
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if(iBookManager != null && iBookManager.asBinder().isBinderAlive()){
            LogUtils.i("unregister listener:"+ BookManagerActivity.this);
            try {
                iBookManager.unregisterListener(myListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mConnection);
        super.onDestroy();
    }

}
