package org.wcw.config.properties;

import org.springframework.beans.factory.annotation.Autowired;

public class ConfigHolder {
    public static OssProperties ossProperties;


    @Autowired
    public void setConfig(OssProperties ossProperties) {
        ConfigHolder.ossProperties = ossProperties;
    }
}
