package com.ost.nettyrpc.config;

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
@ConfigurationProperties(prefix = "zk", ignoreUnknownFields = false)
@PropertySource("classpath:config/my.properties")
@Data
@Component
public class MyProperties {
    private String zkAddress;
}
