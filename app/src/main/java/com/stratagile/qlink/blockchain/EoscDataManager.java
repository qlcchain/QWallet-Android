/*
 * Copyright (c) 2017-2018 PlayerOne.
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.stratagile.qlink.blockchain;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.socks.library.KLog;
import com.stratagile.qlink.blockchain.api.EosChainInfo;
import com.stratagile.qlink.blockchain.bean.JsonToBeanResultBean;
import com.stratagile.qlink.blockchain.bean.JsonToBinRequest;
import com.stratagile.qlink.blockchain.chain.Action;
import com.stratagile.qlink.blockchain.chain.PackedTransaction;
import com.stratagile.qlink.blockchain.chain.SignedTransaction;
import com.stratagile.qlink.blockchain.cypto.ec.EosPrivateKey;
import com.stratagile.qlink.blockchain.types.TypeChainId;
import com.stratagile.qlink.blockchain.util.GsonEosTypeAdapterFactory;

import java.util.ArrayList;

import io.eblock.eos4j.Ecc;
import io.eblock.eos4j.EosRpcService;
import io.eblock.eos4j.api.vo.transaction.Transaction;

/**
 * Created by swapnibble on 2017-11-03.
 */
public class EoscDataManager {
    public static String eosNode = "http://api-mainnet.starteos.io";
    Context mContext;
    EosChainInfo mChainInfoBean = new EosChainInfo();
    JsonToBeanResultBean mJsonToBeanResultBean = new JsonToBeanResultBean();
    String[] permissions;
    SignedTransaction txnBeforeSign;
    String contract, action, message;
    String privateKeyStr;
    EosRpcService rpc;
    private EosCallBack eosCallBack;
    Gson mGson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation().create();
    public EoscDataManager(Context context, String privateKeyStr, String contract) {
        mContext = context;
        this.contract = contract;
        this.privateKeyStr = privateKeyStr;
        rpc = new EosRpcService(eosNode);
    }

    public void pushAction(String message, String permissionAccount, EosCallBack eosCallBack) {
        this.message = message;
        this.action = "transfer";
        permissions = new String[]{permissionAccount + "@" + "owner"};
        this.eosCallBack = eosCallBack;
        getChainInfo();
    }

    public void getChainInfo() {
        mChainInfoBean = rpc.getChainInfo1();
        getAbiJsonBean();
    }

    public void getAbiJsonBean() {
        JsonToBinRequest jsonToBinRequest = new JsonToBinRequest(contract, action, message.replaceAll("\\r|\\n", ""));
        mJsonToBeanResultBean = rpc.getAbiJsonToBean(jsonToBinRequest);
        txnBeforeSign = createTransaction(contract, action, mJsonToBeanResultBean.getBinargs(), permissions, mChainInfoBean);
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txnBeforeSign.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txnBeforeSign));
    }

    private void pushTransactionRetJson(PackedTransaction body) {
        try {
            Transaction transaction = rpc.pushTransaction(body);
            if (eosCallBack != null) {
                eosCallBack.pushTransactionCallBack(transaction.getProcessed().getId());
            }
        } catch (Exception e) {
            if (eosCallBack != null) {
                eosCallBack.pushTransactionCallBack("");
            }
        }
    }

    private SignedTransaction createTransaction(String contract, String actionName, String dataAsHex, String[] permissions, EosChainInfo chainInfo) {

        Action action = new Action(contract, actionName);
        action.setAuthorization(permissions);
        action.setData(dataAsHex);


        SignedTransaction txn = new SignedTransaction();
        txn.addAction(action);
        txn.putSignatures(new ArrayList<String>());


        if (null != chainInfo) {
            txn.setReferenceBlock(chainInfo.getHeadBlockId());
            txn.setExpiration(chainInfo.getTimeAfterHeadBlockTime(30000));
        }
        return txn;
    }

    public interface EosCallBack {
        void pushTransactionCallBack(String txid);
    }
}
