package com.plugin.controller;

import com.plugin.common.ModuleFederationConfig;
import com.plugin.manager.PluginManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/plugin")
public class PluginController {
    @Autowired
    private PluginManager pluginManager;

    @GetMapping("/module-federation")
    public ModuleFederationConfig getModuleFederation(){
        return pluginManager.getAllModuleFederationConfigs();
    }
}
