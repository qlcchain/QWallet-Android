package io.eblock.eos4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.apache.commons.lang3.StringUtils;

import io.eblock.eos4j.api.vo.SignParam;
import io.eblock.eos4j.api.vo.transaction.push.Tx;
import io.eblock.eos4j.api.vo.transaction.push.TxAction;
import io.eblock.eos4j.api.vo.transaction.push.TxRequest;
import io.eblock.eos4j.api.vo.transaction.push.TxSign;
import io.eblock.eos4j.ese.Action;
import io.eblock.eos4j.ese.DataParam;
import io.eblock.eos4j.ese.DataType;
import io.eblock.eos4j.ese.Ese;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:31:49
 */
public class OfflineSign {

	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public OfflineSign() {
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * 
	 * @param compression
	 * @param pushTransaction
	 * @param signatures
	 * @return
	 * @throws Exception
	 */
	public String pushTransaction(String compression, Tx pushTransaction, String[] signatures) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
//		Gson gson = new GsonBuilder().registerTypeAdapter(TxRequest.class, new TxRequestTypeAdapter()).create();
//		String gsonStr = gson.toJson(new TxRequest(compression, pushTransaction, signatures));
		String mapJakcson = mapper.writeValueAsString(new TxRequest(compression, pushTransaction, signatures));
		return mapJakcson;
	}

	public class TxRequestTypeAdapter extends TypeAdapter<TxRequest> {

		@Override
		public void write(JsonWriter out, TxRequest value) throws IOException {
			out.beginObject();
			//按自定义顺序输出字段信息
			out.name("compression").value(value.getCompression());
			out.name("transaction").value(new Gson().toJson(value.getTransaction()));
			out.name("signatures").beginArray().value(StringUtils.join(value.getSignatures())).endArray();
			out.endObject();
		}

		@Override
		public TxRequest read(JsonReader in) throws IOException {
			return null;
		}
	}



	/**
	 * 离线签名转账
	 * 
	 * @param signParam
	 * @param pk
	 * @param contractAccount
	 * @param from
	 * @param to
	 * @param quantity
	 * @param memo
	 * @return
	 * @throws Exception
	 */
	public String transfer(SignParam signParam, String pk, String contractAccount, String from, String to,
			String quantity, String memo) throws Exception {
		Tx tx = new Tx();
		tx.setExpiration(signParam.getHeadBlockTime().getTime() / 1000 + signParam.getExp());
		tx.setRef_block_num(signParam.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(signParam.getRefBlockPrefix());
		tx.setNet_usage_words(0l);
		tx.setMax_cpu_usage_ms(0l);
		tx.setDelay_sec(0l);
		// actions
		List<TxAction> actions = new ArrayList<TxAction>();
		// data
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		dataMap.put("from", from);
		dataMap.put("to", to);
		dataMap.put("quantity", new DataParam(quantity, DataType.asset, Action.transfer).getValue());
		dataMap.put("memo", memo);
		// action
		TxAction action = new TxAction(from, contractAccount, "transfer", dataMap);
		actions.add(action);
		tx.setActions(actions);
		// sgin
		String sign = Ecc.signTransaction(pk, new TxSign(signParam.getChainId(), tx));
		// data parse
		String data = Ecc.parseTransferData(from, to, quantity, memo);
		// reset data
		action.setData(data);
		// reset expiration
		tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
		return pushTransaction("none", tx, new String[] { sign });
	}

	/**
	 * 离线签名创建账户
	 * 
	 * @param signParam
	 * @param pk
	 * @param creator
	 * @param newAccount
	 * @param owner
	 * @param active
	 * @param buyRam
	 * @return
	 * @throws Exception
	 */
	public String createAccount(SignParam signParam, String pk, String creator, String newAccount, String owner,
			String active, Long buyRam) throws Exception {
		Tx tx = new Tx();
		tx.setExpiration(signParam.getHeadBlockTime().getTime() / 1000 + signParam.getExp());
		tx.setRef_block_num(signParam.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(signParam.getRefBlockPrefix());
		tx.setNet_usage_words(0l);
		tx.setMax_cpu_usage_ms(0l);
		tx.setDelay_sec(0l);
		// actions
		List<TxAction> actions = new ArrayList<TxAction>();
		tx.setActions(actions);
		// create
		Map<String, Object> createMap = new LinkedHashMap<String, Object>();
		createMap.put("creator", creator);
		createMap.put("name", newAccount);
		createMap.put("owner", owner);
		createMap.put("active", active);
		TxAction createAction = new TxAction(creator, "eosio", "newaccount", createMap);
		actions.add(createAction);
		// buyrap
		Map<String, Object> buyMap = new LinkedHashMap<String, Object>();
		buyMap.put("payer", creator);
		buyMap.put("receiver", newAccount);
		buyMap.put("bytes", buyRam);
		TxAction buyAction = new TxAction(creator, "eosio", "buyrambytes", buyMap);
		actions.add(buyAction);
		// sgin
		String sign = Ecc.signTransaction(pk, new TxSign(signParam.getChainId(), tx));
		// data parse
		String accountData = Ese.parseAccountData(creator, newAccount, owner, active);
		createAction.setData(accountData);
		// data parse
		String ramData = Ese.parseBuyRamData(creator, newAccount, buyRam);
		buyAction.setData(ramData);
		// reset expiration
		tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
		return pushTransaction("none", tx, new String[] { sign });
	}
}
