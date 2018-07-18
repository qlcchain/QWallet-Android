package com.stratagile.qlink.data.api;


import com.stratagile.qlink.entity.AssetsWarpper;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.BetResult;
import com.stratagile.qlink.entity.BuyQlc;
import com.stratagile.qlink.entity.ChainVpn;
import com.stratagile.qlink.entity.ConnectedWifiRecord;
import com.stratagile.qlink.entity.CreateWallet;
import com.stratagile.qlink.entity.EthWalletDetail;
import com.stratagile.qlink.entity.FreeNum;
import com.stratagile.qlink.entity.FreeRecord;
import com.stratagile.qlink.entity.GoogleResult;
import com.stratagile.qlink.entity.ImportWalletResult;
import com.stratagile.qlink.entity.MainAddress;
import com.stratagile.qlink.entity.RaceTimes;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.Record;
import com.stratagile.qlink.entity.RecordVpn;
import com.stratagile.qlink.entity.RegisterVpn;
import com.stratagile.qlink.entity.RegisterWiFi;
import com.stratagile.qlink.entity.Reward;
import com.stratagile.qlink.entity.ServerTime;
import com.stratagile.qlink.entity.TransactionResult;
import com.stratagile.qlink.entity.UpLoadAvatar;
import com.stratagile.qlink.entity.UpdateVpn;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.entity.WifiRegisteResult;

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
import retrofit2.http.QueryMap;

import static com.stratagile.qlink.data.api.API.sendRow;
import static com.stratagile.qlink.data.api.API.url_bet;
import static com.stratagile.qlink.data.api.API.url_bnb_2_qlc;
import static com.stratagile.qlink.data.api.API.url_freeConnection;
import static com.stratagile.qlink.data.api.API.url_get_server_time;
import static com.stratagile.qlink.data.api.API.url_main_address;
import static com.stratagile.qlink.data.api.API.url_queryFreeRecords;
import static com.stratagile.qlink.data.api.API.url_race_times;
import static com.stratagile.qlink.data.api.API.url_transaction_v2;
import static com.stratagile.qlink.data.api.API.url_zs_free_num;
import static com.stratagile.qlink.data.api.API.user_update_avatar;

/**
 * Created by hu on 2017/5/16.
 */

public interface HttpApi {

    @POST(API.url_get_ssids)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<WifiRegisteResult> getRegistedSsid(@Body RequestBody map);

    @POST(API.url_save_ssid)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterWiFi> registeWWifi(@Body RequestBody map);
    @POST(API.url_save_ssid2)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterWiFi> registeWWifiV3(@Body RequestBody map);


    @POST(API.url_createWallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> createWallet(@Body RequestBody map);
    @POST(API.url_importWallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<CreateWallet> importWallet(@Body RequestBody map);


    @POST(API.url_getBalance)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Balance> getBalance(@Body RequestBody map);

    @POST(API.url_reward)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Reward> reward(@Body RequestBody map);

    @POST(API.url_get_raw)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Raw> getRaw(@Body RequestBody map);


    @POST(API.url_record_querys)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<Record> recordQuerys(@Body RequestBody map);


    @POST(API.url_record_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ConnectedWifiRecord> recordSave(@Body RequestBody map);

    @POST(API.url_neo_exchange_qlc)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BuyQlc> buyQlc(@Body RequestBody map);

    @POST(API.url_transaction)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BuyQlc> trasaction(@Body RequestBody map);

    @POST(API.url_vpn_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterWiFi> vpnRegister(@Body RequestBody map);


    @POST(API.url_vpn_save2)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RegisterVpn> vpnRegisterV2(@Body RequestBody map);

    @POST(API.url_vpn_query)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ChainVpn> vpnQuery(@Body RequestBody map);

    @POST(API.url_vpn_query_v3)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ChainVpn> vpnQueryV3(@Body RequestBody map);

    @POST(API.vertify_vpn_name)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<VertifyVpn> vertifyVpnName(@Body RequestBody map);

    @POST(API.vpn_record_save)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<RecordVpn> vpnRecordSave(@Body RequestBody map);

    @GET(API.latlngParseCountry)
    Observable<GoogleResult> latlngParseCountry(@QueryMap Map<String, String> params);

    @GET(API.url_c_nodes)
    Observable<String> getNodes();

    @POST(API.url_batchImportWallet)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<ImportWalletResult> batchImportWallet(@Body RequestBody map);

    @POST(user_update_avatar)
    @Multipart
    Observable<UpLoadAvatar> updateMyAvatar(@Part("p2pId") RequestBody p2pId, @Part MultipartBody.Part head);

    @GET(API.user_headView)
    Observable<UpLoadAvatar> userHeadView(@QueryMap Map<String, String> map);

    @POST(API.heart_beat)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> heartBeat(@Body RequestBody map);


    @POST(API.update_vpn_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<UpdateVpn> updateVpnInfo(@Body RequestBody map);


    @POST(API.update_wifi_info)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> updateWiFiInfo(@Body RequestBody map);

    @POST(API.getUnspentAsset)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<AssetsWarpper> getUnspentAsset(@Body RequestBody map);

    @POST(sendRow)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> sendRawTransaction(@Body RequestBody map);

    @POST(url_transaction_v2)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<TransactionResult> v2Transaction(@Body RequestBody map);

    @POST(url_main_address)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<MainAddress> getMainAddress(@Body RequestBody map);

    @POST(url_bnb_2_qlc)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BuyQlc> bnb2qlc(@Body RequestBody map);

    @GET(API.url_eth_wallet_value)
    Observable<EthWalletDetail> getEthAddressDetail(@Path("address") String address, @QueryMap Map<String, String> map);


    /*************************************************/
//    @POST(url_get_server_time)
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    Observable<ServerTime> getServerTime(@Body RequestBody map);
//
//    @POST(url_bet)
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    Observable<BetResult> betRace(@Body RequestBody map);
//
//    @POST(url_race_times)
//    @Headers({"Content-Type: application/json","Accept: application/json"})
//    Observable<RaceTimes> getRaceTimes(@Body RequestBody map);

    @POST(url_zs_free_num)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<FreeNum> zsFreeNum(@Body RequestBody map);

    @POST(url_freeConnection)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<BaseBack> freeConnection(@Body RequestBody map);

    @POST(url_queryFreeRecords)
    @Headers({"Content-Type: application/json","Accept: application/json"})
    Observable<FreeRecord> queryFreeRecords(@Body RequestBody map);

    /*************************************************/
}
