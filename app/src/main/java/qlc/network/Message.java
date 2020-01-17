package qlc.network;

import okhttp3.Response;
import okhttp3.WebSocket;
import okio.ByteString;

public interface Message {
	
	public void onOpen(WebSocket webSocket, Response response);
	public void onMessage(WebSocket webSocket, String text);
	public void onMessage(WebSocket webSocket, ByteString bytes);
	public void onClosing(WebSocket webSocket, int code, String reason);
	public void onClosed(WebSocket webSocket, int code, String reason);
	public void onFailure(WebSocket webSocket, Throwable t, Response response);
	
}
