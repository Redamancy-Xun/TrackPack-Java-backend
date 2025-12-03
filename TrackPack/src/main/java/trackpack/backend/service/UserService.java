package trackpack.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import trackpack.backend.common.Page;
import trackpack.backend.controller.backpack.response.BackpackResponse;
import trackpack.backend.controller.user.response.UserInfoResponse;
import trackpack.backend.dto.SessionData;
import trackpack.backend.entity.Backpack;
import trackpack.backend.entity.User;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;
import trackpack.backend.util.SaltMD5Util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface UserService {


    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param contactInfo 联系方式
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    UserInfoResponse signup(String username, String password, String contactInfo) throws MyException;

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return UserInfoResponse
     * @throws MyException 异常
     */
    UserInfoResponse login(String username, String password) throws MyException;

    /**
     * 根据 header(cookie) 检测登录状况
     * @return -1 会话过期，0 表示未登录，1 成功登录
     * @throws MyException 通用异常
     */
    Integer checkLogin() throws MyException;

    /**
     * 登出
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    UserInfoResponse logout();

    /**
     * 注销用户
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    UserInfoResponse deleteUser();

    /**
     * 获取用户信息
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    UserInfoResponse getUserInfo();

    /**
     * 更新用户信息
     * @param username 用户名
     * @param contactInfo 联系方式
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    UserInfoResponse updateUserInfo(String username, String contactInfo) throws MyException;

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    UserInfoResponse changePassword(String oldPassword, String newPassword) throws MyException ;

    /**
     * 根据userId获取用户信息
     * @param userId 用户id
     * @return  User
     * @throws MyException 通用异常
     */
    User getUserById(String userId) throws MyException;

    /**
     * 获取用户的 backpack 列表
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<BackpackResponse>
     * @throws MyException 通用异常
     */
    public Page<BackpackResponse> getUserBackpacks(Boolean orderByCreateTimeDesc, Integer page, Integer pageSize) throws MyException;
}
