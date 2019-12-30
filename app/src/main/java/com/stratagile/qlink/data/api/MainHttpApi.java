package com.stratagile.qlink.data.api;


import com.stratagile.qlink.entity.Active;
import com.stratagile.qlink.entity.ActiveList;
import com.stratagile.qlink.entity.AppVersion;
import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
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
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.entity.ShowAct;
import com.stratagile.qlink.entity.SysTime;
import com.stratagile.qlink.entity.TokenPrice;
import com.stratagile.qlink.entity.Tpcs;
import com.stratagile.qlink.entity.TradeOrder;
import com.stratagile.qlink.entity.TransactionResult;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.UpdateVpn;
import com.stratagile.qlink.entity.UserInfo;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.VertifyVpn;
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
import com.stratagile.qlink.entity.reward.RewardList;
import com.stratagile.qlink.entity.reward.RewardTotal;
import com.stratagile.qlink.entity.stake.UnLock;
import com.stratagile.qlink.entity.topup.CountryList;
import com.stratagile.qlink.entity.topup.IspList;
import com.stratagile.qlink.entity.topup.PayToken;
import com.stratagile.qlink.entity.topup.TopupOrder;
import com.stratagile.qlink.entity.topup.TopupOrderList;
import com.stratagile.qlink.entity.topup.TopupProduct;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import static com.stratagile.qlink.data.api.MainAPI.url_mining_claim;
import static com.stratagile.qlink.data.api.MainAPI.url_mining_list;
import static com.stratagile.qlink.data.api.MainAPI.url_mining_reward_list;
import static com.stratagile.qlink.data.api.MainAPI.url_mining_reward_rank;
import static com.stratagile.qlink.data.api.MainAPI.url_mining_reward_total;
import static com.stratagile.qlink.data.api.MainAPI.url_sys_bbackup;
import static com.stratagile.qlink.data.api.MainAPI.url_trade_mining_index;


/**
 * Created by zl on 2018/06/13.
 */

public interface MainHttpApi {

    @POST(MainAPI.url_get_ssids)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<WifiRegisteResult> getRegistedSsid(@Body RequestBody map);

    @POST(MainAPI.url_save_ssid)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterWiFi> registeWWifi(@Body RequestBody map);
    @POST(MainAPI.url_save_ssid2)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterWiFi> registeWWifiV3(@Body RequestBody map);


    @POST(MainAPI.url_createWallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> createWallet(@Body RequestBody map);
    @POST(MainAPI.url_importWallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<CreateWallet> importWallet(@Body RequestBody map);


    @POST(MainAPI.url_getBalance)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Balance> getBalance(@Body RequestBody map);

    @POST(MainAPI.url_reward)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Reward> reward(@Body RequestBody map);

    @POST(MainAPI.url_get_raw)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Raw> getRaw(@Body RequestBody map);


    @POST(MainAPI.url_record_querys)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Record> recordQuerys(@Body RequestBody map);


    @POST(MainAPI.url_record_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ConnectedWifiRecord> recordSave(@Body RequestBody map);

    @POST(MainAPI.url_neo_exchange_qlc)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BuyQlc> buyQlc(@Body RequestBody map);

    @POST(MainAPI.url_transaction)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BuyQlc> trasaction(@Body RequestBody map);

    @POST(MainAPI.url_vpn_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterWiFi> vpnRegister(@Body RequestBody map);


    @POST(MainAPI.url_vpn_save2)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterVpn> vpnRegisterV2(@Body RequestBody map);

    @POST(MainAPI.url_vpn_query)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ChainVpn> vpnQuery(@Body RequestBody map);

    @POST(MainAPI.url_vpn_query_v3)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ChainVpn> vpnQueryV3(@Body RequestBody map);

    @POST(MainAPI.vertify_vpn_name)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<VertifyVpn> vertifyVpnName(@Body RequestBody map);

    @POST(MainAPI.vpn_record_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RecordVpn> vpnRecordSave(@Body RequestBody map);

    @GET(MainAPI.latlngParseCountry)
    Observable<GoogleResult> latlngParseCountry(@QueryMap Map<String, String> params);

    @GET(MainAPI.url_c_nodes)
    Observable<String> getNodes();

    @POST(MainAPI.url_batchImportWallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ImportWalletResult> batchImportWallet(@Body RequestBody map);

    @POST(MainAPI.url_user_upload_headview)
    @Multipart
    Observable<UpLoadAvatar> updateMyAvatar(@Part("account") RequestBody account, @Part("token") RequestBody token, @Part MultipartBody.Part head);

    @POST(MainAPI.url_uploadIdCard)
    @Multipart
    Observable<Passport> updateIdCard(@Part("account") RequestBody account, @Part("token") RequestBody token, @Part MultipartBody.Part faceOhoto, @Part MultipartBody.Part holdingPhoto);

    @GET(MainAPI.user_headView)
    Observable<UpLoadAvatar> userHeadView(@QueryMap Map<String, String> map);

    @POST(MainAPI.heart_beat)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> heartBeat(@Body RequestBody map);


    @POST(MainAPI.update_vpn_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<UpdateVpn> updateVpnInfo(@Body RequestBody map);


    @POST(MainAPI.update_wifi_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> updateWiFiInfo(@Body RequestBody map);

    @POST(MainAPI.getUnspentAsset)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<AssetsWarpper> getUnspentAsset(@Body RequestBody map);

    @POST(MainAPI.url_get_server_time)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<SysTime> getServerTime(@Body RequestBody map);

    @POST(MainAPI.sendRow)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> sendRawTransaction(@Body RequestBody map);

    @POST(MainAPI.url_transaction_v2)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TransactionResult> v2Transaction(@Body RequestBody map);

    @POST(MainAPI.url_main_address)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MainAddress> getMainAddress(@Body RequestBody map);

    @POST(MainAPI.url_bnb_2_qlc)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BuyQlc> bnb2qlc(@Body RequestBody map);

    @GET(MainAPI.url_eth_wallet_value)
    Observable<EthWalletDetail> getEthAddressDetail(@Path("address") String address, @QueryMap Map<String, String> map);


    @POST(MainAPI.url_zs_free_num)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<FreeNum> zsFreeNum(@Body RequestBody map);

    @POST(MainAPI.url_freeConnection)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<FreeNum> freeConnection(@Body RequestBody map);

    @POST(MainAPI.url_queryFreeRecords)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<FreeRecord> queryFreeRecords(@Body RequestBody map);

    @POST(MainAPI.reportVpnInfo)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> reportVpnInfo(@Body RequestBody map);

    @POST(MainAPI.act_get)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Active> getAct(@Body RequestBody map);

    @POST(MainAPI.act_asset)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ActiveList> getActAsset(@Body RequestBody map);

    @POST(MainAPI.act_show)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ShowAct> getShowAct(@Body RequestBody map);

    @POST(MainAPI.get_eth_wallet_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EthWalletInfo> getEthWalletInfo(@Body RequestBody map);

    @POST(MainAPI.get_neo_wallet_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<NeoWalletInfo> getNeoWalletInfo(@Body RequestBody map);


    @POST(MainAPI.url_token_price)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TokenPrice> getTokenPrice(@Body RequestBody map);

    @POST(MainAPI.url_eth_address_history)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EthWalletTransaction> getEthWalletTransaction(@Body RequestBody map);

    @POST(MainAPI.url_eth_history)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<OnlyEthTransactionHistory> getOnlyEthTransaction(@Body RequestBody map);

    @POST(MainAPI.url_neo_address_history)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<NeoWalletTransactionHistory> getNeoWalletTransaction(@Body RequestBody map);


    @POST(MainAPI.url_main_net_unspent)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<AssetsWarpper> getMainUnspentAsset(@Body RequestBody map);

    @POST(MainAPI.url_neo_token_transacation)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<NeoTransfer> neoTokenTransaction(@Body RequestBody map);

    @POST(MainAPI.url_kline)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<KLine> getTokenKLine(@Body RequestBody map);

    @POST(MainAPI.url_bina_tpcs)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Tpcs> getTpcs(@Body RequestBody map);

    @POST(MainAPI.url_report_wallet_create)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> reportWalletCreate(@Body RequestBody map);

    @POST(MainAPI.url_wallet_transaction_report)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> reportWalletTransaction(@Body RequestBody map);

    @POST(MainAPI.url_query_winq_gas)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<WinqGasBack> queryWinqGas(@Body RequestBody map);

    @POST(MainAPI.url_got_winq_gas)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<GotWinqGas> gotWinqGas(@Body RequestBody map);

    @POST(MainAPI.url_neo_gas_claim)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ClaimData> neoGasClaim(@Body RequestBody map);

    @POST(MainAPI.url_eos_account_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EosAccountInfo> getEosAccountInfo(@Body RequestBody map);

    @POST(MainAPI.url_eos_token_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EosTokens> getEosTokenList(@Body RequestBody map);

    @POST(MainAPI.url_get_account_resource)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EosResource> getEosTResource(@Body RequestBody map);

    @POST(MainAPI.url_eos_account_transaction_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EosAccountTransaction> getEosAccountTransaction(@Body RequestBody map);

    @POST(MainAPI.url_bina_gettokens)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<LocalTokenBean> getBinaTokens(@Body RequestBody map);

    @POST(MainAPI.url_create_eos_account)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> createEosAccount(@Body RequestBody map);

    @POST(MainAPI.url_create_eos_need_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EosNeedInfo> getEosNeedInfo(@Body RequestBody map);

    @POST(MainAPI.url_eos_resource_price)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EosResourcePrice> getEosResourcePrice(@Body RequestBody map);


    @GET(MainAPI.url_key_account)
    Observable<EosKeyAccount> getKeyAccount(@Query("public_key") String address);


    @POST(MainAPI.url_vcode_signup_code)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> getSignUpVcode(@Body RequestBody map);


    @POST(MainAPI.url_vcode_sign_in_code)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> getSignInVcode(@Body RequestBody map);

    @POST(MainAPI.url_vcode_change_password_code)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> getForgetPasswordVcode(@Body RequestBody map);

    @POST(MainAPI.url_user_change_password)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<VcodeLogin> resetPassword(@Body RequestBody map);

    @POST(MainAPI.ulr_user_sign_up)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<VcodeLogin> userRegister(@Body RequestBody map);

    @POST(MainAPI.url_user_sign_in)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Register> userLogin(@Body RequestBody map);

    @POST(MainAPI.url_user_signin_code)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<VcodeLogin> vCodeLogin(@Body RequestBody map);

    @POST(MainAPI.url_financial_product_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Product> getProductList(@Body RequestBody map);

    @POST(MainAPI.url_financial_product_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ProductDetail> getProductDetail(@Body RequestBody map);

    @POST(MainAPI.url_financial_product_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> buyQLCProduct(@Body RequestBody map);

    @POST(MainAPI.url_financial_order_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Order> getOrderList(@Body RequestBody map);

    @POST(MainAPI.url_financial_redeem)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Order> redeemOrder(@Body RequestBody map);

    @POST(MainAPI.url_user_invite_ranking)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MyRanking> getRankings(@Body RequestBody map);

    @POST(MainAPI.url_user_rich_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EarnRank> getEarnRankings(@Body RequestBody map);

    @POST(MainAPI.url_finance_history_record)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<HistoryRecord> getHistoryRecord(@Body RequestBody map);

    @POST(MainAPI.url_user_invite)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<InviteList> getInivteTop5(@Body RequestBody map);

    @POST(MainAPI.url_user_change_nickname)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> changeNickName(@Body RequestBody map);

    @POST(MainAPI.url_entrust_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> generateEntrustBuyQgasOrder(@Body RequestBody map);

    @POST(MainAPI.url_entrust_order_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EntrustOrderList> getEntrustOrderList(@Body RequestBody map);

    @POST(MainAPI.url_entrust_order_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<EntrustOrderInfo> getEntrustOrderInfo(@Body RequestBody map);

    @POST(MainAPI.url_entrust_cancel_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> cancelEntrustOrder(@Body RequestBody map);

    @POST(MainAPI.url_trade_buy_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<GenerageTradeOrder> generateTradeBuyQgasOrder(@Body RequestBody map);

    @POST(MainAPI.url_trade_buyer_confirm)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> tradeBuyerConfirm(@Body RequestBody map);

    @POST(MainAPI.url_trade_sell_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TradeOrder> generateTradeSellOrder(@Body RequestBody map);

    @POST(MainAPI.url_trade_sell_order_txid)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TradeOrder> tradeSellOrderTxid(@Body RequestBody map);

    @POST(MainAPI.url_trade_seller_confirm)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> tradeSellerConfirm(@Body RequestBody map);

    @POST(MainAPI.url_trade_order_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TradeOrderList> tradeOrderList(@Body RequestBody map);

    @POST(MainAPI.url_trade_order_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TradeOrderDetail> tradeOrderInfo(@Body RequestBody map);

    @POST(MainAPI.url_trade_cancel)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> tradeOrderCancel(@Body RequestBody map);

    @POST(MainAPI.url_user_userinfo)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<UserInfo> getUserInfo(@Body RequestBody map);

    @POST(MainAPI.url_app_version_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<AppVersion> getAppLastVersion(@Body RequestBody map);

    @POST(MainAPI.url_pairs)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TradePair> getPairs(@Body RequestBody map);

    @POST(MainAPI.url_contact_unlock)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<UnLock> unLock(@Body RequestBody map);

    @POST(MainAPI.url_topup_productlist)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupProduct> getTopupProductList(@Body RequestBody map);

    @POST(MainAPI.url_topup_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupOrder> topupCreateOrder(@Body RequestBody map);

    @POST(MainAPI.url_topup_confirm)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupOrder> topupOrderConfirm(@Body RequestBody map);

    @POST(MainAPI.url_topup_order_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupOrderList> getTopupOrderList(@Body RequestBody map);

    @POST(MainAPI.url_topup_cancel_order)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupOrder> topupCancelOrder(@Body RequestBody map);

    @POST(MainAPI.url_log_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> saveLog(@Body RequestBody map);

    @POST(MainAPI.url_sys_dict)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Dict> qurryDict(@Body RequestBody map);

    @POST(MainAPI.url_reward_tatal)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RewardTotal> getRewardTotal(@Body RequestBody map);

    @POST(MainAPI.url_inivte_amount)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<InviteTotal> getInviteAmount(@Body RequestBody map);

    @POST(MainAPI.url_bind_qlcchain_wallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> bindQlcWallet(@Body RequestBody map);

    @POST(MainAPI.url_reward_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RewardList> getRewardList(@Body RequestBody map);

    @POST(MainAPI.url_reward_claims)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ClaimQgas> claimQgas(@Body RequestBody map);

    @POST(MainAPI.url_reward_claim_invite)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ClaimQgas> claimInviteQgas(@Body RequestBody map);

    @POST(MainAPI.url_bind_push)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> bindPush(@Body RequestBody map);

    @POST(MainAPI.url_user_logout)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> userLogout(@Body RequestBody map);

    @POST(MainAPI.url_pay_token)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<PayToken> payToken(@Body RequestBody map);

    @POST(url_mining_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MiningAct> miningList(@Body RequestBody map);

    @POST(url_mining_reward_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MiningRewardList> getMiningRewardList(@Body RequestBody map);

    @POST(url_mining_reward_rank)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MiningRank> getMiningRewardRankList(@Body RequestBody map);

    @POST(url_trade_mining_index)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MiningIndex> getTradeMiningIndex(@Body RequestBody map);

    @POST(url_mining_claim)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ClaimQgas> claimQlc(@Body RequestBody map);

    @POST(url_mining_reward_total)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RewardTotal> getMiningRewardTotal(@Body RequestBody map);

    @POST(url_sys_bbackup)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> sysBackUp(@Body RequestBody map);

    @POST(MainAPI.url_country_list)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<CountryList> getCountryList(@Body RequestBody map);

    @POST(MainAPI.url_getIspList)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<IspList> getIspList(@Body RequestBody map);

    @POST(MainAPI.provinceList)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<IspList> provinceList(@Body RequestBody map);

    @POST(MainAPI.saveDeductionTokenTxid)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupOrder> saveDeductionTokenTxid(@Body RequestBody map);

    @POST(MainAPI.savePayTokenTxid)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TopupOrder> savePayTokenTxid(@Body RequestBody map);

    @POST(MainAPI.url_trade_appeal)
    @Multipart
    Observable<TradeOrderDetail> generateAppeal(@Part("account") RequestBody account, @Part("token") RequestBody token, @Part("tradeOrderId") RequestBody tradeOrderId, @Part("reason") RequestBody reason, @Part MultipartBody.Part photo1, @Part MultipartBody.Part photo2, @Part MultipartBody.Part photo3, @Part MultipartBody.Part photo4);
//    Observable<TradeOrderDetail> generateAppeal(@Part("account") RequestBody account, @Part("token") RequestBody token, @Part MultipartBody.Part photo1);

    /*************************************************/
}
