## jerry-mouse prd 05

### 运行 servlet war包

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.5 | 2024-07-07 | 添加 [jerry-mouse prd05.md](./prd05.md) | ljl |

### 1、项目背景
为学习类加载器，并让服务器能支持运行外部编写的servlet打成的war包，需要完善服务器的功能

### 2、需求描述
#### 2.1 功能需求

1、在jerry-mouse的 src/test目录下新建目录webapps，做为存放外部war包的根目录。

2、编写简单的servlet, 在对应project的web.xml里注册好servlet，打成war包，放到webapps目录下，例如webapp1.war

3、运行jerry-mouse-server, 对应webapp的servlet生效，可以正常访问。server本地的contextPath暂记为root

4、支持多个webapp war包不冲突

#### 2.2 非功能需求

要求支持一定的并发数。使用[Apache Jmeter](https://jmeter.apache.org/download_jmeter.cgi) 等工具进行压力测试。

测试200线程，30秒反复请求

要求高可用，用postman和浏览器反复测试，可以稳定复现结果，不会出现未响应的情况

### 3、实现要求
#### 实现接口

1、请实现下列接口
```java
// 实现Context对init-parameter的操作，并能在启动时解析web.xml中的相关配置
public interface ServletContext {
    String getInitParameter(String name);
    Enumeration<String> getInitParameterNames();
    boolean setInitParameter(String name, String value);
    // appName 
    String getContextPath();
}

// 确认以下接口的正确性
public interface HttpServletRequest extends ServletRequest {
    String getCharacterEncoding();
    void setCharacterEncoding(String env);
    String getContentType();
}
public interface HttpServletResponse extends ServletResponse {
    void setCharacterEncoding(String charset);
    String getCharacterEncoding();
    void setContentType(String type);
    String getContentType();
}
```

2、实现自定义类加载器，能够加载webapps下，不同的war包中的类，包括解压后的.class和.jar包，不产生冲突

#### 输出文档
- 输出本章节的实现方案和经验总结文档
- 输出类加载器介绍与实现方案

### 4、完成时间
预计用时 1 DAY，要求 `2024.7.7` 提交代码，文档`2024.8.31前`统一完成
