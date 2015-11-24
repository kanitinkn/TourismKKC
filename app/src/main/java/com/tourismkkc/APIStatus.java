package com.tourismkkc;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Amnart on 24/11/2558.
 */
public class APIStatus {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String status;
    private String action;
    private String reason;
}
