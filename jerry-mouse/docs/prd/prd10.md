## jerry-mouse prd 10

### 支持 Spring Boot

| 版本号  | 发布日期       | 变更内容                                  | 负责人 |
|------|------------|---------------------------------------|-----|
| 1.10 | 2024-07-11 | 添加 [jerry-mouse prd10.md](./prd10.md) | ljl |

### 1、项目背景

为适应使用springboot框架的进行的开发，需要嵌入式Web服务器替代原本默认的Tomcat服务器

因此需要构建相关的接口，让用户可以方便地使用jerry-mouse替换掉tomcat，使用springboot框架进行开发

### 2、需求描述
#### 2.1 功能需求

1、SpringBoot web项目中，剔除掉tomcat的依赖，加上jerry-mouse的依赖，并实现一个构造jerry-mouse-server的Factory后，
启动SpringBoot项目，跑通hello world。

2、用Bearer Token方式， 生成jwt进行鉴权，实现`登录-欢迎-刷新权限-登出`接口，在剔除了`Tomcat`,改用`jerry-mouse`的`SpringBoot`上运行
使用apifox等工具设置前后置操作，编写接口，进行测试

3、要求编写SpringBoot web应用的程序员可以使用.yml等方式修改服务器启动端口，合法区间1024-65535，不设置或非法值则使用默认端口8080，有冲突则依次递增。

设置的key为server.port，和协议一致。

### 3、实现要求

#### 项目结构
要求测试项目Spring Boot Application 的启动类如下(名称不限，下同)
```java
@SpringBootApplication
// 引入JerryMouse对应的webFactory
@Import(JerryMouseWebServerConfig.class)
public class Application1 {
    public static void main(String[] args) {
        SpringApplication.run(Application1.class);
    }
}
```
部分maven依赖如下
```xml
<dependency>
    <groupId>com.github</groupId>
    <artifactId>spring-boot-starter-jerry-mouse</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

新建模块spring-boot-starter-jerry-mouse，在其中编写WebServer工厂`JerryMouseWebServerConfig`, 部分maven依赖如下
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>2.6.3</version>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-tomcat</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    <dependency>
        <groupId>com.github</groupId>
        <artifactId>jerry-mouse-server</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

jerry-mouse-server 即为之前写的服务器，里面不允许添加任何spring依赖，做到轻量级

token鉴权分别实现本地无状态和redis缓存有效期两种TokenService

#### 实现接口 

应用层：使用SpringBoot 框架， 实现下面四个Http接口, 用嵌入式jerry-mouse运行

（root是项目名）

---

- 1、登录接口

`/root/auth/login`

body: json格式
```json
{
    "username": "username",
    "password": "password"
}
```
成功，返回用户信息，Header上带上Authorization字段，"Bearer " + token
失败，返回401

客户端设置后置操作：提取Response Header的Authorization字段到全局变量Authorization

---

- 2、欢迎接口

/root/auth/welcome
前置操作：设置Auth 类型Bearer Token，并提取变量Authorization到Token(下同，不赘述)
实际上看打印出的报文即可发现，就是Header字段加上了Authorization

服务端校验，成功，返回用户信息和欢迎信息；失败，返回401

---

- 3、刷新权限接口
/root/auth/refresh
服务端校验，成功，返回用户信息和欢迎信息，并将新的token返回，下次客户端带上。刷新有效期

---

- 4、登出接口
/root/auth/logout
服务端校验，成功，登出(让token失效)；失败，返回401
登出接口的校验需要使用spring-aop的机制实现，并在返回的message中体现。

---

jerry-mouse：

1、新建spring-boot-starter-jerry-mouse模块，实现 `JerryMouseWebServerConfig`，用于
生成jerry-mouse webserver, 让用户无感知地(除了开始的时候import)使用jerry-mouse，取代默认的Tomcat

2、补充完成让Spring Boot App 正常跑起来的全部接口

关注 FilterRegistration.Dynamic 和 ServletRegistration.Dynamic 接口的实现，尚未实现的接口打Error级别的log，若不影响运行，抛异常

3、选用自行实现的reactor框架，不要直接使用netty，会出现问题(为什么？)

#### 输出文档

- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 2 DAY，要求 `2024.7.11` 提交代码，文档`2024.8.31前`统一完成
