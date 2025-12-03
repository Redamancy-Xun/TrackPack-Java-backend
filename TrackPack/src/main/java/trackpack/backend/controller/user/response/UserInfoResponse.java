package trackpack.backend.controller.user.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import trackpack.backend.entity.User;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@ApiModel("用户信息返回")
public class UserInfoResponse {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("联系方式")
    private String contactInfo;

    @ApiModelProperty("用户创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户注销时间")
    private LocalDateTime deleteTime;

    @ApiModelProperty("SessionId")
    private String sessionId;

    public UserInfoResponse(User user) {
        this.username = user.getUsername();
        this.contactInfo = user.getContactInfo();
        this.createTime = user.getCreateTime();
        this.deleteTime = user.getDeleteTime();

        this.sessionId = null;
    }

    public UserInfoResponse(User user, String sessionId) {
        this.username = user.getUsername();
        this.contactInfo = user.getContactInfo();
        this.createTime = user.getCreateTime();
        this.deleteTime = user.getDeleteTime();

        this.sessionId = sessionId;
    }
}
