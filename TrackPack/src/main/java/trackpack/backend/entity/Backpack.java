package trackpack.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Entity
@ApiModel("backpack 背包信息")
@TableName(value ="backpack")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Backpack {

    @Id
    @ApiModelProperty("背包id")
    @TableId(value = "backpack_id")
    private String backpackId;

    @ApiModelProperty("背包名称")
    @TableId(value = "backpack_name")
    private String backpackName;

    @ApiModelProperty("背包电量")
    @TableId(value = "backpack_battery")
    private Double backpackBattery;

    @ApiModelProperty("背包激活时间")
    @TableId(value = "activation_time")
    private LocalDateTime activationTime;

//    @TableField(updateStrategy = FieldStrategy.IGNORED)
    @ApiModelProperty("背包删除时间")
    @TableId(value = "delete_time")
    private LocalDateTime deleteTime;

    @ApiModelProperty("背包位置")
    @TableId(value = "location")
    private String location;

    @ApiModelProperty("背包网络状态" +
            "【" +
            "0：蓝牙连接 " +
            "1：MQTT通讯" +
            "】")
    @TableId(value = "network_status")
    private Integer networkStatus;

    @ApiModelProperty("背包mac地址")
    @TableId(value = "mac_address")
    private String macAddress;

    @ApiModelProperty("背包ip地址")
    @TableId(value = "ip_address")
    private String ipAddress;

//    @ManyToOne
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    @JsonBackReference // 解决循环引用问题
//    private user user;

    @ApiModelProperty("背包拥有者id")
    @TableId(value = "user_id")
    private String userId;

    @ApiModelProperty("无源标签数量")
    @TableId(value = "rfid_tag_num")
    private Integer rfidTagNum;

//    @OneToMany(mappedBy = "backpack")
//    @JsonManagedReference // 解决循环引用问题
//    private Set<RfidTag> items; // 定义与Item的一对多关系

    // Getters and Setters
    // (省略其他getter和setter)

//    public Integer getBackpackID() {
//        return backpackID;
//    }
//
//    public void setBackpackID(Integer backpackID) {
//        this.backpackID = backpackID;
//    }
//
//    public String getBackpackName() {
//        return backpackName;
//    }
//
//    public void setBackpackName(String backpackName) {
//        this.backpackName = backpackName;
//    }
//
//    public Float getModuleBattery() {
//        return moduleBattery;
//    }
//
//    public void setModuleBattery(Float moduleBattery) {
//        this.moduleBattery = moduleBattery;
//    }
//
//    public Date getActivationTime() {
//        return activationTime;
//    }
//
//    public void setActivationTime(Date activationTime) {
//        this.activationTime = activationTime;
//    }
//
//    public String getLocation() {
//        return location;
//    }
//
//    public void setLocation(String location) {
//        this.location = location;
//    }
//
//    public String getNetworkStatus() {
//        return networkStatus;
//    }
//
//    public void setNetworkStatus(String networkStatus) {
//        this.networkStatus = networkStatus;
//    }
//
//    public String getMacAddress() {
//        return macAddress;
//    }
//
//    public void setMacAddress(String macAddress) {
//        this.macAddress = macAddress;
//    }
//
//    public String getIpAddress() {
//        return ipAddress;
//    }
//
//    public void setIpAddress(String ipAddress) {
//        this.ipAddress = ipAddress;
//    }
//
//    public Integer getDeleteflag() {
//        return deleteflag;
//    }
//
//    public void setDeleteflag(Integer deleteflag) {
//        this.deleteflag = deleteflag;
//    }
//
//    public Integer getActivatedFlag() {
//        return activatedFlag;
//    }
//
//    public void setActivatedFlag(Integer activatedFlag) {
//        this.activatedFlag = activatedFlag;
//    }
//
//    public user getUser() {
//        return user;
//    }
//
//    public void setUser(user user) {
//        this.user = user;
//        if (user == null) {
//            this.userId = null;
//        } else {
//            this.userId = user.getUserID();
//        }
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//
//    public Set<RfidTag> getItems() {
//        return items;
//    }
//
//    public void setItems(Set<RfidTag> items) {
//        this.items = items;
//    }
}
