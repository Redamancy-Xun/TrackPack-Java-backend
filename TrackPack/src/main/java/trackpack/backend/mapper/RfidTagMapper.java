package trackpack.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import trackpack.backend.entity.RfidTag;

@Mapper
public interface RfidTagMapper extends BaseMapper<RfidTag> {

    // MyBatis-Plus自定义SQL：根据 rfidTagId 将对应的无源标签的 deleteTime 设置为 null
    @Update("UPDATE rfid_tag SET delete_time = null WHERE rfid_tag_id = #{rfidTagId}")
    void recoverRfidTagByRfidTagId(String rfidTagId);
}
