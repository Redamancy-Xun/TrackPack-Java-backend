package trackpack.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
//import trackpack.backend.entity.History;
import trackpack.backend.entity.RfidTagAction;

@Mapper
public interface RfidTagActionMapper extends BaseMapper<RfidTagAction> {
}
