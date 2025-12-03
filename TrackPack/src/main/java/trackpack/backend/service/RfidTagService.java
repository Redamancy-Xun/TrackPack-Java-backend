package trackpack.backend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import trackpack.backend.controller.rfidtag.response.RfidTagResponse;
import trackpack.backend.entity.Backpack;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;

import java.time.LocalDateTime;

public interface RfidTagService {
    
    /**
     * 加入一个新的无源标签（物品）
     * @param rfidTagId
     * @param backpackId
     * @param itemName
     * @return RfidTagResponse
     * @throws MyException
     */
    RfidTagResponse addRfidTag(String rfidTagId, String backpackId, String itemName) throws MyException;

    /**
     * 删除一个无源标签（物品）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    RfidTagResponse deleteRfidTag(String rfidTagId) throws MyException;

    /**
     * 加入一个物品（无源标签）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    RfidTagResponse addRfidTagItem(String rfidTagId) throws MyException;

    /**
     * 移除一个物品（无源标签）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    RfidTagResponse removeRfidTagItem(String rfidTagId) throws MyException;

    /**
     * 更新一个无源标签的物品名称
     * @param rfidTagId
     * @param itemName
     * @return RfidTagResponse
     * @throws MyException
     */
    RfidTagResponse updateRfidTagItemName(String rfidTagId, String itemName) throws MyException;

    /**
     * 获取一个无源标签（物品）的信息
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    RfidTagResponse getRfidTag(String rfidTagId) throws MyException;
}
