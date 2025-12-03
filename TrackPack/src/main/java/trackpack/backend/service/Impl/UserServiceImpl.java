package trackpack.backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trackpack.backend.common.Page;
import trackpack.backend.controller.backpack.response.BackpackResponse;
import trackpack.backend.controller.user.response.UserInfoResponse;
import trackpack.backend.dto.SessionData;
import trackpack.backend.entity.Backpack;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;
import trackpack.backend.mapper.BackpackMapper;
import trackpack.backend.mapper.UserMapper;
import trackpack.backend.entity.User;
import trackpack.backend.service.BackpackService;
import trackpack.backend.service.UserService;
import trackpack.backend.util.RedisUtils;
import trackpack.backend.util.SaltMD5Util;
import trackpack.backend.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BackpackService backpackService;

    @Autowired
    private BackpackMapper backpackMapper;

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private HttpServletRequest request;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param contactInfo 联系方式
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @Override
    //@Transactional 注解标记该方法需要进行事务管理，在出现 MyException 异常时进行回滚操作
    @Transactional(rollbackFor = MyException.class)
    public UserInfoResponse signup(String username, String password, String contactInfo) throws MyException {

        User user = User.builder()
                .id(UUID.randomUUID().toString().substring(0, 8))
                .username(username)
                .password(SaltMD5Util.generateSaltPassword((password)))
                .contactInfo(contactInfo)
                .createTime(LocalDateTime.now())
                .deleteTime(null)
                .build();

        //调用 userMapper 的 insert 方法将用户信息插入到数据库中
        if (userMapper.insert(user) == 0){
            throw new MyException(EnumExceptionType.INSERT_FAILED);
        }

        //生成 sessionId 和 sessionData，分别存入 sessionUtils 和 redisUtils 中，设置过期时间为 86400 秒
        String sessionId = sessionUtils.generateSessionId();
        SessionData sessionData = new SessionData(user);
        sessionUtils.setSessionId(sessionId);
        redisUtils.set(user.getId(), sessionId, 604800);
        redisUtils.set(sessionId, sessionData, 604800);

        return new UserInfoResponse(user, sessionId);
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return UserInfoResponse
     * @throws MyException 异常
     */
    @Override
    public UserInfoResponse login(String username, String password) throws MyException {

        // 构造一个 QueryWrapper 用于查询用户信息，根据 username 从数据库中查询对应的多个用户信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        userQueryWrapper.isNull("delete_time");
        List<User> users = userMapper.selectList(userQueryWrapper);

        // 如果用户不存在，则报错提醒
        if (users.isEmpty()){
            throw new MyException(EnumExceptionType.USER_NOT_EXIST);
        }

        // 如果密码不正确，则报错提醒
        User user = null;
        for (User u : users) {
            if (SaltMD5Util.verifySaltPassword(password, u.getPassword())) {
                user = u;
                break;
            }
        }
        if (user == null){
            throw new MyException(EnumExceptionType.PASSWORD_ERROR);
        }

        //生成 sessionId 和 sessionData，分别存入 sessionUtils 和 redisUtils 中，设置过期时间为 86400 秒
        String sessionId = sessionUtils.generateSessionId();
        SessionData sessionData = new SessionData(user);
        sessionUtils.setSessionId(sessionId);
        redisUtils.set(user.getId(), sessionId, 604800);
        redisUtils.set(sessionId, sessionData, 604800);

        //最后构造一个 LoginInfo 对象
        return new UserInfoResponse(user, sessionId);
    }

    /**
     * 根据 header(cookie) 检测登录状况
     * @return -1 会话过期，0 表示未登录，1 成功登录
     * @throws MyException 通用异常
     */
    @Override
    public Integer checkLogin() throws MyException {

        //从请求头中获得 sessionId
        String key = request.getHeader("session");

        // 如果请求头中没有 sessionId，则返回未登录
        if (key == null || !redisUtils.hasKey(key)) {
            return 0;
        }
        // 如果 sessionId 过期，则删除 sessionId，并返回会话过期
        if (redisUtils.isExpire(key)) {
            redisUtils.del(key);
            return -1;
        }

        // 刷新 sessionData
        sessionUtils.refreshData(null);

        return 1;
    }

    /**
     * 登出
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @Override
    public UserInfoResponse logout() {

//        // 获取当前用户的 userId
//        String userId = sessionUtils.getUserId();
//
//        // 删除 redis 中的 sessionId 和 sessionData
//        redisUtils.del(userId);
//        redisUtils.del(sessionUtils.getSessionId());

        User user = getUserById(sessionUtils.getUserId());
        sessionUtils.invalidate();
        return new UserInfoResponse(user);
    }

    /**
     * 注销用户
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @Override
    public UserInfoResponse deleteUser() {
        String userId = sessionUtils.getUserId();
        User user = userMapper.selectById(userId);

        user.setUsername("用户" + UUID.randomUUID().toString().substring(0, 5));
        user.setPassword(null);
        user.setContactInfo(null);
        user.setDeleteTime(LocalDateTime.now());

        if(userMapper.updateById(user) == 0){
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        userMapper.clearSensitiveInfoById(userId);

        // 删除对应的 backpack
        QueryWrapper<Backpack> backpackQueryWrapper = new QueryWrapper<>();
        backpackQueryWrapper.eq("user_id", userId);
        backpackQueryWrapper.isNull("delete_time");
        List<Backpack> backpacks = backpackMapper.selectList(backpackQueryWrapper);
        for (Backpack backpack : backpacks) {
            backpackService.deleteBackpack(backpack.getBackpackId());
        }

        sessionUtils.invalidate();

        return new UserInfoResponse(user);
    }

    /**
     * 获取用户信息
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @Override
    public UserInfoResponse getUserInfo() {

        // 获取当前用户的 userId
        User user = getUserById(sessionUtils.getUserId());
        sessionUtils.refreshData(null);

        //最后构造一个 LoginInfo 对象
        return new UserInfoResponse(user);
    }

    /**
     * 更新用户信息
     * @param username 用户名
     * @param contactInfo 联系方式
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @Override
    public UserInfoResponse updateUserInfo(String username, String contactInfo) throws MyException {

        // 获取当前用户的 userId
        User user = getUserById(sessionUtils.getUserId());
        sessionUtils.refreshData(null);

        // 更新用户信息
        if (username != null){
            user.setUsername(username);
        }
        if (contactInfo != null){
            user.setContactInfo(contactInfo);
        }

        if (userMapper.updateById(user) == 0){
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        return new UserInfoResponse(user);
    }

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @Override
    public UserInfoResponse changePassword(String oldPassword, String newPassword) throws MyException {

        // 获取当前用户的 userId
        User user = getUserById(sessionUtils.getUserId());
        sessionUtils.refreshData(null);

        if (!SaltMD5Util.verifySaltPassword(oldPassword, user.getPassword())){
            throw new MyException(EnumExceptionType.OLD_PASSWORD_ERROR);
        }

        user.setPassword(SaltMD5Util.generateSaltPassword(newPassword));

        if (userMapper.updateById(user) == 0){
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        return new UserInfoResponse(user);
    }

    /**
     * 根据userId获取用户信息
     * @param userId 用户id
     * @return  User
     * @throws MyException 通用异常
     */
    @Override
    public User getUserById(String userId) throws MyException{

        sessionUtils.refreshData(null);

        // 构造一个 QueryWrapper 用于查询用户信息，根据 userId 从数据库中查询对应的用户信息
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userId);
        userQueryWrapper.isNull("delete_time");
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null){
            throw new MyException(EnumExceptionType.USER_NOT_EXIST);
        }
        return user;
    }

    /**
     * 获取用户的 backpack 列表
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<BackpackResponse>
     * @throws MyException 通用异常
     */
    @Override
    public Page<BackpackResponse> getUserBackpacks(Boolean orderByCreateTimeDesc, Integer page, Integer pageSize) throws MyException {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        if (orderByCreateTimeDesc == null) {
            orderByCreateTimeDesc = false;
        }

        User user = getUserById(sessionUtils.getUserId());
        sessionUtils.refreshData(null);

        // 构造一个 QueryWrapper 用于查询 backpack 信息，根据 userId 从数据库中查询对应的 backpack 信息
        QueryWrapper<Backpack> backpackQueryWrapper = new QueryWrapper<>();
        backpackQueryWrapper.eq("user_id", user.getId());
        backpackQueryWrapper.isNull("delete_time");
        backpackQueryWrapper.orderByDesc(orderByCreateTimeDesc, "activation_time");
        PageHelper.startPage(page, pageSize);
        Page<Backpack> backpackPage = new Page<>(new PageInfo<>(backpackMapper.selectList(backpackQueryWrapper)));
        List<Backpack> backpacks = backpackPage.getItems();

        // 构造一个 BackpackResponse 的列表
        List<BackpackResponse> backpackResponses = new ArrayList<>();
        for (Backpack backpack : backpacks) {
            backpackResponses.add(new BackpackResponse(backpack));
        }

        Page<BackpackResponse> backpackResponsePage = new Page<>();
        backpackResponsePage.setTotal(backpackPage.getTotal());
        backpackResponsePage.setPageNum(backpackPage.getPageNum());
        backpackResponsePage.setPageSize(backpackPage.getPageSize());
        backpackResponsePage.setItems(backpackResponses);
        backpackResponsePage.setPages(backpackPage.getPages());

        return backpackResponsePage;
    }
}
