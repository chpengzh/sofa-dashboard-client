package com.alipay.sofa.dashboard.client.properties;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
public class SofaDashboardMySQLProperties {

    private String           driver                   = "com.mysql.jdbc.Driver";

    private String           url                      = "jdbc:mysql://127.0.0.1:3306/sofa_dashboard_client";

    private String           username                 = "root";

    private String           password                 = "";

    private int              maximumActiveConnections = 10;

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMaximumActiveConnections() {
        return maximumActiveConnections;
    }

    public void setMaximumActiveConnections(int maximumActiveConnections) {
        this.maximumActiveConnections = maximumActiveConnections;
    }
}
