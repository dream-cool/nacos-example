package com.alibaba.nacos.example.spring.boot.controller;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.Executor;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping("config")
public class ConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);


    private ConfigService configService = NacosFactory.createConfigService("127.0.0.1:8848");

    @NacosValue(value = "${useLocalCache:false}", autoRefreshed = true)
    private boolean useLocalCache;

    @NacosValue(value = "${monitor.db1:}", autoRefreshed = true)
    private String monitorDb1;

    @NacosValue(value = "${monitor.db2:}", autoRefreshed = true)
    private String monitorDb2;

    public ConfigController() throws NacosException {
    }

    @RequestMapping("/status")
    @ResponseBody
    public String getServerStatus() {
        return configService.getServerStatus();
    }

    @RequestMapping(value = "/get", method = GET)
    @ResponseBody
    public boolean get() {
        return useLocalCache;
    }

    @RequestMapping(value = "/get/db1", method = GET)
    @ResponseBody
    public String db1() {
        return monitorDb1;
    }

    @RequestMapping(value = "/get/db2", method = GET)
    @ResponseBody
    public String db2() {
        return monitorDb2;
    }

    @RequestMapping("/addListener")
    @ResponseBody
    public String addListener() throws NacosException {
        configService.addListener("example", "DEFAULT_GROUP", new Listener() {

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                LOGGER.info("the configInfo is changed for example dataId in DEFAULT_GROUP,\n [{}] ", configInfo);
            }
        });
        return "ok";
    }

    @RequestMapping("/removeListener")
    @ResponseBody
    public String removeListener() throws NacosException {
        configService.removeListener("example", "DEFAULT_GROUP", new Listener() {

            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                LOGGER.info("the configInfo is changed for example dataId in DEFAULT_GROUP,\n [{}] ", configInfo);
            }
        });
        return "ok";
    }

    @RequestMapping("/getConfig/{dataId}/{group}")
    @ResponseBody
    public String getConfig(@PathVariable("dataId") String dataId,
                            @PathVariable("group") String group) throws NacosException {
        return configService.getConfig(dataId, group, 5000);
    }



    @RequestMapping("/publishConfig/{dataId}/{group}/{content}")
    @ResponseBody
    public boolean publishConfig(@PathVariable("dataId") String dataId,
                                @PathVariable("group") String group,
                                @PathVariable("content") String content) throws NacosException {
        return configService.publishConfig(dataId, group, content);
    }

    @RequestMapping("/removeConfig/{dataId}/{group}")
    @ResponseBody
    public boolean removeConfig(@PathVariable("dataId")String dataId,
                               @PathVariable("group") String group) throws NacosException {
        return configService.removeConfig(dataId, group);
    }
}