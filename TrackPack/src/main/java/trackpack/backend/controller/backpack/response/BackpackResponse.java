package trackpack.backend.controller.backpack.response;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trackpack.backend.entity.Backpack;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@ApiModel("背包信息返回")
public class BackpackResponse {

    @ApiModelProperty("背包id")
    private String backpackId;

    @ApiModelProperty("背包名称")
    private String backpackName;

    @ApiModelProperty("背包电量")
    private Double backpackBattery;

    @ApiModelProperty("背包激活时间")
    private LocalDateTime activationTime;

    @ApiModelProperty("背包删除时间")
    private LocalDateTime deleteTime;

    @ApiModelProperty("背包位置")
    private String location;

    @ApiModelProperty("背包网络状态" +
            "【" +
            "0：蓝牙连接 " +
            "1：MQTT通讯" +
            "】")
    private Integer networkStatus;

    @ApiModelProperty("背包mac地址")
    private String macAddress;

    @ApiModelProperty("背包ip地址")
    private String ipAddress;

//    @ManyToOne
//    @JoinColumn(name = "user_id", insertable = false, updatable = false)
//    @JsonBackReference // 解决循环引用问题
//    private user user;

    @ApiModelProperty("背包拥有者id")
    private String userId;

    @ApiModelProperty("无源标签数量")
    private Integer rfidTagNum;

    public BackpackResponse(Backpack backpack) {
        this.backpackId = backpack.getBackpackId();
        this.backpackName = backpack.getBackpackName();
        this.backpackBattery = backpack.getBackpackBattery();
        this.activationTime = backpack.getActivationTime();
        this.deleteTime = backpack.getDeleteTime();
        this.location = backpack.getLocation();
        this.networkStatus = backpack.getNetworkStatus();
        this.macAddress = backpack.getMacAddress();
        this.ipAddress = backpack.getIpAddress();
        this.userId = backpack.getUserId();
        this.rfidTagNum = backpack.getRfidTagNum();
    }

}
