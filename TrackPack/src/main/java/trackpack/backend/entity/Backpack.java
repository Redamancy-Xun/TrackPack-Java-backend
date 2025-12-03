package trackpack.backend.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "backpack")
public class Backpack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "backpack_id")
    private Integer backpackID;

    @Column(name = "backpack_name")
    private String backpackName;

    @Column(name = "module_battery")
    private Float moduleBattery;

    @Column(name = "activation_time")
    private Date activationTime;

    @Column(name = "location")
    private String location;

    @Column(name = "network_status")
    private String networkStatus;

    @Column(name = "mac_address")
    private String macAddress;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "delete_flag")
    private Integer deleteflag;

    @Column(name = "activate_flag")
    private Integer activatedFlag;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference // 解决循环引用问题
    private User user;

    @Column(name = "user_id", nullable = true)
    private Integer userId;

    @OneToMany(mappedBy = "backpack")
    @JsonManagedReference // 解决循环引用问题
    private Set<Item> items; // 定义与Item的一对多关系

    // Getters and Setters
    // (省略其他getter和setter)

    public Integer getBackpackID() {
        return backpackID;
    }

    public void setBackpackID(Integer backpackID) {
        this.backpackID = backpackID;
    }

    public String getBackpackName() {
        return backpackName;
    }

    public void setBackpackName(String backpackName) {
        this.backpackName = backpackName;
    }

    public Float getModuleBattery() {
        return moduleBattery;
    }

    public void setModuleBattery(Float moduleBattery) {
        this.moduleBattery = moduleBattery;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNetworkStatus() {
        return networkStatus;
    }

    public void setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Integer getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(Integer deleteflag) {
        this.deleteflag = deleteflag;
    }

    public Integer getActivatedFlag() {
        return activatedFlag;
    }

    public void setActivatedFlag(Integer activatedFlag) {
        this.activatedFlag = activatedFlag;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        if (user == null) {
            this.userId = null;
        } else {
            this.userId = user.getUserID();
        }
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }
}
