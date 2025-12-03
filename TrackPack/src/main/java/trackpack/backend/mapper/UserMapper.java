// src/main/java/undestiny/demo20240726/mapper/UserMapper.java
package trackpack.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;
import trackpack.backend.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
//    user findByUsername(String username);

    // MyBatis-Plus自定义SQL：根据 userId 将对应的用户的 password、contact_info 设置为 null
    @Update("update user set password = null, contact_info = null where id = #{userId}")
    void clearSensitiveInfoById(String userId);
}
