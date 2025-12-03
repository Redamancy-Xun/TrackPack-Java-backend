package trackpack.backend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@ApiModel("user 用户信息")
@TableName(value ="user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @ApiModelProperty("id")
    @TableId(value = "id")
    private String id;

    @ApiModelProperty("用户名")
    @TableField(value = "username")
    private String username;

    @ApiModelProperty("密码")
    @TableField(value = "password")
    private String password;

    @ApiModelProperty("联系方式")
    @TableField(value = "contact_info")
    private String contactInfo;

    @ApiModelProperty("用户创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @ApiModelProperty("用户注销时间")
    @TableField(value = "delete_time")
    private LocalDateTime deleteTime;

//    @OneToMany(mappedBy = "user")
//    @JsonManagedReference // 解决循环引用问题
//    private Set<Backpack> backpacks;
//    //@OneToMany(mappedBy = "user") 注解表示 user 与 Backpack 是一对多的关系，一个用户可以拥有多个背包。
//    //mappedBy = "user" 指定 Backpack 实体中定义关系的一方是 user 属性。
//    //private Set<Backpack> backpacks; 定义了一个 Backpack 对象的集合，表示该用户拥有的背包。
//
//    // Getters and Setters
//
//    public Integer getUserID() {
//        return userID;
//    }
//
//    public void setUserID(Integer userID) {
//        this.userID = userID;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getContactInfo() {
//        return contactInfo;
//    }
//
//    public void setContactInfo(String contactInfo) {
//        this.contactInfo = contactInfo;
//    }
//
//    public Set<Backpack> getBackpacks() {
//        return backpacks;
//    }
//
//    public void setBackpacks(Set<Backpack> backpacks) {
//        this.backpacks = backpacks;
//    }
}
