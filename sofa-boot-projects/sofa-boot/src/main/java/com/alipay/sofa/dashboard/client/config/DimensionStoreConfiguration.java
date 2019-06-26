package com.alipay.sofa.dashboard.client.config;

import com.alipay.sofa.dashboard.client.properties.SofaDashboardMySQLProperties;
import com.alipay.sofa.dashboard.client.properties.SofaDashboardProperties;
import com.alipay.sofa.dashboard.client.store.DashboardMySQLStoreConfig;
import com.alipay.sofa.dashboard.client.store.DimensionStore;
import com.alipay.sofa.dashboard.client.store.MySQLDimensionStore;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chen.pengzhi (chpengzh@foxmail.com)
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties({ SofaDashboardProperties.class })
@ConditionalOnProperty(prefix = "com.alipay.sofa.dashboard.client", value = "enable", matchIfMissing = true)
public class DimensionStoreConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "com.alipay.sofa.dashboard.client.store", value = "mysql", matchIfMissing = true)
    @ConditionalOnClass({ MySQLDimensionStore.class, SqlSessionFactory.class })
    public DimensionStore<?> createMySQLDimensionStore(SofaDashboardProperties prop) {
        SofaDashboardMySQLProperties mysqlProps = prop.getMysql();
        return new MySQLDimensionStore(DashboardMySQLStoreConfig.newBuilder()
            .driver(mysqlProps.getDriver())
            .url(mysqlProps.getUrl())
            .username(mysqlProps.getUsername())
            .password(mysqlProps.getPassword())
            .maximumActiveConnections(mysqlProps.getMaximumActiveConnections())
            .build());
    }

}
