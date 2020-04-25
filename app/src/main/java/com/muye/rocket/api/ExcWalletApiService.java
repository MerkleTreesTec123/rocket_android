package com.muye.rocket.api;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.response.Response;
import com.muye.rocket.entity.exc_wallet.AddressInfo;
import com.muye.rocket.entity.exc_wallet.ExcAddressListEntity;
import com.muye.rocket.entity.exc_wallet.ExcAssetsDetail;
import com.muye.rocket.entity.exc_wallet.ExcAssetsEntity;
import com.muye.rocket.entity.exc_wallet.ExcRechargeAddress;
import com.muye.rocket.entity.exc_wallet.ExcWalletRecord;
import com.muye.rocket.entity.exc_wallet.ExcWithdrawSettingEntity;
import com.muye.rocket.mvp.exc_wallet.contract.ExcWalletRecordContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ExcWalletApiService {
    /**
     * @param map 参数:
     *            token
     *            base 单位
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_WALLET_ASSETS)
    Observable<Response<ExcAssetsEntity>> fetchAssets(@QueryMap Map<String, String> map);

    /**
     * 币种详情
     *
     * @param token
     * @param coinID
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_WALLET_ASSETS_DETAIL)
    Observable<Response<ExcAssetsDetail>> fetchAssetsDetail(@Query("token") String token,
                                                            @Query("coin_id") String coinID);

    /**
     * 充值地址
     *
     * @param map 参数:
     *            token
     *            symbol->coinID
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_WALLET_RECHARGE_ADDRESS)
    Observable<Response<ExcRechargeAddress>> fetchRechargeAddress(@QueryMap Map<String, String> map);

    /**
     * 资产账单
     *
     * @param coinID
     * @param recordType In(1, "充值"),
     *                   Out(2, "提现"),
     *                   Transfer(3,"转账"), // 以前是转账，现在改为 CNY 提现
     *                   Receive(4,"接受转账"), // 以前是接受转账，现在改为 CNY 充值
     *                   InnerTransferOut(7,"划转到C2C账户"),
     *                   InnerTransferIn(8,"划转到币币账户"),
     *                   Other(6,"其他");
     * @param page
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_WALLET_RECORD)
    Observable<Response<List<ExcWalletRecord>>> fetchWalletRecord(@Query("token") String token,
                                                                  @Query("coinId") String coinID,
                                                                  @Query("operation") @ExcWalletRecordContract.RecordType String recordType,
                                                                  @Query("currentPage") int page);

    /**
     * 提币地址
     *
     * @param token
     * @param coinID
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_WALLET_WITHDRAW_ADDRESS_LIST)
    Observable<Response<ExcAddressListEntity>> fetchAddress(@Query("token") String token,
                                                            @Query("symbol") String coinID);

    /**
     * 删除提币地址
     *
     * @param token
     * @param addressID
     * @return
     */
    @FormUrlEncoded
    @POST(ExcWalletURLConfig.URL_EXC_WALLET_DELETE_WITHDRAW_ADDRESS)
    Observable<Response<BaseEntity>> deleteAddress(@Field("token") String token,
                                                   @Field("addressId") String addressID);

    /**
     * 添加提币地址
     *
     * @param params 参数:
     *               token
     *               symbol
     *               phoneCode
     *               totpCode google验证码
     *               withdrawAddr
     *               password
     *               remark
     * @return
     */
    @FormUrlEncoded
    @POST(ExcWalletURLConfig.URL_EXC_WALLET_ADD_ADDRESS)
    Observable<Response<BaseEntity>> submitNewAddress(@FieldMap Map<String, String> params);

    /**
     * @param params 参数:
     *               token
     *               appSecret
     *               type 添加提币地址104
     * @return
     */
    @FormUrlEncoded
    @POST(ExcWalletURLConfig.URL_EXC_SEND_SMS_BIND)
    Observable<Response<BaseEntity>> sendSMS2BindPhone(@FieldMap Map<String, String> params);

    /**
     * @param params 参数:
     *               token
     *               symbol->coinID
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_WITHDRAW_SETTING)
    Observable<Response<ExcWithdrawSettingEntity>> fetchWithdrawSetting(@QueryMap Map<String, String> params);

    /**
     * @param params 参数:
     *               token
     *               symbol->coinID
     *               withdrawAmount
     *               withdrawAddr 地址ID
     *               memo 备注
     *               phoneCode
     *               tradePwd
     *               totpCode
     *               btcfeesIndex 默认0
     * @return
     */
    @FormUrlEncoded
    @POST(ExcWalletURLConfig.URL_EXC_WITHDRAW)
    Observable<Response<BaseEntity>> submitWithdraw(@FieldMap Map<String, String> params);

    /**
     * 获取地址信息
     *
     * @param token
     * @param address
     * @return
     */
    @GET(ExcWalletURLConfig.URL_EXC_ADDRESS_INFO)
    Observable<Response<AddressInfo>> fetchAddressInfo(@Query("token") String token,
                                                       @Query("address") String address);
}
