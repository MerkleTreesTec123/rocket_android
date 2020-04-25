package com.muye.rocket.api;

import com.ifenduo.lib_base.entity.BaseEntity;
import com.ifenduo.lib_http.response.Response;
import com.muye.rocket.entity.BannerEntity;
import com.muye.rocket.entity.BindWalletBean;
import com.muye.rocket.entity.CoinBean;
import com.muye.rocket.entity.DiscoverBannerEntity;
import com.muye.rocket.entity.DiscoverCat;
import com.muye.rocket.entity.DiscoverEntity;
import com.muye.rocket.entity.DiscoverHomeEntity;
import com.muye.rocket.entity.GoogleDevice;
import com.muye.rocket.entity.InviteRankEntity;
import com.muye.rocket.entity.InviteRecord;
import com.muye.rocket.entity.InviteRewardDetailEntity;
import com.muye.rocket.entity.LoginEntity;
import com.muye.rocket.entity.MarqueeEntity;
import com.muye.rocket.entity.MillionCatMyData;
import com.muye.rocket.entity.MillionInviteBean;
import com.muye.rocket.entity.MyWinRecord;
import com.muye.rocket.entity.SafeSettingEntity;
import com.muye.rocket.entity.StartLotteryData;
import com.muye.rocket.entity.UploadResult;
import com.muye.rocket.entity.WinningPeopleShowData;
import com.muye.rocket.entity.exc_wallet.RocketBalance;
import com.muye.rocket.entity.ieo.IEOHomeEntity;
import com.muye.rocket.entity.ieo.IEOProgress;
import com.muye.rocket.entity.ieo.IEORecord;
import com.muye.rocket.entity.ieo.IEORecordDetail;
import com.muye.rocket.entity.ieo.IEOReleaseEntity;
import com.muye.rocket.mvp.me.contract.SendSMSContract;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RApiService {

    /**
     * 发送不需要签名的短信
     *
     * @param area
     * @param phone
     * @param type  112手机端注册，102绑定手机
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_SEND_PHONE_MESSAGE_NOT_SIGNTRUE)
    Observable<Response<BaseEntity>> sendNotSignatureSMS(@Field("area") String area,
                                                         @Field("phone") String phone,
                                                         @Field("type") String type);

    /**
     * 发送绑定手机号验证码
     * @param area
     * @param phone
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.SEND_BIND_PHONE)
    Observable<Response<BaseEntity>> sendBindPhoneSMS(@Field("area") String area,
                                                      @Field("phone") String phone,
                                                      @Field("type") String type);

    /**
     * 绑定手机号
     * @param token
     * @param area
     * @param phone
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.BIND_PHONE)
    Observable<Response<BaseEntity>> bindPhone(@Field("token") String token,
                                               @Field("area") String area,
                                               @Field("phone") String phone,
                                               @Field("code") String code);

    /**
     * 发送邮箱验证码(php)
     * @param email
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_SEND_EMAIL)
    Observable<Response<BaseEntity>> sendEmil(@Field("email") String email,
                                              @Field("type") String type);

    /**
     * 发送邮箱验证码(java)
     * @param token
     * @param address
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_SEND_EMAIL_JAVA)
    Observable<Response<BaseEntity>> sendEmilJ(@Field("token") String token,
                                              @Field("address") String address);

    /**
     * 绑定邮箱
     * @param token
     * @param code
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.BIND_EMAIL)
    Observable<Response<BaseEntity>> bindEmail(@Field("token") String token,
                                               @Field("code") String code);

    /**
     * 校验邮箱验证码
     * @param email
     * @param code
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.CODE_IS_OK)
    Observable<Response<BaseEntity>> checkEmailCode(@Field("email") String email,
                                                    @Field("code") String code,
                                                    @Field("type") String type);

    /**
     * 手机注册
     *
     * @param area
     * @param phone
     * @param smsCode
     * @param password
     * @param payPassword
     * @param inviteCode
     * @param regType
     * @param ecode
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_PHONE_REGISTER)
    Observable<Response<BaseEntity>> submitPhoneRegister(@Field("area") String area,
                                                         @Field("regName") String phone,
                                                         @Field("pcode") String smsCode,
                                                         @Field("password") String password,
                                                         @Field("tradePassword") String payPassword,
                                                         @Field("intro_user") String inviteCode,
                                                         @Field("regType") String regType,
                                                         @Field("ecode") String ecode);

    @FormUrlEncoded
    @POST(RURLConfig.URL_EMAIL_REGIST)
    Observable<Response<BaseEntity>> submitEmailRegister(@Field("femail") String phone,
                                                         @Field("pcode") String smsCode,
                                                         @Field("password") String password,
                                                         @Field("tradePassword") String payPassword,
                                                         @Field("intro_user") String inviteCode,
                                                         @Field("regType") String regType,
                                                         @Field("ecode") String ecode);

    /**
     * 登录
     *
     * @param area
     * @param phone
     * @param password
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_LOGIN)
    Observable<Response<LoginEntity>> submitLogin(@Field("area") String area,
                                                  @Field("loginName") String phone,
                                                  @Field("password") String password,
                                                  @Field("type") String type);

    /**
     * 获取Banner
     *
     * @return
     */
    @GET(RURLConfig.URL_BANNER)
    Observable<Response<List<BannerEntity>>> fetchBanner();

    /**
     * 获取adBanner
     *
     * @return
     */
    @GET(RURLConfig.URL_AD_BANNER)
    Observable<Response<List<BannerEntity>>> fetchAdBanner();

    /**
     * 公告列表
     *
     * @param uid
     * @return
     */
    @GET(RURLConfig.URL_MARQUEE)
    Observable<Response<List<MarqueeEntity>>> fetchMarquee(@Query("fid") String uid);

    /**
     * @param params 参数:
     *               token
     * @return
     */
    @GET(RURLConfig.URL_SAFE_CENTER_INFO)
    Observable<Response<SafeSettingEntity>> fetchSafeSetting(@QueryMap Map<String, String> params);

    /**
     * 谷歌关联设备信息
     *
     * @param params
     * @return
     */
    @GET(RURLConfig.URL_GOOGLE_DEVICE)
    Observable<Response<GoogleDevice>> fetchGoogleDevice(@QueryMap Map<String, String> params);

    /**
     * @param params 参数:
     *               token
     *               code
     *               totpKey
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_BIND_GOOGLE_CODE)
    Observable<Response<BaseEntity>> bindGoogle(@FieldMap Map<String, String> params);

    /**
     * @param params 参数:
     *               token
     *               realname
     *               identitytype
     *               identityno
     *               address
     *               idCardZmImgURL
     *               idCardFmImgURL
     *               idCardScImgURL
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_SUBMIT_REL_NAME)
    Observable<Response<BaseEntity>> submitRealName(@FieldMap Map<String, String> params);

    /***
     * 自动识别
     * @param params
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_SUBMIT_REL_ZG)
    Observable<Response<BaseEntity>> submitRealNameGz(@FieldMap Map<String, String> params);

    /**
     * 修改/绑定，登录和交易密码
     *
     * @param params 参数:
     *               token
     *               originPwd 原密码
     *               newPwd
     *               reNewPwd
     *               phoneCode
     *               totpCode
     *               identityCode
     *               pwdType 1支付密码 2登录密码
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_SUBMIT_PASSWORD)
    Observable<Response<BaseEntity>> submitPassword(@FieldMap Map<String, String> params);

    /**
     * 验证短信验证码
     *
     * @param phone
     * @param code
     * @param type
     * @return
     */
    @GET(RURLConfig.URL_CHECK_SMS_CODE)
    Observable<Response<BaseEntity>> checkSMSCode(@Query("phone") String phone,
                                                  @Query("code") String code,
                                                  @Query("type") @SendSMSContract.SMSCodeType String type);

    /**
     * 找回交易密码
     *
     * @param params 参数:
     *               token
     *               area
     *               phone
     *               newPassword
     *               newPassword2
     *               code
     *               totpCode
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_FIND_TRANS_PASSWORD)
    Observable<Response<BaseEntity>> findSafePassword(@FieldMap Map<String, String> params);

    /**
     * 找回交易密码
     *
     * @param params 参数:
     *               area
     *               phone
     *               newPassword
     *               newPassword2
     *               code
     *               totpCode
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.URL_FIND_LOGIN_PASSWORD)
    Observable<Response<BaseEntity>> findLoginPassword(@FieldMap Map<String, String> params);

    /**
     * 排行榜
     *
     * @param token
     * @return
     */
    @GET(RURLConfig.URL_INVITE_RANKING)
    Observable<Response<InviteRankEntity>> fetchInviteRank(@Query("uid") String token);

    /**
     * 个人直推记录
     *
     * @param token
     * @param page
     * @return
     */
    @GET(RURLConfig.URL_INVITE_RECORD)
    Observable<Response<List<InviteRecord>>> fetchInviteRecord(@Query("uid") String token,
                                                               @Query("page") int page);

    /**
     * 累计收益详情
     *
     * @param token
     * @param coinID
     * @param page
     * @return
     */
    @GET(RURLConfig.URL_INVITE_REWARD_DETAIL)
    Observable<Response<InviteRewardDetailEntity>> fetchInviteRewardDetail(@Query("uid") String token,
                                                                           @Query("coin_id") String coinID,
                                                                           @Query("page") int page);

    /**
     * 发现Banner
     *
     * @return
     */
    @GET(RURLConfig.URL_DISCOVER_BANNER)
    Observable<Response<List<DiscoverBannerEntity>>> fetchDiscoverBanner();

    /**
     * 发现推荐
     *
     * @return
     */
    @GET(RURLConfig.URL_DISCOVER_RECOMMEND)
    Observable<Response<List<DiscoverEntity>>> fetchDiscoverRecommend();

    /**
     * 发现首页列表
     *
     * @return
     */
    @GET(RURLConfig.URL_DISCOVER_LIST)
    Observable<Response<List<DiscoverHomeEntity>>> fetchDiscoverHomeList();

    /**
     * 发现列表
     *
     * @param catID
     * @param page
     * @return
     */
    @GET(RURLConfig.URL_DISCOVER_CONTENT)
    Observable<Response<List<DiscoverEntity>>> fetchDiscoverList(@Query("catid") String catID,
                                                                 @Query("page") int page);

    /**
     * 分类接口
     *
     * @return
     */
    @GET(RURLConfig.URL_DISCOVER_CAT)
    Observable<Response<List<DiscoverCat>>> fetchDiscoverCat();

    /**
     * IEO首页
     *
     * @return
     */
    @GET(RURLConfig.URL_IEO_HOME)
    Observable<Response<IEOHomeEntity>> fetchIEOHomeData();

    /**
     * 购买IEO
     *
     * @param token
     * @param password
     * @param parID
     * @return
     */
    @GET(RURLConfig.URL_IEO_BUY)
    Observable<Response<BaseEntity>> submitBuyIEO(@Query("uid") String token,
                                                  @Query("pwd") String password,
                                                  @Query("par_id") String parID);

    /**
     * IEO兑换进度
     *
     * @param token
     * @return
     */
    @GET(RURLConfig.URL_IEO_PROGRESS)
    Observable<Response<IEOProgress>> fetchIEOProgress(@Query("uid") String token);

    /**
     * IEO释放量
     *
     * @return
     */
    @GET(RURLConfig.URL_IEO_RELEASE)
    Observable<Response<IEOReleaseEntity>> fetchIEORelease(@Query("uid") String token);

    /**
     * 我的IEO
     *
     * @param token
     * @param page
     * @return
     */
    @GET(RURLConfig.URL_IEO_RECORD)
    Observable<Response<List<IEORecord>>> fetchIEORecord(@Query("uid") String token,
                                                         @Query("page") int page);

    /**
     * 我的IEO详情
     *
     * @param toke
     * @param id
     * @param page
     * @return
     */
    @GET(RURLConfig.URL_IEO_RECORD_DETAIL)
    Observable<Response<IEORecordDetail>> fetchIEORecordDetail(@Query("uid") String toke,
                                                               @Query("log_id") String id,
                                                               @Query("page") int page);

    /**
     * 获取系统时间
     *
     * @return
     */
    @GET(RURLConfig.URL_SYSTEM_TIME)
    Observable<Response<String>> fetchSystemTime();

    /**
     * 上传图片
     *
     * @param part
     * @param uid
     * @return
     */
    @Multipart
    @POST(RURLConfig.URL_UPLOAD_IMAGE)
    Observable<Response<UploadResult>> uploadImage(@Part MultipartBody.Part part,
                                                   @Query("uid") String uid,
                                                   @Query("auth") String auth,
                                                   @Query("ext") String ext);


    /**
     * 获取钱包地址
     * @param uid
     * @param jwt
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.QR_CODE_BINDE_WALLET)
    Observable<Response<String>> qrcodeAddress(@Field("uid") String uid,
                                               @Field("jwt") String jwt);

    /**
     * 查询关联绑定的cat列表
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.BIND_CAT_LIST)
    Observable<Response<List<BindWalletBean>>> bindWalletLists(@Field("uid") String uid,
                                                               @Field("jwt") String jwt);


    /**
     * 查询CAT钱余额
     * @param uid
     * @param jwt
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.REMAIN_INQUIRE)
    Observable<Response<List<CoinBean>>> requireRemain(@Field("uid") String uid,
                                                       @Field("jwt") String jwt);

    /**
     * 今天我的抽奖等级
     * @param uid
     * @param jwt
     * @param userinfo
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.MY_LOTTERY_RESULT)
    Observable<Response<MillionCatMyData>> requestMyLotteryResult(@Field("uid") String uid,
                                                                  @Field("jwt") String jwt,
                                                                  @Field("type") int type,
                                                                  @Field("userinfo") String userinfo);

    /**
     * 查询中奖人的进行滚动展示
     * @param uid
     * @param jwt
     * @param userinfo
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.LOTTERY_RECORD)
    Observable<Response<List<WinningPeopleShowData>>> recordLottery(@Field("uid") String uid,
                                                                    @Field("jwt") String jwt,
                                                                    @Field("type") int type,
                                                                    @Field("userinfo") String userinfo);

    /**
     * 查询是否可以抽奖
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.IS_LOOTERY)
    Observable<Response<StartLotteryData>> isStartLottery(@Field("uid") String uid,
                                                          @Field("jwt") String jwt,
                                                          @Field("type") int type,
                                                          @Field("userinfo") String userinfo);


    /**
     * 我的抽奖记录
     * @param uid
     * @param jwt
     * @param userinfo
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.MY_WIN_RECORD)
    Observable<Response<List<MyWinRecord>>> myWinningRecord(@Field("uid") String uid,
                                                            @Field("jwt") String jwt,
                                                            @Field("type") int type,
                                                            @Field("userinfo") String userinfo);

    /**
     * 我的邀请记录
     * @param uid
     * @param jwt
     * @param userinfo
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.MY_INVITER_RECORD)
    Observable<Response<MillionInviteBean>> fetchMyInviteRecord(@Field("uid") String uid,
                                                               @Field("jwt") String jwt,
                                                                @Field("userinfo") String userinfo);

    /**
     *查询是否有资格抽宝箱
     * @param uid
     * @param jwt
     * @param userinfo
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.CAN_LOTTERY)
    Observable<Response<Integer>> requestCanLottery(@Field("uid") String uid,
                                                    @Field("jwt") String jwt,
                                                    @Field("userinfo") String userinfo);

    /**
     * 获取当期要加载的网页地址
     * @return
     */
    @GET(RURLConfig.SPACE_PLAN_URL)
    Observable<Response<String>> showSplanUrl();


    /**
     * 提币到交易所
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.REFLECT_URL)
    Observable<Response<BaseEntity>> reflectCoin(@Field("num") String num,
                                                 @Field("cat_uid") String catuid,
                                                 @Field("coin_name") String coinname,
                                                 @Field("rocket_uid") String rockeId,
                                                 @Field("userinfo") String userinfo,
                                                 @Field("jwt") String jwt);

    /**
     * 提币到交易所，加密码
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.REFECT_FREE_PASSWORD)
    Observable<Response<BaseEntity>> reflectCoinFreePassWord(@Field("num") String num,
                                                             @Field("cat_uid") String catuid,
                                                             @Field("coin_name") String coinname,
                                                             @Field("rocket_uid") String rockeId,
                                                             @Field("userinfo") String userinfo,
                                                             @Field("wallet_password") String cat_password,
                                                             @Field("jwt") String jwt);

    /**
     * 驳回原因
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.REASON)
    Observable<Response<String>> getReason(@Field("token") String token);

    /**
     * 是否显示交易信息
     * @return
     */
    @GET(RURLConfig.MY_TEST_SALE)
    Observable<Response<Integer>> showTrader();

    /**
     * 修改支付密码前验证码手机号
     * @param token
     * @param phone
     * @param code
     * @param area
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.UPDATE_PAY_PASSWORD)
    Observable<Response<BaseEntity>> updatePayPasswordWord(@Field("token") String token,
                                                           @Field("phone") String phone,
                                                           @Field("code") String code,
                                                           @Field("area") String area);

    /**
     * 提币到哥伦布
     * @param catId
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.WITH_DRAW_CAT)
    Observable<Response<BaseEntity>> fetchWithDraw(@Field("cat_uid") String catId,
                                                   @Field("coin_name") String coinName,
                                                   @Field("rocket_uid") String rocketId,
                                                   @Field("num") String num,
                                                   @Field("userinfo") String userinfo,
                                                   @Field("password") String password,
                                                   @Field("jwt") String jwt);


    /**
     * 查询火箭余额
     * @param rocketId
     * @param userinfo
     * @param jwt
     * @return
     */
    @FormUrlEncoded
    @POST(RURLConfig.SEAECH_ROCKET_BALANCE)
    Observable<Response<List<RocketBalance>>> searchRocketBalance(@Field("rocket_id") String rocketId,
                                                                  @Field("userinfo") String userinfo,
                                                                  @Field("jwt") String jwt);

    /**
     * 保存用户的推送id
     * @param uid
     * @param registId
     * @return
     */
    @GET(RURLConfig.SAVA_USER_PUSHID)
    Observable<Response<BaseEntity>> putUserRegistId(@Query("uid") String uid,
                                                     @Query("jid") String registId,
                                                     @Query("type") String type);

    /**
     * 是否开启长连接
     * @return
     */
    @POST(RURLConfig.GET_OPEN_WS)
    Observable<Response<Integer>> getOpenWs();
}
