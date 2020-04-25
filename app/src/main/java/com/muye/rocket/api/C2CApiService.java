package com.muye.rocket.api;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.response.Response;
import com.muye.rocket.entity.c2c.C2CCoin;
import com.muye.rocket.entity.c2c.C2CCoinBalance;
import com.muye.rocket.entity.c2c.C2COrder;
import com.muye.rocket.entity.c2c.C2CPayAccountInfo;
import com.muye.rocket.entity.c2c.C2CReleaseEntity;
import com.muye.rocket.entity.c2c.C2CTransactionInfo;
import com.muye.rocket.entity.c2c.WalletDetailRecord;
import com.muye.rocket.entity.c2c.WalletEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface C2CApiService {
    /**
     * 币种列表
     *
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_COIN_LIST)
    Observable<Response<List<C2CCoin>>> fetchCoinList();

    /**
     * 发布信息
     *
     * @param token
     * @param coinID
     * @param orderType 1 买 2 卖
     * @param page
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_RELEASE_LIST)
    Observable<Response<List<C2CReleaseEntity>>> fetchReleaseList(@Query("uid") String token,
                                                                  @Query("coin_id") String coinID,
                                                                  @Query("type") String orderType,
                                                                  @Query("page") int page);

    /**
     * 发布信息详情
     *
     * @param releaseID
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_RELEASE_LIST)
    Observable<Response<List<C2CReleaseEntity>>> fetchReleaseDetail(@Query("uid") String token,
                                                                    @Query("id") String releaseID);

    /**
     * 订单列表
     *
     * @param coinID
     * @param orderType 1 买 2 卖
     * @param page
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_ORDER_LIST)
    Observable<Response<List<C2COrder>>> fetchOrderList(@Query("uid") String token,
                                                        @Query("type") String orderType,
                                                        @Query("coin_id") String coinID,
                                                        @Query("cid") String releaseID,
                                                        @Query("id") String orderID,
                                                        @Query("page") int page);

    /**
     * 交易
     *
     * @param token
     * @param releaseID
     * @param num
     * @param type
     * @param password
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_SUBMIT_TRANSACTION)
    Observable<Response<String>> submitTransaction(@Query("uid") String token,
                                                   @Query("cid") String releaseID,
                                                   @Query("nums") String num,
                                                   @Query("type") String type,
                                                   @Query("pay_type") String payType,
                                                   @Query("deal_psw") String password);

    /**
     * 订单详情
     *
     * @param orderID
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_ORDER)
    Observable<Response<List<C2COrder>>> fetchC2COrderDetail(@Query("uid") String token,
                                                             @Query("id") String orderID);

    /**
     * 修改订单状态
     *
     * @param token
     * @param orderID
     * @param imageURL
     * @param password
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_UPDATE_ORDER_STATUS)
    Observable<Response<BaseEntity>> updateOrderStatus(@Query("uid") String token,
                                                       @Query("id") String orderID,
                                                       @Query("pay_type") String payType,
                                                       @Query("reason") String reason,
                                                       @Query("payvoucher") String imageURL,
                                                       @Query("status") String password);

    /**
     * 获取银行等信息
     *
     * @param token
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_PAY_ACCOUTN_INFO)
    Observable<Response<C2CPayAccountInfo>> fetchC2CPayAccountInfo(@Query("uid") String token);

    /**
     * 修改及添加银行等信息
     *
     * @param token
     * @param realName
     * @param bankCardInfo
     * @param alipayInfo
     * @param wechatInfo
     * @param password
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_PAY_ACCOUNT)
    Observable<Response<BaseEntity>> submitC2CPayAccount(@Query("uid") String token,
                                                         @Query("truename") String realName,
                                                         @Query("bank") String bankCardInfo,
                                                         @Query("alipay") String alipayInfo,
                                                         @Query("wxpay") String wechatInfo,
                                                         @Query("deal_psw") String password);

    /**
     * 发布交易
     *
     * @param token
     * @param coinID
     * @param coinName
     * @param price
     * @param num
     * @param min
     * @param max
     * @param payType  1、银行卡 2、支付宝 3、微信 4、银行卡支付宝 5、银行卡微信 6、支付宝微信 7、银行卡支付宝微信
     * @param type     1、买入 2卖出
     * @param password
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_RELEASE)
    Observable<Response<String>> c2cRelease(@Query("uid") String token,
                                            @Query("coin_id") String coinID,
                                            @Query("coinname") String coinName,
                                            @Query("price") String price,
                                            @Query("nums") String num,
                                            @Query("min") String min,
                                            @Query("max") String max,
                                            @Query("pay_type") String payType,
                                            @Query("type") String type,
                                            @Query("deal_psw") String password);

    /**
     * C2C钱包币种余额
     *
     * @param token
     * @param coinID
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_COIN_BALANCE)
    Observable<Response<C2CCoinBalance>> fetchCoinBalance(@Query("uid") String token,
                                                          @Query("coin_id") String coinID);

    /**
     * 获取发布信息
     *
     * @param token
     * @param coinID
     * @param type
     * @param page
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_MY_RELEASE_LIST)
    Observable<Response<List<C2CTransactionInfo>>> fetchMyReleaseList(@Query("uid") String token,
                                                                      @Query("coin_id") String coinID,
                                                                      @Query("id") String id,
                                                                      @Query("type") String type,
                                                                      @Query("page") int page);

    /**
     * 更新发布状态
     *
     * @param token
     * @param id
     * @param status
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_UPDATE_RELEASE_STATUS)
    Observable<Response<BaseEntity>> updateC2CUpdateReleaseStatus(@Query("uid") String token,
                                                                  @Query("id") String id,
                                                                  @Query("status") String status);

    /**
     * 钱包
     *
     * @param token
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_WALLET)
    Observable<Response<WalletEntity>> fetchWallet(@Query("uid") String token);

    /**
     * 资产明细
     *
     * @param token
     * @param coinID
     * @param type
     * @param page
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_WALLET_DETAIL_RECORD)
    Observable<Response<List<WalletDetailRecord>>> fetchWalletDetailRecord(@Query("uid") String token,
                                                                           @Query("coin_id") String coinID,
                                                                           @Query("type") String type,
                                                                           @Query("page") int page);

    /**
     * 根据c2cCoinID获取资产钱包币种信息
     *
     * @param token
     * @param coinID
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_ASSETS_BALANCE)
    Observable<Response<C2CCoinBalance>> fetchAssetsBalance(@Query("token") String token,
                                                            @Query("coin_id") String coinID);

    /**
     * 资金划转
     *
     * @param token
     * @param coinID
     * @param num
     * @param transferType 1:币币到c2c；2：c2c到币币
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST(C2CURLConfig.URL_C2C_TRANSFER)
    Observable<Response<BaseEntity>> submitTransfer(@Field("token") String token,
                                                    @Field("coin_id") String coinID,
                                                    @Field("number") String num,
                                                    @Field("type") String transferType,
                                                    @Field("tradpassword") String password);

    /**
     * 根据资产钱包币种id获取c2c钱包币种信息
     *
     * @param token
     * @param assetsCoinID
     * @return
     */
    @GET(C2CURLConfig.URL_C2C_BALANCE_BY_ASSETS_COIN_ID)
    Observable<Response<C2CCoinBalance>> fetchBalanceByAssetsCoinID(@Query("token") String token,
                                                                    @Query("coin_id") String assetsCoinID);


}
