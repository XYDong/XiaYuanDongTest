package xyd.com.xiayuandongtest.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 描述：
 * 作者：shuiq_000
 * 邮箱：2028318192@qq.com
 *
 * @version 1.0
 * @time: 2017/12/18 21:44
 */

public class MyUtils {
    public static void closeOutIO(ObjectOutputStream objectOutputStream) {
        if(objectOutputStream != null){
            try {
                objectOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void closeInIO(ObjectInputStream objectInputStream) {
        if(objectInputStream != null){
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
