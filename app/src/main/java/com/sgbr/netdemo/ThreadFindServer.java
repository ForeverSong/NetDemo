package com.sgbr.netdemo;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

class ThreadFindServer implements Runnable {

    private int port;
    private String msg;
    private String ip;
    private DatagramSocket ds;

    ThreadFindServer(String msg, String ip, int port, DatagramSocket ds) {
        this.msg = msg;
        this.ip = ip;
        this.port = port;
        this.ds = ds;
    }

    private void send(String msg, String ip, int port) throws IOException {
        DatagramPacket dp = new DatagramPacket(msg.getBytes(), msg.getBytes().length, new InetSocketAddress(ip, port));
        ds.send(dp);
    }

    @Override
    public void run() {
        try {
            this.send(msg, ip, port);
        } catch (IOException e) {
            Log.e(FindServerActivity.TAG, "发送出错：" + e.getMessage());
        }
    }
}
