package org.sas.gateway.utils;

public class EliminarSession {

    private String appKey;
    private String user;
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "EliminarSession{" +
                "appKey='" + appKey + '\'' +
                ", user='" + user + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
