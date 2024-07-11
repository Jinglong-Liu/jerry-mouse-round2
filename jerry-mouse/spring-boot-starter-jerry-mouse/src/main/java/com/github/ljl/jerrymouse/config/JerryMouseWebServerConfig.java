package com.github.ljl.jerrymouse.config;

import com.github.ljl.jerrymouse.bootstrap.JerryMouseBootStrap;
import com.github.ljl.jerrymouse.support.context.ApplicationContext;
import com.github.ljl.jerrymouse.support.servlet.ServletConfigWrapper;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerException;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.AbstractServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.DispatcherServlet;

import javax.annotation.Resource;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-11 14:43
 **/

@Configuration
public class JerryMouseWebServerConfig {

    @Resource
    private Environment environment;

    @Bean
    public ServletWebServerFactory servletWebServerFactory() {
        return new JerryMouseServletWebServerFactory();
    }

    class JerryMouseServletWebServerFactory extends AbstractServletWebServerFactory {
        // 默认
        private static final String applicationName = "/root";

        @Override
        public WebServer getWebServer(ServletContextInitializer... initializers) {
            return new JerryMouseWebServer(applicationName,  initializers);
        }

        class JerryMouseWebServer implements WebServer {
            private Integer PORT = 8080; //default
            private final Integer MAX_PORT = 65535;
            private final Integer MIN_PORT = 1024;

            public JerryMouseBootStrap server;
            private final ApplicationContext applicationContext;
            public JerryMouseWebServer(String applicationName, ServletContextInitializer... initializers) {
                if (Objects.nonNull(environment)) {
                    this.PORT = checkPort(environment.getProperty("server.port"));
                }
                this.server = new JerryMouseBootStrap();
                this.applicationContext = server.getApplicationContext(applicationName);
                Arrays.stream(initializers).forEach(servletContextInitializer -> {
                    try {
                        servletContextInitializer.onStartup(applicationContext);
                    } catch (ServletException e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            public void start() throws WebServerException {
                // start 前，dispatcherServlet已经被springmvc创建并注册好，key = dispatcherServlet
                final String dispatcherServletName = "dispatcherServlet";
                applicationContext.log("jerry-mouse-servlet-web-server-factory start");
                try {
                    Servlet servlet = applicationContext.getServlet(dispatcherServletName);
                    if (servlet instanceof DispatcherServlet) {
                        DispatcherServlet dispatcherServlet = (DispatcherServlet) servlet;
                        applicationContext.registerServlet(applicationName + "/", dispatcherServlet);
                        ServletConfig servletConfig = new ServletConfigWrapper(applicationContext, dispatcherServletName, servlet.getClass().getName());
                        dispatcherServlet.init(servletConfig);
                    }
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                server.startSpringBootApplication(PORT);
            }

            @Override
            public void stop() throws WebServerException {
                server.stop();
            }

            @Override
            public int getPort() {
                return PORT;
            }

            private final Object lock = new Object();

            /**
             *
             * @param object environment.getProperty("server.port")
             * @return valid port
             * number = Integer.parseInt(object)
             * 1024-65535: valid, return number
             * other/not set: 8080
             * conflict: next port(like 8081, 8082, ...)
             *
             * @throws IllegalStateException : not port Available
             */
            private Integer checkPort(Object object) {
                synchronized (lock) {
                    try {
                        int setPort = Integer.parseInt((String) object);
                        int number = setPort;
                        if (number < MIN_PORT || number > MAX_PORT) {
                            number = PORT;
                        }
                        while (number <= MAX_PORT) {
                            while (!isPortAvailable(number)) {
                                number++;
                            }
                            if (Objects.equals(number, MAX_PORT + 1)) {
                                throw new IllegalStateException("All Port over" + setPort + " are not Available!");
                            }
                            return number;
                        }
                    } catch (NumberFormatException ignore) {
                        // invalid or not set port, using default 8080
                        int number = PORT;
                        while(number <= MAX_PORT) {
                            if (!isPortAvailable(number)) {
                                number++;
                            }
                            if (Objects.equals(number, MAX_PORT + 1)) {
                                throw new IllegalStateException("All Ports over 8080 are not Available!");
                            }
                        }
                        return number;
                    }
                }
                return PORT;
            }
            private boolean isPortAvailable(int port) {
                try (ServerSocket ignored = new ServerSocket(port)) {
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }
        }
    }
}
