package com.stratagile.qlc;

import com.socks.library.KLog;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;
import qlc.network.Message;

public class ReceiveMessage implements Message {


    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        KLog.i("连接成功。。");
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        KLog.i("接收到消息：" + s);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString byteString) {

    }

    @Override
    public void onClosing(WebSocket webSocket, int i, String s) {

    }

    @Override
    public void onClosed(WebSocket webSocket, int i, String s) {

    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable throwable, Response response) {

    }
}
