package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;

public class P2PRpc extends QlcRpc {
	
    public P2PRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
     * Return online representative accounts that have voted recently
     * @param params null
     * @return []address: addresses list for representative accounts
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject onlineRepresentatives(JSONArray params) throws IOException {
        return client.call("net_onlineRepresentatives", params);
    }
    
}
