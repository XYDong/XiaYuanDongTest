package xyd.com.xiayuandongtest.tcp_service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyd.com.xiayuandongtest.R;
import xyd.com.xiayuandongtest.utils.MyUtils;

public class MsgActivity extends Activity {

    @BindView(R.id.btn_send)
    Button btnSend;

    public static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    public static final int MESSAGE_SOCKET_CONNECTED = 2;
    @BindView(R.id.et_body)
    EditText etBody;
    @BindView(R.id.tv_msg)
    TextView tvMsg;
    private Socket mClientSocket;
    private MyHandler myHandler;
    private PrintWriter printWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        ButterKnife.bind(this);
        myHandler = new MyHandler(this);
        Intent intent = new Intent(this, TCPService.class);
        startService(intent);
        new Thread(){
            @Override
            public void run() {
                connectTCPServer();
            }
        }.start();
    }

    private void connectTCPServer() {

        Socket socket = null;
        while(socket == null){
            try {
                socket = new Socket("localhost", 8869);
                mClientSocket = socket;
                printWriter = new PrintWriter(
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),true);
                myHandler.sendEmptyMessage(MESSAGE_RECEIVE_NEW_MSG);
                System.out.println("connect server success");

            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed, retry...");
            }
        }

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!MsgActivity.this.isFinishing()){
                String readLine = bufferedReader.readLine();
                System.out.println("receive:"+readLine);
                if(readLine != null){
                    String time = formatDateTime(System.currentTimeMillis());
                    String showMsg = "server" + time + " : " + readLine +"\n";
                    myHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG,showMsg).sendToTarget();
                }
            }
            System.out.println("quit...");
            MyUtils.close(printWriter);
            MyUtils.close(bufferedReader);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String formatDateTime(long l) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(l));
    }


    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        String body = etBody.getText().toString();
        if(!TextUtils.isEmpty(body)){
            printWriter.println(body);
            etBody.setText("");
            String time = formatDateTime(System.currentTimeMillis());
            String showMsg = "self" + time + " : " + body + "\n";
            tvMsg.setText(tvMsg.getText() + showMsg);
        }

    }


    /**
     * 声明一个静态的Handler内部类，并持有外部类的弱引用
     */
    private class MyHandler extends Handler {

        private final WeakReference<MsgActivity> mActivty;

        private MyHandler(MsgActivity mActivty) {
            this.mActivty = new WeakReference<MsgActivity>(mActivty);
        }

        @Override
        public void handleMessage(Message msg) {
            MsgActivity activity = mActivty.get();
            if (activity != null) {
                switch (msg.what) {
                    case MESSAGE_RECEIVE_NEW_MSG:
                        tvMsg.setText(etBody.getText()+ (String)msg.obj);
                        break;
                    case MESSAGE_SOCKET_CONNECTED:
                        btnSend.setEnabled(true);
                        break;
                    default:
                        super.handleMessage(msg);
                        break;
                }
            }
        }
    }

}
