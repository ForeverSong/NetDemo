package com.sgbr.netdemo;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ThreadListenServer implements Runnable {
    private DatagramSocket ds;
    private Handler mhandler;

    ThreadListenServer(Handler mHandler, DatagramSocket ds) {
        this.mhandler = mHandler;
        this.ds = ds;
    }

    @Override
    public void run() {
        byte[] bys = new byte[8];
        DatagramPacket dp = new DatagramPacket(bys, bys.length);

        // 循环监听
        while (true) {
            try {
                ds.receive(dp);
                /*
                 * Only the original thread that created a view hierarchy can touch its views
                 * 不能直接访问UI线程
                 * showView.append(dp.getAddress().getHostAddress() + "\n");
                 */
                Message msg = new Message();
                msg.what = FindServerActivity.tv_showServer;
                msg.obj = dp.getAddress().getHostAddress();
                mhandler.sendMessage(msg);
            } catch (IOException e) {
                Log.e(FindServerActivity.TAG, "接收失败，" + e.getMessage());
            }
        }
    }
}
