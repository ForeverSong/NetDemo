package com.sgbr.netdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FindServerActivity extends AppCompatActivity {

    public static final String TAG = "FindActivity";
    public static final int tv_showServer = 0;
    public static final int ed_ip = 1;
    private ExecutorService executor;

    // 组件
    private EditText med_Ip;
    private TextView mtv_showServer;

    // 网络相关
    private DatagramSocket ds;
    private ThreadFindServer tfs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_server);

        // 1、初始化组件
        Button mBtnFind = findViewById(R.id.btn_search);
        Button mBtnConnect = findViewById(R.id.btn_connect);
        mtv_showServer = findViewById(R.id.tv_show);
        med_Ip = findViewById(R.id.ed_ip);
        EditText med_Port = findViewById(R.id.ed_port);

        med_Ip.setText(getIP());

        // 2、初始化线程池
        executor = Executors.newCachedThreadPool();

        // 3、初始化网络相关参数
        try {
            ds = new DatagramSocket();
        } catch (IOException e) {
            Log.e(FindServerActivity.TAG, "创建ds失败，" + e.getMessage());
        }
        tfs = new ThreadFindServer("", med_Ip.getText().toString(), Integer.parseInt(med_Port.getText().toString()), ds);
        executor.execute(new ThreadListenServer(mHandler, ds));

        // 4、设置事件
        // 事件相关
        MyOnclick myOnclick = new MyOnclick();
        mBtnFind.setOnClickListener(myOnclick);
        mBtnConnect.setOnClickListener(myOnclick);

    }


    class MyOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_search:
                    mtv_showServer.setText("服务器列表：\n");
                    executor.execute(tfs);
                    Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btn_connect:
                    Toast.makeText(getApplicationContext(), "功能开发中……", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }


    // 只有在自己的线程中才能操作
    private Handler mHandler = new Handler(Objects.requireNonNull(Looper.myLooper())) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case tv_showServer:
                    mtv_showServer.append(msg.obj + "\n");
                    break;
                case ed_ip:
                    med_Ip.setText((String) msg.obj);
                    break;
            }
        }
    };

    // 获取本机ip，并且生成广播地址
    private static String getIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        String hostAddress = inetAddress.getHostAddress();
                        hostAddress = hostAddress.substring(0, hostAddress.lastIndexOf(".")) + ".255";
                        return hostAddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("WifiPreference IpAddress", ex.toString());
        }
        return null;
    }
}
