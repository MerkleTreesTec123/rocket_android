package com.ifenduo.lib_base.tools;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ifenduo.common.tool.DateTimeTool;
import com.ifenduo.lib_base.entity.User;
import com.tencent.mmkv.MMKV;


public class MMKVTools {

    public static void init(Context context) {
        MMKV.initialize(context);
    }

    public static void clearAll(Context context) {
        MMKV.defaultMMKV().clearAll();
    }

    /**
     * 是否是第一次打开APP
     *
     * @return
     */
    public static boolean isFirstOpenApp() {
        return MMKV.mmkvWithID("FirstOpenApp").decodeBool("isFirstOpenApp", true);
    }

    public static void saveFirstOpenApp(boolean isFirstOpenApp) {
        MMKV.mmkvWithID("FirstOpenApp").encode("isFirstOpenApp", isFirstOpenApp);
    }


    private static boolean getBoolean(String key) {
        return MMKV.defaultMMKV().decodeBool(key, false);
    }

    private static String getString(String key) {
        return MMKV.defaultMMKV().decodeString(key, "");
    }


    /**
     * 保存uid
     *
     * @param uid
     */
    public static void saveUID(String uid) {
        MMKV.defaultMMKV().encode("uid", uid);
    }

    /**
     * 获取uid
     *
     * @return
     */
    public static String getUID() {
        return getString("uid");
    }

    /**
     * 保存userName
     *
     * @param userName
     */
    public static void saveUserName(String userName) {
        MMKV.defaultMMKV().encode("userName", userName);
    }

    /**
     * 获取userName
     */
    public static String getUserName() {
        return getString("userName");
    }

    /**
     * 保存Nick
     *
     * @param userName
     */
    public static void saveNick(String userName) {
        MMKV.defaultMMKV().encode("nick", userName);
    }

    /**
     * 获取Nick
     */
    public static String getNick() {
        return getString("nick");
    }

    /**
     * 保存token
     *
     * @param uid
     */
    public static void saveToken(String uid) {
        MMKV.defaultMMKV().encode("token", uid);
    }

    /**
     * 获取保存token
     *
     * @return
     */
    public static String getToken() {
        return getString("token");
    }

    public static void saveSecretKey(String secretKey) {
        MMKV.defaultMMKV().encode("secretKey", secretKey);
    }

    public static String getSecretKey() {
        return getString("secretKey");
    }

    /**
     * 保存会员等级
     *
     * @param level
     */
    public static void saveLevel(String level) {
        MMKV.defaultMMKV().encode("level", level);
    }

    /**
     * 获取会员等级
     */
    public static String getLevel() {
        return getString("level");
    }

    /**
     * 保存登录密码
     *
     * @param loginPassword
     */
    public static void saveLoginPassword(String loginPassword) {
        MMKV.defaultMMKV().encode("loginPassword", loginPassword);
    }

    /**
     * 获取登录密码
     */
    public static String getLoginPassword() {
        return getString("loginPassword");
    }


    /**
     * 保存电话区号
     *
     * @param area
     */
    public static void saveArea(String area) {
        MMKV.defaultMMKV().encode("area", area);
    }

    /**
     * 获取电话区号
     *
     * @return
     */
    public static String getArea() {
        return getString("area");
    }

    /**
     * 保存注册时间
     *
     * @param registerTime
     */
    public static void saveRegisterTime(String registerTime) {
        MMKV.defaultMMKV().encode("registerTime", registerTime);
    }

    /**
     * 获取注册时间
     *
     * @return
     */
    public static String getRegisterTime() {
        String registerTime = getString("registerTime");
        if (TextUtils.isEmpty(registerTime)) {
            registerTime = "0";
        }
        long registerTime_ = Long.parseLong(registerTime);
        return DateTimeTool.formatTimeWithPattern(registerTime_, "yyyy/MM/dd");
    }

    public static void saveUser(User user) {
        if (user != null) {
            saveUID(user.getFid());
            saveUserName(user.getFloginname());
            saveArea(user.getFareacode());
            saveRegisterTime(user.getFregistertime());
            saveLevel(user.getLevel());
            saveInvitationCode(user.getFid());
            saveNick(user.getFnickname());
            saveHasBindGoogleCode(user.isFgooglebind());
            savePhone(user.getFtelephone());
            saveEmail(user.getFemail());
            saveAllUserInfo(user);
        }
    }

    public static void saveLanguage(String language) {
        MMKV.mmkvWithID("language_setting").encode("language", language);
    }

    public static String getLanguage() {
        String language = "";
        language =  MMKV.mmkvWithID("language_setting").decodeString("language", "");
        if (TextUtils.isEmpty(language)) {
            language = LanguageUtil.SIMPLIFIED_CHINESE;
        }
        return language;
    }

    public static void savePayPasswordStatus(boolean hasSet) {
        MMKV.defaultMMKV().encode("hasSetPayPassword", hasSet);
    }

    public static boolean hasSetPayPassword() {
        return getBoolean("hasSetPayPassword");
    }

    public static void saveInvitationCode(String invitationCode) {
        MMKV.defaultMMKV().encode("invitationCode", invitationCode);
    }

    public static String getInvitationCode() {
        return getString("invitationCode");
    }


    public static void changeIsShowAssetsWalletNum() {
        MMKV.defaultMMKV().encode("isShowAssetsWalletNum", !isShowAssetsWalletNum());
    }

    public static boolean isShowAssetsWalletNum() {
        return MMKV.defaultMMKV().decodeBool("isShowAssetsWalletNum", true);
    }

    public static void changeIsShowC2CWalletNum() {
        MMKV.defaultMMKV().encode("isShowC2CWalletNum", !isShowC2CWalletNum());
    }

    public static boolean isShowC2CWalletNum() {
        return MMKV.defaultMMKV().decodeBool("isShowC2CWalletNum", true);
    }

    public static void saveHasBindGoogleCode(boolean hasBind) {
        MMKV.defaultMMKV().encode("hasBindGoogleCode", hasBind);
    }

    public static boolean hasBindGoogleCode() {
        return getBoolean("hasBindGoogleCode");
    }

    public static void saveHasBindPhone(boolean hasBind) {
        MMKV.defaultMMKV().encode("hasBindPhone", hasBind);
    }

    public static boolean hasBindPhone() {
        return getBoolean("hasBindPhone");
    }

    public static void saveHasBindEmail(boolean hasBind) {
        MMKV.defaultMMKV().encode("hasBindEmail", hasBind);
    }

    public static boolean hasBindEmail() {
        return getBoolean("hasBindEmail");
    }

    /**
     *
     * @param status 0审核中 2已驳回 10认证成功 其他未认证
     */
    public static void saveRelNameStatus(String status) {
        MMKV.defaultMMKV().encode("realNameStatus", status);
    }

    /**
     * @return 0审核中 2已驳回 10认证成功 其他未认证
     */
    public static String getRelNameStatus() {
        return getString("realNameStatus");
    }


    /**
     * 设置是否开启交易功能状态
     * @param isOpen
     */
    public static void setOpenTrade(boolean isOpen){
        MMKV.defaultMMKV().encode("isOpenTrade", isOpen);
    }

    /**
     * 获取是否开启交易功能
     * @return
     */
    public static boolean isOpenTrade(){
        return getBoolean("isOpenTrade");
    }

    /**
     * 设置用户手机号码
     * @param phone
     */
    public static void savePhone(String phone){
        MMKV.defaultMMKV().encode("user_phone", phone);
    }

    /**
     * 获取用户手机号
     * @return
     */
    public static String getPhone(){
        return getString("user_phone");
    }

    /**
     * 设置用户邮箱
     * @param email
     */
    public static void saveEmail(String email){
        MMKV.defaultMMKV().encode("user_email", email);
    }

    /**
     * 获取用户邮箱
     * @return
     */
    public static String getEmail(){
        return getString("user_email");
    }

    /**
     * 保存我的抽奖等级
     * @param level
     */
    public static void setMyLotteryLevel(int level){
        MMKV.defaultMMKV().encode("my_lottery_level", level);
    }

    /**
     * 获取我的抽奖结果
     * @return
     */
    public static int getMyLotteryLevel(){
         return MMKV.defaultMMKV().decodeInt("my_lottery_level", -1);
    }

    /**
     * 保存我当前抽奖状态
     * @param status
     */
    public static void setLottery(boolean status){
        MMKV.defaultMMKV().encode("my_is_lottery", status);
    }

    /**
     * 获取我的抽奖状态
     * @return
     */
    public static boolean isLottery(){
        return MMKV.defaultMMKV().decodeBool("my_is_lottery", false);
    }

    /**
     * 保存实名姓名
     * @param name
     */
    public static void setRealName(String name){
        MMKV.defaultMMKV().encode("my_real_name", name);
    }

    /**
     * 获取实名姓名
     * @return
     */
    public static String getRealName(){
        return getString("my_real_name");
    }

    /**
     * 保存显示的Url
     * @return
     */
    public static void saveNavWebUrl(String url){
        MMKV.defaultMMKV().encode("navigation_show_url", url);
    }

    /**
     * 获取底部导航显示url
     */
    public static String getNavWebUrl(){
        return getString("navigation_show_url");
    }

    /**
     * 保存全部的user信息
     * @param user
     */
    public static void saveAllUserInfo(User user){
        String userJson = new Gson().toJson(user);
        MMKV.defaultMMKV().encode("user_all_info",userJson);
    }

    /**
     * 获取登录返回的用户所有user信息
     * @return
     */
    public static String getAllUserInfo(){
        return getString("user_all_info");
    }

    /**
     * 保存引导
     * @param gone
     */
    public static void goneGuide(int gone){
        MMKV.defaultMMKV().encode("action_button_drag",gone);
    }

    /**
     * 查询引导
     * @return
     */
    public static int isGoneGuide(){
        return  MMKV.defaultMMKV().decodeInt("action_button_drag", -1);
    }


    /**
     * 保存是否开启长连接
     */
    public static void saveOpenWs(int tag){
        MMKV.defaultMMKV().encode("openws",tag);
    }

    /**
     * 是否开启长连接(1开启，0关闭)
     * @return
     */
    public static int isOpenWs(){
        return  MMKV.defaultMMKV().decodeInt("openws", 0);
    }

    /**
     * 设置是否开启手势密码
     * @param isOpen
     */
    public static void setOpenGesture(boolean isOpen){
        MMKV.defaultMMKV().encode("opengesture", isOpen);
    }

    /**
     * 获取是否开启交易功能
     * @return
     */
    public static boolean isOpenGesture(){
        return getBoolean("opengesture");
    }

}
