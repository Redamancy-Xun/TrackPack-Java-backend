package trackpack.backend.controller.rfidtag;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trackpack.backend.common.Result;
import trackpack.backend.controller.rfidtag.response.RfidTagResponse;
import trackpack.backend.exception.MyException;
import trackpack.backend.service.RfidTagService;

import javax.validation.constraints.NotNull;

@Api("RfidTagController")
@RestController
@RequestMapping("/rfidTag")
@Slf4j
public class RfidTagController {

    @Autowired
    private RfidTagService rfidTagService;

    /**
     * 加入一个新的无源标签（物品）
     * @param rfidTagId
     * @param backpackId
     * @param itemName
     * @return RfidTagResponse
     * @throws MyException
     */
    @PostMapping(value = "/addRfidTag", produces = "application/json")
    @ApiOperation(value = "加入一个新的无源标签（物品）", response = RfidTagResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "无源标签ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "backpackId", value = "背包ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "itemName", value = "物品名称", required = true, paramType = "query", dataType = "String")
    })
    public Result addRfidTag(@NotNull @RequestParam("rfidTagId") String rfidTagId,
                             @NotNull @RequestParam("backpackId") String backpackId,
                             @NotNull @RequestParam("itemName") String itemName) {
        try {
            return Result.success(rfidTagService.addRfidTag(rfidTagId, backpackId, itemName));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 删除一个无源标签（物品）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @GetMapping(value = "/deleteRfidTag", produces = "application/json")
    @ApiOperation(value = "删除一个无源标签（物品）", response = RfidTagResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "无源标签ID", required = true, paramType = "query", dataType = "String")
    })
    public Result deleteRfidTag(@NotNull @RequestParam("rfidTagId") String rfidTagId) {
        try {
            return Result.success(rfidTagService.deleteRfidTag(rfidTagId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 加入一个物品（无源标签）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @PostMapping(value = "/addRfidTagItem", produces = "application/json")
    @ApiOperation(value = "加入一个物品（无源标签）", response = RfidTagResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "无源标签ID", required = true, paramType = "query", dataType = "String")
    })
    public Result addRfidTagItem(@NotNull @RequestParam("rfidTagId") String rfidTagId) {
        try {
            return Result.success(rfidTagService.addRfidTagItem(rfidTagId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 移除一个物品（无源标签）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @GetMapping(value = "/removeRfidTagItem", produces = "application/json")
    @ApiOperation(value = "移除一个物品（无源标签）", response = RfidTagResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "无源标签ID", required = true, paramType = "query", dataType = "String")
    })
    public Result removeRfidTagItem(@NotNull @RequestParam("rfidTagId") String rfidTagId) {
        try {
            return Result.success(rfidTagService.removeRfidTagItem(rfidTagId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 更新一个无源标签的物品名称
     * @param rfidTagId
     * @param itemName
     * @return RfidTagResponse
     * @throws MyException
     */
    @PostMapping(value = "/updateRfidTagItemName", produces = "application/json")
    @ApiOperation(value = "更新一个无源标签的物品名称", response = RfidTagResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "无源标签ID", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "itemName", value = "物品名称", required = true, paramType = "query", dataType = "String")
    })
    public Result updateRfidTagItemName(@NotNull @RequestParam("rfidTagId") String rfidTagId,
                                        @NotNull @RequestParam("itemName") String itemName) {
        try {
            return Result.success(rfidTagService.updateRfidTagItemName(rfidTagId, itemName));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }

    /**
     * 获取一个无源标签（物品）的信息
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @GetMapping(value = "/getRfidTag", produces = "application/json")
    @ApiOperation(value = "获取一个无源标签（物品）的信息【标签状态（0：未使用 1：正在使用）】", response = RfidTagResponse.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "rfidTagId", value = "无源标签ID", required = true, paramType = "query", dataType = "String")
    })
    public Result getRfidTag(@NotNull @RequestParam("rfidTagId") String rfidTagId) {
        try {
            return Result.success(rfidTagService.getRfidTag(rfidTagId));
        } catch (Exception e) {
            if (e instanceof MyException) {
                return Result.result(((MyException) e).getEnumExceptionType());
            }
            return Result.fail(e.getMessage());
        }
    }
}
