package xyd.com.xiayuandongtest.binders;

/**
 * Created by Administrator on 2018/1/22.
 */

public interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
