package com.ipfsservice;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
public class IpfsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpfsServiceApplication.class, args);
    }

}
