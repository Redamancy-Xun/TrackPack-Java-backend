package trackpack.backend.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import trackpack.backend.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * session缓存实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("SessionData 会话实体")
public class SessionData implements Serializable {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("联系方式")
    private String contactInfo;

    @ApiModelProperty("用户创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("用户注销时间")
    private LocalDateTime deleteTime;

//    @ApiModelProperty("性别(0男，1女，2其他)")
//    @TableField(value = "gender")
//    private Integer gender;
//
//    @ApiModelProperty("年龄")
//    @TableField(value = "age")
//    private Integer age;
//
//    @ApiModelProperty("国籍")
//    @TableField(value = "nation")
//    private String nation;

//    @ApiModelProperty("手机号")
//    private String telephone;
//
//    @ApiModelProperty("头像")
//    private String portrait;

//    @ApiModelProperty("身份(0是学生，1是老师)")
//    @TableField(value = "role")
//    private Integer role;

//    @ApiModelProperty("个性签名")
//    private String signature;
//
//    @ApiModelProperty("获赞数")
//    private Integer likeCount;
//
//    @ApiModelProperty("评论数")
//    private Integer commentCount;
//
//    @ApiModelProperty("收藏数")
//    private Integer starCount;
//
//    @ApiModelProperty("转发数")
//    private Integer shareCount;
//
//    @ApiModelProperty("性别(0男,1女,2未知)")
//    private Integer gender;
//
//    @ApiModelProperty("年龄")
//    private Integer age;
//
//    @ApiModelProperty("身份(0是超级管理员，1是管理员，2是已认证用户，3是同意协议用户，4是未同意协议用户)")
//    private Integer role;
//
//    @ApiModelProperty("国籍")
//    private String nation;
//
//    @ApiModelProperty("省份")
//    private String province;
//
//    @ApiModelProperty("城市")
//    private String city;


//    public SessionData(user user, List<ScoreAction> history) {
//        this.id = user.getId();
//        this.username = user.getUsername();
//        this.gender = user.getGender();
//        this.age = user.getAge();
//        this.nation = user.getNation();
//        this.telephone = user.getTelephone();
//        this.portrait = user.getPortrait();
//        this.role = user.getRole();
//        this.advice = user.getAdvice();
//
//        this.history = history;
//    }

    public SessionData(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.contactInfo = user.getContactInfo();
        this.createTime = user.getCreateTime();
        this.deleteTime = user.getDeleteTime();
    }

}
