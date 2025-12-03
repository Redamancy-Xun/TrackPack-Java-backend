package trackpack.backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackpack.backend.common.Page;
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
public class RfidTagActionServiceImpl implements RfidTagActionService {

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
     * @param commandId 指令编号（0：激活标签并增加物品 1：增加物品 2：修改物品名称 3：删除标签并移除物品 4：移除物品 5：删除标签）
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
        queryWrapper.eq("action_id", rfidTagActionId);
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
        queryWrapper.eq("action_id", rfidTagActionId);
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
     * @param orderByCreateTimeDesc 是否按照创建时间倒叙排序 true 表示按照创建时间倒叙排序，false 表示按照创建时间正序排序 默认为 false
     * @param page 页码
     * @param pageSize 每页数量
     * @return List<RfidTagAction> 标签动作列表
     * @throws MyException
     */
    @Override
    public Page<RfidTagAction> findRfidTagActionByRfidTagId(String rfidTagId, Boolean orderByCreateTimeDesc, Integer page, Integer pageSize) throws MyException {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }
        if (orderByCreateTimeDesc == null) {
            orderByCreateTimeDesc = false;
        }

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
        queryWrapper1.ge("action_time", rfidTag.getActivationTime());
        queryWrapper1.orderByDesc(orderByCreateTimeDesc, "action_time");
        PageHelper.startPage(page, pageSize);

        return new Page<>(new PageInfo<>(rfidTagActionMapper.selectList(queryWrapper1)));
    }

}
