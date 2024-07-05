## jerry-mouse prd 01 

### socket编程实现HTTP请求获取与响应回复

| 版本号 | 发布日期       | 变更内容                                    | 负责人 |
|-----|------------|-----------------------------------------|-----|
| 1.1 | 2024-07-05 | 添加 [jerry-mouse prd01.md](./prd01.md) | ljl |

### 1、项目背景
为学习socket编程和reactor技术，了解netty框架，了解Http报文结构，现要求完成一个最基本Http服务器

### 2、需求描述
#### 2.1 功能需求
要求手动实现Reactor框架，以及使用Netty框架，分别实现一个最简单的服务器，功能如下：

程序启动后， 用户使用Postman等工具，在本地的8888端口，发送任意http请求，要求：
- 程序能在控制台打印出接收到的Http报文
- 设置Http响应报文并返回，要求Body显示Hello, Reactor! 和 Hello, Netty!。也可直接访问：http://localhost:8888/


#### 2.2 非功能需求

要求支持一定的并发数。使用[Apache Jmeter](https://jmeter.apache.org/download_jmeter.cgi) 等工具进行压力测试。

测试200线程，30秒反复请求，要求 `0 error`

### 3、实现要求
#### 实现接口
请分别使用Netty框架和手写Reactor框架，实现接口
```java
public interface WebServerBootStrap {
    void start();
}
```
功能是启动监听端口，能完成上述需求。

#### 输出文档
- 输出一篇博客介绍Reactor框架的实现
- 输出本章节的经验总结文档

### 4、完成时间
预计用时 1 DAY，要求 `2024.7.5` 提交代码，文档`2024.8.31前`统一完成
