package trackpack.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import trackpack.backend.common.Page;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.entity.RfidTagAction;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;

import java.time.LocalDateTime;
import java.util.List;

public interface RfidTagActionService {

    /**
     * 插入 RFID 标签动作
     * @param rfidTagId RFID 标签 ID
     * @param actionUserId 动作用户 ID
     * @param commandId 命令 ID
     * @param itemOldName 物品旧名称
     * @param itemNewName 物品新名称
     * @param action 动作描述
     * @param actionTime 动作时间
     * @param deleteTime 删除时间
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    RfidTagAction insertRfidTagAction(String rfidTagId, String actionUserId, Integer commandId, String itemOldName,
                                             String itemNewName, String action, LocalDateTime actionTime, LocalDateTime deleteTime);

    /**
     * 删除 RFID 标签动作
     * @param rfidTagActionId RFID 标签动作 ID
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    RfidTagAction deleteRfidTagAction(Integer rfidTagActionId) throws MyException;

    /**
     * 查找 RFID 标签动作
     * @param rfidTagActionId RFID 标签动作 ID
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    RfidTagAction findRfidTagAction(Integer rfidTagActionId) throws MyException;

    /**
     * 根据 RFID 标签 ID 查找 RFID 标签动作
     * @param rfidTagId RFID 标签 ID
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<RfidTagAction> 标签动作列表
     * @throws MyException
     */
    public Page<RfidTagAction> findRfidTagActionByRfidTagId(String rfidTagId, Boolean orderByCreateTimeDesc, Integer page, Integer pageSize) throws MyException;
}
