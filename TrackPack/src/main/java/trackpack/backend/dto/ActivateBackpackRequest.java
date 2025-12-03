package trackpack.backend.dto;

public class ActivateBackpackRequest {
    private Integer backpackId;
    private Integer userId;

    // Getters and Setters
    public Integer getBackpackId() {
        return backpackId;
    }

    public void setBackpackId(Integer backpackId) {
        this.backpackId = backpackId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}