// src/main/java//demo20240726/service/UserServiceImpl.java
package trackpack.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import trackpack.backend.mapper.UserMapper;
import trackpack.backend.entity.User;

import java.util.Optional;
@Service
public class UserServiceImpl {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.save(user);
    }

    public User findByUsername(String username) {

        return userMapper.findByUsername(username);
    }

    public Optional<User> findById(Integer userId) {
        return userMapper.findById(userId); // 确保 findById 方法在实例上下文中调用
    }
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
