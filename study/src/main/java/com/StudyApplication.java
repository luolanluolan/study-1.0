package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author : luolan
 * @Date: 2022-03-11 16:59
 * @Description :
 */
//@MapperScan(basePackages={"study.*.mapper"})
//@EnableJpaRepositories(basePackages = "study.*.repository") //扫描 @Repositories 注解
@EnableEurekaServer
@SpringBootApplication//(scanBasePackages = {"study.*"})
@ServletComponentScan("study.config")
@EnableJpaAuditing //启动Jpa的审计功能,可为表中 版本号、创建时间、修改时间 、创建者、修改者 字段自动填充
@EnableFeignClients //开启OpenFeign扫描
public class StudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyApplication.class, args);
    }

}
