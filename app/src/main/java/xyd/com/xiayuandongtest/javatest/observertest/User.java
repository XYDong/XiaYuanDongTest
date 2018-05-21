package xyd.com.xiayuandongtest.javatest.observertest;


public class User implements Observer {
    @Override
    public void update(String message) {
        System.out.println("收到消息：" + message);
    }
}
