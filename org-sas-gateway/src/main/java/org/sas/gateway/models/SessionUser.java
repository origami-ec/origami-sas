package org.sas.gateway.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class SessionUser {

    private String username;
    private String id;
    private String pathModulo;
    private String lastUrl;
    private String appKey;
    private String user;
    private LocalDateTime lastAccessTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathModulo() {
        return pathModulo;
    }

    public void setPathModulo(String pathModulo) {
        this.pathModulo = pathModulo;
    }

    public String getLastUrl() {
        return lastUrl;
    }

    public void setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionUser that = (SessionUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SessionUser{" +
                "username='" + username + '\'' +
                ", id='" + id + '\'' +
                ", pathModulo='" + pathModulo + '\'' +
                ", appKey='" + appKey + '\'' +
                ", lastUrl='" + lastUrl + '\'' +
                '}';
    }
}
