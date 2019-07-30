package com.stratagile.qlc;

import com.socks.library.KLog;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class QlcWebSocketLister extends WebSocketListener {

    private WebSocket webSocket;
    private OkHttpClient okHttpClient;

    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public QlcWebSocketLister(String url) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }

        if (webSocket == null) {
            Request request = (new Request.Builder()).url(url).build();
            webSocket = okHttpClient.newWebSocket(request, this);
        }
    }

    public boolean sendMessage(String content) {
       return webSocket.send(content);
    }

    public boolean disConnect() {
        if (webSocket != null) {
            return webSocket.close(1000, "GoogBye");
        }
        return false;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        super.onMessage(webSocket, text);
        KLog.i("websocket接收到消息: " + text);
        message.onMessage(text);
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        KLog.i("连接成功。。。");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        super.onClosing(webSocket, code, reason);
    }

    public interface Message {
        void onMessage(String content);
    }

}
