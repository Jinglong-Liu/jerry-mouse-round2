package com.github.ljl.demo.web.springboot;

import com.github.ljl.jerrymouse.config.JerryMouseWebServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 11:53
 **/

@SpringBootApplication
@Import(JerryMouseWebServerConfig.class)
public class Application1 {
    private static final Logger logger = LoggerFactory.getLogger(Application1.class);
    public static void main(String[] args) {
        logger.info("Application prepare to start...");
        SpringApplication.run(Application1.class);
    }
}
