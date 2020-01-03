package qlc.mng;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import qlc.bean.StateBlock;
import qlc.network.QlcClient;
import qlc.utils.Helper;

public class RewardsMng {

	/**
	 * 
	 * returns airdrop contract reward block by contract send block hash
     * @param client:qlc client
	 * @param blockHash send block hash
	 * @throws IOException io exception 
	 * @return StateBlock:contract reward block
	 */
	public static StateBlock getReceiveRewardBlock(QlcClient client, byte[] blockHash) throws IOException {

    	if (blockHash == null)
    		return null;
		
		JSONArray params = new JSONArray();
		params.add(Helper.byteToHexString(blockHash));
		
		JSONObject json = client.call("rewards_getReceiveRewardBlock", params);
		if (json.containsKey("result")) {
			json = json.getJSONObject("result");
			return new Gson().fromJson(json.toJSONString(), StateBlock.class);
		}
		
		return null;
	}

}
