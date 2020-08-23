package com.zkytech.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
//@EnableDiscoveryClient
//@EnableHystrix

public class ResourceServer {
    public static void main(String[] args) {
        SpringApplication.run(ResourceServer.class,args);
    }
}
