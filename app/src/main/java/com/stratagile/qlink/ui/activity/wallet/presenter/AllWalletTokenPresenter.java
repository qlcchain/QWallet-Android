package com.stratagile.qlink.ui.activity.wallet.presenter;

import android.Manifest;
import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.ui.activity.wallet.contract.AllWalletTokenContract;
import com.stratagile.qlink.ui.activity.wallet.AllWalletTokenActivity;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.ToastUtil;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.RationaleListener;

import org.greenrobot.eventbus.EventBus;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.http.HttpService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: presenter of AllWalletTokenActivity
 * @date 2018/10/24 15:49:29
 */
public class AllWalletTokenPresenter implements AllWalletTokenContract.AllWalletTokenContractPresenter {

    HttpAPIWrapper httpAPIWrapper;
    private final AllWalletTokenContract.View mView;
    private CompositeDisposable mCompositeDisposable;
    private AllWalletTokenActivity mActivity;

    @Inject
    public AllWalletTokenPresenter(@NonNull HttpAPIWrapper httpAPIWrapper, @NonNull AllWalletTokenContract.View view, AllWalletTokenActivity activity) {
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

    @Override
    public void getETHWalletDetail(String address, Map map) {
        Disposable disposable = httpAPIWrapper.getEthWalletInfo(map)
                .subscribe(new Consumer<EthWalletInfo>() {
                    @Override
                    public void accept(EthWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        getEthTokensInfo(baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getNeoWalletDetail(String address, Map map) {
        Disposable disposable = httpAPIWrapper.getNeoWalletInfo(map)
                .subscribe(new Consumer<NeoWalletInfo>() {
                    @Override
                    public void accept(NeoWalletInfo baseBack) throws Exception {
                        //isSuccesse
                        getNeoTokensInfo(baseBack);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void getToeknPrice(ArrayList<TokenInfo> arrayList, HashMap map) {
        Disposable disposable = httpAPIWrapper.getTokenPrice(map)
                .subscribe(new Consumer<TokenPrice>() {
                    @Override
                    public void accept(TokenPrice baseBack) throws Exception {
                        //isSuccesse
                        for (int i = 0; i < baseBack.getData().size(); i++) {
                            if (arrayList.get(i).isMainNetToken()) {
                                arrayList.get(i).setTokenPrice(baseBack.getData().get(i).getPrice());
                            }
                        }
                        mView.getTokenPriceBack(arrayList);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        //onComplete
                        KLog.i("onComplete");
                    }
                });
        mCompositeDisposable.add(disposable);
    }

    private void getTokenPrice(ArrayList<TokenInfo> tokenInfos) {
        HashMap<String, Object> infoMap = new HashMap<>();
        String[] tokens = new String[tokenInfos.size()];
        for (int i = 0; i < tokenInfos.size(); i++) {
            tokens[i] = tokenInfos.get(i).getTokenSymol();
        }
        infoMap.put("symbols", tokens);
        infoMap.put("coin", ConstantValue.currencyBean.getName());

        getToeknPrice(tokenInfos, infoMap);
    }

    private void getEthTokensInfo(EthWalletInfo ethWalletInfo) {
        ArrayList<TokenInfo> tokenInfos = new ArrayList<>();
        TokenInfo tokenInfo1 = new TokenInfo();
        tokenInfo1.setTokenName("ETH");
        tokenInfo1.setTokenSymol("ETH");
        tokenInfo1.setTokenAddress(ethWalletInfo.getData().getAddress());
        tokenInfo1.setTokenValue(ethWalletInfo.getData().getETH().getBalance());
        tokenInfo1.setTokenImgName("eth_eth");
        tokenInfo1.setWalletType(AllWallet.WalletType.EthWallet);
        tokenInfo1.setWalletAddress(ethWalletInfo.getData().getAddress());
        tokenInfo1.setMainNetToken(true);
        tokenInfos.add(tokenInfo1);
        if (ethWalletInfo.getData().getTokens() == null) {
            getTokenPrice(tokenInfos);
            return;
        }
        for (int i = 0; i < ethWalletInfo.getData().getTokens().size(); i++) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol());
            tokenInfo.setTokenSymol(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getSymbol());
            tokenInfo.setTokenValue(ethWalletInfo.getData().getTokens().get(i).getBalance());
            tokenInfo.setTokenAddress(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getAddress());
            tokenInfo.setTokenDecimals(new Integer(ethWalletInfo.getData().getTokens().get(i).getTokenInfo().getDecimals()));
            tokenInfo.setWalletType(AllWallet.WalletType.EthWallet);
            tokenInfo.setWalletAddress(ethWalletInfo.getData().getAddress());
            if (isEthMainNetToken(ethWalletInfo.getData().getTokens().get(i))) {
                tokenInfo.setTokenImgName(getEthTokenImg(ethWalletInfo.getData().getTokens().get(i)));
                tokenInfo.setMainNetToken(true);
            } else {
                tokenInfo.setMainNetToken(false);
            }
            tokenInfos.add(tokenInfo);
        }
        getTokenPrice(tokenInfos);
    }

    private int getTokenCount(EthWalletInfo.DataBean.TokensBean tokensBean) {
        return (int) (tokensBean.getBalance() / Math.pow(10, Double.parseDouble(tokensBean.getTokenInfo().getDecimals())));
    }

    /**
     * 判断该token是否为主网的token，是否有估值
     *
     * @return
     */
    private boolean isEthMainNetToken(EthWalletInfo.DataBean.TokensBean tokensBean) {
        String jsonStr = new Gson().toJson(tokensBean.getTokenInfo().getPrice());
        KLog.i(jsonStr);
        if (!"false".equals(jsonStr)) {
            return true;
        } else {
            //没有上交易所
            return false;
        }
    }

    private String getEthTokenImg(EthWalletInfo.DataBean.TokensBean tokensBean) {
        String jsonStr = new Gson().toJson(tokensBean.getTokenInfo().getPrice());
        KLog.i(jsonStr);
        if (!"false".equals(jsonStr)) {
            JSONObject.parseObject(jsonStr).getString("ts");
            return "eth_" + tokensBean.getTokenInfo().getSymbol().toLowerCase();
        } else {
            //没有上交易所
            return "";
        }
    }

    private void getNeoTokensInfo(NeoWalletInfo neoWalletInfo) {
        ArrayList<TokenInfo> tokenInfos = new ArrayList<>();
        for (int i = 0; i < neoWalletInfo.getData().getBalance().size(); i++) {
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setTokenName(neoWalletInfo.getData().getBalance().get(i).getAsset());
            tokenInfo.setTokenSymol(neoWalletInfo.getData().getBalance().get(i).getAsset_symbol());
            tokenInfo.setTokenValue(neoWalletInfo.getData().getBalance().get(i).getAmount());
            tokenInfo.setTokenImgName(getNeoTokenImg(neoWalletInfo.getData().getBalance().get(i)));
            tokenInfo.setWalletAddress(neoWalletInfo.getData().getAddress());
            tokenInfo.setMainNetToken(true);
            tokenInfo.setWalletType(AllWallet.WalletType.NeoWallet);
            tokenInfos.add(tokenInfo);
        }
        getTokenPrice(tokenInfos);
    }

    private String getNeoTokenImg(NeoWalletInfo.DataBean.BalanceBean balanceBean) {
        return "neo_" + balanceBean.getAsset_symbol().toLowerCase();
    }

    /**
     * 查询代币余额
     */
    public static Observable<BigInteger> getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {
        return Observable.just(fromAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new io.reactivex.functions.Function<String, BigInteger>() {
                    @Override
                    public BigInteger apply(String s) throws Exception {
                        String methodName = "balanceOf";
                        List<Type> inputParameters = new ArrayList<>();
                        List<TypeReference<?>> outputParameters = new ArrayList<>();
                        Address address = new Address(fromAddress);
                        inputParameters.add(address);

                        TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
                        };
                        outputParameters.add(typeReference);
                        Function function = new Function(methodName, inputParameters, outputParameters);
                        String data = FunctionEncoder.encode(function);
                        Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

                        EthCall ethCall;
                        BigInteger balanceValue = BigInteger.ZERO;
                        try {
                            ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
                            List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
                            balanceValue = (BigInteger) results.get(0).getValue();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return balanceValue;
                    }
                });
    }

    @Override
    public void getPermission() {
        AndPermission.with(mActivity)
                .requestCode(101)
                .permission(
                        Manifest.permission.CAMERA
                )
                .rationale(rationaleListener)
                .callback(permission)
                .start();
    }

    private PermissionListener permission = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            LocalAssetsUtils.updateGreanDaoFromLocal();
            // 权限申请成功回调。
            if (requestCode == 101) {
                mView.getCameraPermissionSuccess();
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 101) {
                KLog.i("权限申请失败");

            }
        }
    };

    /**
     * Rationale支持，这里自定义对话框。
     */
    private RationaleListener rationaleListener = (requestCode, rationale) -> {
        AlertDialog.newBuilder(mActivity)
                .setTitle(AppConfig.getInstance().getResources().getString(R.string.Permission_Requeset))
                .setMessage(AppConfig.getInstance().getResources().getString(R.string.We_Need_Some_Permission_to_continue))
                .setPositiveButton(AppConfig.getInstance().getResources().getString(R.string.Agree), (dialog, which) -> {
                    rationale.resume();
                })
                .setNegativeButton(AppConfig.getInstance().getResources().getString(R.string.Reject), (dialog, which) -> {
                    rationale.cancel();
                }).show();
    };

}