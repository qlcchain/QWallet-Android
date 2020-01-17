package qlc.rpc.impl;

import java.io.IOException;
import java.math.BigInteger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import qlc.bean.StateBlock;
import qlc.mng.TransactionMng;
import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;
import qlc.utils.Constants;
import qlc.utils.Helper;
import qlc.utils.StringUtil;

public class TransactionRpc extends QlcRpc {

	public TransactionRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	
	/**
	 *  
	 * Return send block by send parameter and private key
	 * @param params
	 * 	send parameter for the block
	 *	from: send address for the transaction
	 *	to: receive address for the transaction
	 *	tokenName: token name
	 *	amount: transaction amount
	 *	sender: optional, sms sender
	 *	receiver: optional, sms receiver
	 *	message: optional, sms message hash
	 *	string: private key
	 * @return JSONObject send block
	 * @throws QlcException qlc exception
	 * @throws IOException io exception 
	 */
	public JSONObject generateSendBlock(JSONArray params) throws IOException {
		
		JSONObject arrayOne = params.getJSONObject(0);
		String from = arrayOne.getString("from");
		String tokenName = arrayOne.getString("tokenName");
		String to = arrayOne.getString("to");
		BigInteger amount = arrayOne.getBigInteger("amount");
		String sender = arrayOne.getString("sender");
		String receiver = arrayOne.getString("receiver");
		String message = arrayOne.getString("message");
		String data = arrayOne.getString("data");
		String privateKey = params.getString(1);
		
		if (StringUtil.isBlank(from) || 
				StringUtil.isBlank(tokenName) || 
				StringUtil.isBlank(to) || 
				amount == null)
			throw new QlcException(Constants.EXCEPTION_BLOCK_CODE_2000, Constants.EXCEPTION_BLOCK_MSG_2000);
		
		if (client == null)
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3000, Constants.EXCEPTION_SYS_MSG_3000);
		
		return TransactionMng.sendBlock(client, from, tokenName, to, amount, sender, receiver, message, data,
				(StringUtil.isNotBlank(privateKey) ? Helper.hexStringToBytes(privateKey) : null));
		
	}
	
	/**
	 * 
	 * Return receive block by send block and private key
	 * @param params
	 * block: send block
	 * address: receive address
	 * string: optonal, private key ,if not set ,will return block without signature and work
	 * @throws QlcException qlc exception
	 * @throws IOException io exception 
	 * @return JSONObject  
	 */
	public JSONObject generateReceiveBlock(JSONArray params) throws IOException {
		
		if (params == null)
				throw new QlcException(Constants.EXCEPTION_BLOCK_CODE_2001, Constants.EXCEPTION_BLOCK_MSG_2001);
		
		JSONObject arrayOne = params.getJSONObject(0);
		String receiveAddress = params.getString(1);
		StateBlock sendBlock = new Gson().fromJson(arrayOne.toJSONString(), StateBlock.class);
		String privateKey = null;
		if (params.size() > 2)
			privateKey = params.getString(2);
		
		if (StringUtil.isBlank(sendBlock.getType()) ||
				StringUtil.isBlank(sendBlock.getToken()) || 
				StringUtil.isBlank(sendBlock.getAddress()) || 
				sendBlock.getBalance() == null || 
				StringUtil.isBlank(sendBlock.getPrevious()) || 
				StringUtil.isBlank(sendBlock.getLink()) || 
				sendBlock.getTimestamp() == null || 
				StringUtil.isBlank(sendBlock.getRepresentative()) ||
				StringUtil.isBlank(receiveAddress))
			throw new QlcException(Constants.EXCEPTION_BLOCK_CODE_2001, Constants.EXCEPTION_BLOCK_MSG_2001);
		
		if (client == null)
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3000, Constants.EXCEPTION_SYS_MSG_3000);
		
		return TransactionMng.receiveBlock(client, sendBlock, receiveAddress,
				(StringUtil.isNotBlank(privateKey) ? Helper.hexStringToBytes(privateKey) : null));
	}
	
	/**
	 * 
	 * Return change block by account and private key
	 * @param params
	 * address:account address
	 * representative:new representative account
	 * chainTokenHash:chian token hash
	 * privateKey:private key ,if not set ,will return block without signature and work
	 * @return JSONObject  
	 * @throws QlcException qlc exception
	 * @throws IOException io exception 
	 * @return JSONObject  
	 */
	public JSONObject generateChangeBlock(JSONArray params) throws IOException {

		String address = params.getString(0);
		String representative = params.getString(1);
		String chainTokenHash = params.getString(2);
		String privateKey = params.getString(3);
		
		if (StringUtil.isBlank(address) || 
				StringUtil.isBlank(representative) || 
				StringUtil.isBlank(chainTokenHash))
			throw new QlcException(Constants.EXCEPTION_BLOCK_CODE_2010, Constants.EXCEPTION_BLOCK_MSG_2010);
		
		if (client == null)
			throw new QlcException(Constants.EXCEPTION_SYS_CODE_3000, Constants.EXCEPTION_SYS_MSG_3000);
		
		return TransactionMng.changeBlock(client, address, representative, chainTokenHash,
				(StringUtil.isNotBlank(privateKey) ? Helper.hexStringToBytes(privateKey) : null));
	}
	
}
