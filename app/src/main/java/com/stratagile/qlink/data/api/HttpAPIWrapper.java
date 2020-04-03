package com.stratagile.qlink.data.api;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.constant.MainConstant;
import com.stratagile.qlink.entity.Active;
import com.stratagile.qlink.entity.ActiveList;
import com.stratagile.qlink.entity.AppVersion;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.BurnQgasAct;
import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.entity.ClaimData;
import com.stratagile.qlink.entity.ConnectedWifiRecord;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.entity.EntrustOrderList;
import com.stratagile.qlink.entity.EosAccountInfo;
import com.stratagile.qlink.entity.EosAccountTransaction;
import com.stratagile.qlink.entity.EosKeyAccount;
import com.stratagile.qlink.entity.EosResource;
import com.stratagile.qlink.entity.EosTokens;
import com.stratagile.qlink.entity.EthWalletDetail;
import com.stratagile.qlink.entity.EthWalletInfo;
import com.stratagile.qlink.entity.EthWalletTransaction;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.entity.GoogleResult;
import com.stratagile.qlink.entity.GotWinqGas;
import com.stratagile.qlink.entity.ImportWalletResult;
import com.stratagile.qlink.entity.IndexInterface;
import com.stratagile.qlink.entity.InviteList;
import com.stratagile.qlink.entity.KLine;
import com.stratagile.qlink.entity.LocalTokenBean;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.entity.NeoTransfer;
import com.stratagile.qlink.entity.NeoWalletInfo;
import com.stratagile.qlink.entity.NeoWalletTransactionHistory;
import com.stratagile.qlink.entity.OnlyEthTransactionHistory;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.entity.RecordVpn;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.entity.ReportList;
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.entity.ShowAct;
import com.stratagile.qlink.entity.SmsReport;
import com.stratagile.qlink.entity.SysTime;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.entity.Tpcs;
import com.stratagile.qlink.entity.TradeOrder;
import com.stratagile.qlink.entity.TransactionResult;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.UpdateVpn;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.entity.VCodeVerifyCode;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.entity.VoteResult;
import com.stratagile.qlink.entity.WifiRegisteResult;
import com.stratagile.qlink.entity.WinqGasBack;
import com.stratagile.qlink.entity.eos.EosNeedInfo;
import com.stratagile.qlink.entity.eos.EosResourcePrice;
import com.stratagile.qlink.entity.finance.EarnRank;
import com.stratagile.qlink.entity.finance.HistoryRecord;
import com.stratagile.qlink.entity.finance.MyRanking;
import com.stratagile.qlink.entity.mining.MiningIndex;
import com.stratagile.qlink.entity.mining.MiningRank;
import com.stratagile.qlink.entity.mining.MiningRewardList;
import com.stratagile.qlink.entity.newwinq.MiningAct;
import com.stratagile.qlink.entity.newwinq.Order;
import com.stratagile.qlink.entity.newwinq.Product;
import com.stratagile.qlink.entity.newwinq.ProductDetail;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.entity.otc.EntrustOrderInfo;
import com.stratagile.qlink.entity.otc.GenerageTradeOrder;
import com.stratagile.qlink.entity.otc.Passport;
import com.stratagile.qlink.entity.otc.TradeOrderDetail;
import com.stratagile.qlink.entity.otc.TradeOrderList;
import com.stratagile.qlink.entity.otc.TradePair;
import com.stratagile.qlink.entity.reward.ClaimQgas;
import com.stratagile.qlink.entity.reward.Dict;
import com.stratagile.qlink.entity.reward.InviteTotal;
import com.stratagile.qlink.entity.reward.InviteeList;
import com.stratagile.qlink.entity.reward.RewardList;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.entity.stake.UnLock;
import com.stratagile.qlink.entity.topup.CountryList;
import com.stratagile.qlink.entity.topup.CreateGroup;
import com.stratagile.qlink.entity.topup.GroupItemList;
import com.stratagile.qlink.entity.topup.IspList;
import com.stratagile.qlink.entity.topup.PayToken;
import com.stratagile.qlink.entity.topup.ProductListV2;
import com.stratagile.qlink.entity.topup.SalePartner;
import com.stratagile.qlink.entity.topup.TopupGroupKindList;
import com.stratagile.qlink.entity.topup.TopupGroupList;
import com.stratagile.qlink.entity.topup.TopupJoinGroup;
import com.stratagile.qlink.entity.topup.TopupOrder;
import com.stratagile.qlink.entity.topup.TopupOrderList;
import com.stratagile.qlink.entity.topup.TopupProduct;
import com.stratagile.qlink.ui.adapter.topup.TopupGroupItemAdapter;
import com.stratagile.qlink.utils.DigestUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.SystemUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.VersionUtil;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import qlc.network.QlcException;
import retrofit2.HttpException;

/**
 * @author hu
 * @desc 对Request实体(不执行)在执行时所调度的线程，以及得到ResponseBody后根据retCode对Result进行进一步处理
 * @date 2017/5/31 16:56
 */
public class HttpAPIWrapper {

    private HttpApi mHttpAPI;
    private MainHttpApi mMainHttpAPI;

    @Inject
    public HttpAPIWrapper(HttpApi mHttpAPI, MainHttpApi mMainHttpAPI) {
        this.mHttpAPI = mHttpAPI;
        this.mMainHttpAPI = mMainHttpAPI;
    }

    public Observable<WifiRegisteResult> getRegistedSsid(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getRegistedSsid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRegistedSsid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<RegisterWiFi> registeWWifi(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.registeWWifi(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.registeWWifi(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RegisterWiFi> registeWWifiV3(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.registeWWifiV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.registeWWifiV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> createWallet(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.createWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.createWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<CreateWallet> importWallet(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.importWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.importWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Balance> getBalance(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getBalance(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getBalance(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Reward> reward(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.reward(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.reward(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Raw> getRaw(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getRaw(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRaw(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Record> recordQuerys(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.recordQuerys(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.recordQuerys(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ConnectedWifiRecord> recordSave(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.recordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.recordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BuyQlc> buyQlc(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.buyQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.buyQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BuyQlc> trasaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.trasaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.trasaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RegisterWiFi> vpnRegister(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vpnRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RegisterVpn> vpnRegisterV2(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vpnRegisterV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnRegisterV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ChainVpn> vpnQuery(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vpnQuery(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnQuery(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ChainVpn> vpnQueryV3(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vpnQueryV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnQueryV3(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<VertifyVpn> vertifyVpnName(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vertifyVpnName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vertifyVpnName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<RecordVpn> vpnRecordSave(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vpnRecordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vpnRecordSave(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<String> getNodes() {
        if (false) {
            return wrapperGetToxJson(mMainHttpAPI.getNodes()).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapperGetToxJson(mHttpAPI.getNodes()).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<GoogleResult> latlngParseCountry(Map map) {
        if (false) {
            return wrapperObject(mMainHttpAPI.latlngParseCountry(map)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapperObject(mHttpAPI.latlngParseCountry(map)).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<ImportWalletResult> batchImportWallet(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.batchImportWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.batchImportWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<UpLoadAvatar> updateMyAvatar(MultipartBody.Part photo, RequestBody account, RequestBody token) {     //String userId, String nickName   userId, nickName
        if (false) {
            return wrapper(mMainHttpAPI.updateMyAvatar(account, token, photo)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateMyAvatar(account, token, photo)).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<Passport> updateIdCard(MultipartBody.Part photo, MultipartBody.Part holdphoto, RequestBody account, RequestBody token) {     //String userId, String nickName   userId, nickName
        if (false) {
            return wrapper(mMainHttpAPI.updateIdCard(token, account, photo, holdphoto)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateIdCard(token, account, photo, holdphoto)).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<UpLoadAvatar> userHeadView(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.userHeadView(map)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.userHeadView(map)).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> heartBeat(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.heartBeat(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.heartBeat(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<UpdateVpn> updateVpnInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.updateVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> updateWiFiInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.updateWiFiInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.updateWiFiInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }


    public Observable<AssetsWarpper> getUnspentAsset(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<SysTime> getServerTime(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getServerTime(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getServerTime(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> sendRawTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.sendRawTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.sendRawTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<TransactionResult> v2Transaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.v2Transaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.v2Transaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<MainAddress> getMainAddress(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getMainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getMainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BuyQlc> bnb2qlc(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.bnb2qlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.bnb2qlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<EthWalletDetail> getEthAddressDetail(String address, Map map) {
        if (false) {
            return wrapperETH(mMainHttpAPI.getEthAddressDetail(address, map)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapperETH(mHttpAPI.getEthAddressDetail(address, map)).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<FreeNum> zsFreeNum(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.zsFreeNum(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.zsFreeNum(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<FreeNum> freeConnection(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.freeConnection(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.freeConnection(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<FreeRecord> queryFreeRecords(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.queryFreeRecords(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.queryFreeRecords(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }

    }

    public Observable<BaseBack> reportVpnInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.reportVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.reportVpnInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Active> getAct(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getAct(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getAct(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ActiveList> getActAsset(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getActAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getActAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ShowAct> getShowAct(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getShowAct(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getShowAct(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EthWalletInfo> getEthWalletInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEthWalletInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEthWalletInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<NeoWalletInfo> getNeoWalletInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getNeoWalletInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getNeoWalletInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TokenPrice> getTokenPrice(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTokenPrice(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTokenPrice(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EthWalletTransaction> getEthWalletTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEthWalletTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEthWalletTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<OnlyEthTransactionHistory> getOnlyEthTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getOnlyEthTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getOnlyEthTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<NeoWalletTransactionHistory> getNeoWalletTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getNeoWalletTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getNeoWalletTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }


    public Observable<AssetsWarpper> getMainUnspentAsset(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getMainUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getMainUnspentAsset(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<NeoTransfer> neoTokenTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.neoTokenTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.neoTokenTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Tpcs> getTpcs(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTpcs(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTpcs(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<KLine> getTokenKLine(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTokenKLine(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTokenKLine(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> reportWalletTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.reportWalletTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.reportWalletTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> reportWalletCreate(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.reportWalletCreate(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.reportWalletCreate(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<WinqGasBack> queryWinqGas(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.queryWinqGas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.queryWinqGas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<GotWinqGas> gotWinqGas(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.gotWinqGas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.gotWinqGas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    //这个接口都用主网的，不用测试网的
    public Observable<ClaimData> neoGasClaim(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.neoGasClaim(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mMainHttpAPI.neoGasClaim(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EosAccountInfo> getEosAccountInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEosAccountInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEosAccountInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EosTokens> getEosTokenList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEosTokenList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEosTokenList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EosResource> getEosTResource(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEosTResource(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEosTResource(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EosAccountTransaction> getEosAccountTransaction(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEosAccountTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEosAccountTransaction(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<LocalTokenBean> getBinaTokens(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getBinaTokens(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getBinaTokens(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> createEosAccount(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.createEosAccount(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.createEosAccount(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EosNeedInfo> getEosNeedInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEosNeedInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEosNeedInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EosResourcePrice> getEosResourcePrice(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEosResourcePrice(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEosResourcePrice(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }


    public Observable<EosKeyAccount> getKeyAccount(Map map) {
        if (false) {
            return (mMainHttpAPI.getKeyAccount(map.get("public_key").toString())).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return (mHttpAPI.getKeyAccount(map.get("public_key").toString())).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> getSignUpVcode(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getSignUpVcode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getSignUpVcode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> getSignInVcode(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getSignInVcode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getSignInVcode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> getForgetPasswordVcode(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getForgetPasswordVcode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getForgetPasswordVcode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<VcodeLogin> resetPassword(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.resetPassword(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.resetPassword(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<VcodeLogin> userRegister(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.userRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.userRegister(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Register> userLogin(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.userLogin(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.userLogin(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<VcodeLogin> vCodeLogin(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.vCodeLogin(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vCodeLogin(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Product> getProductList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getProductList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getProductList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ProductDetail> getProductDetail(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getProductDetail(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getProductDetail(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> buyQLCProduct(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.buyQLCProduct(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.buyQLCProduct(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Order> getOrderList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Order> redeemOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.redeemOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.redeemOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    //更多的排名
    public Observable<MyRanking> getRankings(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getRankings(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRankings(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    //富豪榜
    public Observable<EarnRank> getEarnRankings(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEarnRankings(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEarnRankings(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<HistoryRecord> getHistoryRecord(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getHistoryRecord(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getHistoryRecord(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<InviteList> getInivteTop5(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getInivteTop5(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getInivteTop5(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> changeNickName(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.changeNickName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.changeNickName(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> generateBuyQgasOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.generateEntrustBuyQgasOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.generateEntrustBuyQgasOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EntrustOrderList> getEntrustOrderList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEntrustOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEntrustOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<EntrustOrderInfo> getEntrustOrderInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getEntrustOrderInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getEntrustOrderInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> cancelEntrustOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.cancelEntrustOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.cancelEntrustOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<GenerageTradeOrder> generateTradeBuyQgasOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.generateTradeBuyQgasOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.generateTradeBuyQgasOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> tradeBuyerConfirm(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.tradeBuyerConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.tradeBuyerConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TradeOrder> generateTradeSellOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.generateTradeSellOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.generateTradeSellOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TradeOrder> tradeSellOrderTxid(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.tradeSellOrderTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.tradeSellOrderTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> tradeSellerConfirm(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.tradeSellerConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.tradeSellerConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TradeOrderList> tradeOrderList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.tradeOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.tradeOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TradeOrderDetail> tradeOrderInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.tradeOrderInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.tradeOrderInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> tradeOrderCancel(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.tradeOrderCancel(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.tradeOrderCancel(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<UserInfo> getUserInfo(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getUserInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getUserInfo(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<AppVersion> getAppLastVersion(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getAppLastVersion(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getAppLastVersion(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TradeOrderDetail> generateAppeal(RequestBody account, RequestBody token, RequestBody tradeOrderId, RequestBody reason, MultipartBody.Part photo1, MultipartBody.Part photo2, MultipartBody.Part photo3, MultipartBody.Part photo4) {
        if (false) {
            return wrapper(mMainHttpAPI.generateAppeal(account, token, tradeOrderId, reason, photo1, photo2, photo3, photo4)).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.generateAppeal(account, token, tradeOrderId, reason, photo1, photo2, photo3, photo4)).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TradePair> getPairs(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getPairs(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getPairs(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<UnLock> unLock(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.unLock(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.unLock(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupProduct> getTopupProductList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTopupProductList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTopupProductList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ProductListV2> getTopupProductListV2(Map map) {
        if (false) {
            return wrapper(mHttpAPI.getTopupProductListV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTopupProductListV2(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupOrder> topupCreateOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.topupCreateOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.topupCreateOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupOrder> topupOrderConfirm(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.topupOrderConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.topupOrderConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupOrderList> getTopupOrderList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTopupOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTopupOrderList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupOrder> topupCancelOrder(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.topupCancelOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.topupCancelOrder(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> saveLog(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.saveLog(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.saveLog(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<Dict> qurryDict(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.qurryDict(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.qurryDict(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<RewardTotal> getRewardTotal(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getRewardTotal(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRewardTotal(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<InviteTotal> getInviteAmount(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getInviteAmount(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getInviteAmount(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> bindQlcWallet(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.bindQlcWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.bindQlcWallet(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<RewardList> getRewardList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getRewardList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRewardList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ClaimQgas> claimQgas(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.claimQgas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.claimQgas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ClaimQgas> claimInviteQgas(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.claimInviteQgas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.claimInviteQgas(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> bindPush(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.bindPush(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.bindPush(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> userLogout(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.userLogout(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.userLogout(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<PayToken> payToken(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.payToken(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.payToken(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<MiningAct> miningList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.miningList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.miningList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<MiningRewardList> getMiningRewardList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getMiningRewardList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getMiningRewardList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<MiningRank> getMiningRewardRankList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getMiningRewardRankList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getMiningRewardRankList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<MiningIndex> getTradeMiningIndex(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTradeMiningIndex(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTradeMiningIndex(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<ClaimQgas> claimQlc(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.claimQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.claimQlc(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<RewardTotal> getMiningRewardTotal(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getMiningRewardTotal(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getMiningRewardTotal(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> sysBackUp(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.sysBackUp(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.sysBackUp(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<CountryList> getCountryList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getCountryList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getCountryList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<IspList> getIspList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getIspList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getIspList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<IspList> provinceList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.provinceList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.provinceList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupOrder> saveDeductionTokenTxid(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.saveDeductionTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.saveDeductionTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<BaseBack> bindQlcChainAddress(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.bindQlcChainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.bindQlcChainAddress(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }
    public Observable<TopupGroupList> getTopupGroupList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTopupGroupList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTopupGroupList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }
    public Observable<CreateGroup> createGroup(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.createGroup(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.createGroup(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }
    public Observable<TopupGroupKindList> getTopupGroupKindList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getTopupGroupKindList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getTopupGroupKindList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupJoinGroup> topupJoinGroup(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.topupJoinGroup(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.topupJoinGroup(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<GroupItemList> getGroupItemList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getGroupItemList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getGroupItemList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupJoinGroup> saveItemDeductionTokenTxid(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.saveItemDeductionTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.saveItemDeductionTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupJoinGroup> itemDeductionTokenConfirm(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.itemDeductionTokenConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.itemDeductionTokenConfirm(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<TopupJoinGroup> saveItemPayTokenTxid(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.saveItemPayTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.saveItemPayTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<InviteeList> getInviteeList(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getInviteeList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getInviteeList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<SalePartner> getRewardList1(Map map) {
        if (false) {
            return wrapper(mMainHttpAPI.getRewardList1(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.getRewardList1(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }
    public Observable<SmsReport> smsReport(Map map) {
        return wrapper(mHttpAPI.smsReport(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }
    public Observable<ReportList> smsList(Map map) {
        return wrapper(mHttpAPI.smsList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BaseBack> sysVote(Map map) {
        return wrapper(mHttpAPI.sysVote(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<VoteResult> sysVoteResult(Map map) {
        return wrapper(mHttpAPI.sysVoteResult(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<BurnQgasAct> burnQgasList(Map map) {
        return wrapper(mHttpAPI.burnQgasList(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
    }

    public Observable<TopupOrder> savePayTokenTxid(Map map) {
        if (false) {
            return wrapper(mHttpAPI.savePayTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.savePayTokenTxid(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    public Observable<VCodeVerifyCode> getVcode(Map map) {
        if (false) {
            return wrapper(mHttpAPI.vcodeVerifyCode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.vcodeVerifyCode(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }
    public Observable<IndexInterface> indexInterface(Map map) {
        if (false) {
            return wrapper(mHttpAPI.indexInterface(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        } else {
            return wrapper(mHttpAPI.indexInterface(addParams(map))).compose(SCHEDULERS_TRANSFORMER);
        }
    }

    /**
     * 给任何Http的Observable加上通用的线程调度器
     */
    private static final ObservableTransformer SCHEDULERS_TRANSFORMER = new ObservableTransformer() {
        @Override
        public ObservableSource apply(@NonNull Observable upstream) {
            return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    };

    private <T extends BaseBack> Observable<T> wrapper(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (!baseResponse.getCode().equals("0")) {
                                            String errorTips = baseResponse.getMsg();
                                            if (errorTips.contains("|")) {
                                                errorTips = errorTips.substring(errorTips.indexOf("|") + 1, errorTips.length());
                                            }
//                                            ToastUtil.displayShortToast(errorTips);
//                                            KLog.i("请求错误。。");
                                            QlcException qlcException = new QlcException(Integer.parseInt(baseResponse.getCode()), errorTips);
                                            e.onError(qlcException);
                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                /**
                 * 网络错误： You've encountered a network error!
                 请打开网络：Please open your network.
                 请求超时：The request has timed out.
                 连接失败: Connection failed.
                 请求失败： The request has failed.
                 */
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                        String errorText = "";
                        if (e instanceof HttpException) {
//                            errorText = "You've encountered a network error!";
                            return;
                        } else if (e instanceof UnknownHostException) {
                            KLog.i("请打开网络");
                            errorText = "Please open your network.";
                        } else if (e instanceof SocketTimeoutException) {
                            KLog.i("请求超时");
                            errorText = "The request has timed out. ";
                        } else if (e instanceof ConnectException) {
                            KLog.i("连接失败");
                            errorText = "Connection failed.";
                        } else if (e instanceof QlcException) {
                            KLog.i("连接失败");
                            errorText = e.getMessage();
                        }else {
                            KLog.i("请求失败");
                            errorText = "The request has failed.";
                        }
                        ToastUtil.displayShortToast(errorText);
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends GoogleResult> Observable<T> wrapperObject(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends EthWalletDetail> Observable<T> wrapperETH(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * 根据Http的response中关于状态码的描述，自定义生成本地的Exception
     *
     * @param resourceObservable
     * @param <T>
     * @return
     */
    private <T extends String> Observable<T> wrapperGetToxJson(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }

    private <T extends ArrayList> Observable<T> wrapperArrayList(Observable<T> resourceObservable) {
        return resourceObservable
                .flatMap(new Function<T, ObservableSource<? extends T>>() {
                    @Override
                    public ObservableSource<? extends T> apply(@NonNull T baseResponse) throws Exception {
                        return Observable.create(
                                new ObservableOnSubscribe<T>() {
                                    @Override
                                    public void subscribe(@NonNull ObservableEmitter<T> e) throws Exception {
                                        if (baseResponse == null) {

                                        } else {
                                            e.onNext(baseResponse);
                                        }
                                    }
                                });
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable e) throws Exception {
                        e.printStackTrace();
                    }
                });
    }

    //需要额外的添加其他的参数进去，所以把原有的参数和额外的参数通过这个方法一起添加进去.
    private static RequestBody addParams(Map<String, String> data) {
        Map<String, Object> map = new HashMap<>();
        //false
        if (false) {
            map.put("appid", MainConstant.MainAppid);
            map.put("timestamp", (Calendar.getInstance().getTimeInMillis() + new Random().nextInt(1000)) + "");
            map.put("params", JSONObject.toJSON(data));
            map.put("system", "Android " + SystemUtil.getSystemVersion() + " " + SystemUtil.getSystemModel() + " " + VersionUtil.getAppVersionCode(AppConfig.getInstance()));
            map.put("sign", DigestUtils.getSignature((JSONObject) JSONObject.toJSON(map), MainConstant.MainSign, "UTF-8"));
        } else {
            map.put("appid", "MIFI");
            map.put("system", "Android" + SystemUtil.getSystemVersion() + " " + SystemUtil.getDeviceBrand() +SystemUtil.getSystemModel() + " version:" + VersionUtil.getAppVersionCode(AppConfig.getInstance()));
            map.put("timestamp", (Calendar.getInstance().getTimeInMillis() + new Random().nextInt(1000)) + "");
            map.put("params", JSONObject.toJSON(data));
            map.put("sign", DigestUtils.getSignature((JSONObject) JSONObject.toJSON(map), MainConstant.unKownKeyButImportant, "UTF-8"));
        }
//        KLog.i("传的参数为:" + map);
        MediaType textType = MediaType.parse("text/plain");
        String bodyStr = JSONObject.toJSON(map).toString();
        KLog.i("加密前的:" + bodyStr);
        try {
            bodyStr = URLEncoder.encode(bodyStr, "UTF-8");
            KLog.i("加密后的:" + bodyStr);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return RequestBody.create(textType, bodyStr);
    }

    public static RequestBody createBody(String jsonBody) {
        MediaType textType = MediaType.parse("text/plain");
        return RequestBody.create(textType, jsonBody);
    }
}
