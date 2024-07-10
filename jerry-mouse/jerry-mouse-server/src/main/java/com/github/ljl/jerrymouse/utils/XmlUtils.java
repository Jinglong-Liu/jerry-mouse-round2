package com.github.ljl.jerrymouse.utils;

import com.github.ljl.jerrymouse.exception.MethodNotSupportException;
import com.github.ljl.jerrymouse.support.classloader.LocalClassLoader;
import com.github.ljl.jerrymouse.support.classloader.WebApplicationClassLoader;
import com.github.ljl.jerrymouse.support.context.ApplicationContext;
import com.github.ljl.jerrymouse.support.context.ApplicationContextManager;
import com.github.ljl.jerrymouse.support.servlet.FilterConfigWrapper;
import com.github.ljl.jerrymouse.support.servlet.ServletConfigWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-06 16:59
 **/

public class XmlUtils {
    private static Logger logger = LoggerFactory.getLogger(XmlUtils.class);

    public static void parseLocalWebXml() {
        // 指定类加载器，用以加载资源以及反射加载类，创建对象
        ClassLoader classLoader = new LocalClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream("web.xml");
        ApplicationContextManager.applyApplicationContext("/root", classLoader);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(resourceAsStream);
            // 本地servlet appName设置为/root
            loadFromWebXml("/root", document, classLoader);
        } catch (DocumentException e) {
            logger.error("read web.xml error");
            e.printStackTrace();
        }
    }
    public static void loadApps(String baseDir, Set<String> appNameSet) {
        File dir = new File(baseDir);

        // filter subDirs
        final List<File> subDirs = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(File::isDirectory)
                .filter(subdir -> appNameSet.contains(subdir.getName()))
                .collect(Collectors.toList());

        CountDownLatch latch = new CountDownLatch(subDirs.size());

        subDirs.forEach(subDir -> {
            ThreadPoolUtils.execute(() -> {
                try {
                    parseAppWebXml(subDir);
                    logger.debug("Load app " + subDir.getName() + " end");
                } catch (MalformedURLException e) {
                    logger.error("Load app " + subDir.getName() + " failed");
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        });
        try {
            latch.await();
            logger.info("Finish load {} webapps", subDirs.size());
        } catch (InterruptedException e) {
            logger.error("count down latch await error when finish load webapps");
            e.printStackTrace();
        }
    }
    public static void parseAppWebXml(File appDir) throws MalformedURLException {

        // url: file:/D:/xxx/xxx
        URL url = appDir.toURI().toURL();
        // 构造对应app的
        ClassLoader classLoader = new WebApplicationClassLoader(new URL[]{url}, new LocalClassLoader());

        Thread.currentThread().setContextClassLoader(classLoader);

        File webXml = new File(appDir, "WEB-INF/web.xml");

        InputStream resourceAsStream;
        try {
            resourceAsStream = new FileInputStream(webXml);
        } catch (FileNotFoundException e) {
            logger.warn("web.xml not found in {}/WEB-INF", appDir.getAbsolutePath());
            return;
        }

        SAXReader saxReader = new SAXReader();
        String appName = "/" + appDir.getName(); // lastname, 并以/开头
        ApplicationContextManager.applyApplicationContext(appName, classLoader);// apply servlet context
        try {
            Document document = saxReader.read(resourceAsStream);
            loadFromWebXml(appName, document, classLoader);
        } catch (DocumentException e) {
            logger.error("read web.xml error");
            e.printStackTrace();
        }
    }
    private static void loadFromWebXml(String appName, Document document, ClassLoader classLoader) {
        ApplicationContext applicationContext = ApplicationContextManager.getApplicationContext(appName);
        loadContextFromWebXml(appName, document);
        loadServletFromWebXml(appName, document, classLoader);
        loadFilterFromWebXml(appName, document, classLoader);
        loadListenerFromWebXml(appName, document);
        loadSessionConfigFromWebXml(appName, document);
        applicationContext.initializeServletContextListeners();
    }
    private static void loadContextFromWebXml(String appName, Document document) {
        /**
         * <context-param>
         *    <param-name>context-param1</param-name>
         *    <param-value>100</param-value>
         * </context-param>
         */
        ServletContext applicationContext = ApplicationContextManager.getApplicationContext(appName);
        Element rootElement = document.getRootElement();
        List<Element> selectNodes = rootElement.elements("context-param");
        for (Element element : selectNodes) {
            String name = element.elementText("param-name");
            String value = element.elementText("param-value");
            applicationContext.setInitParameter(name, value);
        }
    }
    private static void loadServletFromWebXml(String appName, Document document, ClassLoader classLoader) {
        try {
            // 类加载器，用于反射创建servlet对象
            ApplicationContext applicationContext = ApplicationContextManager.getApplicationContext(appName);

            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.elements("servlet");
            Map<String, ServletConfig> map = new HashMap<>();

            /**
             * 1, 找到所有的servlet标签，找到servlet-name和servlet-class, 创建对应的ServletConfig
             */
            for (Element element : selectNodes) {
                String name = element.elementText("servlet-name");
                String className = element.elementText("servlet-class");
                ServletConfig servletConfig = new ServletConfigWrapper(applicationContext,name, className);
                map.put(name, servletConfig);

                List<Element> initParamElements = element.elements("init-param");
                initParamElements.stream().forEach(param -> {
                    String paramName = param.elementText("param-name");
                    String paramValue = param.elementText("param-value");
                    ((ServletConfigWrapper) servletConfig).setInitParameter(paramName, paramValue);
                });
            }
            /**
             * 2, 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
             */
            List<Element> mappingElements = rootElement.elements("servlet-mapping");
            for (Element mappingElement : mappingElements) {
                String name = mappingElement.elementText("servlet-name");
                String urlPattern = mappingElement.elementText("url-pattern");
                // 检查 <servlet-name> 是否存在于 <servlet> 元素中
                if (map.containsKey(name)) {
                    ServletConfig config = map.get(name);
                    String clazzName = ((ServletConfigWrapper) config).getClazzName();
                    Class<?> clazz = classLoader.loadClass(clazzName);
                    // 加载class;
                    HttpServlet httpServlet = (HttpServlet) clazz.getDeclaredConstructor().newInstance();
                    /**
                     * 3. 注册对应的 url + servlet 到 context
                     */
                    applicationContext.registerServlet(appName + urlPattern, httpServlet);
                    /**
                     * 4. 绑定servlet和对应的config, 并初始化
                     */
                    httpServlet.init(config);
                }
            }
        } catch (Exception e) {
            logger.error("load server from web.xml failed", e);
            e.printStackTrace();
        }
    }

    private static void loadFilterFromWebXml(String appName, Document document, ClassLoader classLoader) {
        try {
            ApplicationContext applicationContext = ApplicationContextManager.getApplicationContext(appName);

            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.elements("filter");
            Map<String, FilterConfigWrapper> map = new HashMap<>();

            /**
             * 格式类似servlet
             */
            for (Element element : selectNodes) {
                String name = element.elementText("filter-name");
                String className = element.elementText("filter-class");
                FilterConfigWrapper filterConfig = new FilterConfigWrapper(name, className, applicationContext);
                map.put(name, filterConfig);

                List<Element> initParamElements = element.elements("init-param");
                initParamElements.stream().forEach(param -> {
                    String paramName = param.elementText("param-name");
                    String paramValue = param.elementText("param-value");
                    // set init parameter
                    filterConfig.setInitParameter(paramName, paramValue);
                });
            }
            /**
             * 2, 根据filter-name找到<filter-mapping>中与其匹配的<url-pattern>
             */
            List<Element> mappingElements = rootElement.elements("filter-mapping");
            for (Element mappingElement : mappingElements) {
                String name = mappingElement.elementText("filter-name");
                String urlPattern = mappingElement.elementText("url-pattern");
                // 检查 <servlet-name> 是否存在于 <servlet> 元素中
                if (map.containsKey(name)) {
                    FilterConfigWrapper config = map.get(name);
                    String clazzName = config.getClassName();
                    Class<?> clazz = classLoader.loadClass(clazzName);
                    // 加载class;
                    Filter filter = (Filter) clazz.getDeclaredConstructor().newInstance();
                    /**
                     * 3. 注册对应的 url + filter 到 context
                     */
                    applicationContext.registerFilter(appName + urlPattern, filter);
                    /**
                     * 4. 绑定filter和对应的config, 并初始化
                     */
                    filter.init(config);
                }
            }
        } catch (Exception e) {
            logger.error("load server from web.xml failed", e);
            e.printStackTrace();
        }
    }

    /**
     * <listener>
     *     <listener-class></listener-class>
     * </listener>
     */
    private static void loadListenerFromWebXml(String appName, Document document) {
        try {
            ApplicationContext applicationContext = ApplicationContextManager.getApplicationContext(appName);

            Element rootElement = document.getRootElement();
            List<Element> selectNodes = rootElement.elements("listener");

            for (Element element : selectNodes) {
                String className = element.elementText("listener-class");
                // servletContext负责创建
                applicationContext.addListener(className);
            }
        } catch (Exception e) {
            logger.error("load listener from web.xml failed", e);
            e.printStackTrace();
        }
    }

    private static void loadSessionConfigFromWebXml(String appName, Document document) {
        /**
         *     <session-config>
         *         <session-timeout>1</session-timeout>
         *         <cookie-config>
         *             <name>JSESSIONID</name>
         *             <path>/</path>
         *         </cookie-config>
         *         <tracking-mode>COOKIE</tracking-mode>
         *     </session-config>
         */
        try {
            ApplicationContext applicationContext = ApplicationContextManager.getApplicationContext(appName);

            Element rootElement = document.getRootElement();
            List<Element> sessionConfigs = rootElement.elements("session-config");

            if (sessionConfigs.size() >= 2) {
                logger.error("only support one session-config in this version");
                throw new MethodNotSupportException("only support one session-config in this version");
            }
            for (Element sessionConfig : sessionConfigs) {
                String sessionTimeout = sessionConfig.elementText("session-timeout");

                /**
                 * default session timeout 单位分钟，<= 0 无限时
                 */
                if (StringUtils.notEmpty(sessionTimeout)) {
                    int timeout = Integer.parseInt(sessionTimeout);
                    applicationContext.setSessionTimeout(timeout);
                }

                /**
                 * 目前只支持COOKIE模式跟踪
                 */
                String trackingMode = sessionConfig.elementText("tracking-mode");
                if (StringUtils.notEmpty(trackingMode)) {
                    if ("COOKIE".equalsIgnoreCase(trackingMode)) {
                        Set<SessionTrackingMode> sessionTrackingModes = Collections.singleton(SessionTrackingMode.COOKIE);
                        applicationContext.setSessionTrackingModes(sessionTrackingModes);
                    } else {
                        logger.error("not support tracking mode:{}", trackingMode);
                        throw new MethodNotSupportException("not support tracking mode:" + trackingMode);
                    }
                }
                /**
                 * cookie-config配置
                 */
                Element cookieConfig = sessionConfig.element("cookie-config");
                if (Objects.nonNull(cookieConfig)) {
                    /**
                     * sessionId 对应的 name
                     */
                    String name = cookieConfig.elementText("name");
                    if (StringUtils.notEmpty(name)) {
                        applicationContext.setSessionIdFieldName(name);
                    }

                    /**
                     * 目前只支持一个app共用一个session-config
                     */
                    String path = cookieConfig.elementText("path");
                    if (StringUtils.notEmpty(path) && ! "/".equalsIgnoreCase(path)) {
                        logger.error("Not support other path match session-config, only '/' in this version");
                        throw new IllegalStateException("unsupported path ");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("load session config from web.xml failed", e);
            e.printStackTrace();
        }
    }
}
