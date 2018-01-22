package xyd.com.xiayuandongtest.tcp_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import xyd.com.xiayuandongtest.utils.MyUtils;

/**
 * Created by Administrator on 2018/1/7.
 */

public class TCPService extends Service {

    public static final String TAG = "TCPService";
    public static final int port = 8688;
    private boolean mIsServiceDestoryed = false;
    private String[] mDefinedMessages = new String[]{
            "你好啊，哈哈",
            "请问你叫什么名字？",
            "今天北京天气怎么样？",
            "你知道吗？我可是可以和多人同时聊天的",
            "给你讲个笑话吧。。。。。"
    };

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    private class TcpServer implements Runnable{

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                System.out.println("tcpservice start");
            } catch (IOException e) {
                System.out.println("establelish tcp server failed, port :" + port);
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestoryed){
                try {
                    Socket socketClient = serverSocket.accept();
                    System.out.println("accpt");
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                responseClient(socketClient);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void responseClient(Socket socketClient) throws IOException{
            //用于接受客户端消息
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            //用于向客户端发消息
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream())), true);

            System.out.println("欢迎来到聊天室");

            while (!mIsServiceDestoryed){
                String readLine = bufferedReader.readLine();
                System.out.println("msg from client :" + readLine);
                if(readLine == null){
                    //客户端
                    break;
                }

                int nextInt = new Random().nextInt(mDefinedMessages.length);
                String msg = mDefinedMessages[nextInt];
                printWriter.println(msg);
                System.out.println("msg : " + msg);
            }
            System.out.println("clent quit.");
            //关闭流
            MyUtils.close(bufferedReader);
            MyUtils.close(printWriter);
            socketClient.close();
        }
    }

}
