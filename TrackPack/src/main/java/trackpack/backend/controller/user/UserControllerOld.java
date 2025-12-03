// src/main/java/undestiny/demo20240726/controller/UserController.java
package trackpack.backend.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trackpack.backend.entity.Backpack;
import trackpack.backend.entity.User;
import trackpack.backend.service.Impl.UserServiceImpl;

import java.util.*;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userServiceImpl.register(user);
    }
    //@PostMapping("/register") 注解将这个方法映射到 HTTP POST 请求 /register 路径。
    //@RequestBody 注解用于从请求体中获取 user 对象。
    //该方法调用 userService.register(user) 方法注册用户，并返回注册后的 user 对象。
    @PostMapping("/login")
    public User login(@RequestBody Map<String,Object> request) {
        //Object 可以使得值是任何对象，可以是字符串，也可以是整数，甚至可以是自定义的java对象
        Integer userID = (Integer) request.get("userID");
        String password = (String) request.get("password");
        // 获取 Optional<user> 对象并处理可能的空值
        User user = userServiceImpl.findById(userID)
                .orElseThrow(() -> new RuntimeException("user not found"));

        if (userServiceImpl.validatePassword(password, user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid user ID or password");
    }
    @PostMapping("/backpacks/ids")
    public List<Object> getUserBackpackIds(@RequestBody Map<String, Integer> request) {
        Integer userId = request.get("userId");
        Optional<User> userOptional = userServiceImpl.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Integer> backpackIds = user.getBackpacks().stream()
                    .map(backpack -> backpack.getBackpackID())
                    .collect(Collectors.toSet());

            // Format the response as you requested
            List<Object> formattedResponse = new ArrayList<>();
            /*formattedResponse.add("您的背包有：");*/
            formattedResponse.addAll(backpackIds);
            return formattedResponse;
        }
        throw new RuntimeException("user not found with ID: " + userId);
    }

    @PostMapping("/backpacks/details")
    public List<Backpack> getUserBackpackDetails(@RequestBody Map<String, Integer> request) {
        Integer userId = request.get("userId");
        Optional<User> userOptional = userServiceImpl.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getBackpacks().stream()
                    .sorted(Comparator.comparing(Backpack::getBackpackID))  // 按照 backpackID 升序排序
                    .collect(Collectors.toList());
        }
        throw new RuntimeException("user not found with ID: " + userId);
    }

}

