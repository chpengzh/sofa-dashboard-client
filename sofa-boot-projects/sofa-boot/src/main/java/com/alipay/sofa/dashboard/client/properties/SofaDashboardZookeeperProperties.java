package com.alipay.sofa.dashboard.client.properties;

public class SofaDashboardZookeeperProperties {

    /**
     * Zookeeper 工作地址.
     */
    private String address;

    /**
     * Zookeeper 客户端错误重试间隔(ms).
     */
    private int baseSleepTimeMs = 1000;

    /**
     * Zookeeper 客户端最大重试次数.
     */
    private int maxRetries = 3;

    /**
     * Zookeeper 客户端会话超时时间(ms).
     */
    private int sessionTimeoutMs = 6000;

    /**
     * Zookeeper 客户端超时时间(ms).
     */
    private int connectionTimeoutMs = 6000;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    @Override
    public String toString() {
        return "SofaDashboardZookeeperProperties{" + "address='" + address + '\''
               + ", baseSleepTimeMs=" + baseSleepTimeMs + ", maxRetries=" + maxRetries
               + ", sessionTimeoutMs=" + sessionTimeoutMs + ", connectionTimeoutMs="
               + connectionTimeoutMs + '}';
    }
}