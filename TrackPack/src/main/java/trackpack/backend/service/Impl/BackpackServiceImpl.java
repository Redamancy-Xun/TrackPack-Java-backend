package trackpack.backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trackpack.backend.common.Page;
import trackpack.backend.controller.backpack.response.BackpackResponse;
import trackpack.backend.controller.rfidtag.response.RfidTagResponse;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.exception.EnumExceptionType;
import trackpack.backend.exception.MyException;
import trackpack.backend.mapper.BackpackMapper;
//import trackpack.backend.mapper.RecordMapper;
import trackpack.backend.entity.Backpack;
//import trackpack.backend.entity.Record;
import trackpack.backend.entity.User;
import trackpack.backend.mapper.RfidTagMapper;
import trackpack.backend.service.BackpackService;
import trackpack.backend.service.RfidTagService;
import trackpack.backend.util.SessionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
public class BackpackServiceImpl implements BackpackService {
//    private static final Logger logger = Logger.getLogger(BackpackServiceImpl.class.getName());

    @Autowired
    private BackpackMapper backpackMapper;

//    private final RecordMapper recordMapper;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private SessionUtils sessionUtils;

    @Autowired
    private RfidTagService rfidTagService;

    @Autowired
    private RfidTagMapper rfidTagMapper;

//    // 使用构造函数注入
//    @Autowired
//    public BackpackServiceImpl(
//            @Lazy BackpackMapper backpackMapper,
//            RecordMapper recordMapper,
//            UserServiceImpl userServiceImpl
//    ) {
//        this.backpackMapper = backpackMapper;
//        this.recordMapper = recordMapper;
//        this.userServiceImpl = userServiceImpl;
//    }

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
    @Override
    public BackpackResponse activateBackpack(String backpackId, String backpackName, Double backpackBattery,
                                             String backpackIpAddress, String backpackMacAddress, String backpackLocation) throws MyException {

        //        logger.info("Activating backpack with ID: " + backpackId + " for user: " + user.getUserID());
//        Optional<Backpack> backpack = backpackMapper.findById(backpackId);
//        if (backpack.isPresent()) {
//            Backpack bp = backpack.get();
//            if (bp.getActivatedFlag() == 0) {
//                bp.setActivatedFlag(1);
//                bp.setUser(user); // 设置user，并更新userId
//                logger.info("Backpack activated: " + bp);
//
//                // 记录激活通知
//                Record record = new Record();
//                record.setUserId(user.getUserID());
//                record.setNotificationContent("Backpack activated: ID = " + backpackId);
//                record.setTimeStamp(new Date());
//                recordMapper.save(record);
//
//                return Optional.of(backpackMapper.save(bp));
//            } else {
//                logger.warning("Backpack is already activated by another user: " + bp.getUser().getUserID());
//            }
//        } else {
//            logger.warning("Backpack not found with ID: " + backpackId);
//        }
//        return Optional.empty();

        sessionUtils.refreshData(null);

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) { // 数据库中不存在该背包
            // 激活背包并插入数据库
            Backpack newBackpack = Backpack.builder()
                    .backpackId(backpackId)
                    .backpackName(backpackName)
                    .backpackBattery(backpackBattery)
                    .activationTime(LocalDateTime.now())
                    .deleteTime(null)
                    .ipAddress(backpackIpAddress)
                    .networkStatus(0)
                    .macAddress(backpackMacAddress)
                    .location(backpackLocation)
                    .rfidTagNum(0)
                    .userId(sessionUtils.getUserId())
                    .build();

            if (backpackMapper.insert(newBackpack) == 0) {
                throw new MyException(EnumExceptionType.INSERT_FAILED);
            }

            return new BackpackResponse(newBackpack);
        } else if (backpack.getDeleteTime() == null) { // 数据库中存在该背包，但已被绑定
            // 返回背包已被绑定
            throw new MyException(EnumExceptionType.BACKPACK_IS_BIND);
        } else { // 数据库中存在该背包，且未被绑定
            // 更新背包信息
            backpack.setBackpackName(backpackName);
            backpack.setBackpackBattery(backpackBattery);
            backpack.setActivationTime(LocalDateTime.now());
            backpack.setDeleteTime(null);
            backpack.setIpAddress(backpackIpAddress);
            backpack.setNetworkStatus(0);
            backpack.setMacAddress(backpackMacAddress);
            backpack.setLocation(backpackLocation);
            backpack.setRfidTagNum(0);
            backpack.setUserId(sessionUtils.getUserId());
            if (backpackMapper.updateById(backpack) == 0) {
                throw new MyException(EnumExceptionType.UPDATE_FAILED);
            }

            backpackMapper.recoverBackpack(backpackId);

            return new BackpackResponse(backpack);
        }
    }


    /**
     * 停用背包
     * @param backpackId
     * @return BackpackResponse
     * @throws MyException
     */
    @Override
    public BackpackResponse deleteBackpack(String backpackId) {
//        logger.info("Deactivating backpack with ID: " + backpackId);
//        Optional<Backpack> backpack = backpackMapper.findById(backpackId);
//        if (backpack.isPresent()) {
//            Backpack bp = backpack.get();
//            logger.info("Before deactivating: userId = " + bp.getUserId());
//            if (bp.getActivatedFlag() == 1 && bp.getUser().equals(user)) {
//                bp.setActivatedFlag(0);
//                bp.setUser(null);
//                logger.info("After deactivating: userId = " + bp.getUserId());
//
//                // 记录停用通知
//                Record record = new Record();
//                record.setUserId(user.getUserID());
//                record.setNotificationContent("Backpack deactivated: ID = " + backpackId);
//                record.setTimeStamp(new Date());
//                recordMapper.save(record);
//
//                return Optional.of(backpackMapper.save(bp));
//            } else {
//                logger.warning("Unauthorized to deactivate or Backpack not found: " + backpackId);
//            }
//        } else {
//            logger.warning("Backpack not found with ID: " + backpackId);
//        }
//        return Optional.empty();

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

        // 停用背包
        backpack.setDeleteTime(LocalDateTime.now());
        if (backpackMapper.update(backpack, queryWrapper) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        // 删除背包内部的所有RFID标签
        QueryWrapper<RfidTag> rfidTagQueryWrapper = new QueryWrapper<>();
        rfidTagQueryWrapper.eq("backpack_id", backpackId);
        rfidTagQueryWrapper.isNull("delete_time");
        List<RfidTag> rfidTags = rfidTagMapper.selectList(rfidTagQueryWrapper);
        for (RfidTag rfidTag : rfidTags) {
            rfidTagService.deleteRfidTag(rfidTag.getRfidTagId());
        }

        return new BackpackResponse(backpack);
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
    @Override
    public BackpackResponse updateBackpack(String backpackId, String backpackName, Double backpackBattery,
                                           String backpackLocation, Integer backpackNetworkStatus) throws MyException {

        sessionUtils.refreshData(null);

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) {
            // 合成一个EnumExceptionType信息;
            throw new MyException(EnumExceptionType.BACKPACK_NOT_EXIST);
        } else if (backpack.getDeleteTime() != null) { // 数据库中存在该背包，但已被停用
            throw new MyException(EnumExceptionType.BACKPACK_IS_DISABLED);
        }

        // 更新背包信息
        if (backpackName != null) {
            backpack.setBackpackName(backpackName);
        }
        if (backpackBattery != null) {
            backpack.setBackpackBattery(backpackBattery);
        }
        if (backpackLocation != null) {
            backpack.setLocation(backpackLocation);
        }
        if (backpackNetworkStatus != null) {
            backpack.setNetworkStatus(backpackNetworkStatus);
        }
        if (backpackMapper.updateById(backpack) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        return new BackpackResponse(backpack);
    }

    /**
     * 更新背包的某一个属性
     * @param backpackId
     * @param key (backpackName: 背包名称, backpackBattery: 背包电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态)
     * @param value
     * @return BackpackResponse
     * @throws MyException
     */
    @Override
    public BackpackResponse updateBackpackAttribute(String backpackId, String key, Object value) throws MyException {

        sessionUtils.refreshData(null);

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) {
            // 合成一个EnumExceptionType信息;
            throw new MyException(EnumExceptionType.BACKPACK_NOT_EXIST);
        } else if (backpack.getDeleteTime() != null) { // 数据库中存在该背包，但已被停用
            throw new MyException(EnumExceptionType.BACKPACK_IS_DISABLED);
        }

        // 更新背包信息
        switch (key) {
            case "backpackName":
                backpack.setBackpackName((String) value);
                break;
            case "backpackBattery":
                backpack.setBackpackBattery((Double) value);
                break;
            case "backpackLocation":
                backpack.setLocation((String) value);
                break;
            case "backpackNetworkStatus":
                backpack.setNetworkStatus((Integer) value);
                break;
            default:
                throw new MyException(EnumExceptionType.INVALID_ATTRIBUTE);
        }

        if (backpackMapper.update(backpack, queryWrapper) == 0) {
            throw new MyException(EnumExceptionType.UPDATE_FAILED);
        }

        return new BackpackResponse(backpack);
    }

    /**
     * 查询背包信息
     * @param backpackId
     * @return BackpackResponse
     * @throws MyException
     */
    @Override
    public BackpackResponse queryBackpack(String backpackId) throws MyException {

        sessionUtils.refreshData(null);

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) {
            // 合成一个EnumExceptionType信息;
            throw new MyException(EnumExceptionType.BACKPACK_NOT_EXIST);
        } else if (backpack.getDeleteTime() != null) { // 数据库中存在该背包，但已被停用
            throw new MyException(EnumExceptionType.BACKPACK_IS_DISABLED);
        }

        return new BackpackResponse(backpack);
    }

    /**
     * 查询背包的某个属性
     * @param backpackId
     * @param key (backpackName: 背包名称, backpackBattery: 背包电量, backpackLocation: 位置, backpackNetworkStatus: 网络状态, backpackActivationTime: 激活时间, backpackIpAddress: IP地址, backpackMacAddress: MAC地址, backpackUserId: 用户ID, backpackRfidTagNum: RFID标签数量)
     * @return Object
     * @throws MyException
     */
    @Override
    public Object queryBackpackAttribute(String backpackId, String key) throws MyException {

        sessionUtils.refreshData(null);

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) {
            // 合成一个EnumExceptionType信息;
            throw new MyException(EnumExceptionType.BACKPACK_NOT_EXIST);
        } else if (backpack.getDeleteTime() != null) { // 数据库中存在该背包，但已被停用
            throw new MyException(EnumExceptionType.BACKPACK_IS_DISABLED);
        }

        // 查询背包信息
        Object result = null;
        switch (key) {
            case "backpackName":
                result = backpack.getBackpackName();
                break;
            case "backpackBattery":
                result = backpack.getBackpackBattery();
                break;
            case "backpackLocation":
                result = backpack.getLocation();
                break;
            case "backpackNetworkStatus":
                result = backpack.getNetworkStatus();
                break;
            case "backpackActivationTime":
                result = backpack.getActivationTime();
                break;
            case "backpackIpAddress":
                result = backpack.getIpAddress();
                break;
            case "backpackMacAddress":
                result = backpack.getMacAddress();
                break;
            case "backpackUserId":
                result = backpack.getUserId();
                break;
            case "backpackRfidTagNum":
                result = backpack.getRfidTagNum();
                break;
            default:
                throw new MyException(EnumExceptionType.INVALID_ATTRIBUTE);
        }
        return result;
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
    @Override
    public Page<RfidTagResponse> queryBackpackRfidTags(String backpackId, Boolean orderByCreateTimeDesc, Integer page, Integer pageSize) throws MyException {
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

        // 查找当前背包是否在数据库中
        QueryWrapper<Backpack> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backpack_id", backpackId);
        Backpack backpack = backpackMapper.selectOne(queryWrapper);

        if (backpack == null) {
            throw new MyException(EnumExceptionType.BACKPACK_NOT_EXIST);
        } else if (backpack.getDeleteTime() != null) { // 数据库中存在该背包，但已被停用
            throw new MyException(EnumExceptionType.BACKPACK_IS_DISABLED);
        }

        // 查询背包的所有无源标签
        QueryWrapper<RfidTag> rfidTagQueryWrapper = new QueryWrapper<>();
        rfidTagQueryWrapper.eq("backpack_id", backpackId);
        rfidTagQueryWrapper.isNull("delete_time");
        rfidTagQueryWrapper.orderByDesc(orderByCreateTimeDesc, "activation_time");
        PageHelper.startPage(page, pageSize);
        Page<RfidTag> rfidTagPage = new Page<>(new PageInfo<>(rfidTagMapper.selectList(rfidTagQueryWrapper)));
        List<RfidTag> rfidTags = rfidTagPage.getItems();

        List<RfidTagResponse> rfidTagResponses = new ArrayList<>();
        for (RfidTag rfidTag : rfidTags) {
            rfidTagResponses.add(rfidTagService.getRfidTag(rfidTag.getRfidTagId()));
        }

        Page<RfidTagResponse> rfidTagResponsePage = new Page<>();
        rfidTagResponsePage.setTotal(rfidTagPage.getTotal());
        rfidTagResponsePage.setPageSize(rfidTagPage.getPageSize());
        rfidTagResponsePage.setTotal(rfidTagPage.getTotal());
        rfidTagResponsePage.setItems(rfidTagResponses);
        rfidTagResponsePage.setPages(rfidTagPage.getPages());

        return rfidTagResponsePage;
    }




//    public Backpack registerBackpack(Backpack backpack, Integer userId) {
//        logger.info("Registering backpack: " + backpack);
//
//        if (userId != null) {
//            Optional<User> userOptional = userServiceImpl.findById(userId);
//            if (userOptional.isPresent()) {
//                backpack.setUser(userOptional.get());
//            } else {
//                logger.severe("user not found with ID: " + userId);
//                throw new IllegalArgumentException("user not found with ID: " + userId);
//            }
//        } else {
//            backpack.setUser(null); // 不分配用户
//        }
//
//        return backpackMapper.save(backpack);
//    }

//    public Optional<Backpack> findById(Integer backpackId) {
//        return backpackMapper.findById(backpackId);
//    }

//    public Backpack save(Backpack backpack, Map<String, Object> oldValues) {
//        // 记录更新通知
//        Record record = new Record();
//        record.setUserId(backpack.getUserId());
//
//        StringBuilder content = new StringBuilder("Backpack updated: ID = " + backpack.getBackpackID() + "\n");
//
//        oldValues.forEach((key, oldValue) -> {
//            Object newValue = getNewValue(backpack, key);
//            if (oldValue != null || newValue != null) { // Check if both oldValue and newValue are not null
//                if (oldValue == null || newValue == null || !oldValue.equals(newValue)) { // Check if the values are different
//                    content.append(key).append(" changed from ")
//                            .append(oldValue == null ? "null" : oldValue)
//                            .append(" to ")
//                            .append(newValue == null ? "null" : newValue)
//                            .append("\n");
//                }
//            }
//        });
//
//        record.setNotificationContent(content.toString().trim()); // Use trim to remove any trailing newline
//        record.setTimeStamp(new Date());
//        recordMapper.save(record);
//
//        return backpackMapper.save(backpack);
//    }

//    /**
//     * 获取背包的属性值
//     * @param backpack
//     * @param key（）
//     * @return
//     */
//    private Object getNewValue(Backpack backpack, String key) {
//        switch (key) {
//            case "backpack_name":
//                return backpack.getBackpackName();
//            case "module_battery":
//                return backpack.getModuleBattery();
//            case "activation_time":
//                return backpack.getActivationTime();
//            case "location":
//                return backpack.getLocation();
//            case "network_status":
//                return backpack.getNetworkStatus();
//            case "mac_address":
//                return backpack.getMacAddress();
//            case "ip_address":
//                return backpack.getIpAddress();
//            case "delete_flag":
//                return backpack.getDeleteflag();
//            case "activate_flag":
//                return backpack.getActivatedFlag();
//            default:
//                return null;
//        }
//    }

//    public void saveRecord(User user, String content) {
//        Record record = new Record();
//        record.setUserId(user.getUserID());
//        record.setNotificationContent(content);
//        record.setTimeStamp(new Date());
//        recordMapper.save(record);
//    }

}


