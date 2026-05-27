package com.user.dto;

public class UserVendorMap {
    private String id;
    private String userId;
    private String venId;

    public UserVendorMap() {
    }

    public UserVendorMap(String id, String userId, String venId) {
        this.id = id;
        this.userId = userId;
        this.venId = venId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVenId() {
        return venId;
    }

    public void setVenId(String venId) {
        this.venId = venId;
    }
}
