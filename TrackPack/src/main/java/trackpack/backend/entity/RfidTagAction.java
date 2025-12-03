package trackpack.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@ApiModel("RfidTagAction 无源标签操作信息")
@TableName(value ="rfid_tag_action")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RfidTagAction {

    @Id
    @ApiModelProperty("操作id")
    @TableId(value = "action_id", type = IdType.AUTO)
    private Integer actionId;

    @ApiModelProperty("操作对象（无源标签）id")
    @TableField(value = "rfid_tag_id")
    private String rfidTagId;

    @ApiModelProperty("操作用户id")
    @TableField(value = "action_user_id")
    private String actionUserId;

    @ApiModelProperty("指令编号" +
            "【" +
            "0：激活标签并增加物品 " +
            "1：增加物品 " +
            "2：修改物品名称 " +
            "3：删除标签并移除物品 " +
            "4：移除物品 " +
            "5：删除标签" +
            "】")
    @TableField(value = "command_id")
    private Integer commandId;

    @ApiModelProperty("操作对象（物品）旧名称")
    @TableField(value = "item_old_name")
    private String itemOldName;

    @ApiModelProperty("操作对象（物品）新名称")
    @TableField(value = "item_new_name")
    private String itemNewName;

    @ApiModelProperty("操作描述")
    @TableField(value = "action")
    private String action;

    @ApiModelProperty("操作时间")
    @TableField(value = "action_time")
    private LocalDateTime actionTime;

    @ApiModelProperty("操作删除时间")
    @TableField(value = "delete_time")
    private LocalDateTime deleteTime;
}
