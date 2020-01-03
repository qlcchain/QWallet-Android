package qlc.network;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcException;
import qlc.network.QlcHttpClient;
import qlc.network.QlcWebSocketClient;
import qlc.utils.Constants;
import qlc.utils.StringUtil;

public class QlcClient {
	
	private final String url;
	
	/**
	 * 
	 * <p>Title: </p> 
	 * <p>Description: </p> 
	 * @param url:node url
	 * @throws MalformedURLException Malformed URL Exception
	 */
	public QlcClient(String url) throws MalformedURLException {
		this.url = url;
	}
	
	public JSONObject call(String method, JSONArray params) throws IOException {
		
		if (StringUtil.isBlank(url))
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3001, Constants.EXCEPTION_SYS_MSG_3001);
		
		JSONObject reqParams = makeRequest(method, params);
		JSONObject response = null;
		if (url.toLowerCase().startsWith(Constants.URL_START_HTTP))
			response = httpReq(reqParams);
		else if (url.toLowerCase().startsWith(Constants.URL_START_WEB_SOCKET))
			response = wsReq(reqParams);
		else
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3002, Constants.EXCEPTION_SYS_MSG_3002);

		if (response == null)
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3003, Constants.EXCEPTION_SYS_MSG_3003);
		if (response.containsKey("result") || response.containsKey("error"))
			return response;
		else
			throw new IOException();
	}
	
	private JSONObject makeRequest(String method, JSONArray params) {
		
		JSONObject request = new JSONObject();
		request.put("jsonrpc", "2.0");
		request.put("id", UUID.randomUUID().toString().replace("-", ""));
		request.put("method", method);
		if (params!=null && !params.isEmpty())
			request.put("params", params);
		return request;
		
	}
	
	// http request
	private JSONObject httpReq(JSONObject reqParams) throws MalformedURLException {
		
		QlcHttpClient client = new QlcHttpClient(url);
		return client.send(reqParams);
		
	}
	
	// websocket reuest
	private JSONObject wsReq(JSONObject reqParams) {
		QlcWebSocketClient client = new QlcWebSocketClient();
		client.startRequest(url);
		client.sendMessage(reqParams.toJSONString());
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String result = QlcWebSocketClient.result;
		client.closeWebSocket();
		
		if (StringUtil.isNotBlank(result)) {
			try {
				JSONObject json = JSONObject.parseObject(result);
				return json;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
