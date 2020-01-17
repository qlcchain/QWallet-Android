package qlc.mng;

import java.io.IOException;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import qlc.bean.Account;
import qlc.bean.TokenMeta;
import qlc.network.QlcClient;

public class TokenMetaMng {

	/**
	 * 
	 * Return account`s token meta info by address and token hash
     * @param client:qlc client
	 * @param tokenHash:the token id
	 * @param address:the account address
	 * @return TokenMeta token info for the account and token hash
	 * @throws IOException io exception
	 */
	public static TokenMeta getTokenMeta(QlcClient client, String tokenHash, String address) throws IOException {
		
		JSONArray params = new JSONArray();
		params.add(address);
		
		JSONObject json = client.call("ledger_accountInfo", params);
		if (json.containsKey("result")) {
			
			json = json.getJSONObject("result");
			
			Account bean = new Gson().fromJson(json.toJSONString(), Account.class);
			List<TokenMeta> tokens = bean.getTokens();
			if (tokens!=null && tokens.size()>0) {
				TokenMeta token = null;
				for (int i=0; i<tokens.size(); i++) {
					token = tokens.get(i);
					if (token.getType().equals(tokenHash))
						return token;
					
					token = null;
				}
			}
		}
		
		return null;
	}

	/**
	 * 
	 * Return account detail info, include each token in the account
	 * @param client:qlc client
	 * @param address:the account address
	 * @return Account  
	 * account : the account address
		coinBalance : balance of main token of the account (default is QLC)
		vote,network,storage,oracle: benefit for the account, if account don't have token QLC, these are omit
		representative : representative address of the account
		[]token: each token info for the account,
	 * @throws IOException ip exception
	 */
	public static Account getAccountMeta(QlcClient client, String address) throws IOException {
		
		JSONArray params = new JSONArray();
		params.add(address);
		
		JSONObject json = client.call("ledger_accountInfo", params);
		if (json.containsKey("result")) {
			
			json = json.getJSONObject("result");
			return new Gson().fromJson(json.toJSONString(), Account.class);
			
		}
		
		return null;
	}
	
}
