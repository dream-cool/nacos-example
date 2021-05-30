package com.alibaba.nacos.example.spring.cloud.controller;

import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
//@RefreshScope
public class ConfigController {

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @Autowired
    private ApplicationContext context;

    /**
     * http://localhost:8080/config/get
     */
    @RequestMapping("/get")
    public boolean get() throws NacosException {
        return useLocalCache;
    }
}