## jerry-mouse prd 06

### 支持 Filter

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.6 | 2024-07-07 | 添加 [jerry-mouse prd06.md](./prd06.md) | ljl |

### 1、项目背景
为学习类JavaWeb中的Filter，并让servlet编写者可以使用Filter的功能，需要完善服务器的功能

### 2、需求描述
#### 2.1 功能需求

1、程序启动时，加载web.xml中的Filter并注册到对应Context

2、Filter支持模糊匹配，且确保不同的webapp不会互相影响。

3、让Servlet编写者定义的Filter功能生效，支持多个Filter，支持前置后置Filter功能，直接解析InitParameter


#### 2.2 非功能需求

要求支持一定的并发数。使用[Apache Jmeter](https://jmeter.apache.org/download_jmeter.cgi) 等工具进行压力测试。

测试200线程，30秒反复请求

要求高可用，用postman和浏览器反复测试，可以稳定复现结果，不会出现未响应的情况

### 3、实现要求
#### 实现接口

1、请实现下列接口，使得servlet编写者能够编写Filter,并使用doFilter进行filter操作
```java
public interface FilterChain {
    void doFilter(ServletRequest request, ServletResponse response);
}

// filter.init(filterConfig)
public interface FilterConfig {
    String getFilterName();
    ServletContext getServletContext();
    String getInitParameter(String name);
    Enumeration<String> getInitParameterNames();
}
```
#### 输出文档
- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 0.5 DAY，要求 `2024.7.7` 提交代码，文档`2024.8.31前`统一完成
