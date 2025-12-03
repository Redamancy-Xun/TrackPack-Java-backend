package trackpack.backend.controller.rfidtag.response;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trackpack.backend.entity.RfidTag;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@ApiModel("无源标签信息返回")
public class RfidTagResponse {

    @ApiModelProperty("无源标签id")
    private String rfidTagId;

    @ApiModelProperty("物品名称")
    private String itemName;

    @ApiModelProperty("标签状态" +
            "【" +
            "0：在包外 " +
            "1：在包中" +
            "】")
    private Integer rfidTagStatus;

    @ApiModelProperty("无源标签激活时间")
    private LocalDateTime activationTime;

    @ApiModelProperty("无源标签删除时间")
    private LocalDateTime deleteTime;

    public RfidTagResponse(RfidTag rfidTag) {
        this.rfidTagId = rfidTag.getRfidTagId();
        this.itemName = rfidTag.getItemName();
        this.rfidTagStatus = rfidTag.getRfidTagStatus();
        this.activationTime = rfidTag.getActivationTime();
        this.deleteTime = rfidTag.getDeleteTime();
    }

}
