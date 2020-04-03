package com.stratagile.qlink.ui.activity.main.presenter;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;

import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.blockchain.cypto.util.HexUtils;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.NeoNodeRPC;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.ui.activity.main.contract.TestContract;
import com.stratagile.qlink.ui.activity.main.TestActivity;
import com.stratagile.qlink.utils.SpUtil;

import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: presenter of TestActivity
 * @date 2018/12/18 11:09:36
 */
public class TestPresenter implements TestContract.TestContractPresenter{

    HttpAPIWrapper httpAPIWrapper;
    private final TestContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private TestActivity mActivity;

    @Inject
    public TestPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull TestContract.View view, TestActivity activity) {
        mView = view;
        this.httpAPIWrapper = httpAPIWrapper;
        mCompositeDisposable = new CompositeDisposable();
        this.mActivity = activity;
    }
    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {
        if (!mCompositeDisposable.isDisposed()) {
             mCompositeDisposable.dispose();
        }
    }

    public void reportWalletCreated(String address, String blockChain, String publicKey, String wif) {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("address", address);
        infoMap.put("blockChain", blockChain);
        infoMap.put("p2pId", SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""));
        infoMap.put("pubKey", publicKey);
        NeoNodeRPC neoNodeRPC = new NeoNodeRPC("");
//        infoMap.put("signData", signingtrasaction(wif, SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "") + address));
        Disposable disposable = httpAPIWrapper.reportWalletCreate(infoMap)
                .subscribe(new Consumer<BaseBack>() {
                    @Override
                    public void accept(BaseBack baseBack) throws Exception {
                        //isSuccesse
                        mView.closeProgressDialog();
                        KLog.i(baseBack.getMsg());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.closeProgressDialog();
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                        mView.closeProgressDialog();
                    }
                });
        mCompositeDisposable.add(disposable);
    }

//    public String signingtrasaction(String wif, String msg) {
//        try {
//            // creating a key object from WiF
//            DumpedPrivateKey dpk = DumpedPrivateKey.fromBase58(getParams(), wif);
//            ECKey key = dpk.getKey();
//            // checking our key object
//            // NetworkParameters main = MainNetParams.get();
//            String check = key.getPrivateKeyAsWiF(getParams());
//            System.out.println(wif.equals(check));  // true
//            Log.e("wif check", String.valueOf(wif.equals(check)));
//            String sign = key.signMessage(msg);
//            KLog.i(sign);
//            KLog.i(key.toString());
//            ECKey ecKey = ECKey.signedMessageToKey(msg, sign);
//            KLog.i(ecKey);
//            // creating Sha object from string
////            Sha256Hash hash = Sha256Hash.wrap(msg);
//            // creating signature
////            ECKey.ECDSASignature sig = key.signMessage(msg);
//            // encoding
////            byte[] res = sig.encodeToDER();
////            // converting to hex
////            //String hex = DatatypeConverter.printHexBinary(res);
////            // String hex = new String(res);
////            String hex = new String(Base64.encode(res, Base64.DEFAULT));
////            Log.e("sigendTransaction", hex);
////            Log.e("decrypttx",""+ HexUtils.toHex(sig.encodeToDER()));
//            return sign;
//        } catch (Exception e) {   //signingkey = ecdsa.from_string(privateKey.decode('hex'), curve=ecdsa.SECP256k1)
//            e.printStackTrace();
//            return "";
//        }
//    }

}