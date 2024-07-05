## jerry-mouse 需求文档

| 版本号 | 发布日期       | 变更内容                                    | 负责人 |
|-----|------------|-----------------------------------------|-----|
| 1.1 | 2024-07-05 | 添加 [jerry-mouse prd01.md](./prd01.md) | ljl |
| 1.0 | 2024-07-02 | jerry-mouse prd初始版本                     | ljl |

### 1、项目背景

为了学习巩固javaWeb和Tomcat基础，了解其设计理念，我们计划开发一个名为 JerryMouse(jerry-mouse) 的定制服务器，替代当前使用的 Tomcat。JerryMouse 将能够运行 Servlet 和 Spring MVC 项目，并能够在 Spring Boot 项目中替换 Tomcat 运行 Web 项目。

### 2、目标

- 1、开发一个名为 JerryMouse 的Web服务器。
- 2、JerryMouse 能够运行标准 Servlet。
- 3、JerryMouse 能够支持 Spring MVC 项目。
- 4、JerryMouse 能够集成到 Spring Boot 项目中，替代 Tomcat 运行 Web 项目。

### 3、功能需求

#### 3.1 JerryMouse 基础功能

- 支持标准的 javax.servlet.Servlet API。
- 提供 最基本的 HTTP 请求处理和响应功能。
- 支持基本的 HTTP 协议功能（GET、POST、PUT、DELETE）。
- 同时支持多个war包运行

#### 3.2 Spring MVC 支持

- JerryMouse 能够同时运行多个不同Spring版本(<6.0)的 Spring MVC 项目。
- 支持 Spring MVC 的 Controller、Service、Repository 层的正常运行。

#### 3.3 Spring Boot 集成

- JerryMouse 能够嵌入到 Spring Boot Web项目中，替代 Tomcat 成为项目的服务器。
- 能跑通简单的Spring Boot Web项目最基本功能
- 在Spring Boot 项目中提供WebServerFactory生成JerryMouse作为服务器运行的示例

#### 3.4 更全面的功能（待补充，可选）

- JerryMouse 应该能够运行稍复杂，功能较多的web项目

### 4、非功能需求

- 性能：JerryMouse 应该能够处理高并发请求，响应时间低于200ms。
- 可用性：系统应具备高可用性，运行稳定，无明显崩溃。

### 5、接口要求

- JerryMouse 提供的接口应与标准的 javax.servlet API 4.0.1 保持一致，以确保兼容性。
- JerryMouse 应该暴露必要的配置接口，允许在 Spring Boot 中进行配置。

### 6、验收标准

- 1、JerryMouse 能够成功运行一个标准的 Servlet 示例。
- 2、能够运行一个 Spring MVC 示例项目。
- 3、能够在 Spring Boot 项目中替代 Tomcat 运行，并正确响应 HTTP 请求。
- 4、通过性能测试，单个请求响应时间小于200ms。

### 7、时间表
- M1: 需求分析与设计（1 Day, 且持续完善）
- M2: JerryMouse 基础功能开发（3 Day）
- M3: Spring MVC 支持开发（3 Day）
- M4: Spring Boot 集成开发（2 Day）
- M5: 其他功能的完善 （3 Day）
- M6: 测试与验收（3 Day）

### 8、输出文档要求

#### 8.1 项目文档
- 需求文档

此文档。也包括各个版本迭代的prd

- 设计文档

各个阶段，各个模块的设计，必要的架构图，流程图

- 开发手册

详细描述JerryMouse的开发流程，以及常见问题解决方案，给学习者以参考

#### 8.2 文档格式

- 所有文档以Markdown格式编写，可在Github上直接解析查看
- 必要的架构图，流程图同时保存.puml格式和.png格式
- 开发手册应在个人博文平台部署，且应面向仅有一点Java基础的同学
- git commit 拆分合理，在github上体现，方便查看

### 9、附录

#### 9.1 现有的Tomcat以及手写实现源码/教程
Apache Tomcat: https://github.com/apache/tomcat

其他手写Tomcat的资料，可用作学习参考

https://houbb.github.io/2016/11/07/web-server-tomcat-01-intro # 推荐

https://www.liaoxuefeng.com/wiki/1545956031987744             # 廖雪峰  

https://github.com/shenshaoming/tomcat

https://github.com/OliverLiy/MyTomcatDemo

https://github.com/CoderXiaohui/mini-tomcat

https://github.com/Rainyn/myTomcat

https://www.jianshu.com/p/dce1ee01fb90

https://github.com/thestyleofme/minicat-parent

https://github.com/nmyphp/mytomcat

#### 9.2 Servlet 接口相关文档

[HttpServletRequest](https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletRequest.html)

[HttpServletResponse](https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServletResponse.html)

[ServletContext](https://docs.oracle.com/javaee/7/api/javax/servlet/ServletContext.html)

#### 9.3 Http 协议文档

[Http 协议文档](https://www.rfc-editor.org/rfc/rfc2616)

[Http Header 介绍](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers)

#### 9.4 Java 学习文档

[javase-tutorial](https://docs.oracle.com/javase/tutorial/)

[类加载](https://docs.oracle.com/javase/7/docs/technotes/guides/lang/cl-mt.html)

#### 9.5 必要的工具安装

[git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)

[jdk](https://www.oracle.com/java/technologies/downloads/)

[maven](https://maven.apache.org/install.html)
