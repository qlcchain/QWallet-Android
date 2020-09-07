package com.today.step.net;

public class Auth {
    private String agent;
    private String uuid;
    private String platform;
    private String appBuild;

    public String getAppBuild() {
        return appBuild;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "agent='" + agent + '\'' +
                ", uuid='" + uuid + '\'' +
                ", platform='" + platform + '\'' +
                ", appBuild='" + appBuild + '\'' +
                ", appVersion='" + appVersion + '\'' +
                '}';
    }

    public void setAppBuild(String appBuild) {
        this.appBuild = appBuild;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    private String appVersion;
}
