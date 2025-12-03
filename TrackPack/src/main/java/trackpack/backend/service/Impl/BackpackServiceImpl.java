package trackpack.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trackpack.backend.mapper.BackpackMapper;
import trackpack.backend.mapper.RecordMapper;
import trackpack.backend.entity.Backpack;
import trackpack.backend.entity.Record;
import trackpack.backend.entity.User;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class BackpackServiceImpl {
    private static final Logger logger = Logger.getLogger(BackpackServiceImpl.class.getName());

    private final BackpackMapper backpackMapper;
    private final RecordMapper recordMapper;
    private final UserServiceImpl userServiceImpl;

    // 使用构造函数注入
    @Autowired
    public BackpackServiceImpl(
            @Lazy BackpackMapper backpackMapper,
            RecordMapper recordMapper,
            UserServiceImpl userServiceImpl
    ) {
        this.backpackMapper = backpackMapper;
        this.recordMapper = recordMapper;
        this.userServiceImpl = userServiceImpl;
    }

    public Optional<Backpack> activateBackpack(Integer backpackId, User user) {
        logger.info("Activating backpack with ID: " + backpackId + " for user: " + user.getUserID());
        Optional<Backpack> backpack = backpackMapper.findById(backpackId);
        if (backpack.isPresent()) {
            Backpack bp = backpack.get();
            if (bp.getActivatedFlag() == 0) {
                bp.setActivatedFlag(1);
                bp.setUser(user); // 设置user，并更新userId
                logger.info("Backpack activated: " + bp);

                // 记录激活通知
                Record record = new Record();
                record.setUserId(user.getUserID());
                record.setNotificationContent("Backpack activated: ID = " + backpackId);
                record.setTimeStamp(new Date());
                recordMapper.save(record);

                return Optional.of(backpackMapper.save(bp));
            } else {
                logger.warning("Backpack is already activated by another user: " + bp.getUser().getUserID());
            }
        } else {
            logger.warning("Backpack not found with ID: " + backpackId);
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<Backpack> deactivateBackpack(Integer backpackId, User user) {
        logger.info("Deactivating backpack with ID: " + backpackId);
        Optional<Backpack> backpack = backpackMapper.findById(backpackId);
        if (backpack.isPresent()) {
            Backpack bp = backpack.get();
            logger.info("Before deactivating: userId = " + bp.getUserId());
            if (bp.getActivatedFlag() == 1 && bp.getUser().equals(user)) {
                bp.setActivatedFlag(0);
                bp.setUser(null);
                logger.info("After deactivating: userId = " + bp.getUserId());

                // 记录停用通知
                Record record = new Record();
                record.setUserId(user.getUserID());
                record.setNotificationContent("Backpack deactivated: ID = " + backpackId);
                record.setTimeStamp(new Date());
                recordMapper.save(record);

                return Optional.of(backpackMapper.save(bp));
            } else {
                logger.warning("Unauthorized to deactivate or Backpack not found: " + backpackId);
            }
        } else {
            logger.warning("Backpack not found with ID: " + backpackId);
        }
        return Optional.empty();
    }

    public Backpack registerBackpack(Backpack backpack, Integer userId) {
        logger.info("Registering backpack: " + backpack);

        if (userId != null) {
            Optional<User> userOptional = userServiceImpl.findById(userId);
            if (userOptional.isPresent()) {
                backpack.setUser(userOptional.get());
            } else {
                logger.severe("User not found with ID: " + userId);
                throw new IllegalArgumentException("User not found with ID: " + userId);
            }
        } else {
            backpack.setUser(null); // 不分配用户
        }

        return backpackMapper.save(backpack);
    }

    public Optional<Backpack> findById(Integer backpackId) {
        return backpackMapper.findById(backpackId);
    }

    public Backpack save(Backpack backpack, Map<String, Object> oldValues) {
        // 记录更新通知
        Record record = new Record();
        record.setUserId(backpack.getUserId());

        StringBuilder content = new StringBuilder("Backpack updated: ID = " + backpack.getBackpackID() + "\n");

        oldValues.forEach((key, oldValue) -> {
            Object newValue = getNewValue(backpack, key);
            if (oldValue != null || newValue != null) { // Check if both oldValue and newValue are not null
                if (oldValue == null || newValue == null || !oldValue.equals(newValue)) { // Check if the values are different
                    content.append(key).append(" changed from ")
                            .append(oldValue == null ? "null" : oldValue)
                            .append(" to ")
                            .append(newValue == null ? "null" : newValue)
                            .append("\n");
                }
            }
        });

        record.setNotificationContent(content.toString().trim()); // Use trim to remove any trailing newline
        record.setTimeStamp(new Date());
        recordMapper.save(record);

        return backpackMapper.save(backpack);
    }

    private Object getNewValue(Backpack backpack, String key) {
        switch (key) {
            case "backpack_name":
                return backpack.getBackpackName();
            case "module_battery":
                return backpack.getModuleBattery();
            case "activation_time":
                return backpack.getActivationTime();
            case "location":
                return backpack.getLocation();
            case "network_status":
                return backpack.getNetworkStatus();
            case "mac_address":
                return backpack.getMacAddress();
            case "ip_address":
                return backpack.getIpAddress();
            case "delete_flag":
                return backpack.getDeleteflag();
            case "activate_flag":
                return backpack.getActivatedFlag();
            default:
                return null;
        }
    }

    public void saveRecord(User user, String content) {
        Record record = new Record();
        record.setUserId(user.getUserID());
        record.setNotificationContent(content);
        record.setTimeStamp(new Date());
        recordMapper.save(record);
    }

}
