package com.study.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author : luolan
 * @Date: 2022-03-16 10:21
 * @Description :
 */
@Configuration
@ConfigurationProperties(prefix = "eureka.instance")
public class ServerConfig {

    private static String instanceId;

    /**
     * 获取当前服务实例名称，使用eureka-instance-id
     * @return
     */
    public static String getInstanceId() {
        return ServerConfig.instanceId;
    }

    public void setInstanceId(String instanceId) {
        ServerConfig.instanceId = instanceId;
    }

}