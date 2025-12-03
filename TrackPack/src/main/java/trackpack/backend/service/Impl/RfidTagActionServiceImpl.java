package trackpack.backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.entity.RfidTagAction;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;
import trackpack.backend.mapper.RfidTagActionMapper;
import trackpack.backend.mapper.RfidTagMapper;
import trackpack.backend.service.RfidTagActionService;
import trackpack.backend.util.SessionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RfidTagActionImpl implements RfidTagActionService {

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private RfidTagActionMapper rfidTagActionMapper;

    @Autowired
    private RfidTagMapper rfidTagMapper;

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
    @Override
    public RfidTagAction insertRfidTagAction(String rfidTagId, String actionUserId, Integer commandId, String itemOldName,
                                    String itemNewName, String action, LocalDateTime actionTime, LocalDateTime deleteTime) throws MyException {

        sessionUtils.refreshData(null);

        RfidTagAction rfidTagAction = RfidTagAction.builder()
                .rfidTagId(rfidTagId)
                .actionUserId(actionUserId)
                .commandId(commandId)
                .itemOldName(itemOldName)
                .itemNewName(itemNewName)
                .action(action)
                .actionTime(actionTime)
                .deleteTime(deleteTime)
                .build();

        if (rfidTagActionMapper.insert(rfidTagAction) == 0) {
            throw new MyException(EnumExceptionType.INSERT_FAILED);
        }

        return rfidTagAction;
    }

    /**
     * 删除 RFID 标签动作
     * @param rfidTagActionId RFID 标签动作 ID
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    @Override
    public RfidTagAction deleteRfidTagAction(Integer rfidTagActionId) throws MyException {

        sessionUtils.refreshData(null);

        QueryWrapper<RfidTagAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rfid_tag_action_id", rfidTagActionId);
        RfidTagAction rfidTagAction = rfidTagActionMapper.selectOne(queryWrapper);

        if (rfidTagAction == null) {
            throw new MyException(EnumExceptionType.RFID_TAG_ACTION_NOT_EXIST);
        } else if (rfidTagAction.getDeleteTime() != null) {
            throw new MyException(EnumExceptionType.RFID_TAG_ACTION_IS_DISABLED);
        }

        rfidTagAction.setDeleteTime(LocalDateTime.now());
        if (rfidTagActionMapper.updateById(rfidTagAction) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        return rfidTagAction;
    }

    /**
     * 查找 RFID 标签动作
     * @param rfidTagActionId RFID 标签动作 ID
     * @return RfidTagAction 标签动作
     * @throws MyException
     */
    @Override
    public RfidTagAction findRfidTagAction(Integer rfidTagActionId) throws MyException {

        sessionUtils.refreshData(null);

        QueryWrapper<RfidTagAction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rfid_tag_action_id", rfidTagActionId);
        RfidTagAction rfidTagAction = rfidTagActionMapper.selectOne(queryWrapper);

        if (rfidTagAction == null) {
            throw new MyException(EnumExceptionType.RFID_TAG_ACTION_NOT_EXIST);
        } else if (rfidTagAction.getDeleteTime() != null) {
            throw new MyException(EnumExceptionType.RFID_TAG_ACTION_IS_DISABLED);
        }

        return rfidTagAction;
    }

    /**
     * 根据 RFID 标签 ID 查找 RFID 标签动作
     * @param rfidTagId RFID 标签 ID
     * @return List<RfidTagAction> 标签动作列表
     * @throws MyException
     */
    @Override
    public List<RfidTagAction> findRfidTagActionByRfidTagId(String rfidTagId) throws MyException {

        sessionUtils.refreshData(null);

        // 查找当前rfidTag是否在数据库中
        QueryWrapper<RfidTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rfid_tag_id", rfidTagId);
        RfidTag rfidTag = rfidTagMapper.selectOne(queryWrapper);

        if (rfidTag == null) {
            throw new MyException(EnumExceptionType.RFID_TAG_NOT_EXIST);
        } else if (rfidTag.getDeleteTime() != null) {
            throw new MyException(EnumExceptionType.RFID_TAG_IS_DISABLED);
        }

        QueryWrapper<RfidTagAction> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("rfid_tag_id", rfidTagId);
        queryWrapper1.isNull("delete_time");

        return rfidTagActionMapper.selectList(queryWrapper1);
    }

}
