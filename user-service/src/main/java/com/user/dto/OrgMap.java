package com.user.dto;

public class OrgMap {
    private String id;
    private String userId;
    private String orgId;

    public OrgMap() {
    }

    public OrgMap(String id, String userId, String orgId) {
        this.id = id;
        this.userId = userId;
        this.orgId = orgId;
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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
