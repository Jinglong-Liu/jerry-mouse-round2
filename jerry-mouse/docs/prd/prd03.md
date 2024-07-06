## jerry-mouse prd 03

### 更多Servlet接口的实现

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.3 | 2024-07-06 | 添加 [jerry-mouse prd03.md](./prd03.md) | ljl |

### 1、项目背景
为服务器能支持更多常用的Servlet接口，方便Servlet编写者使用，现要求完善之前未实现的request，response接口

### 2、需求描述
#### 2.1 功能需求

要求实现HttpServletRequest 和 HttpServletResponse的部分接口，具体见 __实现要求__ 部分

接口含义可以查看JavaDoc

要求正确运行com.github.ljl.jerrymouse.servlet包下新增的三个servlet, 这三个servlet会用到本次实现的部分接口。

控制台查看打印，并查看请求返回

在基于Reactor和Netty的两种服务器上均能直接运行，分别测试

#### 2.2 非功能需求

要求支持一定的并发数。使用[Apache Jmeter](https://jmeter.apache.org/download_jmeter.cgi) 等工具进行压力测试。

测试200线程，30秒反复请求

要求高可用，用postman和浏览器反复测试，可以稳定复现结果，不会出现未响应的情况

### 3、实现要求
#### 实现接口

请实现下列接口，正确运行com.github.ljl.jerrymouse.servlet包下三个新增servlet

重点关注如何让servlet编写者通过HttpResponse接口直接写回数据，而不是直接使用服务器内部的Request新增的接口

| interface           | method               | 说明                          |
|---------------------|----------------------|-----------------------------|
| HttpServletRequest  | getContentType       |                             |
|                     | getCharacterEncoding |                             |
|                     | getMethod            | 已实现                         |
|                     | getServerPort        | 暂时返回8888                    |
|                     | getRequestURI        | 已实现                         |
|                     | getContentLength     | 默认值-1                       |
|                     | getContentLengthLong | 默认值-1                       |
|                     | getDispatcherType    | 暂时返回 DispatcherType.REQUEST |
|                     | getAttributeNames    |                             |
|                     | getParameterNames    |                             |
|                     | getParameter         |                             |
|                     | getParameterValues   |                             |
|                     | getParameter         |                             |
|                     | getInputStream       | 重点关注                        |
|                     | getReader            | 重点关注                        |
| HttpServletResponse | containsHeader       |                             |
|                     | setHeader            |                             |
|                     | addHeader            |                             |
|                     | setStatus            |                             |
|                     | getStatus            |                             |
|                     | getHeader            |                             |
|                     | getHeaders           |                             |
|                     | getHeaderNames       |                             |
|                     | getContentType       |                             |
|                     | getOutputStream      | 重点关注                        |
|                     | getWriter            | 重点关注                        |
|                     | getCharacterEncoding |                             |
|                     | setContentLength     |                             |
|                     | setContentLengthLong |                             |
|                     | setContentType       |                             |
|                     | flushBuffer          | 重点关注                        |
|                     | resetBuffer          | 重点关注                        |

因为目前直需要运行jerry-mouse本地的servlet，因此getContextPath等先不实现。

#### 输出文档
- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 0.5 DAY，要求 `2024.7.6` 提交代码，文档`2024.8.31前`统一完成
