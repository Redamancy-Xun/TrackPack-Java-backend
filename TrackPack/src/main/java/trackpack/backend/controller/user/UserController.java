package trackpack.backend.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trackpack.backend.common.Result;
import trackpack.backend.controller.user.response.UserInfoResponse;
import trackpack.backend.exception.MyException;
import trackpack.backend.service.UserService;

@Api("UserController")
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册
     * @param username 用户名
     * @param password 密码
     * @param contactInfo 联系方式
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @PostMapping(value = "/signup", produces = "application/json")
    @ApiOperation(value = "注册", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contactInfo", value = "联系方式", required = true, paramType = "query", dataType = "String")
    })
    public Result signup(@NotNull @RequestParam("username") String username,
                         @NotNull @RequestParam("password") String password,
                         @NotNull @RequestParam("contactInfo") String contactInfo) {
        try {
            return Result.success(userService.signup(username, password, contactInfo));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return UserInfoResponse
     * @throws MyException 异常
     */
    @PostMapping(value = "/login", produces = "application/json")
    @ApiOperation(value = "登录", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query", dataType = "String")
    })
    public Result login(@NotNull @RequestParam("username") String username,
                        @NotNull @RequestParam("password") String password) {
        try {
            return Result.success(userService.login(username, password));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据 header(cookie) 检测登录状况
     * @return -1 会话过期，0 表示未登录，1 成功登录
     * @throws MyException 通用异常
     */
    @GetMapping(value = "/checkLogin", produces = "application/json")
    @ApiOperation(value = "检测登录状况（return：-1 会话过期，0 表示未登录，1 成功登录）")
    public Result checkLogin() {
        try {
            return Result.success(userService.checkLogin());
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 登出
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @GetMapping(value = "/logout", produces = "application/json")
    @ApiOperation(value = "登出", response = UserInfoResponse.class)
    public Result logout() {
        try {
            return Result.success(userService.logout());
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 注销用户
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @GetMapping(value = "/delete", produces = "application/json")
    @ApiOperation(value = "注销用户", response = UserInfoResponse.class)
    public Result delete() {
        try {
            return Result.success(userService.deleteUser());
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取用户信息
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @GetMapping(value = "/getUserInfo", produces = "application/json")
    @ApiOperation(value = "获取用户信息", response = UserInfoResponse.class)
    public Result getUserInfo() {
        try {
            return Result.success(userService.getUserInfo());
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param username 用户名
     * @param contactInfo 联系方式
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @PostMapping(value = "/updateUserInfo", produces = "application/json")
    @ApiOperation(value = "更新用户信息", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "contactInfo", value = "联系方式", required = true, paramType = "query", dataType = "String")
    })
    public Result updateUserInfo(@NotNull @RequestParam("username") String username,
                                 @NotNull @RequestParam("contactInfo") String contactInfo) {
        try {
            return Result.success(userService.updateUserInfo(username, contactInfo));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 修改密码
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return UserInfoResponse
     * @throws MyException 通用异常
     */
    @PostMapping(value = "/changePassword", produces = "application/json")
    @ApiOperation(value = "修改密码", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oldPassword", value = "旧密码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "newPassword", value = "新密码", required = true, paramType = "query", dataType = "String")
    })
    public Result changePassword(@NotNull @RequestParam("oldPassword") String oldPassword,
                                 @NotNull @RequestParam("newPassword") String newPassword) {
        try {
            return Result.success(userService.changePassword(oldPassword, newPassword));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取用户的 backpack 列表
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<BackpackResponse>
     * @throws MyException 通用异常
     */
    @GetMapping(value = "/getBackpackList", produces = "application/json")
    @ApiOperation(value = "获取用户的 backpack 列表", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderByCreateTimeDesc", value = "是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false", paramType = "query", dataType = "Boolean"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", paramType = "query", dataType = "Integer")
    })
    public Result getBackpackList(@RequestParam(value = "orderByCreateTimeDesc", defaultValue = "false") Boolean orderByCreateTimeDesc,
                                  @RequestParam(value = "page", defaultValue = "1") Integer page,
                                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            return Result.success(userService.getUserBackpacks(orderByCreateTimeDesc, page, pageSize));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据userId获取用户信息
     * @param userId 用户id
     * @return  User
     * @throws MyException 通用异常
     */
    @GetMapping(value = "/getUserById", produces = "application/json")
    @ApiOperation(value = "根据userId获取用户信息", response = UserInfoResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true, paramType = "query", dataType = "String")
    })
    public Result getUserById(@NotNull @RequestParam("userId") String userId) {
        try {
            return Result.success(userService.getUserById(userId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

}
