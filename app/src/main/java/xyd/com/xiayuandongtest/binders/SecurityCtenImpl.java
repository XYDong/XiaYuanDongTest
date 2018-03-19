package xyd.com.xiayuandongtest.binders;

import android.os.RemoteException;

import xyd.com.xiayuandongtest.ISecurityCenter;

/**
 * Created by Administrator on 2018/1/28.
 */

public class SecurityCtenImpl extends ISecurityCenter.Stub {
    public static final char SECRET_CODE= '^';

    @Override
    public String encrypt(String content) throws RemoteException {
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] ^= SECRET_CODE;
        }
        return new String(chars);
    }

    @Override
    public String decypt(String password) throws RemoteException {
        return encrypt(password);
    }
}
