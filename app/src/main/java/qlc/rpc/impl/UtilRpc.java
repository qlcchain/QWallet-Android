package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;

public class UtilRpc extends QlcRpc {

    public UtilRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
     * Decrypt the cryptograph string by passphrase
     * @param params string : cryptograph, encoded by base64
                     string : passphrase
     * @return string : raw data
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject decrypt(JSONArray params) throws IOException {
        return client.call("util_decrypt", params);
    }

    /**
     * Encrypt encrypt raw data by passphrase
     * @param params string : cryptograph, encoded by base64
                     string : passphrase
     * @return string: cryptograph , encoded by base64
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject encrypt(JSONArray params) throws IOException {
        return client.call("util_encrypt", params);
    }

    /**
     * Return balance by specific unit for raw value
     * @param params string: raw value
                    string: unit
                    string: optional, token name , if not set , default is QLC
     * @return string: balance for the unit
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject rawToBalance(JSONArray params) throws IOException {
        return client.call("util_rawToBalance", params);
    }

    /**
     * Return raw value for the balance by specific unit
     * @param params string: balance
                    string: unit
                    string: optional, token name , if not set , default is QLC
     * @return string: raw value
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject balanceToRaw(JSONArray params) throws IOException {
        return client.call("util_balanceToRaw", params);
    }

}
