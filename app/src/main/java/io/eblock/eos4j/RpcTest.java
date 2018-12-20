/** 
 * @Package io.eblock.eos4j 
 * @Description 
 * @author yifang.huang
 * @date 2018年11月22日 下午3:22:26 
 * @version V1.0 
 */ 
package io.eblock.eos4j;

import java.util.List;

import io.eblock.eos4j.api.exception.ApiException;
import io.eblock.eos4j.api.vo.Block;
import io.eblock.eos4j.api.vo.ChainInfo;
import io.eblock.eos4j.api.vo.SignParam;
import io.eblock.eos4j.api.vo.account.Account;
import io.eblock.eos4j.api.vo.transaction.Transaction;

/** 
 * @Description 
 * @author yifang.huang
 * @date 2018年11月22日 下午3:22:26 
 */
public class RpcTest {

	public static void main(String[] args) {
		getAccount();
	}
	
	public void getBalance() {

		// 主网
		EosRpcService rpc = new EosRpcService("https://node1.zbeos.com");
		List<String> balance = rpc.getCurrencyBalance("eosio.token", "yfhuangeos2g", "EOS");
		System.out.println(balance.get(0));
		
	}
	
	public static void getAccount() {

		// 主网
		EosRpcService rpc = new EosRpcService("http://api-mainnet.starteos.io");
		Account account = rpc.getAccount("huzhipeng124");
		System.out.println(account.toString());
		
	}

	public static void transfer() {

		// 主网
		EosRpcService rpc = new EosRpcService("http://api-mainnet.starteos.io");
		System.out.println("============= 转账EOS ===============");
//		OfflineSign sign = new OfflineSign();
//		// 交易信息
//		String content = "";
//		// 获取离线签名参数
//		SignParam params = rpc.getOfflineSignParams(60l);
//		try {
//			content = sign.transfer(params, "5Hv72iQk7sjR5xjdMsgH56bBRamvPg1gvh1kZ2SY8RSTvZy9xTb", "eosio.token",
//					"yfhuangeos4g", "yfhuangeos2g", "0.0001 EOS", "offline java");
//			System.out.println(content);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		// 广播交易
//		try {
//			Transaction tx = rpc.pushTransaction(content);
//			System.out.println(tx.getTransactionId());
//		} catch (ApiException ex) {
//			System.out.println(ex.getError().getCode());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		try {
//			//eos转账
//			Transaction t1 = rpc.transfer("5Hv72iQk7sjR5xjdMsgH56bBRamvPg1gvh1kZ2SY8RSTvZy9xTb", "eosio.token", "yfhuangeos4g", "yfhuangeos2g", "0.0001 EOS", "hhhh");
//			//代币转账
////			Transaction t1 = rpc.transfer("5KBWYsYudu9APDdUrHVPPDdyVMhjrRVq3zqDAEzhuogDR3caApb","machaoxiaoai", "machaoxiaoai","test12344444", "10.0000 SYS", "");
//			System.out.println("转账成功 = " + t1.getTransactionId()+" \n ");
//		}catch(Exception ex) {
//			ex.printStackTrace();
//		}
		
	}
}
