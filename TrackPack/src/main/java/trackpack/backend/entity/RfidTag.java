package trackpack.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@ApiModel("Item 物品")
@TableName(value ="reply")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @Column(name = "rfidtag_id")
    @JsonProperty("rfidtag_id")  // 将 JSON 字段名映射到实体字段
    private Integer rfidtagId;

    @Column(name = "item_name")
    @JsonProperty("item_name")
    private String itemName;

    @Column(name = "item_status")
    @JsonProperty("item_status")
    private String itemStatus;

    @Column(name = "delete_flag")
    @JsonProperty("delete_flag")
    private Integer deleteFlag;

    @ManyToOne
    @JoinColumn(name = "backpack_id", insertable = false, updatable = false, nullable = true)
    @JsonBackReference // 解决循环引用问题
    private Backpack backpack;

    @Column(name = "backpack_id", nullable = true)
    @JsonProperty("backpack_id")
    private Integer backpackId;

    // Getters and Setters
    // (省略其他getter和setter)

    public Integer getRfidtagId() {
        return rfidtagId;
    }

    public void setRfidtagId(Integer rfidtagId) {
        this.rfidtagId = rfidtagId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(String itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Backpack getBackpack() {
        return backpack;
    }

    public void setBackpack(Backpack backpack) {
        this.backpack = backpack;
        if (backpack == null) {
            this.backpackId = null;
        } else {
            this.backpackId = backpack.getBackpackID();
        }
    }

    public Integer getBackpackId() {
        return backpackId;
    }

    public void setBackpackId(Integer backpackId) {
        this.backpackId = backpackId;
    }
}
