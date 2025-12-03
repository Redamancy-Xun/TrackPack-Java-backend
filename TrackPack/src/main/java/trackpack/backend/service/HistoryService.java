//package trackpack.backend.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import trackpack.backend.entity.RfidTag;
//import trackpack.backend.mapper.RfidTagActionMapper;
//import trackpack.backend.entity.Backpack;
//import trackpack.backend.entity.History;
//
//import java.util.Date;
//
//@Service
//public class HistoryService {
//
//    @Autowired
//    private RfidTagActionMapper historyMapper;
//
//    @Autowired
//    private BackpackServiceImpl backpackServiceImpl;
//
//    public History createHistory(Integer backpackId, Integer rfidtagId, String locationChange, String userAction) {
//        Backpack backpack = backpackServiceImpl.findById(backpackId)
//                .orElseThrow(() -> new IllegalArgumentException("Backpack not found with ID: " + backpackId));
//
//        RfidTag rfidTag = backpack.getItems().stream()
//                .filter(i -> i.getRfidtagId().equals(rfidtagId))
//                .findFirst()
//                .orElseThrow(() -> new IllegalArgumentException("RfidTag not found with RFID tag ID: " + rfidtagId + " in the backpack."));
//
//        History history = new History();
//        history.setItem(rfidTag);
//        history.setLocationChange(locationChange);
//        history.setUserAction(userAction);
//        history.setTimestamp(new Date());
//
//        return historyMapper.save(history);
//    }
//}
