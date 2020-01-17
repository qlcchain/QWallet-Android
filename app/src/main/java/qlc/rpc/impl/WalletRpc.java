package qlc.rpc.impl;

import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import qlc.network.QlcClient;
import qlc.network.QlcException;
import qlc.rpc.QlcRpc;

public class WalletRpc extends QlcRpc {
	
    public WalletRpc(QlcClient client) {
		super(client);
		// TODO Auto-generated constructor stub
	}

	/**
     * Return balance for each token of the wallet
     * @param params string: master address of the wallet
                     string: passphrase
     * @return balance of each token in the wallet
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject getBalances(JSONArray params) throws IOException {
        return client.call("wallet_getBalances", params);
    }

    /**
     * Returns raw key (public key and private key) for a account
     * @param params string: account address
                     string: passphrase
     * @return private key and public key for the address
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject getRawKey(JSONArray params) throws IOException {
        return client.call("wallet_getRawKey", params);
    }

    /**
     * Generate new seed
     * @param params null
     * @return string: hex string for seed
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject newSeed(JSONArray params) throws IOException {
        return client.call("wallet_newSeed", params);
    }

    /**
     * Create new wallet and Return the master address
     * @param params string: passphrase
                     string: optional, hex string for seed, if not set, will create seed randomly
     * @return string : master address of the wallet
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject newWallet(JSONArray params) throws IOException {
        return client.call("wallet_newWallet", params);
    }


    /**
     * Change wallet password
     * @param params string: master address of the wallet
                     string: old passphrase
                     string: new passphrase
     * @return null
     * @throws QlcException qlc exception
     * @throws IOException io exception
     */
    public JSONObject changePassword(JSONArray params) throws IOException {
        return client.call("wallet_changePassword", params);
    }

}
