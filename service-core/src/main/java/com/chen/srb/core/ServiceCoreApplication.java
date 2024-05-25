package com.chen.srb.core;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.chen.srb","com.chen.common"})
public class ServiceCoreApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ServiceCoreApplication.class,args);
    }
}
