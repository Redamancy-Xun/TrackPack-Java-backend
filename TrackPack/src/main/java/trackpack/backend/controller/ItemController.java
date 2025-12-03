package trackpack.backend.controller;

import trackpack.backend.entity.Backpack;
import trackpack.backend.entity.Item;
import trackpack.backend.service.BackpackService;
import trackpack.backend.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    private BackpackService backpackService;

    @PostMapping("/register")
    public ResponseEntity<?> registerItem(@RequestBody Item item) {
        logger.info("Received request to register item: " + item);

        if (item.getRfidtagId() == null) {
            return ResponseEntity.status(400).body("rfidtag_id is required and must be provided.");
        }

        // Check if an item with the same rfidtag_id already exists
        Optional<Item> existingItem = itemService.findById(item.getRfidtagId());
        if (existingItem.isPresent()) {
            return ResponseEntity.status(409).body("Item with rfidtag_id " + item.getRfidtagId() + " is already registered.");
        }

        Integer backpackId = item.getBackpackId();

        try {
            if (backpackId != null) {
                Optional<Backpack> backpackOptional = backpackService.findById(backpackId);
                if (backpackOptional.isPresent()) {
                    item.setBackpack(backpackOptional.get());
                } else {
                    return ResponseEntity.status(404).body("Backpack not found with ID: " + backpackId);
                }
            } else {
                item.setBackpack(null);
            }

            Item savedItem = itemService.save(item);
            logger.info("Item saved successfully: " + savedItem);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            logger.error("Error occurred while registering item: " + e.getMessage(), e);
            return ResponseEntity.status(500).body("An error occurred while registering the item.");
        }
    }



    @PostMapping("/query")
    public Optional<Item> queryItem(@RequestBody Map<String, Integer> request) {
        Integer rfidtagId = request.get("rfidtag_id");
        return itemService.findById(rfidtagId);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateItem(@RequestBody Map<String, Object> request) {
        Integer rfidtagId = (Integer) request.get("rfidtag_id");
        Integer backpackId = (Integer) request.get("backpack_id");

        Optional<Item> itemOptional = itemService.findById(rfidtagId);
        if (itemOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Item not found.");
        }

        Item item = itemOptional.get();

        if (item.getBackpackId() == null) {
            // 当前没有关联的backpack，允许更新
            if (backpackId != null) {
                Optional<Backpack> newBackpackOptional = backpackService.findById(backpackId);
                if (newBackpackOptional.isPresent()) {
                    item.setBackpack(newBackpackOptional.get());
                } else {
                    return ResponseEntity.status(404).body("Backpack not found.");
                }
            }
        } else {
            // 当前有关联的backpack，只有当前backpack_id的请求可以更新
            if (!item.getBackpackId().equals(backpackId)) {
                return ResponseEntity.status(403).body("Unauthorized to update this item.");
            } else {
                Optional<Backpack> newBackpackOptional = backpackService.findById(backpackId);
                if (newBackpackOptional.isPresent()) {
                    item.setBackpack(newBackpackOptional.get());
                } else {
                    return ResponseEntity.status(404).body("Backpack not found.");
                }
            }
        }

        // 如果请求中包含新的 backpack_id，则进行转移
        if (request.containsKey("new_backpack_id")) {
            Integer newBackpackId = (Integer) request.get("new_backpack_id");
            if (newBackpackId != null) {
                Optional<Backpack> newBackpackOptional = backpackService.findById(newBackpackId);
                if (newBackpackOptional.isPresent()) {
                    Backpack newBackpack = newBackpackOptional.get();
                    item.setBackpack(newBackpack);
                } else {
                    return ResponseEntity.status(404).body("未找到新背包。");
                }
            } else {
                item.setBackpack(null); // 如果 new_backpack_id 为 null，则取消与任何 Backpack 的关联
            }
        }

        // 动态更新其他属性
        request.forEach((key, value) -> {
            try {
                switch (key) {
                    case "item_name":
                        item.setItemName((String) value);
                        break;
                    case "item_status":
                        item.setItemStatus((String) value);
                        break;
                    case "delete_flag":
                        item.setDeleteFlag((Integer) value);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                // 处理解析异常
                e.printStackTrace();
            }
        });

        // 保存更新后的Item
        itemService.save(item);
        return ResponseEntity.ok("Item updated successfully.");
    }
}
