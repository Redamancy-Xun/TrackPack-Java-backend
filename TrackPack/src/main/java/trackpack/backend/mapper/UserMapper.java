// src/main/java/undestiny/demo20240726/mapper/UserRepository.java
package trackpack.backend.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trackpack.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
