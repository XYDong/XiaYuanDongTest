package xyd.com.xiayuandongtest.binders;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/1/28.
 */

public class IBinderPool implements xyd.com.xiayuandongtest.IBinderPool {

    public static final int BINDER_SECURITY_CENTER = 10010;
    public static final int BINDER_COMPUTE = 10011;


    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case BINDER_SECURITY_CENTER:
                binder = new SecurityCtenImpl();
                break;
            case BINDER_COMPUTE:
                binder = new ComputeImpl();
                break;
                default: break;
        }
        return  binder;
    }

    @Override
    public IBinder asBinder() {
        return null;
    }
}
