package trackpack.backend.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackpack.backend.entity.Backpack;

import java.util.List;

@Repository
public interface BackpackRepository extends JpaRepository<Backpack, Integer> {
    List<Backpack> findByUser_UserID(Integer userID);
    //定义了一个自定义查询方法 findByUser_UserID，用于查找与指定用户 ID 关联的所有 Backpack 实体。
    //List<Backpack> 表示返回值类型是一个 Backpack 对象的列表。
    //方法名 findByUser_UserID 遵循 Spring Data JPA 的查询方法命名约定，其中 User 是 Backpack 实体类中关联的 User 对象，UserID 是 User 实体的一个属性。
    //Integer userID 是方法的参数，表示用户的 ID。
}
