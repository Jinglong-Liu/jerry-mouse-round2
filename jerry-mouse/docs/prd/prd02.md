## jerry-mouse prd 01

### 解析Http请求报文, 分发请求，并运行本地servlet

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.2 | 2024-07-06 | 添加 [jerry-mouse prd02.md](./prd02.md) | ljl |

### 1、项目背景
为学习Http报文结构，请求分发策略，初步认识servlet，现要求完成一个能实现最基本分发功能，运行本地Servlet的Http服务器

### 2、需求描述
#### 2.1 功能需求
要求在之前实现的Reactor框架和基于Netty框架的基础上，分别实现一个最简单的Http服务器，功能如下：

程序启动后， 用户使用postman, 浏览器等工具，在本地的8888端口，发送http请求，要求：

| 请求url                            | 请求方式 | 响应内容                   |
|----------------------------------|------|------------------------|
| http://127.0.0.1:8888/index.html | GET  | resources/index.html内容 |
| http://127.0.0.1:8888/hello      | GET  | Hello servlet!         |
| http://127.0.0.1:8888/<其他情况>     | GET  | 404页面(简单提示语即可)         |

#### 2.2 非功能需求

要求支持一定的并发数。使用[Apache Jmeter](https://jmeter.apache.org/download_jmeter.cgi) 等工具进行压力测试。

测试200线程，30秒反复请求，要求 `0 error`

要求高可用，用postman和浏览器反复测试，可以稳定复现结果，不会出现未响应的情况

要求一定的健壮性： http请求带上任意合法的params和body, 返回效果一致，能正确解析URI，不受参数影响

### 3、实现要求
#### 实现接口
请给出基于手写Reactor框架和Netty框架的实现。实际上，由于之前实现的代码的可扩展性，先实现Reactor版本的功能，再用很少的代码即可适配到Netty版本上。

请实现接口

- 实现HttpServletRequest接口, 解析封装请求报文，实现`getRequestURI`, `getMethod`, `getServletContext`接口
- 实现HttpServletResponse接口，提供`getSocketWriter`接口，供内部使用，写回信息
- 实现ServletContext接口，提供`registerServlet`和`getServlet`接口，供内部使用

这些HttpServlet相关接口非常多，先不全部实现，先实现本轮次需要使用到的接口。其他未实现接口抛异常处理，或暂不处理。

- 本地编写一个HelloServlet接口，重写doGet方法，要求能通过response写回`Hello Servlet`
- resources目录下写一个简单的index.html页面，用于返回

仅一个HelloServlet，在程序启动时注册，暂不需要支持多个Servlet，留到后续处理，但请保持扩展性。

#### 输出文档
- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 1 DAY，要求 `2024.7.6` 提交代码，文档`2024.8.31前`统一完成
