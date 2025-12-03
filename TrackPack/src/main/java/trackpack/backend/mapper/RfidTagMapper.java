package trackpack.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import trackpack.backend.entity.RfidTag;

@Mapper
public interface ItemMapper extends BaseMapper<RfidTag> {
}
