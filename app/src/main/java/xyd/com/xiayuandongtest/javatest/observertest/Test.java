package xyd.com.xiayuandongtest.javatest.observertest;

public class Test {
    public static void main(String[] args) {
        User user = new User();
        WechatServer wechatServer = new WechatServer();
        wechatServer.registerObserver(user);
        wechatServer.setMessage("98K了解一下");
    }
}
