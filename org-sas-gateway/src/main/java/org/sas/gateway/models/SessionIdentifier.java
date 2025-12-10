package org.sas.gateway.models;

public class SessionIdentifier {
    private String ip;
    private String userAgent;

    public SessionIdentifier(String ip, String userAgent) {
        this.ip = ip;
        this.userAgent = userAgent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "SessionIdentifier{" +
                "ip='" + ip + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
