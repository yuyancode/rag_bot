package org.wcw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ComponentScan(basePackages = {"org.wcw.**",})
@MapperScan(basePackages = {"org.wcw.*.mapper",})
@EnableAsync
public class RagBotWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(RagBotWebApplication.class, args);
    }
}
