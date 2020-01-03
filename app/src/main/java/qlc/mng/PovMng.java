package qlc.mng;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;

public class PovMng {
    
    /**
     * 
     * Return current block height
     * @param client:qlc client
     * @throws IOException io exception 
     * @return current block height  
     */
    public static Long getCurrentBlockHeight(QlcClient client) throws IOException {
    	
    	try {
			JSONArray params = new JSONArray();
			JSONObject json = client.call("pov_getMiningInfo", params);
			if (json.containsKey("result")) {
				
				json = json.getJSONObject("result");
				if (!json.isEmpty())
					return json.getLong("currentBlockHeight");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0l;
    }
    
    /**
     * 
     * Return fittest block header of PoV main chain, used by send TXs
     * @param client:qlc client
     * @throws IOException io exception 
     * @return current block height  
     */
    public static Long getFittestHeader(QlcClient client) throws IOException {
    	
    	try {
			JSONArray params = new JSONArray();
			params.add(0);
			JSONObject json = client.call("pov_getFittestHeader", params);
			if (!json.isEmpty() && json.containsKey("result")) {
				
				json = json.getJSONObject("result");
				if (!json.isEmpty() && json.containsKey("basHdr"))
					json = json.getJSONObject("basHdr");
				if (!json.isEmpty())
					return json.getLong("height");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0l;
    }

}

