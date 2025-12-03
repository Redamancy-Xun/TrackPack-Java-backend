package trackpack.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ApiModel("RfidTag 无源标签")
@TableName(value ="rfid_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RfidTag {

    @Id
    @ApiModelProperty("无源标签id")
    @TableId(value = "rfid_tag_id")
    private String rfidTagId;

    @ApiModelProperty("物品名称")
    @TableId(value = "item_name")
    private String itemName;

    @ApiModelProperty("标签状态" +
            "【" +
            "0：未使用 " +
            "1：正在使用" +
            "】")
    @TableId(value = "rfid_tag_status")
    private Integer rfidTagStatus;

    @ApiModelProperty("无源标签激活时间")
    @TableId(value = "activation_time")
    private LocalDateTime activationTime;

    @ApiModelProperty("无源标签删除时间")
    @TableId(value = "delete_time")
    private LocalDateTime deleteTime;

//    @ManyToOne
//    @JoinColumn(name = "backpack_id", insertable = false, updatable = false, nullable = true)
//    @JsonBackReference // 解决循环引用问题
//    private Backpack backpack;

    @ApiModelProperty("无源标签绑定的背包id")
    @TableId(value = "backpack_id")
    private String backpackId;

//    // Getters and Setters
//    // (省略其他getter和setter)
//
//    public Integer getRfidtagId() {
//        return rfidtagId;
//    }
//
//    public void setRfidtagId(Integer rfidtagId) {
//        this.rfidtagId = rfidtagId;
//    }
//
//    public String getItemName() {
//        return itemName;
//    }
//
//    public void setItemName(String itemName) {
//        this.itemName = itemName;
//    }
//
//    public String getItemStatus() {
//        return itemStatus;
//    }
//
//    public void setItemStatus(String itemStatus) {
//        this.itemStatus = itemStatus;
//    }
//
//    public Integer getDeleteFlag() {
//        return deleteFlag;
//    }
//
//    public void setDeleteFlag(Integer deleteFlag) {
//        this.deleteFlag = deleteFlag;
//    }
//
//    public Backpack getBackpack() {
//        return backpack;
//    }
//
//    public void setBackpack(Backpack backpack) {
//        this.backpack = backpack;
//        if (backpack == null) {
//            this.backpackId = null;
//        } else {
//            this.backpackId = backpack.getBackpackID();
//        }
//    }
//
//    public Integer getBackpackId() {
//        return backpackId;
//    }
//
//    public void setBackpackId(Integer backpackId) {
//        this.backpackId = backpackId;
//    }
}
