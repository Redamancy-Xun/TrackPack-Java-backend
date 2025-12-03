package trackpack.backend.controller.rfidtagaction;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trackpack.backend.common.Result;
import trackpack.backend.entity.RfidTagAction;
import trackpack.backend.exception.MyException;
import trackpack.backend.service.RfidTagActionService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Api("RfidTagActionController")
@RestController
@RequestMapping("/rfidTagAction")
@Slf4j
public class RfidTagActionController {
    
    @Autowired
    private RfidTagActionService rfidTagActionService;

//    /**
//     * 插入 RFID 标签动作
//     * @param rfidTagId RFID 标签 ID
//     * @param actionUserId 动作用户 ID
//     * @param commandId 指令编号（0：激活标签并增加物品 1：增加物品 2：修改物品名称 3：删除标签并移除物品 4：移除物品 5：删除标签）
//     * @param itemOldName 物品旧名称
//     * @param itemNewName 物品新名称
//     * @param action 动作描述
//     * @param actionTime 动作时间
//     * @param deleteTime 删除时间
//     * @return RfidTagAction 标签动作
//     * @throws MyException
//     */
//    @PostMapping(value = "/insertRfidTagAction", produces = "application/json")
//    @ApiOperation(value = "插入 RFID 标签动作", response = RfidTagAction.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "rfidTagId", value = "RFID 标签 ID", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "actionUserId", value = "动作用户 ID", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "commandId", value = "指令编号（0：激活标签并增加物品 1：增加物品 2：修改物品名称 3：删除标签并移除物品 4：移除物品 5：删除标签）", required = true, paramType = "query", dataType = "Integer"),
//            @ApiImplicitParam(name = "itemOldName", value = "物品旧名称", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "itemNewName", value = "物品新名称", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "action", value = "动作描述", required = true, paramType = "query", dataType = "String"),
//            @ApiImplicitParam(name = "actionTime", value = "动作时间", required = true, paramType = "query", dataType = "LocalDateTime"),
//            @ApiImplicitParam(name = "deleteTime", value = "删除时间", required = true, paramType = "query", dataType = "LocalDateTime")
//    })
//    public Result insertRfidTagAction(@NotNull @RequestParam("rfidTagId") String rfidTagId,
//                                      @NotNull @RequestParam("actionUserId") String actionUserId,
//                                      @NotNull @RequestParam("commandId") Integer commandId,
//                                      @NotNull @RequestParam("itemOldName") String itemOldName,
//                                      @NotNull @RequestParam("itemNewName") String itemNewName,
//                                      @NotNull @RequestParam("action") String action,
//                                      @NotNull @RequestParam("actionTime") LocalDateTime actionTime,
//                                      @NotNull @RequestParam("deleteTime") LocalDateTime deleteTime) {
//        try {
//            return Result.success(rfidTagActionService.insertRfidTagAction(rfidTagId, actionUserId, commandId, itemOldName, itemNewName, action, actionTime, deleteTime));
//        } catch (Exception e) {
//            if (e instanceof MyException) {
//                return Result.result(((MyException) e).getEnumExceptionType());
//            }
//            return Result.fail(e.getMessage());
//        }
//    }

    /**
     * 删除 RFID 标签动作
     * @param rfidTagActionId RFID 标签动作 ID
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    @GetMapping(value = "/deleteRfidTagAction", produces = "application/json")
    @ApiOperation(value = "删除 RFID 标签动作", response = RfidTagAction.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagActionId", value = "RFID 标签动作 ID", required = true, paramType = "query", dataType = "Integer")
    })
    public Result deleteRfidTagAction(@NotNull @RequestParam("rfidTagActionId") Integer rfidTagActionId) {
        try {
            return Result.success(rfidTagActionService.deleteRfidTagAction(rfidTagActionId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 查找 RFID 标签动作
     * @param rfidTagActionId RFID 标签动作 ID
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    @GetMapping(value = "/findRfidTagAction", produces = "application/json")
    @ApiOperation(value = "查找 RFID 标签动作【指令编号（0：激活标签并增加物品 1：增加物品 2：修改物品名称 3：删除标签并移除物品 4：移除物品 5：删除标签）】", response = RfidTagAction.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagActionId", value = "RFID 标签动作 ID", required = true, paramType = "query", dataType = "Integer")
    })
    public Result findRfidTagAction(@NotNull @RequestParam("rfidTagActionId") Integer rfidTagActionId) {
        try {
            return Result.success(rfidTagActionService.findRfidTagAction(rfidTagActionId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 根据 RFID 标签 ID 查找 RFID 标签动作
     * @param rfidTagId RFID 标签 ID
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<RfidTagAction> 标签动作列表
     * @throws MyException
     */
    @GetMapping(value = "/findRfidTagActionByRfidTagId", produces = "application/json")
    @ApiOperation(value = "根据 RFID 标签 ID 查找 RFID 标签动作", response = RfidTagAction.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "RFID 标签 ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "orderByCreateTimeDesc", value = "是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false", paramType = "query", dataType = "Boolean"),
            @ApiImplicitParam(name = "page", value = "页码", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页数量", paramType = "query", dataType = "Integer")
    })
    public Result findRfidTagActionByRfidTagId(@NotNull @RequestParam("rfidTagId") String rfidTagId,
                                               @RequestParam(value = "orderByCreateTimeDesc", defaultValue = "false") Boolean orderByCreateTimeDesc,
                                               @RequestParam("page") Integer page,
                                               @RequestParam("pageSize") Integer pageSize) {
        try {
            return Result.success(rfidTagActionService.findRfidTagActionByRfidTagId(rfidTagId, orderByCreateTimeDesc, page, pageSize));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

}
