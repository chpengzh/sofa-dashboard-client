package com.alipay.sofa.dashboard.client.properties;

public class SofaDashboardClientProperties {
    /**
     * 是否可用
     */
    private boolean enable = true;

    /**
     * 实例地址
     */
    private String instanceIp = "";

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getInstanceIp() {
        return instanceIp;
    }

    public void setInstanceIp(String instanceIp) {
        this.instanceIp = instanceIp;
    }

    @Override
    public String toString() {
        return "SofaDashboardClientProperties{" + "enable=" + enable + ", instanceIp='"
               + instanceIp + '\'' + '}';
    }
}