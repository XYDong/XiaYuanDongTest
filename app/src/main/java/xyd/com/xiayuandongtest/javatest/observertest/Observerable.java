package xyd.com.xiayuandongtest.javatest.observertest;

/**
 - @Description:  发布者
 - @Author:  xyd
 - @Time:  2018/5/18 10:27
 */

public interface Observerable {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObserver();

}
