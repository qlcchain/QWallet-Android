package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;
import qlc.rpc.QlcRpc;

public class RewardsRpc extends QlcRpc {

	public RewardsRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
	 * returns airdrop contract reward block by contract send block hash
	 * @param params string: contract send block hash
	 * @return block: contract reward block
	 * @throws IOException io exception
	 */
	public JSONObject getReceiveRewardBlock(JSONArray params) throws IOException {
		return client.call("rewards_getReceiveRewardBlock", params);
	}

	/**
	 * returns total airdrop qgas amount for a specific pledge
	 * @param params string: transaction id for the pledge
	 * @return uint64: total rewards
	 * @throws IOException io exception
	 */
	public JSONObject getTotalRewards(JSONArray params) throws IOException {
		return client.call("rewards_getTotalRewards", params);
	}

	/**
	 * returns airdrop qgas reward detail info for a specific pledge
	 * @param params string: transaction id for the pledge
	 * @return rewardsInfo: rewards detail
	 * @throws IOException io exception
	 */
	public JSONObject getRewardsDetail(JSONArray params) throws IOException {
		return client.call("rewards_getRewardsDetail", params);
	}

	/**
	 * returns airdrop qgas rewards for a specific confidant address
	 * @param params string: confidant address
	 * @return map: rewards - key: hash of confidant Id - value: rewards amount
	 * @throws IOException io exception
	 */
	public JSONObject getConfidantRewards(JSONArray params) throws IOException {
		return client.call("rewards_getConfidantRewards", params);
	}

	/**
	 * returns airdrop qgas rewards detail info for a specific confidant address
	 * @param params string: confidant address
	 * @return rewardsInfo: detail info for rewards
	 * @throws IOException io exception
	 */
	public JSONObject getConfidantRewordsDetail(JSONArray params) throws IOException {
		return client.call("rewards_getConfidantRewordsDetail", params);
	}

}
