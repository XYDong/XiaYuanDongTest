package xyd.com.xiayuandongtest.binders;

import android.os.RemoteException;

import xyd.com.xiayuandongtest.ICompute;

/**
 * Created by Administrator on 2018/1/28.
 */

public class ComputeImpl extends ICompute.Stub {


    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
