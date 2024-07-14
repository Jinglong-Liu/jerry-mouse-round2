package com.github.ljl.jerrymouse.bootstrap;

// import com.github.ljl.jerrymouse.server.netty.NettyServer;
import com.github.ljl.jerrymouse.server.nio.ReactorServer;
import com.github.ljl.jerrymouse.support.context.ApplicationContext;
import com.github.ljl.jerrymouse.support.context.ApplicationContextManager;
import com.github.ljl.jerrymouse.utils.WarUtils;
import com.github.ljl.jerrymouse.utils.XmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:18
 **/

public class JerryMouseBootStrap {
    private static Logger logger = LoggerFactory.getLogger(JerryMouseBootStrap.class);
    //private WebServerBootStrap webServerBootStrap = new NettyServer();
    private WebServerBootStrap webServerBootStrap = new ReactorServer();
    private static final String baseWarDir = "D:\\java-learning\\jerry-mouse-round2\\jerry-mouse\\jerry-mouse-server\\src\\test\\webapps";
    public void start(int port) {
        // 处理服务器本地servlet
        // XmlUtils.parseLocalWebXml();
        // 解压
        Set<String> appNameSet = WarUtils.extract(baseWarDir);
        // 扫描注册
        XmlUtils.loadApps(baseWarDir, appNameSet);
        // 启动server
        webServerBootStrap.start(port);
    }

    //TODO
    public void stop() {
        logger.info("server stop");
    }

    public void startBootApplication(int port) {
        webServerBootStrap.start(port);
    }
    // 用于SpringBoot 嵌入式jerry-mouse
    public ApplicationContext getApplicationContext(String applicationName) {
        return ApplicationContextManager.getOrCreateApplication(applicationName);
    }
}
