package io.eblock.eos4j.api.service;

import com.stratagile.qlink.blockchain.api.EosChainInfo;
import com.stratagile.qlink.blockchain.bean.JsonToBeanResultBean;
import com.stratagile.qlink.blockchain.bean.JsonToBinRequest;
import com.stratagile.qlink.blockchain.chain.PackedTransaction;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.eblock.eos4j.api.vo.Block;
import io.eblock.eos4j.api.vo.TableRows;
import io.eblock.eos4j.api.vo.ChainInfo;
import io.eblock.eos4j.api.vo.TableRowsReq;
import io.eblock.eos4j.api.vo.account.Account;
import io.eblock.eos4j.api.vo.action.Actions;
import io.eblock.eos4j.api.vo.transaction.Transaction;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:26:12
 */
public interface RpcService {

	@GET("/v1/chain/get_info")
	Call<ChainInfo> getChainInfo();

	@GET("/v1/chain/get_info")
	Call<EosChainInfo> getChainInfo1();

	@POST("/v1/chain/abi_json_to_bin")
	Call<JsonToBeanResultBean> jsonToBin(@Body JsonToBinRequest body);

	@POST("/v1/chain/get_block")
	Call<Block> getBlock(@Body Map<String, String> requestFields);

	@POST("/v1/chain/get_account")
	Call<Account> getAccount(@Body Map<String, String> requestFields);

    @POST("/v1/chain/get_currency_balance")
    Call<List<String>> getCurrencyBalance(@Body Map<String, String> requestFields);

	@POST("/v1/chain/push_transaction")
	Call<Transaction> pushTransaction(@Body LinkedHashMap<String, Object> request);

	@POST("/v1/chain/push_transaction")
	Call<Transaction> pushTransaction(@Body PackedTransaction request);

	@POST("/v1/chain/get_table_rows")
	Call<TableRows> getTableRows(@Body TableRowsReq request);

    @POST("/v1/history/get_actions")
    Call<Actions> getActions(@Body Map<String, Object> requestFields);

}
