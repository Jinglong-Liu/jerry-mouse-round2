package com.github.ljl.jerrymouse.bootstrap;

import com.github.ljl.jerrymouse.server.netty.NettyServer;
import com.github.ljl.jerrymouse.server.nio.ReactorServer;
import com.github.ljl.jerrymouse.utils.XmlUtils;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:18
 **/

public class JerryMouseBootStrap {
    // private WebServerBootStrap webServerBootStrap = new NettyServer();
    private WebServerBootStrap webServerBootStrap = new ReactorServer();
    public void start() {
        XmlUtils.parseLocalWebXml();
        webServerBootStrap.start();
    }
}
