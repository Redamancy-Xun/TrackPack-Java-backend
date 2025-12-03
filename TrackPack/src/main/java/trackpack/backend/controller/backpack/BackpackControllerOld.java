//package trackpack.backend.controller.backpack;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import trackpack.backend.dto.ActivateBackpackRequest;
//import trackpack.backend.dto.DeactivateBackpackRequest;
//import trackpack.backend.entity.Backpack;
//import trackpack.backend.entity.RfidTag;
//import trackpack.backend.entity.User;
//import trackpack.backend.service.Impl.BackpackServiceImpl;
//import trackpack.backend.service.Impl.RfidTagServiceImpl;
//import trackpack.backend.service.Impl.UserServiceImpl;
//
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/backpacks")
//public class BackpackController {
//    private static final Logger logger = LoggerFactory.getLogger(BackpackController.class);
//
//    @Autowired
//    private BackpackServiceImpl backpackServiceImpl;
//
//    @Autowired
//    private UserServiceImpl userServiceImpl;
//
//    @Autowired
//    private RfidTagServiceImpl rfidTagServiceImpl;
//
//    @PostMapping("/activate")
//    public String activateBackpack(@RequestBody ActivateBackpackRequest request) {
//        Optional<User> userOptional = userServiceImpl.findById(request.getUserId());
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            Optional<Backpack> backpackOptional = backpackServiceImpl.activateBackpack(request.getBackpackId(), user);
//            if (backpackOptional.isPresent()) {
//                return "Backpack activated successfully!";
//            } else {
//                return "Backpack is already activated by another user.";
//            }
//        }
//        return "user not found.";
//    }
//
//    @PostMapping("/deactivate")
//    public String deactivateBackpack(@RequestBody DeactivateBackpackRequest request) {
//        Optional<User> userOptional = userServiceImpl.findById(request.getUserId());
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            Optional<Backpack> backpackOptional = backpackServiceImpl.deactivateBackpack(request.getBackpackId(), user);
//            if (backpackOptional.isPresent()) {
//                return "Backpack deactivated successfully!";
//            } else {
//                return "Unauthorized to deactivate or Backpack not found.";
//            }
//        }
//        return "user not found.";
//    }
//
//
//    @PostMapping("/register")
//    public Backpack registerBackpack(@RequestBody Backpack backpack) {
//        Integer userId = backpack.getUserId(); // 从 Backpack 对象中获取 userId
//        return backpackServiceImpl.registerBackpack(backpack, userId);
//    }
//
//    @PostMapping("/query")
//    public Optional<Backpack> queryBackpack(@RequestBody Map<String, Integer> request) {
//        Integer backpackId = request.get("backpack_id");
//        return backpackServiceImpl.findById(backpackId);
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<?> updateBackpack(@RequestBody Map<String, Object> request) {
//        Integer backpackId = (Integer) request.get("backpack_id");
//        Integer userId = (Integer) request.get("user_id");
//        Integer newUserId = (Integer) request.get("new_user_id");
//
//        Optional<Backpack> backpackOptional = backpackServiceImpl.findById(backpackId);
//        if (backpackOptional.isEmpty()) {
//            return ResponseEntity.status(404).body("Backpack not found.");
//        }
//
//        Backpack backpack = backpackOptional.get();
//        if (!backpack.getUserId().equals(userId)) {
//            return ResponseEntity.status(403).body("Unauthorized to update this backpack.");
//        }
//
//
//        // 处理所有权转移
//        if (newUserId != null && !newUserId.equals(userId)) {
//            Optional<User> newUserOptional = userServiceImpl.findById(newUserId);
//            if (newUserOptional.isPresent()) {
//                backpack.setUser(newUserOptional.get());
//            } else {
//                return ResponseEntity.status(404).body("未找到新用户。");
//            }
//        }
//
//        // 捕获旧值
//        Map<String, Object> oldValues = new HashMap<>();
//        request.forEach((key, value) -> {
//            oldValues.put(key, getCurrentValue(backpack, key));
//        });
//
//        // 动态更新属性
//        request.forEach((key, value) -> {
//            try {
//                switch (key) {
//                    case "backpack_name":
//                        backpack.setBackpackName((String) value);
//                        break;
//                    case "module_battery":
//                        backpack.setModuleBattery(Float.parseFloat(value.toString()));
//                        break;
//                    case "activation_time":
//                        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
//                        LocalDateTime localDateTime = LocalDateTime.parse((String) value, formatter);
//                        Date activationTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
//                        backpack.setActivationTime(activationTime);
//                        break;
//                    case "location":
//                        backpack.setLocation((String) value);
//                        break;
//                    case "network_status":
//                        backpack.setNetworkStatus((String) value);
//                        break;
//                    case "mac_address":
//                        backpack.setMacAddress((String) value);
//                        break;
//                    case "ip_address":
//                        backpack.setIpAddress((String) value);
//                        break;
//                    case "delete_flag":
//                        backpack.setDeleteflag((Integer) value);
//                        break;
//                    case "activate_flag":
//                        backpack.setActivatedFlag((Integer) value);
//                        break;
//                    default:
//                        break;
//                }
//            } catch (Exception e) {
//                // 处理解析异常
//                e.printStackTrace();
//            }
//        });
//
//        // 保存更新后的背包，并记录通知
//        backpackServiceImpl.save(backpack, oldValues);
//        return ResponseEntity.ok("Backpack updated successfully.");
//    }
//
//    private Object getCurrentValue(Backpack backpack, String key) {
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
//
//    @PostMapping("/items/ids")
//    public List<Object> getBackpackItemIds(@RequestBody Map<String, Integer> request) {
//        Integer backpackId = request.get("backpackId");
//        Optional<Backpack> backpackOptional = backpackServiceImpl.findById(backpackId);
//        if (backpackOptional.isPresent()) {
//            Backpack backpack = backpackOptional.get();
//            Set<Integer> itemIds = backpack.getItems().stream()
//                    .map(RfidTag::getRfidtagId)
//                    .collect(Collectors.toSet());
//
//            // 格式化响应
//            List<Object> formattedResponse = new ArrayList<>();
//            /*formattedResponse.add("该背包中的物品ID有：");*/
//            formattedResponse.addAll(itemIds);
//            return formattedResponse;
//        }
//        return Collections.singletonList("未找到ID为: " + backpackId + " 的背包");
//    }
//
//    @PostMapping("/items/details")
//    public ResponseEntity<?> getBackpackItemDetails(@RequestBody Map<String, Integer> request) {
//        Integer backpackId = request.get("backpackId");
//        Optional<Backpack> backpackOptional = backpackServiceImpl.findById(backpackId);
//        if (backpackOptional.isPresent()) {
//            Backpack backpack = backpackOptional.get();
//            List<RfidTag> rfidTags = backpack.getItems().stream()
//                    .sorted(Comparator.comparing(RfidTag::getRfidtagId))  // 按照 rfidtagId 升序排序
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(rfidTags);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("未找到ID为: " + backpackId + " 的背包");
//    }
//}
