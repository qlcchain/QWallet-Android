package qlc.rpc;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.mng.AccountMng;
import qlc.network.QlcException;
import qlc.utils.Constants;
import qlc.utils.Helper;
import qlc.utils.MnemonicUtil;
import qlc.utils.SeedUtil;
import qlc.utils.StringUtil;

public class AccountRpc {

	/**
     * Create a new account by seed and index
     * @param params
     * seed
     * index : optional, index for account, if not set, default value is 0
     * @return JSONObject: the private and public key for account
			privKey: private key for the new account
			pubKey: public key for the new account
	 * @throws QlcException qlc exception 
     */
	public static JSONObject create(JSONArray params) {
		
		String seed = params.getString(0);
		Integer index = params.getInteger(1);
		
		if (StringUtil.isBlank(seed))
    		throw new QlcException(Constants.EXCEPTION_CODE_1001, Constants.EXCEPTION_MSG_1001);
		
		return AccountMng.keyPairFromSeed(Helper.hexStringToBytes(seed), index);
		
	}

	/**
     * Create a new account by seed and index
     * @return seed
     */
	public static String newSeed() {
		
		return Helper.byteToHexString(SeedUtil.generateSeed());
		
	}
	
	/**
	 * 
	 * Create new accounts randomly
	 * @param params:number of accounts, default is 10
	 * @return JSONArray:account info
	 * @throws QlcException qlc exception 
	 */
    public static JSONArray newAccounts(JSONArray params) {
    	
    	Integer numbers = params.getInteger(0);
    	return AccountMng.newAccounts(numbers);
    	
    }

	/**
     * Return account address by public key
     * @param params: public key
     * @return account address
	 * @throws QlcException qlc exception 
     */
	public static String forPublicKey(JSONArray params) {

		String publicKey = params.getString(0);
 		if (StringUtil.isBlank(publicKey))
 			throw new QlcException(Constants.EXCEPTION_CODE_1002, Constants.EXCEPTION_MSG_1002);
 		
		return AccountMng.publicKeyToAddress(Helper.hexStringToBytes(publicKey));
		
	}
	
	/**
     * Return public key for account address
     * @param params:account address
     * @return public key
	 * @throws QlcException qlc exception 
     */
	public static String publicKey(JSONArray params) {

		String address = params.getString(0);
		return AccountMng.addressToPublicKey(address);
		
	}
	
	/**
     * Returns whether the address is valid or not
     * @param params:account address
     * @return bool: if valid , return true, or return false
	 * @throws QlcException qlc exception 
     */
	public static boolean validate(JSONArray params) {

		String address = params.getString(0);
		return AccountMng.isValidAddress(address);
		
	}
	
	/**
	 * 
	 * Return mnemonics by seed
	 * @param params seed
	 * @return String
	 */
	public static String seedToMnemonics(JSONArray params) {
		
		String seed = params.getString(0);
		if (StringUtil.isBlank(seed))
			throw new QlcException(Constants.EXCEPTION_CODE_1001, Constants.EXCEPTION_MSG_1001);
		
		List<String> mnemonicList = MnemonicUtil.seedToMnemonic(Helper.hexStringToBytes(seed), MnemonicUtil.MnemonicLanguage.ENGLISH);
		return StringUtil.join(mnemonicList.toArray(), " ");
	}
	
	/**
	 * 
	 * Return seed by mnemonics
	 * @param params mnemonics
	 * @return String
	 */
	public static String mnemonicsToSeed(JSONArray params) {
		
		String mnemonics = params.getString(0);
		if (StringUtil.isBlank(mnemonics))
			throw new QlcException(Constants.EXCEPTION_CODE_1007, Constants.EXCEPTION_MSG_1007);
		
		byte[] seed = MnemonicUtil.mnemonicToSeed(MnemonicUtil.toList(mnemonics),  MnemonicUtil.MnemonicLanguage.ENGLISH);
		return Helper.byteToHexString(seed);
	}

}
