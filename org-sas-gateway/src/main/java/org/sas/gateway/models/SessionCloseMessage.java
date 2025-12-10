package org.sas.gateway.models;


import java.time.Instant;


public class SessionCloseMessage {
    private String correlationId;
    private Long moduleId;
    private String moduleName;
    private String closeUrl;
    private SessionUser sessionUser;
    private String responseQueue;
    private Instant timestamp;

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCloseUrl() {
        return closeUrl;
    }

    public void setCloseUrl(String closeUrl) {
        this.closeUrl = closeUrl;
    }

    public SessionUser getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(SessionUser sessionUser) {
        this.sessionUser = sessionUser;
    }

    public String getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(String responseQueue) {
        this.responseQueue = responseQueue;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "SessionCloseMessage{" +
                "correlationId='" + correlationId + '\'' +
                ", moduleId=" + moduleId +
                ", moduleName='" + moduleName + '\'' +
                ", closeUrl='" + closeUrl + '\'' +
                ", sessionUser=" + sessionUser +
                ", responseQueue='" + responseQueue + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
