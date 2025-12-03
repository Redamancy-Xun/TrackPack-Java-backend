package trackpack.backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackpack.backend.controller.rfidtag.response.RfidTagResponse;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.entity.RfidTagAction;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;
import trackpack.backend.mapper.BackpackMapper;
import trackpack.backend.mapper.RfidTagActionMapper;
import trackpack.backend.mapper.RfidTagMapper;
import trackpack.backend.entity.Backpack;
import trackpack.backend.service.RfidTagActionService;
import trackpack.backend.service.RfidTagService;
import trackpack.backend.util.SessionUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RfidTagServiceImpl implements RfidTagService {
//    private static final Logger logger = Logger.getLogger(RfidTagServiceImpl.class.getName());

    @Autowired
    private RfidTagMapper rfidTagMapper;

    @Autowired
    private RfidTagActionService rfidTagActionService;

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private BackpackMapper backpackMapper;


    /**
     * 加入一个新的无源标签（物品）
     * @param rfidTagId
     * @param backpackId
     * @param itemName
     * @return RfidTagResponse
     * @throws MyException
     */
    @Override
    public RfidTagResponse addRfidTag(String rfidTagId, String backpackId, String itemName) throws MyException {
//        logger.info("Registering rfidTag: " + rfidTag);
//
//        if (backpackId != null) {
//            Optional<Backpack> backpackOptional = backpackServiceImpl.findById(backpackId);
//            if (backpackOptional.isPresent()) {
//                rfidTag.setBackpack(backpackOptional.get());
//            } else {
//                logger.severe("Backpack not found with ID: " + backpackId);
//                throw new IllegalArgumentException("Backpack not found with ID: " + backpackId);
//            }
//        } else {
//            rfidTag.setBackpack(null); // 确保在backpackId为空时，不会尝试关联Backpack
//            rfidTag.setBackpackId(null);
//        }
//        return rfidTagMapper.save(rfidTag);

        sessionUtils.refreshData(null);

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) { // 数据库中不存在该背包
            throw new MyException(EnumExceptionType.BACKPACK_NOT_EXIST);
        } else if (backpack.getDeleteTime() != null) { // 数据库中存在该背包，但已被停用
            throw new MyException(EnumExceptionType.BACKPACK_IS_DISABLED);
        }

        // 查找当前rfidTag是否在数据库中
        QueryWrapper<RfidTag> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("rfid_tag_id", rfidTagId);
        RfidTag rfidTag = rfidTagMapper.selectOne(queryWrapper1);

        if (rfidTag == null) {
            // 创建新的无源标签
            RfidTag newRfidTag = RfidTag.builder()
                    .rfidTagId(rfidTagId)
                    .backpackId(backpackId)
                    .itemName(itemName)
                    .activationTime(LocalDateTime.now())
                    .rfidTagStatus(1)
                    .build();

            if (rfidTagMapper.insert(newRfidTag) == 0) {
                throw new MyException(EnumExceptionType.INSERT_FAILED);
            }

            // 背包的无源标签数量加一
            backpack.setRfidTagNum(backpack.getRfidTagNum() + 1);
            if (backpackMapper.updateById(backpack) == 0) {
                throw new MyException(EnumExceptionType.UPDATE_FAILED);
            }

            // 记录无源标签的操作
            rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 0, null, itemName,
                    "激活标签 { " + rfidTagId + " } 并增加物品 { " + itemName + " }", LocalDateTime.now(), null);

            return new RfidTagResponse(newRfidTag);
        } else if (rfidTag.getDeleteTime() == null) {
            throw new MyException(EnumExceptionType.RFID_TAG_IS_BIND);
        } else {
            // 更新无源标签
            rfidTag.setBackpackId(backpackId);
            rfidTag.setItemName(itemName);
            rfidTag.setActivationTime(LocalDateTime.now());
            rfidTag.setRfidTagStatus(1);
            rfidTag.setDeleteTime(null);

            if (rfidTagMapper.updateById(rfidTag) == 0) {
                throw new MyException(EnumExceptionType.UPDATE_FAILED);
            }

            rfidTagMapper.recoverRfidTagByRfidTagId(rfidTagId);

            // 背包的无源标签数量加一
            backpack.setRfidTagNum(backpack.getRfidTagNum() + 1);
            if (backpackMapper.updateById(backpack) == 0) {
                throw new MyException(EnumExceptionType.UPDATE_FAILED);
            }

            // 记录无源标签的操作
            rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 0, null, itemName,
                    "激活标签 { " + rfidTagId + " } 并增加物品 { " + itemName + " }", LocalDateTime.now(), null);

            return new RfidTagResponse(rfidTag);
        }
    }

    /**
     * 删除一个无源标签（物品）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @Override
    public RfidTagResponse deleteRfidTag(String rfidTagId) throws MyException {

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

        // 更新无源标签
        rfidTag.setDeleteTime(LocalDateTime.now());
        if (rfidTagMapper.updateById(rfidTag) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 判断无源标签是否在背包中
        if (rfidTag.getRfidTagStatus() == 1) {
            // 背包的无源标签数量减一
            QueryWrapper<Backpack> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("backpack_id", rfidTag.getBackpackId());
            Backpack backpack = backpackMapper.selectOne(queryWrapper1);
            backpack.setRfidTagNum(backpack.getRfidTagNum() - 1);
            if (backpackMapper.updateById(backpack) == 0) {
                throw new MyException(EnumExceptionType.UPDATE_FAILED);
            }

            // 记录无源标签的操作
            rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 3, rfidTag.getItemName(), null,
                    "删除标签 { " + rfidTagId + " } 并移除物品 { " + rfidTag.getItemName() + " }", LocalDateTime.now(), null);
        } else {
            // 记录无源标签的操作
            rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 5, rfidTag.getItemName(), null,
                    "删除标签 { " + rfidTagId + " }", LocalDateTime.now(), null);
        }

        return new RfidTagResponse(rfidTag);
    }

    /**
     * 加入一个物品（无源标签）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @Override
    public RfidTagResponse addRfidTagItem(String rfidTagId) throws MyException {

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

        // 更新无源标签
        rfidTag.setRfidTagStatus(1);
        if (rfidTagMapper.updateById(rfidTag) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 背包的无源标签数量加一
        QueryWrapper<Backpack> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("backpack_id", rfidTag.getBackpackId());
        Backpack backpack = backpackMapper.selectOne(queryWrapper1);
        backpack.setRfidTagNum(backpack.getRfidTagNum() + 1);
        if (backpackMapper.updateById(backpack) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 记录无源标签的操作
        rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 1, null, rfidTag.getItemName(),
                "增加物品 { " + rfidTag.getItemName() + " }", LocalDateTime.now(), null);

        return new RfidTagResponse(rfidTag);
    }

    /**
     * 移除一个物品（无源标签）
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @Override
    public RfidTagResponse removeRfidTagItem(String rfidTagId) throws MyException {

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

        // 更新无源标签
        rfidTag.setRfidTagStatus(0);
        if (rfidTagMapper.updateById(rfidTag) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 背包的无源标签数量减一
        QueryWrapper<Backpack> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("backpack_id", rfidTag.getBackpackId());
        Backpack backpack = backpackMapper.selectOne(queryWrapper1);
        backpack.setRfidTagNum(backpack.getRfidTagNum() - 1);
        if (backpackMapper.updateById(backpack) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 记录无源标签的操作
        rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 4, rfidTag.getItemName(), null,
                "移除物品 { " + rfidTag.getItemName() + " }", LocalDateTime.now(), null);

        return new RfidTagResponse(rfidTag);
    }

    /**
     * 更新一个无源标签的物品名称
     * @param rfidTagId
     * @param itemName
     * @return RfidTagResponse
     * @throws MyException
     */
    @Override
    public RfidTagResponse updateRfidTagItemName(String rfidTagId, String itemName) throws MyException {

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

        // 获取原物品名称
        String oldItemName = rfidTag.getItemName();

        // 更新无源标签
        rfidTag.setItemName(itemName);
        if (rfidTagMapper.updateById(rfidTag) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 记录无源标签的操作
        rfidTagActionService.insertRfidTagAction(rfidTagId, sessionUtils.getUserId(), 2, oldItemName, itemName,
                "更新物品 { " + oldItemName + " } 为 { " + itemName + " }", LocalDateTime.now(), null);

        return new RfidTagResponse(rfidTag);
    }

    /**
     * 获取一个无源标签（物品）的信息
     * @param rfidTagId
     * @return RfidTagResponse
     * @throws MyException
     */
    @Override
    public RfidTagResponse getRfidTag(String rfidTagId) throws MyException {

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

        return new RfidTagResponse(rfidTag);
    }

//    public Optional<RfidTag> findById(Integer rfidtagId) {
//        return rfidTagMapper.findById(rfidtagId);
//    }
//
//    public RfidTag save(RfidTag rfidTag) {
//        return rfidTagMapper.save(rfidTag);
//    }
}
