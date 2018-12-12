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

import io.eblock.eos4j.EosRpcService;
import io.eblock.eos4j.api.vo.transaction.Transaction;

import static com.stratagile.qlink.blockchain.EoscDataManager.eosNode;

/**
 * Created by pocketEos on 2018/5/8.
 * Dapp合约调用
 */

public class PushDatamanger {

    public String permissonion = "owner";
    Callback mCallback;
    Context mContext;
    EosChainInfo mChainInfoBean = new EosChainInfo();
    JsonToBeanResultBean mJsonToBeanResultBean = new JsonToBeanResultBean();
    String[] permissions;
    SignedTransaction txnBeforeSign;
    EosRpcService rpc;
    String privateKeyStr;
    String permissionAccount;
    Gson mGson = new GsonBuilder()
            .registerTypeAdapterFactory(new GsonEosTypeAdapterFactory())
            .excludeFieldsWithoutExposeAnnotation().create();

    String contract, message;

    public PushDatamanger(Context context, String privateKeyStr, String permiss) {
        mContext = context;
        rpc = new EosRpcService(eosNode);
        this.privateKeyStr = privateKeyStr;
        permissonion = permiss;
    }

    public void createAccount(String message, String buyRamBean, String permissionAccount, Callback callback) {
        this.message = message;
        this.contract = "eosio";
        mCallback = callback;
        this.permissionAccount = permissionAccount;
        permissions = new String[]{permissionAccount + "@" + permissonion};
        getChainInfo();

        String buyRamBin = getAbiJsonBean(contract, "buyrambytes", buyRamBean);

        String createAccountBin = getAbiJsonBean(contract, "newaccount", message);

        KLog.i(buyRamBin);
        KLog.i(createAccountBin);

        Action buyRamAction = new Action(contract, "buyrambytes");
        buyRamAction.setAuthorization(permissions);
        buyRamAction.setData(buyRamBin);

        Action createAccountAction = new Action(contract, "newaccount");
        createAccountAction.setAuthorization(permissions);
        createAccountAction.setData(createAccountBin);


        SignedTransaction txn = new SignedTransaction();
        txn.addAction(createAccountAction);
        txn.addAction(buyRamAction);

        txn.putSignatures(new ArrayList<String>());


        if (null != mChainInfoBean) {
            txn.setReferenceBlock(mChainInfoBean.getHeadBlockId());
            txn.setExpiration(mChainInfoBean.getTimeAfterHeadBlockTime(60000));
        }
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txn.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txn));

    }

    /**
     * 抵押操作
     */
    public void stakeCpuAndNet(String cpuAndNetMessage, String permissionAccount, Callback callback) {
        this.contract = "eosio";
        mCallback = callback;
        this.permissionAccount = permissionAccount;
        permissions = new String[]{permissionAccount + "@" + permissonion};
        getChainInfo();
        String stakeCpuBin = getAbiJsonBean(contract, "delegatebw", cpuAndNetMessage);
        Action stakeAction = new Action(contract, "delegatebw");
        stakeAction.setAuthorization(permissions);
        stakeAction.setData(stakeCpuBin);

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(stakeAction);

        txn.putSignatures(new ArrayList<String>());


        if (null != mChainInfoBean) {
            txn.setReferenceBlock(mChainInfoBean.getHeadBlockId());
            txn.setExpiration(mChainInfoBean.getTimeAfterHeadBlockTime(60000));
        }
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txn.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txn));
    }

    /**
     * 赎回cpu和net
     */
    public void unStakeCpuAndNet(String cpuAndNetMessage, String permissionAccount, Callback callback) {
        this.contract = "eosio";
        mCallback = callback;
        this.permissionAccount = permissionAccount;
        permissions = new String[]{permissionAccount + "@" + permissonion};
        getChainInfo();
        String stakeCpuBin = getAbiJsonBean(contract, "undelegatebw", cpuAndNetMessage);
        Action stakeAction = new Action(contract, "undelegatebw");
        stakeAction.setAuthorization(permissions);
        stakeAction.setData(stakeCpuBin);

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(stakeAction);

        txn.putSignatures(new ArrayList<String>());


        if (null != mChainInfoBean) {
            txn.setReferenceBlock(mChainInfoBean.getHeadBlockId());
            txn.setExpiration(mChainInfoBean.getTimeAfterHeadBlockTime(60000));
        }
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txn.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txn));
    }

    /**
     * 购买ram
     */
    public void buyRam(String ramMessage, String permissionAccount, Callback callback) {
        this.contract = "eosio";
        mCallback = callback;
        this.permissionAccount = permissionAccount;
        permissions = new String[]{permissionAccount + "@" + permissonion};
        getChainInfo();
        String buyRamBin = getAbiJsonBean(contract, "buyram", ramMessage);
        Action buyRamAction = new Action(contract, "buyram");
        buyRamAction.setAuthorization(permissions);
        buyRamAction.setData(buyRamBin);

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(buyRamAction);

        txn.putSignatures(new ArrayList<String>());


        if (null != mChainInfoBean) {
            txn.setReferenceBlock(mChainInfoBean.getHeadBlockId());
            txn.setExpiration(mChainInfoBean.getTimeAfterHeadBlockTime(60000));
        }
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txn.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txn));
    }

    /**
     * 出售ram
     */
    public void sellRam(String ramMessage, String permissionAccount, Callback callback) {
        this.contract = "eosio";
        mCallback = callback;
        this.permissionAccount = permissionAccount;
        permissions = new String[]{permissionAccount + "@" + permissonion};
        getChainInfo();
        String buyRamBin = getAbiJsonBean(contract, "sellram", ramMessage);
        Action buyRamAction = new Action(contract, "sellram");
        buyRamAction.setAuthorization(permissions);
        buyRamAction.setData(buyRamBin);

        SignedTransaction txn = new SignedTransaction();
        txn.addAction(buyRamAction);

        txn.putSignatures(new ArrayList<String>());


        if (null != mChainInfoBean) {
            txn.setReferenceBlock(mChainInfoBean.getHeadBlockId());
            txn.setExpiration(mChainInfoBean.getTimeAfterHeadBlockTime(60000));
        }
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txn.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txn));
    }

    public void pushAction(String message, String permissionAccount, Callback callback) {
        this.message = message;
        this.contract = "eosio";
        mCallback = callback;
        this.permissionAccount = permissionAccount;
        permissions = new String[]{permissionAccount + "@" + permissonion};
        getChainInfo();
    }

    public void getChainInfo() {
        mChainInfoBean = rpc.getChainInfo1();
//        getAbiJsonBean();
    }

    private String getAbiJsonBean(String contract, String action, String message) {
        JsonToBinRequest jsonToBinRequest = new JsonToBinRequest(contract, action, message.replaceAll("\\r|\\n", ""));

        String jsonStr = mGson.toJson(jsonToBinRequest);
        KLog.i(jsonStr);
        JsonToBeanResultBean mJsonToBeanResultBean = rpc.getAbiJsonToBean(jsonToBinRequest);
        return mJsonToBeanResultBean.getBinargs();
    }



    public void getAbiJsonBean() {
        JsonToBinRequest jsonToBinRequest = new JsonToBinRequest(contract, "transfer", message.replaceAll("\\r|\\n", ""));

        String jsonStr = mGson.toJson(jsonToBinRequest);
        KLog.i(jsonStr);

        mJsonToBeanResultBean = rpc.getAbiJsonToBean(jsonToBinRequest);
        txnBeforeSign = createTransaction(contract, "transfer", mJsonToBeanResultBean.getBinargs(), permissions, mChainInfoBean);
        EosPrivateKey eosPrivateKey = new EosPrivateKey(privateKeyStr);
        txnBeforeSign.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
        pushTransactionRetJson(new PackedTransaction(txnBeforeSign));
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

//    public void getRequreKey(GetRequiredKeys getRequiredKeys) {
//
//        HttpUtils.postRequest(BaseUrl.HTTP_get_required_keys, this, mGson.toJson(getRequiredKeys), new JsonCallback<ResponseBean>() {
//            @Override
//            public void onSuccess(Response<ResponseBean> response) {
//                if (response.body().code == 0) {
//                    RequreKeyResult requreKeyResult = (RequreKeyResult) JsonUtil.parseStringToBean(mGson.toJson(response.body().data), RequreKeyResult.class);
//                    EosPrivateKey eosPrivateKey = new EosPrivateKey(PublicAndPrivateKeyUtils.getPrivateKey(requreKeyResult.getRequired_keys().get(0), userpassword));
//                    txnBeforeSign.sign(eosPrivateKey, new TypeChainId(mChainInfoBean.getChain_id()));
//                    pushTransactionRetJson(new PackedTransaction(txnBeforeSign));
//                } else {
//                    if (ShowDialog.dialog != null) {
//                        ShowDialog.dissmiss();
//                    }
//                    ToastUtils.showLongToast(response.body().message);
//                    mCallback.getResult(new Gson().toJson(response.body().data));
//                }
//            }
//        });
//
//    }

    public void pushTransactionRetJson(PackedTransaction body) {
        try {
            KLog.i(new Gson().toJson(body));
            Transaction transaction = rpc.pushTransaction(body);
            if (mCallback != null) {
                mCallback.getResult(transaction.getProcessed().getId());
            }
        } catch (Exception e) {
            if (mCallback != null) {
                mCallback.getResult("");
            }
        }
    }
    public interface Callback {
        void getResult(String result);
    }
}
