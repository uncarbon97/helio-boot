package cc.uncarbon.module.sys.util;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpLogic;
import lombok.experimental.UtilityClass;

import java.util.List;

/**
 * 后台管理专用 StpUtil
 * 文档见 SA-Token -> 多账户认证：https://sa-token.cc/doc.html#/up/many-account
 *
 * @author Uncarbon
 */
@UtilityClass
public class AdminStpUtil {

    /**
     * 账号类型标识
     */
    public static final String TYPE = "AdminStpUtil";

    /**
     * 底层的 StpLogic 对象
     */
    public static StpLogic stpLogic = new StpLogic(TYPE);

    /**
     * 获取当前 StpLogic 的账号类型
     * @return See Note
     */
    public static String getLoginType(){
        return stpLogic.getLoginType();
    }

    /**
     * 重置 StpLogic 对象
     * <br> 1、更改此账户的 StpLogic 对象
     * <br> 2、put 到全局 StpLogic 集合中
     *
     * @param newStpLogic /
     */
    public static void setStpLogic(StpLogic newStpLogic) {
        // 重置此账户的 StpLogic 对象
        stpLogic = newStpLogic;

        // 添加到全局 StpLogic 集合中
        // 以便可以通过 SaManager.getStpLogic(type) 的方式来全局获取到这个 StpLogic
        SaManager.putStpLogic(newStpLogic);
    }

    /**
     * 获取 StpLogic 对象
     * @return /
     */
    public static StpLogic getStpLogic() {
        return stpLogic;
    }


    // ------------------- 获取token 相关 -------------------

    /**
     * 返回token名称
     * @return 此StpLogic的token名称
     */
    public static String getTokenName() {
        return stpLogic.getTokenName();
    }

    /**
     * 在当前会话写入当前TokenValue
     * @param tokenValue token值
     */
    public static void setTokenValue(String tokenValue){
        stpLogic.setTokenValue(tokenValue);
    }

    /**
     * 在当前会话写入当前TokenValue
     * @param tokenValue token值
     * @param cookieTimeout Cookie存活时间(秒)
     */
    public static void setTokenValue(String tokenValue, int cookieTimeout){
        stpLogic.setTokenValue(tokenValue, cookieTimeout);
    }

    /**
     * 获取当前TokenValue
     * @return 当前tokenValue
     */
    public static String getTokenValue() {
        return stpLogic.getTokenValue();
    }

    /**
     * 获取当前TokenValue (不裁剪前缀)
     * @return /
     */
    public static String getTokenValueNotCut(){
        return stpLogic.getTokenValueNotCut();
    }

    /**
     * 获取当前会话的Token信息
     * @return token信息
     */
    public static SaTokenInfo getTokenInfo() {
        return stpLogic.getTokenInfo();
    }


    // ------------------- 登录相关操作 -------------------

    // --- 登录

    /**
     * 会话登录
     * @param id 账号id，建议的类型：（long | int | String）
     */
    public static void login(Object id) {
        stpLogic.login(id);
    }

    /**
     * 会话登录，并指定登录设备类型
     * @param id 账号id，建议的类型：（long | int | String）
     * @param device 设备类型
     */
    public static void login(Object id, String device) {
        stpLogic.login(id, device);
    }

    /**
     * 会话登录，并指定是否 [记住我]
     *
     * @param id              账号id，建议的类型：（long | int | String）
     * @param isLastingCookie 是否为持久Cookie
     */
    public static void login(Object id, boolean isLastingCookie) {
        stpLogic.login(id, isLastingCookie);
    }

    /**
     * 会话登录，并指定此次登录token的有效期, 单位:秒
     *
     * @param id      账号id，建议的类型：（long | int | String）
     * @param timeout 此次登录token的有效期, 单位:秒 （如未指定，自动取全局配置的timeout值）
     */
    public static void login(Object id, long timeout) {
        stpLogic.login(id, timeout);
    }

    /**
     * 会话登录，并指定所有登录参数Model
     *
     * @param id         登录id，建议的类型：（long | int | String）
     * @param loginModel 此次登录的参数Model
     */
    public static void login(Object id, SaLoginModel loginModel) {
        stpLogic.login(id, loginModel);
    }

    /**
     * 创建指定账号id的登录会话
     * @param id 登录id，建议的类型：（long | int | String）
     * @return 返回会话令牌
     */
    public static String createLoginSession(Object id) {
        return stpLogic.createLoginSession(id);
    }

    /**
     * 创建指定账号id的登录会话
     * @param id 登录id，建议的类型：（long | int | String）
     * @param loginModel 此次登录的参数Model
     * @return 返回会话令牌
     */
    public static String createLoginSession(Object id, SaLoginModel loginModel) {
        return stpLogic.createLoginSession(id, loginModel);
    }

    // --- 注销

    /**
     * 会话注销
     */
    public static void logout() {
        stpLogic.logout();
    }

    /**
     * 会话注销，根据账号id
     * @param loginId 账号id
     */
    public static void logout(Object loginId) {
        stpLogic.logout(loginId);
    }

    /**
     * 会话注销，根据账号id 和 设备类型
     *
     * @param loginId 账号id
     * @param device 设备类型 (填null代表注销所有设备类型)
     */
    public static void logout(Object loginId, String device) {
        stpLogic.logout(loginId, device);
    }

    /**
     * 会话注销，根据指定 Token
     *
     * @param tokenValue 指定token
     */
    public static void logoutByTokenValue(String tokenValue) {
        stpLogic.logoutByTokenValue(tokenValue);
    }

    /**
     * 踢人下线，根据账号id
     * <p> 当对方再次访问系统时，会抛出NotLoginException异常，场景值=-5 </p>
     *
     * @param loginId 账号id
     */
    public static void kickout(Object loginId) {
        stpLogic.kickout(loginId);
    }

    /**
     * 踢人下线，根据账号id 和 设备类型
     * <p> 当对方再次访问系统时，会抛出NotLoginException异常，场景值=-5 </p>
     *
     * @param loginId 账号id
     * @param device 设备类型 (填null代表踢出所有设备类型)
     */
    public static void kickout(Object loginId, String device) {
        stpLogic.kickout(loginId, device);
    }

    /**
     * 踢人下线，根据指定 Token
     * <p> 当对方再次访问系统时，会抛出NotLoginException异常，场景值=-5 </p>
     *
     * @param tokenValue 指定token
     */
    public static void kickoutByTokenValue(String tokenValue) {
        stpLogic.kickoutByTokenValue(tokenValue);
    }

    /**
     * 顶人下线，根据账号id 和 设备类型
     * <p> 当对方再次访问系统时，会抛出NotLoginException异常，场景值=-4 </p>
     *
     * @param loginId 账号id
     * @param device 设备类型 (填null代表顶替所有设备类型)
     */
    public static void replaced(Object loginId, String device) {
        stpLogic.replaced(loginId, device);
    }


    // 查询相关

    /**
     * 当前会话是否已经登录
     * @return 是否已登录
     */
    public static boolean isLogin() {
        return stpLogic.isLogin();
    }

    /**
     * 检验当前会话是否已经登录，如未登录，则抛出异常
     */
    public static void checkLogin() {
        stpLogic.checkLogin();
    }

    /**
     * 获取当前会话账号id, 如果未登录，则抛出异常
     * @return 账号id
     */
    public static Object getLoginId() {
        return stpLogic.getLoginId();
    }

    /**
     * 获取当前会话账号id, 如果未登录，则返回默认值
     * @param <T> 返回类型
     * @param defaultValue 默认值
     * @return 登录id
     */
    public static <T> T getLoginId(T defaultValue) {
        return stpLogic.getLoginId(defaultValue);
    }

    /**
     * 获取当前会话账号id, 如果未登录，则返回null
     * @return 账号id
     */
    public static Object getLoginIdDefaultNull() {
        return stpLogic.getLoginIdDefaultNull();
    }

    /**
     * 获取当前会话账号id, 并转换为String类型
     * @return 账号id
     */
    public static String getLoginIdAsString() {
        return stpLogic.getLoginIdAsString();
    }

    /**
     * 获取当前会话账号id, 并转换为int类型
     * @return 账号id
     */
    public static int getLoginIdAsInt() {
        return stpLogic.getLoginIdAsInt();
    }

    /**
     * 获取当前会话账号id, 并转换为long类型
     * @return 账号id
     */
    public static long getLoginIdAsLong() {
        return stpLogic.getLoginIdAsLong();
    }

    /**
     * 获取指定Token对应的账号id，如果未登录，则返回 null
     * @param tokenValue token
     * @return 账号id
     */
    public static Object getLoginIdByToken(String tokenValue) {
        return stpLogic.getLoginIdByToken(tokenValue);
    }

    /**
     * 获取当前 Token 的扩展信息（此函数只在jwt模式下生效）
     * @param key 键值
     * @return 对应的扩展数据
     */
    public static Object getExtra(String key) {
        return stpLogic.getExtra(key);
    }

    /**
     * 获取指定 Token 的扩展信息（此函数只在jwt模式下生效）
     * @param tokenValue 指定的 Token 值
     * @param key 键值
     * @return 对应的扩展数据
     */
    public static Object getExtra(String tokenValue, String key) {
        return stpLogic.getExtra(tokenValue, key);
    }


    // ------------------- User-Session 相关 -------------------

    /**
     * 获取指定账号id的Session, 如果Session尚未创建，isCreate=是否新建并返回
     * @param loginId 账号id
     * @param isCreate 是否新建
     * @return Session对象
     */
    public static SaSession getSessionByLoginId(Object loginId, boolean isCreate) {
        return stpLogic.getSessionByLoginId(loginId, isCreate);
    }

    /**
     * 获取指定key的Session, 如果Session尚未创建，则返回null
     * @param sessionId SessionId
     * @return Session对象
     */
    public static SaSession getSessionBySessionId(String sessionId) {
        return stpLogic.getSessionBySessionId(sessionId);
    }

    /**
     * 获取指定账号id的Session，如果Session尚未创建，则新建并返回
     * @param loginId 账号id
     * @return Session对象
     */
    public static SaSession getSessionByLoginId(Object loginId) {
        return stpLogic.getSessionByLoginId(loginId);
    }

    /**
     * 获取当前会话的Session, 如果Session尚未创建，isCreate=是否新建并返回
     * @param isCreate 是否新建
     * @return Session对象
     */
    public static SaSession getSession(boolean isCreate) {
        return stpLogic.getSession(isCreate);
    }

    /**
     * 获取当前会话的Session，如果Session尚未创建，则新建并返回
     * @return Session对象
     */
    public static SaSession getSession() {
        return stpLogic.getSession();
    }


    // ------------------- Token-Session 相关 -------------------

    /**
     * 获取指定Token-Session，如果Session尚未创建，则新建并返回
     * @param tokenValue Token值
     * @return Session对象
     */
    public static SaSession getTokenSessionByToken(String tokenValue) {
        return stpLogic.getTokenSessionByToken(tokenValue);
    }

    /**
     * 获取当前Token-Session，如果Session尚未创建，则新建并返回
     * @return Session对象
     */
    public static SaSession getTokenSession() {
        return stpLogic.getTokenSession();
    }

    /**
     * 获取当前匿名 Token-Session （可在未登录情况下使用的Token-Session）
     * @return Token-Session 对象
     */
    public static SaSession getAnonTokenSession() {
        return stpLogic.getAnonTokenSession();
    }


    // ------------------- [临时有效期] 验证相关 -------------------

    /**
     * 检查当前token 是否已经[临时过期]，如果已经过期则抛出异常
     */
    public static void checkActivityTimeout() {
        stpLogic.checkActivityTimeout();
    }

    /**
     * 续签当前token：(将 [最后操作时间] 更新为当前时间戳)
     * <h1>请注意: 即使token已经 [临时过期] 也可续签成功，
     * 如果此场景下需要提示续签失败，可在此之前调用 checkActivityTimeout() 强制检查是否过期即可 </h1>
     */
    public static void updateLastActivityToNow() {
        stpLogic.updateLastActivityToNow();
    }


    // ------------------- 过期时间相关 -------------------

    /**
     * 获取当前登录者的 token 剩余有效时间 (单位: 秒)
     * @return token剩余有效时间
     */
    public static long getTokenTimeout() {
        return stpLogic.getTokenTimeout();
    }

    /**
     * 获取当前登录者的 User-Session 剩余有效时间 (单位: 秒)
     * @return token剩余有效时间
     */
    public static long getSessionTimeout() {
        return stpLogic.getSessionTimeout();
    }

    /**
     * 获取当前 Token-Session 剩余有效时间 (单位: 秒)
     * @return token剩余有效时间
     */
    public static long getTokenSessionTimeout() {
        return stpLogic.getTokenSessionTimeout();
    }

    /**
     * 获取当前 token [临时过期] 剩余有效时间 (单位: 秒)
     * @return token [临时过期] 剩余有效时间
     */
    public static long getTokenActivityTimeout() {
        return stpLogic.getTokenActivityTimeout();
    }

    /**
     * 对当前 Token 的 timeout 值进行续期
     * @param timeout 要修改成为的有效时间 (单位: 秒)
     */
    public static void renewTimeout(long timeout) {
        stpLogic.renewTimeout(timeout);
    }

    /**
     * 对指定 Token 的 timeout 值进行续期
     * @param tokenValue 指定token
     * @param timeout 要修改成为的有效时间 (单位: 秒)
     */
    public static void renewTimeout(String tokenValue, long timeout) {
        stpLogic.renewTimeout(tokenValue, timeout);
    }


    // ------------------- 角色验证操作 -------------------

    /**
     * 获取：当前账号的角色集合
     * @return /
     */
    public static List<String> getRoleList() {
        return stpLogic.getRoleList();
    }

    /**
     * 获取：指定账号的角色集合
     * @param loginId 指定账号id
     * @return /
     */
    public static List<String> getRoleList(Object loginId) {
        return stpLogic.getRoleList(loginId);
    }

    /**
     * 判断：当前账号是否拥有指定角色, 返回true或false
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(String role) {
        return stpLogic.hasRole(role);
    }

    /**
     * 判断：指定账号是否含有指定角色标识, 返回true或false
     * @param loginId 账号id
     * @param role 角色标识
     * @return 是否含有指定角色标识
     */
    public static boolean hasRole(Object loginId, String role) {
        return stpLogic.hasRole(loginId, role);
    }

    /**
     * 判断：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * @param roleArray 角色标识数组
     * @return true或false
     */
    public static boolean hasRoleAnd(String... roleArray){
        return stpLogic.hasRoleAnd(roleArray);
    }

    /**
     * 判断：当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     * @param roleArray 角色标识数组
     * @return true或false
     */
    public static boolean hasRoleOr(String... roleArray){
        return stpLogic.hasRoleOr(roleArray);
    }

    /**
     * 校验：当前账号是否含有指定角色标识, 如果验证未通过，则抛出异常: NotRoleException
     * @param role 角色标识
     */
    public static void checkRole(String role) {
        stpLogic.checkRole(role);
    }

    /**
     * 校验：当前账号是否含有指定角色标识 [指定多个，必须全部验证通过]
     * @param roleArray 角色标识数组
     */
    public static void checkRoleAnd(String... roleArray){
        stpLogic.checkRoleAnd(roleArray);
    }

    /**
     * 校验：当前账号是否含有指定角色标识 [指定多个，只要其一验证通过即可]
     * @param roleArray 角色标识数组
     */
    public static void checkRoleOr(String... roleArray){
        stpLogic.checkRoleOr(roleArray);
    }


    // ------------------- 权限验证操作 -------------------

    /**
     * 获取：当前账号的权限码集合
     * @return /
     */
    public static List<String> getPermissionList() {
        return stpLogic.getPermissionList();
    }

    /**
     * 获取：指定账号的权限码集合
     * @param loginId 指定账号id
     * @return /
     */
    public static List<String> getPermissionList(Object loginId) {
        return stpLogic.getPermissionList(loginId);
    }

    /**
     * 判断：当前账号是否含有指定权限, 返回true或false
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermission(String permission) {
        return stpLogic.hasPermission(permission);
    }

    /**
     * 判断：指定账号id是否含有指定权限, 返回true或false
     * @param loginId 账号id
     * @param permission 权限码
     * @return 是否含有指定权限
     */
    public static boolean hasPermission(Object loginId, String permission) {
        return stpLogic.hasPermission(loginId, permission);
    }

    /**
     * 判断：当前账号是否含有指定权限, [指定多个，必须全部具有]
     * @param permissionArray 权限码数组
     * @return true 或 false
     */
    public static boolean hasPermissionAnd(String... permissionArray){
        return stpLogic.hasPermissionAnd(permissionArray);
    }

    /**
     * 判断：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     * @param permissionArray 权限码数组
     * @return true 或 false
     */
    public static boolean hasPermissionOr(String... permissionArray){
        return stpLogic.hasPermissionOr(permissionArray);
    }

    /**
     * 校验：当前账号是否含有指定权限, 如果验证未通过，则抛出异常: NotPermissionException
     * @param permission 权限码
     */
    public static void checkPermission(String permission) {
        stpLogic.checkPermission(permission);
    }

    /**
     * 校验：当前账号是否含有指定权限 [指定多个，必须全部验证通过]
     * @param permissionArray 权限码数组
     */
    public static void checkPermissionAnd(String... permissionArray) {
        stpLogic.checkPermissionAnd(permissionArray);
    }

    /**
     * 校验：当前账号是否含有指定权限 [指定多个，只要其一验证通过即可]
     * @param permissionArray 权限码数组
     */
    public static void checkPermissionOr(String... permissionArray) {
        stpLogic.checkPermissionOr(permissionArray);
    }


    // ------------------- id 反查token 相关操作 -------------------

    /**
     * 获取指定账号id的tokenValue
     * <p> 在配置为允许并发登录时，此方法只会返回队列的最后一个token，
     * 如果你需要返回此账号id的所有token，请调用 getTokenValueListByLoginId
     * @param loginId 账号id
     * @return token值
     */
    public static String getTokenValueByLoginId(Object loginId) {
        return stpLogic.getTokenValueByLoginId(loginId);
    }

    /**
     * 获取指定账号id指定设备类型端的tokenValue
     * <p> 在配置为允许并发登录时，此方法只会返回队列的最后一个token，
     * 如果你需要返回此账号id的所有token，请调用 getTokenValueListByLoginId
     * @param loginId 账号id
     * @param device 设备类型
     * @return token值
     */
    public static String getTokenValueByLoginId(Object loginId, String device) {
        return stpLogic.getTokenValueByLoginId(loginId, device);
    }

    /**
     * 获取指定账号id的tokenValue集合
     * @param loginId 账号id
     * @return 此loginId的所有相关token
     */
    public static List<String> getTokenValueListByLoginId(Object loginId) {
        return stpLogic.getTokenValueListByLoginId(loginId);
    }

    /**
     * 获取指定账号id指定设备类型端的tokenValue 集合
     * @param loginId 账号id
     * @param device 设备类型
     * @return 此loginId的所有相关token
     */
    public static List<String> getTokenValueListByLoginId(Object loginId, String device) {
        return stpLogic.getTokenValueListByLoginId(loginId, device);
    }

    /**
     * 返回当前会话的登录设备类型
     * @return 当前令牌的登录设备类型
     */
    public static String getLoginDevice() {
        return stpLogic.getLoginDevice();
    }


    // ------------------- 会话管理 -------------------

    /**
     * 根据条件查询Token
     * @param keyword 关键字
     * @param start 开始处索引 (-1代表查询所有)
     * @param size 获取数量
     * @param sortType 排序类型（true=正序，false=反序）
     *
     * @return token集合
     */
    public static List<String> searchTokenValue(String keyword, int start, int size, boolean sortType) {
        return stpLogic.searchTokenValue(keyword, start, size, sortType);
    }

    /**
     * 根据条件查询SessionId
     * @param keyword 关键字
     * @param start 开始处索引 (-1代表查询所有)
     * @param size 获取数量
     * @param sortType 排序类型（true=正序，false=反序）
     *
     * @return sessionId集合
     */
    public static List<String> searchSessionId(String keyword, int start, int size, boolean sortType) {
        return stpLogic.searchSessionId(keyword, start, size, sortType);
    }

    /**
     * 根据条件查询Token专属Session的Id
     * @param keyword 关键字
     * @param start 开始处索引 (-1代表查询所有)
     * @param size 获取数量
     * @param sortType 排序类型（true=正序，false=反序）
     *
     * @return sessionId集合
     */
    public static List<String> searchTokenSessionId(String keyword, int start, int size, boolean sortType) {
        return stpLogic.searchTokenSessionId(keyword, start, size, sortType);
    }


    // ------------------- 账号封禁 -------------------

    /**
     * 封禁：指定账号
     * <p> 此方法不会直接将此账号id踢下线，如需封禁后立即掉线，请追加调用 StpUtil.logout(id)
     *
     * @param loginId 指定账号id
     * @param time 封禁时间, 单位: 秒 （-1=永久封禁）
     */
    public static void disable(Object loginId, long time) {
        stpLogic.disable(loginId, time);
    }

    /**
     * 判断：指定账号是否已被封禁 (true=已被封禁, false=未被封禁)
     *
     * @param loginId 账号id
     * @return /
     */
    public static boolean isDisable(Object loginId) {
        return stpLogic.isDisable(loginId);
    }

    /**
     * 校验：指定账号是否已被封禁，如果被封禁则抛出异常
     * @param loginId 账号id
     */
    public static void checkDisable(Object loginId) {
        stpLogic.checkDisable(loginId);
    }

    /**
     * 获取：指定账号剩余封禁时间，单位：秒（-1=永久封禁，-2=未被封禁）
     * @param loginId 账号id
     * @return /
     */
    public static long getDisableTime(Object loginId) {
        return stpLogic.getDisableTime(loginId);
    }

    /**
     * 解封：指定账号
     * @param loginId 账号id
     */
    public static void untieDisable(Object loginId) {
        stpLogic.untieDisable(loginId);
    }


    // ------------------- 分类封禁 -------------------

    /**
     * 封禁：指定账号的指定服务
     * <p> 此方法不会直接将此账号id踢下线，如需封禁后立即掉线，请追加调用 StpUtil.logout(id)
     * @param loginId 指定账号id
     * @param service 指定服务
     * @param time 封禁时间, 单位: 秒 （-1=永久封禁）
     */
    public static void disable(Object loginId, String service, long time) {
        stpLogic.disable(loginId, service, time);
    }

    /**
     * 判断：指定账号的指定服务 是否已被封禁 (true=已被封禁, false=未被封禁)
     * @param loginId 账号id
     * @param service 指定服务
     * @return /
     */
    public static boolean isDisable(Object loginId, String service) {
        return stpLogic.isDisable(loginId, service);
    }

    /**
     * 校验：指定账号 指定服务 是否已被封禁，如果被封禁则抛出异常
     * @param loginId 账号id
     * @param services 指定服务，可以指定多个
     */
    public static void checkDisable(Object loginId, String... services) {
        stpLogic.checkDisable(loginId, services);
    }

    /**
     * 获取：指定账号 指定服务 剩余封禁时间，单位：秒（-1=永久封禁，-2=未被封禁）
     * @param loginId 账号id
     * @param service 指定服务
     * @return see note
     */
    public static long getDisableTime(Object loginId, String service) {
        return stpLogic.getDisableTime(loginId, service);
    }

    /**
     * 解封：指定账号、指定服务
     * @param loginId 账号id
     * @param services 指定服务，可以指定多个
     */
    public static void untieDisable(Object loginId, String... services) {
        stpLogic.untieDisable(loginId, services);
    }


    // ------------------- 阶梯封禁 -------------------

    /**
     * 封禁：指定账号，并指定封禁等级
     * @param loginId 指定账号id
     * @param level 指定封禁等级
     * @param time 封禁时间, 单位: 秒 （-1=永久封禁）
     */
    public static void disableLevel(Object loginId, int level, long time) {
        stpLogic.disableLevel(loginId, level, time);
    }

    /**
     * 封禁：指定账号的指定服务，并指定封禁等级
     * @param loginId 指定账号id
     * @param service 指定封禁服务
     * @param level 指定封禁等级
     * @param time 封禁时间, 单位: 秒 （-1=永久封禁）
     */
    public static void disableLevel(Object loginId, String service, int level, long time) {
        stpLogic.disableLevel(loginId, service, level, time);
    }

    /**
     * 判断：指定账号是否已被封禁到指定等级
     *
     * @param loginId 指定账号id
     * @param level 指定封禁等级
     * @return /
     */
    public static boolean isDisableLevel(Object loginId, int level) {
        return stpLogic.isDisableLevel(loginId, level);
    }

    /**
     * 判断：指定账号的指定服务，是否已被封禁到指定等级
     *
     * @param loginId 指定账号id
     * @param service 指定封禁服务
     * @param level 指定封禁等级
     * @return /
     */
    public static boolean isDisableLevel(Object loginId, String service, int level) {
        return stpLogic.isDisableLevel(loginId, service, level);
    }

    /**
     * 校验：指定账号是否已被封禁到指定等级（如果已经达到，则抛出异常）
     *
     * @param loginId 指定账号id
     * @param level 封禁等级 （只有 封禁等级 ≥ 此值 才会抛出异常）
     */
    public static void checkDisableLevel(Object loginId, int level) {
        stpLogic.checkDisableLevel(loginId, level);
    }

    /**
     * 校验：指定账号的指定服务，是否已被封禁到指定等级（如果已经达到，则抛出异常）
     *
     * @param loginId 指定账号id
     * @param service 指定封禁服务
     * @param level 封禁等级 （只有 封禁等级 ≥ 此值 才会抛出异常）
     */
    public static void checkDisableLevel(Object loginId, String service, int level) {
        stpLogic.checkDisableLevel(loginId, service, level);
    }

    /**
     * 获取：指定账号被封禁的等级，如果未被封禁则返回-2
     *
     * @param loginId 指定账号id
     * @return /
     */
    public static int getDisableLevel(Object loginId) {
        return stpLogic.getDisableLevel(loginId);
    }

    /**
     * 获取：指定账号的 指定服务 被封禁的等级，如果未被封禁则返回-2
     *
     * @param loginId 指定账号id
     * @param service 指定封禁服务
     * @return /
     */
    public static int getDisableLevel(Object loginId, String service) {
        return stpLogic.getDisableLevel(loginId, service);
    }


    // ------------------- 身份切换 -------------------

    /**
     * 临时切换身份为指定账号id
     * @param loginId 指定loginId
     */
    public static void switchTo(Object loginId) {
        stpLogic.switchTo(loginId);
    }

    /**
     * 结束临时切换身份
     */
    public static void endSwitch() {
        stpLogic.endSwitch();
    }

    /**
     * 当前是否正处于[身份临时切换]中
     * @return 是否正处于[身份临时切换]中
     */
    public static boolean isSwitch() {
        return stpLogic.isSwitch();
    }

    /**
     * 在一个代码段里方法内，临时切换身份为指定账号id
     * @param loginId 指定账号id
     * @param function 要执行的方法
     */
    public static void switchTo(Object loginId, SaFunction function) {
        stpLogic.switchTo(loginId, function);
    }


    // ------------------- 二级认证 -------------------

    /**
     * 在当前会话 开启二级认证
     * @param safeTime 维持时间 (单位: 秒)
     */
    public static void openSafe(long safeTime) {
        stpLogic.openSafe(safeTime);
    }

    /**
     * 当前会话 是否处于二级认证时间内
     * @return true=二级认证已通过, false=尚未进行二级认证或认证已超时
     */
    public static boolean isSafe() {
        return stpLogic.isSafe();
    }

    /**
     * 检查当前会话是否已通过二级认证，如未通过则抛出异常
     */
    public static void checkSafe() {
        stpLogic.checkSafe();
    }

    /**
     * 获取当前会话的二级认证剩余有效时间 (单位: 秒, 返回-2代表尚未通过二级认证)
     * @return 剩余有效时间
     */
    public static long getSafeTime() {
        return stpLogic.getSafeTime();
    }

    /**
     * 在当前会话 结束二级认证
     */
    public static void closeSafe() {
        stpLogic.closeSafe();
    }

}
