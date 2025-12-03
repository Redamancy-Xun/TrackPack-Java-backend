package trackpack.backend.controller.backpack;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trackpack.backend.common.Result;
import trackpack.backend.controller.backpack.response.BackpackResponse;
import trackpack.backend.exception.MyException;
import trackpack.backend.service.BackpackService;

import javax.validation.constraints.NotNull;

@Api("BackpackController")
@RestController
@RequestMapping("/backpack")
@Slf4j
public class BackpackController {

    @Autowired
    private BackpackService backpackService;

    /**
     * 激活背包
     * @param backpackId
     * @param backpackName
     * @param backpackBattery
     * @param backpackIpAddress
     * @param backpackMacAddress
     * @param backpackLocation
     * @return BackpackResponse
     * @throws MyException
     */
    @PostMapping(value = "/activate", produces = "application/json")
    @ApiOperation(value = "激活背包", response = BackpackResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackName", value = "背包名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackBattery", value = "背包电量", required = true, paramType = "query", dataType = "Double"),
            @ApiImplicitParam(name = "backpackIpAddress", value = "背包IP地址", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackMacAddress", value = "背包MAC地址", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackLocation", value = "背包位置", required = true, paramType = "query", dataType = "String")
    })
    public Result activateBackpack(@NotNull @RequestParam("backpackId") String backpackId,
                                   @NotNull @RequestParam("backpackName") String backpackName,
                                   @NotNull @RequestParam("backpackBattery") Double backpackBattery,
                                   @NotNull @RequestParam("backpackIpAddress") String backpackIpAddress,
                                   @NotNull @RequestParam("backpackMacAddress") String backpackMacAddress,
                                   @NotNull @RequestParam("backpackLocation") String backpackLocation) {
        try {
            return Result.success(backpackService.activateBackpack(backpackId, backpackName, backpackBattery, backpackIpAddress, backpackMacAddress, backpackLocation));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }


    /**
     * 停用背包
     * @param backpackId
     * @return BackpackResponse
     * @throws MyException
     */
    @GetMapping(value = "/deactivate", produces = "application/json")
    @ApiOperation(value = "停用背包", response = BackpackResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String")
    })
    public Result deactivateBackpack(@NotNull @RequestParam("backpackId") String backpackId) {
        try {
            return Result.success(backpackService.deleteBackpack(backpackId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新背包信息
     * @param backpackId
     * @param backpackName
     * @param backpackBattery
     * @param backpackLocation
     * @param backpackNetworkStatus
     * @return BackpackResponse
     * @throws MyException
     */
    @PostMapping(value = "/update", produces = "application/json")
    @ApiOperation(value = "更新背包信息", response = BackpackResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackName", value = "背包名称", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackBattery", value = "背包电量", required = true, paramType = "query", dataType = "Double"),
            @ApiImplicitParam(name = "backpackLocation", value = "背包位置", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackNetworkStatus", value = "背包网络状态（0：蓝牙连接 1：MQTT通讯）", required = true, paramType = "query", dataType = "Integer")
    })
    public Result updateBackpack(@NotNull @RequestParam("backpackId") String backpackId,
                                 @NotNull @RequestParam("backpackName") String backpackName,
                                 @NotNull @RequestParam("backpackBattery") Double backpackBattery,
                                 @NotNull @RequestParam("backpackLocation") String backpackLocation,
                                 @NotNull @RequestParam("backpackNetworkStatus") Integer backpackNetworkStatus) {
        try {
            return Result.success(backpackService.updateBackpack(backpackId, backpackName, backpackBattery, backpackLocation, backpackNetworkStatus));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新背包的某一个属性
     * @param backpackId
     * @param key (backpackName: 背包名称, backpackBattery: 模块电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态（0：蓝牙连接 1：MQTT通讯）)
     * @param value
     * @return BackpackResponse
     * @throws MyException
     */
    @PostMapping(value = "/updateProperty", produces = "application/json")
    @ApiOperation(value = "更新背包的某一个属性", response = BackpackResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "key", value = "属性名（backpackName: 背包名称, backpackBattery: 模块电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态（0：蓝牙连接 1：MQTT通讯））", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "value", value = "属性值", required = true, paramType = "query", dataType = "String")
    })
    public Result updateBackpackProperty(@NotNull @RequestParam("backpackId") String backpackId,
                                         @NotNull @RequestParam("key") String key,
                                         @NotNull @RequestParam("value") String value) {
        try {
            return Result.success(backpackService.updateBackpackAttribute(backpackId, key, value));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询背包信息
     * @param backpackId
     * @return BackpackResponse
     * @throws MyException
     */
    @GetMapping(value = "/query", produces = "application/json")
    @ApiOperation(value = "查询背包信息", response = BackpackResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String")
    })
    public Result queryBackpack(@NotNull @RequestParam("backpackId") String backpackId) {
        try {
            return Result.success(backpackService.queryBackpack(backpackId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询背包的某个属性
     * @param backpackId
     * @param key (backpackName: 背包名称, backpackBattery: 背包电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态（0：蓝牙连接 1：MQTT通讯）, backpackActivationTime: 激活时间, backpackIpAddress: IP地址, backpackMacAddress: MAC地址, backpackUserId: 用户ID, backpackRfidTagNum: RFID标签数量)
     * @return Object
     * @throws MyException
     */
    @GetMapping(value = "/queryProperty", produces = "application/json")
    @ApiOperation(value = "查询背包的某个属性", response = Object.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "key", value = "属性名（backpackName: 背包名称, backpackBattery: 背包电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态（0：蓝牙连接 1：MQTT通讯）, backpackActivationTime: 激活时间, backpackIpAddress: IP地址, backpackMacAddress: MAC地址, backpackUserId: 用户ID, backpackRfidTagNum: RFID标签数量）", required = true, paramType = "query", dataType = "String")
    })
    public Result queryBackpackProperty(@NotNull @RequestParam("backpackId") String backpackId,
                                        @NotNull @RequestParam("key") String key) {
        try {
            return Result.success(backpackService.queryBackpackAttribute(backpackId, key));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查询背包的所有无源标签
     * @param backpackId
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<RfidTagResponse>
     * @throws MyException
     */
    @GetMapping(value = "/queryRfidTags", produces = "application/json")
    @ApiOperation(value = "查询背包的所有无源标签", response = Object.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderByCreateTimeDesc", value = "是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false", paramType = "query", dataType = "Boolean", defaultValue = "false"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", paramType = "query", dataType = "Integer")
    })
    public Result queryRfidTags(@NotNull @RequestParam("backpackId") String backpackId,
                                @RequestParam(value = "orderByCreateTimeDesc", defaultValue = "false") Boolean orderByCreateTimeDesc,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        try {
            return Result.success(backpackService.queryBackpackRfidTags(backpackId, orderByCreateTimeDesc, page, pageSize));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }
}
