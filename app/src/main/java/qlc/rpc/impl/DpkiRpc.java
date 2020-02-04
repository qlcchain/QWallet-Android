package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import qlc.bean.StateBlock;
import qlc.mng.BlockMng;
import qlc.mng.WalletMng;
import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;
import qlc.utils.Constants;
import qlc.utils.Helper;

public class DpkiRpc extends QlcRpc {

	public DpkiRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get a verifier register block
	 * @param params account : account to register verifier, type : verifier type (email/weChat), id : verifier address to receive verify request (email address/weChat id)
	 * @return verifier register block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getVerifierRegisterBlock(JSONArray params) throws IOException {
		return client.call("dpki_getVerifierRegisterBlock", params);
	}

	/**
	 * Get a verifier unregister block
	 * @param params account : account to register verifier, type : verifier type (email/weChat)
	 * @return verifier unregister block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getVerifierUnregisterBlock(JSONArray params) throws IOException {
		return client.call("dpki_getVerifierUnregisterBlock", params);
	}

	/**
	 * Get all the verifiers
	 * @param null
	 * @return account : verifier account, type : verifier type, id : verifier address to receive verify request
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getAllVerifiers(JSONArray params) throws IOException {
		return client.call("dpki_getAllVerifiers", params);
	}

	/**
	 * Get all the specified type of verifiers
	 * @param type : verifier type (email/weChat)
	 * @return account : verifier account, type : verifier type, id : verifier address to receive verify request
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getVerifiersByType(JSONArray params) throws IOException {
		return client.call("dpki_getVerifiersByType", params);
	}

	/**
	 * Get all the verifiers registered by the specified account
	 * @param account : verifier register account
	 * @return account : verifier account, type : verifier type, id : verifier address to receive verify request
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getVerifiersByAccount(JSONArray params) throws IOException {
		return client.call("dpki_getVerifiersByAccount", params);
	}

	/**
	 * Get a publish block to publish a id/publicKey pair
	 * @param account : account to publish, type : verifier type (email/weChat), id : id address, pubKey : public key, fee : fee of this publish (5 qgas at least), verifiers : verifiers to verify this id
	 * @return block : publish block, verifiers : verifier info with a random verification code
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getPublishBlock(JSONArray params) throws IOException {
		return client.call("dpki_getPublishBlock", params);
	}

	/**
	 * Get a publish block to publish a id/publicKey pair and process
	 * @param account : account to publish, type : verifier type (email/weChat), id : id address, pubKey : public key, fee : fee of this publish (5 qgas at least), verifiers : verifiers to verify this id, priKey : private key
	 * @return block : publish block, verifiers : verifier info with a random verification code
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getPublishBlockAndProcess(JSONArray params) throws IOException {
		
		if (client == null)
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3000, Constants.EXCEPTION_SYS_MSG_3000);
		
		if (params == null)
			throw new QlcException(Constants.EXCEPTION_PUBLISH_BLOCK_CODE_4001, Constants.EXCEPTION_PUBLISH_BLOCK_MSG_4001);
		
		if (params.size() != 2)
			throw new QlcException(Constants.EXCEPTION_PUBLISH_BLOCK_CODE_4001, Constants.EXCEPTION_PUBLISH_BLOCK_MSG_4001);
		
		JSONArray publicBlockParams = params.getJSONArray(0);
		String privateKeyStr = params.getString(1);
		byte[] privateKey = Helper.hexStringToBytes(privateKeyStr);

		JSONObject publishBlockResult = client.call("dpki_getPublishBlock", publicBlockParams);
		if (publishBlockResult.containsKey("result")) {
			publishBlockResult = publishBlockResult.getJSONObject("result");
			if (publishBlockResult.containsKey("block")) {
				StateBlock publishBlock = new Gson().fromJson(publishBlockResult.getString("block"), StateBlock.class);
				if (privateKey!=null && privateKey.length==64) {
					
					// check private key and link
					String priKey = Helper.byteToHexString(privateKey);
					String pubKey = priKey.substring(64);
					
					// set signature
					byte[] receiveBlockHash = BlockMng.getHash(publishBlock);
					byte[] signature = WalletMng.sign(receiveBlockHash, Helper.hexStringToBytes(priKey.replace(pubKey, "")));
					boolean signCheck = WalletMng.verify(signature, receiveBlockHash, Helper.hexStringToBytes(pubKey));
					if (!signCheck)
						throw new QlcException(Constants.EXCEPTION_CODE_1005, Constants.EXCEPTION_MSG_1005);
					publishBlock.setSignature(Helper.byteToHexString(signature));
					
					// set work
					/*String work = WorkUtil.generateWork(Helper.hexStringToBytes(BlockMng.getRoot(publishBlock)));
					publishBlock.setWork(work);*/
					
					// process
			    	LedgerRpc lrpc = new LedgerRpc(client);
					JSONArray processParams = new JSONArray();
					processParams.add(JSONObject.parseObject(new Gson().toJson(publishBlock)));
					return lrpc.process(processParams);
				}
			}
		}
		
		return null;
	}

	/**
	 * Get a unpublish block to unpublish a id/publicKey pair
	 * @param account : account to publish, type : verifier type (email/weChat), id : id address, pubKey : public key to unpublish, hash : hash returned by dpki_getPublishBlock
	 * @return block : unpublish block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getUnPublishBlock(JSONArray params) throws IOException {
		return client.call("dpki_getUnPublishBlock", params);
	}

	/**
	 * Get publish info by type and id address
	 * @param type : verifier type (email/weChat), id : id address
	 * @return publishInfo : published infos
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getPubKeyByTypeAndID(JSONArray params) throws IOException {
		return client.call("dpki_getPubKeyByTypeAndID", params);
	}

	/**
	 * Get publish info by type
	 * @param type : verifier type (email/weChat)
	 * @return publishInfo : published infos
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getPublishInfosByType(JSONArray params) throws IOException {
		return client.call("dpki_getPublishInfosByType", params);
	}

	/**
	 * Get publish info by account and type
	 * @param account : account to publish,type : verifier type (email/weChat)
	 * @return publishInfo : published infos
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getPublishInfosByAccountAndType(JSONArray params) throws IOException {
		return client.call("dpki_getPublishInfosByAccountAndType", params);
	}

	/**
	 * Get a oracle block
	 * @param account : verify account, type : verify type (email/weChat), id : id address to verify, pk : public key to verify, code : random code returned by pushlisher_getPublishBlock, hash : publish block hash to verify
	 * @return block : oracle block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getOracleBlock(JSONArray params) throws IOException {
		return client.call("dpki_getOracleBlock", params);
	}

	/**
	 * Get oracle infos by type
	 * @param type : verify type (email/weChat), get all types if type is empty
	 * @return oracleInfo : oracle info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getOracleInfosByType(JSONArray params) throws IOException {
		return client.call("dpki_getOracleInfosByType", params);
	}

	/**
	 * Get oracle infos by type and id
	 * @param type : verify type (email/weChat), id : id address
	 * @return oracleInfo : oracle info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getOracleInfosByTypeAndID(JSONArray params) throws IOException {
		return client.call("dpki_getOracleInfosByTypeAndID", params);
	}

	/**
	 * Get oracle infos by account and type
	 * @param account : verify account, type : verify type (email/weChat), get all types if type is empty
	 * @return oracleInfo : oracle info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getOracleInfosByAccountAndType(JSONArray params) throws IOException {
		return client.call("dpki_getOracleInfosByAccountAndType", params);
	}

	/**
	 * Get oracle infos by hash
	 * @param hash : publish block hash to verify
	 * @return oracleInfo : oracle info
	 * @throws QlcException qlc exception
	 * @throws IOException io exception
	 */
	public JSONObject getOracleInfosByHash(JSONArray params) throws IOException {
		return client.call("dpki_getOracleInfosByHash", params);
	}

}
