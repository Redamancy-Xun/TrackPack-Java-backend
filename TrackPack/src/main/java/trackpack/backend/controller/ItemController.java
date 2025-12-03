//package trackpack.backend.controller;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import trackpack.backend.entity.Backpack;
//import trackpack.backend.entity.RfidTag;
//import trackpack.backend.service.Impl.BackpackServiceImpl;
//import trackpack.backend.service.Impl.RfidTagServiceImpl;
//
//import java.util.Map;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/api/items")
//public class ItemController {
//
//    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
//
//    @Autowired
//    private RfidTagServiceImpl rfidTagServiceImpl;
//
//    @Autowired
//    private BackpackServiceImpl backpackServiceImpl;
//
//    @PostMapping("/register")
//    public ResponseEntity<?> registerItem(@RequestBody RfidTag rfidTag) {
//        logger.info("Received request to register rfidTag: " + rfidTag);
//
//        if (rfidTag.getRfidtagId() == null) {
//            return ResponseEntity.status(400).body("rfidtag_id is required and must be provided.");
//        }
//
//        // Check if an rfidTag with the same rfidtag_id already exists
//        Optional<RfidTag> existingItem = rfidTagServiceImpl.findById(rfidTag.getRfidtagId());
//        if (existingItem.isPresent()) {
//            return ResponseEntity.status(409).body("RfidTag with rfidtag_id " + rfidTag.getRfidtagId() + " is already registered.");
//        }
//
//        Integer backpackId = rfidTag.getBackpackId();
//
//        try {
//            if (backpackId != null) {
//                Optional<Backpack> backpackOptional = backpackServiceImpl.findById(backpackId);
//                if (backpackOptional.isPresent()) {
//                    rfidTag.setBackpack(backpackOptional.get());
//                } else {
//                    return ResponseEntity.status(404).body("Backpack not found with ID: " + backpackId);
//                }
//            } else {
//                rfidTag.setBackpack(null);
//            }
//
//            RfidTag savedRfidTag = rfidTagServiceImpl.save(rfidTag);
//            logger.info("RfidTag saved successfully: " + savedRfidTag);
//            return ResponseEntity.ok(savedRfidTag);
//        } catch (Exception e) {
//            logger.error("Error occurred while registering rfidTag: " + e.getMessage(), e);
//            return ResponseEntity.status(500).body("An error occurred while registering the rfidTag.");
//        }
//    }
//
//
//
//    @PostMapping("/query")
//    public Optional<RfidTag> queryItem(@RequestBody Map<String, Integer> request) {
//        Integer rfidtagId = request.get("rfidtag_id");
//        return rfidTagServiceImpl.findById(rfidtagId);
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<?> updateItem(@RequestBody Map<String, Object> request) {
//        Integer rfidtagId = (Integer) request.get("rfidtag_id");
//        Integer backpackId = (Integer) request.get("backpack_id");
//
//        Optional<RfidTag> itemOptional = rfidTagServiceImpl.findById(rfidtagId);
//        if (itemOptional.isEmpty()) {
//            return ResponseEntity.status(404).body("RfidTag not found.");
//        }
//
//        RfidTag rfidTag = itemOptional.get();
//
//        if (rfidTag.getBackpackId() == null) {
//            // 当前没有关联的backpack，允许更新
//            if (backpackId != null) {
//                Optional<Backpack> newBackpackOptional = backpackServiceImpl.findById(backpackId);
//                if (newBackpackOptional.isPresent()) {
//                    rfidTag.setBackpack(newBackpackOptional.get());
//                } else {
//                    return ResponseEntity.status(404).body("Backpack not found.");
//                }
//            }
//        } else {
//            // 当前有关联的backpack，只有当前backpack_id的请求可以更新
//            if (!rfidTag.getBackpackId().equals(backpackId)) {
//                return ResponseEntity.status(403).body("Unauthorized to update this rfidTag.");
//            } else {
//                Optional<Backpack> newBackpackOptional = backpackServiceImpl.findById(backpackId);
//                if (newBackpackOptional.isPresent()) {
//                    rfidTag.setBackpack(newBackpackOptional.get());
//                } else {
//                    return ResponseEntity.status(404).body("Backpack not found.");
//                }
//            }
//        }
//
//        // 如果请求中包含新的 backpack_id，则进行转移
//        if (request.containsKey("new_backpack_id")) {
//            Integer newBackpackId = (Integer) request.get("new_backpack_id");
//            if (newBackpackId != null) {
//                Optional<Backpack> newBackpackOptional = backpackServiceImpl.findById(newBackpackId);
//                if (newBackpackOptional.isPresent()) {
//                    Backpack newBackpack = newBackpackOptional.get();
//                    rfidTag.setBackpack(newBackpack);
//                } else {
//                    return ResponseEntity.status(404).body("未找到新背包。");
//                }
//            } else {
//                rfidTag.setBackpack(null); // 如果 new_backpack_id 为 null，则取消与任何 Backpack 的关联
//            }
//        }
//
//        // 动态更新其他属性
//        request.forEach((key, value) -> {
//            try {
//                switch (key) {
//                    case "item_name":
//                        rfidTag.setItemName((String) value);
//                        break;
//                    case "item_status":
//                        rfidTag.setItemStatus((String) value);
//                        break;
//                    case "delete_flag":
//                        rfidTag.setDeleteFlag((Integer) value);
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
//        // 保存更新后的Item
//        rfidTagServiceImpl.save(rfidTag);
//        return ResponseEntity.ok("RfidTag updated successfully.");
//    }
//}
