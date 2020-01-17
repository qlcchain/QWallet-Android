package qlc.mng;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import qlc.bean.Token;
import qlc.network.QlcClient;

public class TokenMng {

	/**
	 * 
	 * Return token info by token id
     * @param client:qlc client
	 * @param tokenName:token name
	 * @throws IOException io exception 
	 * @return Token  
	 */
	public static Token getTokenByTokenName(QlcClient client, String tokenName) throws IOException {
		
		JSONArray params = new JSONArray();
		params.add(tokenName);
		
		JSONObject json = client.call("ledger_tokenInfoByName", params);
		if (json.containsKey("result")) {
			json = json.getJSONObject("result");
			return new Gson().fromJson(json.toJSONString(), Token.class);
		}
		
		return null;
	}
}
