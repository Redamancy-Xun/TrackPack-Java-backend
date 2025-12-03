//package trackpack.backend.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import trackpack.backend.entity.History;
//import trackpack.backend.service.HistoryService;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/history")
//public class HistoryController {
//
//    @Autowired
//    private HistoryService historyService;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> createHistory(@RequestBody Map<String, Object> request) {
//        try {
//            Integer backpackId = (Integer) request.get("backpack_id");
//            Integer rfidtagId = (Integer) request.get("rfidtag_id");
//            String locationChange = (String) request.get("location_change");
//            String userAction = (String) request.get("user_action");
//
//            History history = historyService.createHistory(backpackId, rfidtagId, locationChange, userAction);
//
//            return ResponseEntity.ok(history);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(404).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("An error occurred while creating the history record.");
//        }
//    }
//}
