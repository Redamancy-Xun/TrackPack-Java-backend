//package trackpack.backend.entity;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@Table(name = "record")
//public class Record {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "notification_id")
//    private Integer notificationId;
//
//    @Column(name = "notification_content", columnDefinition = "LONGTEXT")
//    private String notificationContent;
//
//    @Column(name = "time_stamp")
//    private Date timeStamp;
//
//    @Column(name = "user_id")
//    private Integer userId;
//
//    // Getters and Setters
//    public Integer getNotificationId() {
//        return notificationId;
//    }
//
//    public void setNotificationId(Integer notificationId) {
//        this.notificationId = notificationId;
//    }
//
//    public String getNotificationContent() {
//        return notificationContent;
//    }
//
//    public void setNotificationContent(String notificationContent) {
//        this.notificationContent = notificationContent;
//    }
//
//    public Date getTimeStamp() {
//        return timeStamp;
//    }
//
//    public void setTimeStamp(Date timeStamp) {
//        this.timeStamp = timeStamp;
//    }
//
//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }
//}
