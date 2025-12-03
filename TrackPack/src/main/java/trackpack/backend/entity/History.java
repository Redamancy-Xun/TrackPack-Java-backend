//package trackpack.backend.entity;
//
//import javax.persistence.*;
//import java.util.Date;
//
//@Entity
//@Table(name = "history")
//public class History {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "record_id")
//    private Integer recordId;
//
//    @Column(name = "location_change")
//    private String locationChange;
//
//    @Column(name = "user_action")
//    private String userAction;
//
//    @ManyToOne
//    @JoinColumn(name = "rfidtag_id", nullable = false)
//    private RfidTag rfidTag;
//
//    @Column(name = "time_stamp")
//    private Date timestamp;
//
//    // Getters and Setters
//
//    public Integer getRecordId() {
//        return recordId;
//    }
//
//    public void setRecordId(Integer recordId) {
//        this.recordId = recordId;
//    }
//
//    public String getLocationChange() {
//        return locationChange;
//    }
//
//    public void setLocationChange(String locationChange) {
//        this.locationChange = locationChange;
//    }
//
//    public String getUserAction() {
//        return userAction;
//    }
//
//    public void setUserAction(String userAction) {
//        this.userAction = userAction;
//    }
//
//    public RfidTag getItem() {
//        return rfidTag;
//    }
//
//    public void setItem(RfidTag rfidTag) {
//        this.rfidTag = rfidTag;
//    }
//
//    public Date getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Date timestamp) {
//        this.timestamp = timestamp;
//    }
//}
