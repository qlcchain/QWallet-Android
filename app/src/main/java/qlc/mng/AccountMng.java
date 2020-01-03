package qlc.mng;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.mng.WalletMng;
import qlc.network.QlcException;
import qlc.utils.Checking;
import qlc.utils.Constants;
import qlc.utils.HashUtil;
import qlc.utils.Helper;
import qlc.utils.SeedUtil;
import qlc.utils.StringUtil;

public final class AccountMng {
	
	public static final String ACCOUNT_ALPHABET = "13456789abcdefghijkmnopqrstuwxyz";

	public static final char[] ACCOUNT_ALPHABET_ARRAY = ACCOUNT_ALPHABET.toCharArray();
	
	public static final Map<String, Character> NUMBER_CHAR_MAP = new HashMap<String, Character>();
	
	public static final Map<Character, String> CHAR_NUMBER_MAP = new HashMap<Character, String>();
	
	// init the NUMBER_CHAR_MAP and CHAR_NUMBER_MAP
	private static void intMap() {
		
		for (int i=0; i<ACCOUNT_ALPHABET_ARRAY.length; i++) {
			String num = Integer.toBinaryString(i);
			while (num.length() < 5) // Not enough 5 digits, add 0
				num = "0" + num;
			NUMBER_CHAR_MAP.put(num, ACCOUNT_ALPHABET_ARRAY[i]);
			CHAR_NUMBER_MAP.put(ACCOUNT_ALPHABET_ARRAY[i], num);
		}
		
	}
	
	/**
	 * 
	 * Create a new account by seed and index
	 * @param seed:seed
	 * @param index:optional, index for account, if not set, default value is 0
	 * @return JSONObject  
	 * 	privKey: private key for the new account
	 *	pubKey: public key for the new account
	 */
    public static JSONObject keyPairFromSeed(byte[] seed, Integer index) {
    	
    	if (seed == null)
    		throw new QlcException(Constants.EXCEPTION_CODE_1001, Constants.EXCEPTION_MSG_1001);
    	index = (index==null) ? 0 : index;
    	
    	Checking.checkSeed(seed);
    	Checking.check(index<0, "Invalid index " + index);

		byte[] privateKey = HashUtil.digest256(seed, ByteBuffer.allocate(4).putInt(index).array());
		byte[] publicKey = WalletMng.createPublicKey(privateKey);
		Checking.checkKey(publicKey);

		JSONObject json = new JSONObject();
		json.put("privKey", Helper.byteToHexString(privateKey) + Helper.byteToHexString(publicKey));
		json.put("pubKey", Helper.byteToHexString(publicKey));
		
		return json;
    }
	
	/**
	 * 
	 * Create new accounts randomly
	 * @param numbers:number of accounts, default is 10
	 * @return JSONArray:account info
	 */
    public static JSONArray newAccounts(Integer numbers) {
    	
    	numbers = (numbers==null) ? 10: numbers;
    	
    	JSONArray array = new JSONArray();
    	JSONObject obj = null;
    	byte[] seed = null;
    	for (int i=0; i<numbers; i++) {
    		seed = SeedUtil.generateSeed();
    		obj = keyPairFromSeed(seed, 0);
    		obj.put("seed", Helper.byteToHexString(seed));
    		array.add(obj);
    		
    		obj = null;
    		seed = null;
    	}
    	
		return array;
    }
    
    /**
     * 
     * Return account address by public key
     * @param publicKey:public key
     * @return String account address
     */
 	public static String publicKeyToAddress(byte[] publicKey) {
 		
 		if (publicKey == null)
 			throw new QlcException(Constants.EXCEPTION_CODE_1002, Constants.EXCEPTION_MSG_1002);
 		
 		intMap();
 		
 		byte[] output = Helper.reverse(HashUtil.digest(5, publicKey));
 		String digestNum = Helper.hexStringToBinary(Helper.byteToHexString((output)));
        
 		String checkValue = "";
 		while (digestNum.length() < output.length * 8)
 			digestNum = "0" + digestNum;
 		for (int i = 0; i < ((output.length * 8) / 5); i++) {
 			String fiveBit = digestNum.substring(i * 5, (i * 5) + 5);
 			checkValue += NUMBER_CHAR_MAP.get(fiveBit);
 		}

 		String paddingValue = "";
 		String publicKeyBinary = Helper.hexStringToBinary(Helper.byteToHexString(publicKey));
 		while (publicKeyBinary.length() < 260)
 			publicKeyBinary = "0" + publicKeyBinary; 
 		for (int i=0; i<publicKeyBinary.length(); i+=5) {
 			String fiveBit = publicKeyBinary.substring(i, i + 5);
 			paddingValue += NUMBER_CHAR_MAP.get(fiveBit);
 		}

 		//return the address prefixed with qlc_ and suffixed with the checksum
 		return "qlc_" + paddingValue + checkValue;
 		
 		
 	}
 	
 	/**
 	 * 
 	 * Return public key for account address
 	 * @param address:account address
 	 * @return String public key
 	 */
 	public static String addressToPublicKey(String address) {
 		
 		if (StringUtil.isBlank(address))
 			throw new QlcException(Constants.EXCEPTION_CODE_1003, Constants.EXCEPTION_MSG_1003);
 		if (address.length()!=64 || !address.substring(0, 4).equals("qlc_"))
 			throw new QlcException(Constants.EXCEPTION_CODE_1004, Constants.EXCEPTION_MSG_1004);
 		
 		intMap();
 		
 		String pub = address.substring(4, address.length()-8);
 		String checksum = address.substring(address.length()-8);
 		
 		String pubBin = "";
 		for (int i = 0; i < pub.length(); i++) {
 			pubBin += CHAR_NUMBER_MAP.get(pub.charAt(i));
 		}
 		pubBin = pubBin.substring(4);
 		
 		String checkBin = "";
 		for (int i = 0; i < checksum.length(); i++) {
 			checkBin += CHAR_NUMBER_MAP.get(checksum.charAt(i));
 		}
 		
 		String hat = Helper.binaryToHexString(checkBin);
 		while (hat.length() < 10)
 			hat = "0" + hat;
 		
 		byte[] checkHex = Helper.reverse(Helper.hexStringToBytes(hat));
 		
 		
 		String fallaciousalbatross = Helper.binaryToHexString(pubBin);
 		while (fallaciousalbatross.length() < 64)
 			fallaciousalbatross = "0" + fallaciousalbatross;
 		
 		byte[] publicKey = Helper.hexStringToBytes(fallaciousalbatross);

 		byte[] output = HashUtil.digest(5, publicKey);
 		if (Arrays.equals(output, checkHex)) 
 			return Helper.byteToHexString(publicKey);
 		
 		return null;
 		
 	}

 	/**
 	 * 
 	 * Returns whether the address is valid or not
 	 * @param address:account address
 	 * @return boolean if valid , return true, or return false
 	 */
    public static boolean isValidAddress(String address) {

    	String[] parts = address.split("_");
        if (parts.length != 2) {
            return false;
        }
        if (!parts[0].equals("qlc")) {
            return false;
        }
        if (parts[1].length() != 60) {
            return false;
        }
        String expectedEncodedChecksum = address.substring(address.length() - 8);
        byte[] checksum = Helper.reverse(HashUtil.digest(5, Helper.hexStringToBytes(addressToPublicKey(address))));
        String binaryChecksum = Helper.leftPad(Helper.hexStringToBinary(Helper.byteToHexString((checksum))), checksum.length * 8);
        
        int codeSize = 5;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < binaryChecksum.length(); i += codeSize) {
        	sb.append(NUMBER_CHAR_MAP.get(binaryChecksum.substring(i, i + codeSize)));
        }
        String encodedChecksum = sb.toString();
        return expectedEncodedChecksum.equals(encodedChecksum);
    }

}
