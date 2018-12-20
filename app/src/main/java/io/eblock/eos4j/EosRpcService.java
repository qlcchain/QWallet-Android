package io.eblock.eos4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stratagile.qlink.blockchain.api.EosChainInfo;
import com.stratagile.qlink.blockchain.bean.JsonToBeanResultBean;
import com.stratagile.qlink.blockchain.bean.JsonToBinRequest;
import com.stratagile.qlink.blockchain.chain.PackedTransaction;

import io.eblock.eos4j.api.service.RpcService;
import io.eblock.eos4j.api.utils.Generator;
import io.eblock.eos4j.api.vo.Block;
import io.eblock.eos4j.api.vo.ChainInfo;
import io.eblock.eos4j.api.vo.SignParam;
import io.eblock.eos4j.api.vo.TableRows;
import io.eblock.eos4j.api.vo.TableRowsReq;
import io.eblock.eos4j.api.vo.account.Account;
import io.eblock.eos4j.api.vo.action.Actions;
import io.eblock.eos4j.api.vo.transaction.Transaction;
import io.eblock.eos4j.api.vo.transaction.push.Tx;
import io.eblock.eos4j.api.vo.transaction.push.TxAction;
import io.eblock.eos4j.api.vo.transaction.push.TxRequest;
import io.eblock.eos4j.api.vo.transaction.push.TxSign;
import io.eblock.eos4j.ese.Action;
import io.eblock.eos4j.ese.DataParam;
import io.eblock.eos4j.ese.DataType;
import io.eblock.eos4j.ese.Ese;

public class EosRpcService {

	private final RpcService rpcService;

	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	public EosRpcService(String baseUrl) {
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
		rpcService = Generator.createService(RpcService.class, baseUrl);
	}

	/**
	 * 获取钱包信息
	 * 
	 * @return
	 */
	public ChainInfo getChainInfo() {
		return Generator.executeSync(rpcService.getChainInfo());
	}

	/**
	 * 获取钱包信息
	 *
	 * @return
	 */
	public EosChainInfo getChainInfo1() {
		return Generator.executeSync(rpcService.getChainInfo1());
	}

	public JsonToBeanResultBean getAbiJsonToBean(JsonToBinRequest jsonToBinRequest) {
		return Generator.executeSync(rpcService.jsonToBin(jsonToBinRequest));
	}

	/**
	 * 获取一个块的信息
	 * 
	 * @param blockNumberOrId
	 *            编号或ID
	 * @return
	 */
	public Block getBlock(String blockNumberOrId) {
		return Generator.executeSync(rpcService.getBlock(Collections.singletonMap("block_num_or_id", blockNumberOrId)));
	}

	/**
	 * 获取账户信息
	 * 
	 * @param account
	 *            账户名
	 * @return
	 */
	public Account getAccount(String account) {
		return Generator.executeSync(rpcService.getAccount(Collections.singletonMap("account_name", account)));
	}

	/**
	 * 获取智能合约数据
	 * 
	 * @param req
	 * @return
	 */
	public TableRows getTableRows(TableRowsReq req) {
		return Generator.executeSync(rpcService.getTableRows(req));
	}

	/**
	 * 发送事务
	 * 
	 * @param compression
	 *            
	 * @param pushTransaction
	 *            推送事务
	 * @param signatures
	 *            签名
	 * @return
	 * @throws Exception
	 */
	public Transaction pushTransaction(String compression, Tx pushTransaction, String[] signatures) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String mapJakcson = mapper.writeValueAsString(new TxRequest(compression, pushTransaction, signatures));
		System.out.println(mapJakcson);
//		return null;
//		return Generator
//				.executeSync(rpcService.pushTransaction(new TxRequest(compression, pushTransaction, signatures)));
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("compression", compression);
		map.put("transaction", pushTransaction);
		map.put("signatures", signatures);
		return Generator
				.executeSync(rpcService.pushTransaction(map));
	}

	public Transaction pushTransaction(PackedTransaction packedTransaction) {
		return Generator.executeSync(rpcService.pushTransaction(packedTransaction));
	}


	/**
	 * 获取用户交易记录
	 * @param accountName 用户名
	 * @param pos 第几条记录
	 * @param offset 每次查询条数
	 * @return
	 * @throws Exception
	 */
    public Actions getActions(String accountName, Integer pos, Integer offset)throws Exception{
        LinkedHashMap<String, Object> requestParameters = new LinkedHashMap<>(3);
        requestParameters.put("account_name", accountName);
        requestParameters.put("pos", pos);
        requestParameters.put("offset", offset);
        return Generator.executeSync(rpcService.getActions(requestParameters));
    }
    
    /**
     * 获取用户合约账户余额
     * @param code 合约
     * @param accountName 账户名
     * @param symbol 合约代码
     * @return
     */
    public List<String> getCurrencyBalance(String code, String accountName, String symbol){
        LinkedHashMap<String, String> requestParameters = new LinkedHashMap<>(3);
        requestParameters.put("code", code);
        requestParameters.put("account", accountName);
        requestParameters.put("symbol", symbol);
        return Generator.executeSync(rpcService.getCurrencyBalance(requestParameters));
    }
    
	/**
	 * 发送事务
	 * 
	 * @param tx
	 * @return
	 * @throws Exception
	 */
	public Transaction pushTransaction(String tx) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Gson gson = new Gson();
		TxRequest txRequest = gson.fromJson(tx, TxRequest.class);
		TxRequest txObj = mapper.readValue(tx, TxRequest.class);
		LinkedHashMap<String, Object> map = new LinkedHashMap<>();
		map.put("compression", txRequest.getCompression());
		map.put("transaction", txRequest.getTransaction());
		map.put("signatures", txRequest.getSignatures());
		return Generator.executeSync(rpcService.pushTransaction(map));
	}

	/**
	 * 获取离线签名参数
	 * 
	 * @param exp
	 *            杩囨湡鏃堕棿绉�
	 * @return
	 */
	public SignParam getOfflineSignParams(Long exp) {
		SignParam params = new SignParam();
		ChainInfo info = getChainInfo();
		Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
		params.setChainId(info.getChainId());
		params.setHeadBlockTime(info.getHeadBlockTime());
		params.setLastIrreversibleBlockNum(info.getLastIrreversibleBlockNum());
		params.setRefBlockPrefix(block.getRefBlockPrefix());
		params.setExp(exp);
		return params;
	}

	/**
	 * 杞处
	 * 
	 * @param pk
	 *            绉侀挜
	 * @param contractAccount
	 *            鍚堢害璐︽埛
	 * @param from
	 *            浠�
	 * @param to
	 *            鍒�
	 * @param quantity
	 *            甯佺閲戦
	 * @param memo
	 *            鐣欒█
	 * @return
	 * @throws Exception
	 */
	public Transaction transfer(String pk, String contractAccount, String from, String to, String quantity, String memo)
			throws Exception {
		// get chain info
		ChainInfo info = getChainInfo();
//		info.setChainId("cf057bbfb72640471fd910bcb67639c22df9f92470936cddc1ade0e2f2e7dc4f");
//		info.setLastIrreversibleBlockNum(826366l);
//		info.setHeadBlockTime(dateFormatter.parse("2018-08-22T09:19:01.000"));
		// get block info
		Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
//		block.setRefBlockPrefix(29454112l);
		// tx
		Tx tx = new Tx();
		tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 90);
		tx.setRef_block_num(info.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(block.getRefBlockPrefix());
		tx.setNet_usage_words(0L);
		tx.setMax_cpu_usage_ms(0L);
		tx.setDelay_sec(0L);
		// actions
		List<TxAction> actions = new ArrayList<TxAction>();
		// data
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		dataMap.put("from", from);
		dataMap.put("to", to);
		dataMap.put("quantity", new DataParam(quantity, DataType.asset, Action.transfer).getValue());
		dataMap.put("memo", memo);
		System.out.println(dataMap);
		// action
		TxAction action = new TxAction(from, contractAccount, "transfer", dataMap);
		actions.add(action);
		tx.setActions(actions);
		// sgin
		String sign = Ecc.signTransaction(pk, new TxSign(info.getChainId(), tx));
		// data parse
		String data = Ecc.parseTransferData(from, to, quantity, memo);
		// reset data
		action.setData(data);
		// reset expiration
		tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
		return pushTransaction("none", tx, new String[] { sign });
	}

	/**
	 * 鍒涘缓璐︽埛
	 * 
	 * @param pk
	 *            绉侀挜
	 * @param creator
	 *            鍒涘缓鑰�
	 * @param newAccount
	 *            鏂拌处鎴�
	 * @param owner
	 *            鍏挜
	 * @param active
	 *            鍏挜
	 * @param buyRam
	 *            ram
	 * @return
	 * @throws Exception
	 */
	public Transaction createAccount(String pk, String creator, String newAccount, String owner, String active,
			Long buyRam) throws Exception {
		// get chain info
		ChainInfo info = getChainInfo();
		System.out.println(info.toString());
		// get block info
		Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
		System.out.println(block.toString());
		// tx
		Tx tx = new Tx();
		tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
		tx.setRef_block_num(info.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(block.getRefBlockPrefix());
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
		String sign = Ecc.signTransaction(pk, new TxSign(info.getChainId(), tx));
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

	/**
	 * 鍒涘缓璐︽埛
	 * 
	 * @param pk
	 *            绉侀挜
	 * @param creator
	 *            鍒涘缓鑰�
	 * @param newAccount
	 *            鏂拌处鎴�
	 * @param owner
	 *            鍏挜
	 * @param active
	 *            鍏挜
	 * @param buyRam
	 *            璐拱绌洪棿鏁伴噺
	 * @param stakeNetQuantity
	 *            缃戠粶鎶垫娂
	 * @param stakeCpuQuantity
	 *            cpu鎶垫娂
	 * @param transfer
	 *            鎶垫娂璧勪骇鏄惁杞�佺粰瀵规柟锛�0鑷繁鎵�鏈夛紝1瀵规柟鎵�鏈�
	 * @return
	 * @throws Exception
	 */
	public Transaction createAccount(String pk, String creator, String newAccount, String owner, String active,
			Long buyRam, String stakeNetQuantity, String stakeCpuQuantity, Long transfer) throws Exception {
		// get chain info
		ChainInfo info = getChainInfo();
		// info.setChainId("cf057bbfb72640471fd910bcb67639c22df9f92470936cddc1ade0e2f2e7dc4f");
		// info.setLastIrreversibleBlockNum(22117l);
		// get block info
		Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
		// block.setRefBlockPrefix(3920078619l);
		// tx
		Tx tx = new Tx();
		tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
		// tx.setExpiration(1528436078);
		tx.setRef_block_num(info.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(block.getRefBlockPrefix());
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
		// buyrap
		Map<String, Object> delMap = new LinkedHashMap<String, Object>();
		delMap.put("from", creator);
		delMap.put("receiver", newAccount);
		delMap.put("stake_net_quantity", new DataParam(stakeNetQuantity, DataType.asset, Action.delegate).getValue());
		delMap.put("stake_cpu_quantity", new DataParam(stakeCpuQuantity, DataType.asset, Action.delegate).getValue());
		delMap.put("transfer", transfer);
		TxAction delAction = new TxAction(creator, "eosio", "delegatebw", delMap);
		actions.add(delAction);
		// // sgin
		String sign = Ecc.signTransaction(pk, new TxSign(info.getChainId(), tx));
		// data parse
		String accountData = Ese.parseAccountData(creator, newAccount, owner, active);
		createAction.setData(accountData);
		// data parse
		String ramData = Ese.parseBuyRamData(creator, newAccount, buyRam);
		buyAction.setData(ramData);
		// data parse
		String delData = Ese.parseDelegateData(creator, newAccount, stakeNetQuantity, stakeCpuQuantity,
				transfer.intValue());
		delAction.setData(delData);
		// reset expiration
		tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
		return pushTransaction("none", tx, new String[] { sign });
	}
	
	/**
	 * 
	 * @param pk
	 * @param voter
	 * @param proxy
	 * @param producers
	 * @return
	 * @throws Exception
	 */
	public Transaction voteproducer(String pk,String voter,String proxy,List<String> producers) throws Exception {
		Comparator<String> comparator = (h1, h2) -> h2.compareTo(h1);
		producers.sort(comparator.reversed());
		// get chain info
		ChainInfo info = getChainInfo();
		// get block info
		Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
		// tx
		Tx tx = new Tx();
		tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
		tx.setRef_block_num(info.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(block.getRefBlockPrefix());
		tx.setNet_usage_words(0l);
		tx.setMax_cpu_usage_ms(0l);
		tx.setDelay_sec(0l);
		// actions
		List<TxAction> actions = new ArrayList<TxAction>();
		// data
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		dataMap.put("voter", voter);
		dataMap.put("proxy", proxy);
		dataMap.put("producers",producers);
		// action
		TxAction action = new TxAction(voter, "eosio", "voteproducer", dataMap);
		actions.add(action);
		tx.setActions(actions);
		// sgin
		String sign = Ecc.signTransaction(pk, new TxSign(info.getChainId(), tx));
		// data parse
		String data = Ecc.parseVoteProducerData(voter, proxy, producers);
		// reset data
		action.setData(data);
		// reset expiration
		tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
		return pushTransaction("none", tx, new String[] { sign });
	}
	
	/**
	 * token close
	 * @param owner
	 * @param symbol
	 * @return
	 * @throws Exception
	 */
	public Transaction close(String pk,String contract,String owner, String symbol)throws Exception {
		ChainInfo info = getChainInfo();			
		Block block = getBlock(info.getLastIrreversibleBlockNum().toString());
		Tx tx = new Tx();
		tx.setExpiration(info.getHeadBlockTime().getTime() / 1000 + 60);
		tx.setRef_block_num(info.getLastIrreversibleBlockNum());
		tx.setRef_block_prefix(block.getRefBlockPrefix());
		tx.setNet_usage_words(0l);
		tx.setMax_cpu_usage_ms(0l);
		tx.setDelay_sec(0l);
		// actions
		List<TxAction> actions = new ArrayList<TxAction>();
		// data
		Map<String, Object> dataMap = new LinkedHashMap<String, Object>();
		dataMap.put("close-owner", owner);
		dataMap.put("close-symbol",  new DataParam(symbol, DataType.symbol, Action.close).getValue());
		// action
		TxAction action = new TxAction(owner,contract,"close",dataMap);
		actions.add(action);
		tx.setActions(actions);
		// sgin
		String sign = Ecc.signTransaction(pk, new TxSign(info.getChainId(), tx));
		// data parse
		String data = Ecc.parseCloseData(owner, symbol);
		// reset data
		action.setData(data);
		// reset expiration
		tx.setExpiration(dateFormatter.format(new Date(1000 * Long.parseLong(tx.getExpiration().toString()))));
		return pushTransaction("none", tx, new String[] { sign });
	}
}
