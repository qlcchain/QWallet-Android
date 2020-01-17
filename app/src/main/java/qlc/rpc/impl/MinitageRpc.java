package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;

public class MinitageRpc extends QlcRpc {

    public MinitageRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
     * Return mintage data by mintage parameters
     * @param params mintageParams: mintage parameters
     * @return string: data for mintage
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject getMintageData(JSONArray params) throws IOException {
        return client.call("mintage_getMintageData", params);
    }

    /**
     * Return contract send block by mintage parameters
     * @param params mintageParams: mintage parameters
     * @return block: mintage block, type is ContractSend
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject getMintageBlock(JSONArray params) throws IOException {
        return client.call("mintage_getMintageBlock", params);
    }

    /**
     * Return contract reward block by contract send block
     * @param params block: contract send block
     * @return block: contract reward block
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject getRewardBlock(JSONArray params) throws IOException {
        return client.call("mintage_getRewardBlock", params);
    }

}
