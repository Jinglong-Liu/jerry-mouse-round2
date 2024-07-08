## jerry-mouse prd 07

### 支持 Context 和 Request 相关的 Listener

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.7 | 2024-07-08 | 添加 [jerry-mouse prd07.md](./prd07.md) | ljl |

### 1、项目背景
为学习类JavaWeb中的Listener，并让servlet编写者可以使用部分Listener的功能，需要完善服务器的功能

### 2、需求描述
#### 2.1 功能需求

1、程序启动时，加载web.xml中的Listener并注册到对应Context

2、确保不同的webapp间Listener不会互相影响。

3、让Servlet编写者定义的相关Listener功能生效，支持以下四种Listener
```java
public interface ServletContextAttributeListener extends EventListener {}
public interface ServletContextListener extends EventListener {}
public interface ServletRequestListener extends EventListener {}
public interface ServletRequestAttributeListener extends EventListener {}
```

#### 2.2 非功能需求

要求支持一定的并发数。使用[Apache Jmeter](https://jmeter.apache.org/download_jmeter.cgi) 等工具进行压力测试。

测试200线程，30秒反复请求

要求高可用，用postman和浏览器反复测试，可以稳定复现结果，不会出现未响应的情况

### 3、实现要求
#### 实现接口

1、请在实现Listener功能的过程中，实现下列接口
```java
// 实现接口
public interface ServletContext {
    ClassLoader getClassLoader();
    <T extends EventListener> void addListener(T t);
    void addListener(Class<? extends EventListener> listenerClass);
    <T extends EventListener> T createListener(Class<T> clazz);

    // 支持 ServletContextAttributeListener
    Object getAttribute(String name);
    Enumeration<String> getAttributeNames();
    void setAttribute(String name, Object object);
    void removeAttribute(String name);
}

// 让用户编写的下列javax.servlet接口实现的功能生效，接口方法从略
public interface ServletContextAttributeListener extends EventListener {}
public interface ServletContextListener extends EventListener {}
public interface ServletRequestListener extends EventListener {}
public interface ServletRequestAttributeListener extends EventListener {}
```
#### 输出文档
- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 0.5 DAY，要求 `2024.7.8` 提交代码，文档`2024.8.31前`统一完成
