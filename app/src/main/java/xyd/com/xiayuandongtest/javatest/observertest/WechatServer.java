package xyd.com.xiayuandongtest.javatest.observertest;

import java.util.ArrayList;
import java.util.List;

public class WechatServer implements Observerable {
    //注意到这个List集合的泛型参数为Observer接口，设计原则：面向接口编程而不是面向实现编程
    private List<Observer> list;
    private String message;

    public void setMessage(String message) {
        this.message = message;
        System.out.println("微信服务更新消息： " + message);
        //消息更新，通知所有观察者
        notifyObserver();
    }

    public WechatServer() {
        list = new ArrayList<>();
    }

    @Override
    public void registerObserver(Observer o) {
        list.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        if (list != null && list.size() > 0) {
            list.remove(o);
        }
    }

    @Override
    public void notifyObserver() {
        if(list == null || list.size() == 0 ){
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Observer observer = list.get(i);
            observer.update(message);
        }
    }
}
