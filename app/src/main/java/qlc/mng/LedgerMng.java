package qlc.mng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import qlc.bean.Pending;
import qlc.bean.Pending.PendingInfo;
import qlc.bean.StateBlock;
import qlc.network.QlcClient;
import qlc.utils.Helper;
import qlc.utils.StringUtil;

public class LedgerMng {
    
    /**
     * 
     * Return block info by block hash
     * @param client:qlc client
     * @param blockHash:block hash
     * @throws IOException io exception 
     * @return StateBlock  
     */
    public static StateBlock getBlockInfoByHash(QlcClient client, byte[] blockHash) throws IOException {
    	
    	if (blockHash == null)
    		return null;
    	
    	JSONArray params = new JSONArray();
    	String[] hashes = {Helper.byteToHexString(blockHash)};
		params.add(hashes);
		JSONObject json = client.call("ledger_blocksInfo", params);
		JSONArray blockArray = null;
		if (json.containsKey("result")) {
			
			blockArray = json.getJSONArray("result");
			
			if (!blockArray.isEmpty())
				return new Gson().fromJson(blockArray.getJSONObject(0).toJSONString(), StateBlock.class);
		} 
		
		return null;
    }

    /**
     * 
     * Return pending info for account
     * @param client:qlc client
     * @param address:account
     * @throws IOException io exception 
     * @return Pending  
     */
    public static Pending getAccountPending(QlcClient client, String address) throws IOException {
    	
    	if (StringUtil.isBlank(address))
    		return null;
    	
    	JSONArray params = new JSONArray();
    	JSONArray addressArr = new JSONArray();
    	addressArr.add(address);
    	params.add(addressArr);
    	params.add(-1);
		JSONObject json = client.call("ledger_accountsPending", params);
		if (json.containsKey("result")) {
			
			Pending pending = new Pending();
			pending.setAddress(address);
			List<PendingInfo> infoList = new ArrayList<PendingInfo>();
			
			json = json.getJSONObject("result");
			JSONArray infoArray = json.getJSONArray(address);
			if (infoArray!=null && infoArray.size()>0) {
				PendingInfo info = null;
				for (int i=0; i<infoArray.size(); i++) {
					info = new Gson().fromJson(infoArray.getJSONObject(i).toJSONString(), PendingInfo.class);
					infoList.add(info);
					
					info = null;
				}
				pending.setInfoList(infoList);
			}
			return pending;
		} 
		
		return null;
    	
    }
	
}

