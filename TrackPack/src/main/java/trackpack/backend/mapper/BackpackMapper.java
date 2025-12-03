package trackpack.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.jdbc.repository.query.Query;
import trackpack.backend.entity.Backpack;

import java.util.List;

@Mapper
public interface BackpackMapper extends BaseMapper<Backpack> {
//    List<Backpack> findByUser_UserID(Integer userID);
//    //定义了一个自定义查询方法 findByUser_UserID，用于查找与指定用户 ID 关联的所有 Backpack 实体。
//    //List<Backpack> 表示返回值类型是一个 Backpack 对象的列表。
//    //方法名 findByUser_UserID 遵循 Spring Data JPA 的查询方法命名约定，其中 user 是 Backpack 实体类中关联的 user 对象，UserID 是 user 实体的一个属性。
//    //Integer userID 是方法的参数，表示用户的 ID。

    // MyBatis-Plus自定义SQL：根据 backpackId 将对应的背包的 deleteTime 设置为 null
    @Update("update backpack set delete_time = null where backpack_id = #{backpackId}")
    void recoverBackpack(String backpackId);
}
