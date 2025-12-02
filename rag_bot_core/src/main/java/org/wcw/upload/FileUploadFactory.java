package org.wcw.upload;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class FileUploadFactory implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    private final Map<String, UploadFileStrategy> map = new HashMap<>();


    @Value("${upload.strategy}")
    private String uploadWay;

    public UploadFileStrategy getUploadStrategy() {
        switch (uploadWay) {
            case "local":
                return map.get("local");
            case "oss":
                return map.get("aliyun");
        }
        return map.get(uploadWay);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        map.putAll(applicationContext.getBeansOfType(UploadFileStrategy.class));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
