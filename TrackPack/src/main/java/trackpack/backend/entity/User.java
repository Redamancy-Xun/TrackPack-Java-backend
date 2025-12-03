package trackpack.backend.entity;

import javax.persistence.*;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userID;

    @Column(name = "user_name")
    private String username;

    @Column(name = "Password")
    private String password;

    @Column(name = "contact_info")
    private String contactInfo;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference // 解决循环引用问题
    private Set<Backpack> backpacks;
    //@OneToMany(mappedBy = "user") 注解表示 User 与 Backpack 是一对多的关系，一个用户可以拥有多个背包。
    //mappedBy = "user" 指定 Backpack 实体中定义关系的一方是 user 属性。
    //private Set<Backpack> backpacks; 定义了一个 Backpack 对象的集合，表示该用户拥有的背包。

    // Getters and Setters

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Set<Backpack> getBackpacks() {
        return backpacks;
    }

    public void setBackpacks(Set<Backpack> backpacks) {
        this.backpacks = backpacks;
    }
}
