package trackpack.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trackpack.backend.entity.RfidTag;
import trackpack.backend.mapper.ItemMapper;
import trackpack.backend.entity.Backpack;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class RfidTagServiceImpl {
    private static final Logger logger = Logger.getLogger(RfidTagServiceImpl.class.getName());

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private BackpackServiceImpl backpackServiceImpl;

    public RfidTag registerItem(RfidTag rfidTag, Integer backpackId) {
        logger.info("Registering rfidTag: " + rfidTag);

        if (backpackId != null) {
            Optional<Backpack> backpackOptional = backpackServiceImpl.findById(backpackId);
            if (backpackOptional.isPresent()) {
                rfidTag.setBackpack(backpackOptional.get());
            } else {
                logger.severe("Backpack not found with ID: " + backpackId);
                throw new IllegalArgumentException("Backpack not found with ID: " + backpackId);
            }
        } else {
            rfidTag.setBackpack(null); // 确保在backpackId为空时，不会尝试关联Backpack
            rfidTag.setBackpackId(null);
        }
        return itemMapper.save(rfidTag);
    }

    public Optional<RfidTag> findById(Integer rfidtagId) {
        return itemMapper.findById(rfidtagId);
    }

    public RfidTag save(RfidTag rfidTag) {
        return itemMapper.save(rfidTag);
    }
}
