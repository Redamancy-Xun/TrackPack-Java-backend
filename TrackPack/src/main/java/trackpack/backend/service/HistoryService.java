package trackpack.backend.service;

import trackpack.backend.dao.HistoryRepository;
import trackpack.backend.entity.History;
import trackpack.backend.entity.Item;
import trackpack.backend.entity.Backpack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private BackpackService backpackService;

    public History createHistory(Integer backpackId, Integer rfidtagId, String locationChange, String userAction) {
        Backpack backpack = backpackService.findById(backpackId)
                .orElseThrow(() -> new IllegalArgumentException("Backpack not found with ID: " + backpackId));

        Item item = backpack.getItems().stream()
                .filter(i -> i.getRfidtagId().equals(rfidtagId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found with RFID tag ID: " + rfidtagId + " in the backpack."));

        History history = new History();
        history.setItem(item);
        history.setLocationChange(locationChange);
        history.setUserAction(userAction);
        history.setTimestamp(new Date());

        return historyRepository.save(history);
    }
}
