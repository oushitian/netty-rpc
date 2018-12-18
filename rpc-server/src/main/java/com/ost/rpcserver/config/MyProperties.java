package com.ost.rpcserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author xyl
 * @Create 2018-12-03 10:22
 * @Desc 写点注释吧
 **/
@Configuration
@ConfigurationProperties(prefix = "my", ignoreUnknownFields = false)
@PropertySource("classpath:config/my.properties")
@Data
@Component
public class MyProperties {
    private String serverAddress;   //服务器发布的IP
    private Integer delay;  //延时发布时间
}
