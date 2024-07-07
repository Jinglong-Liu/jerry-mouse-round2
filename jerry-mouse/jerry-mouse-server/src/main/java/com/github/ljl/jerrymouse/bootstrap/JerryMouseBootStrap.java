package com.github.ljl.jerrymouse.bootstrap;

import com.github.ljl.jerrymouse.server.netty.NettyServer;
import com.github.ljl.jerrymouse.server.nio.ReactorServer;
import com.github.ljl.jerrymouse.utils.WarUtils;
import com.github.ljl.jerrymouse.utils.XmlUtils;

import java.util.Set;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:18
 **/

public class JerryMouseBootStrap {
    // private WebServerBootStrap webServerBootStrap = new NettyServer();
    private WebServerBootStrap webServerBootStrap = new ReactorServer();
    private static final String baseWarDir = "D:\\java-learning\\jerry-mouse-round2\\jerry-mouse\\jerry-mouse-server\\src\\test\\webapps";
    public void start() {
        // 处理服务器本地servlet
        XmlUtils.parseLocalWebXml();
        // 解压
        Set<String> appNameSet = WarUtils.extract(baseWarDir);
        // 扫描注册
        XmlUtils.loadApps(baseWarDir, appNameSet);
        // 启动server
        webServerBootStrap.start();
    }
}
