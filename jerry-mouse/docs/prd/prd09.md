## jerry-mouse prd 08

### 支持 Session 和 Cookie

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.9 | 2024-07-10 | 添加 [jerry-mouse prd09.md](./prd09.md) | ljl |

### 1、项目背景
为适应使用springmvc框架的进行前后端分离开发(只实现后端，不用管页面)的需求，需要完善服务器的功能

### 2、需求描述
#### 2.1 功能需求

1、程序启动时，能够根据springmvc项目的web.xml中的DispatcherServlet，将所有的请求由框架内部的DispatcherServlet分发处理，在jerry-mouse上跑通springmvc的war包的Hello World

2、让Servlet编写者可以正确使用Session，并支持其相关Listener配置

3、采用前后端分离的模式，用springmvc框架实现登录、欢迎、登出三个接口，用apifox(推荐)或者postman测试
并在jerry-mouse服务器上跑通

要求

3.1 登录接口： 
```
url: /<app>/auth/login method: POST
```
body:
```json
{
    "username":"username",
    "password":"password"
}
```
登录成功，返回
```json
{
    "status": 200,
    "message": "Login successful",
    "data": {
        "user": {
            "userId": "id001",
            "username": "username",
            "password": "password",
            "desc": "desc1",
            "sessionId": "1720577789483817ce92c"
        }
    }
}
```
// 登录失败(用户名或密码错误)，返回
cookie:
```
JSESSIONID: <生成的sessionId>
```
body
```json
{
  "status": 401,
  "message": "Invalid credentials",
  "data": {}
}
```
可以设置后置操作，存到全局变量中，方便后续接口读取

3.2 欢迎接口：
```
url: /<app>/welcome method: GET
cookie: 带上JSESSION(变量名与登录成功返回的session相同即可)
```
session校验有效，返回
body
```json
{
    "status": 200,
    "message": "Welcome",
    "data": {
        "user": {
            "userId": "id001",
            "username": "username",
            "password": "password",
            "desc": "desc1",
            "sessionId": "1720577789483817ce92c"
        }
    }
}
```
session校验失败，返回
body
```json
{
    "status": 401,
    "message": "Unauthorized, I can't welcome you",
    "data": {}
}
```

3.3 登出接口
```
url: /<app>/auth/logout method: POST
cookie: 带上JSESSION(变量名与登录成功返回的session相同即可)
```
校验成功，退出登录，返回
```json
{
    "status": 200,
    "message": "Logout successful",
    "data": {
        "user": {
            "userId": "id001",
            "username": "username",
            "password": "password",
            "desc": "desc1",
            "sessionId": "1720578779199e5b444fa"
        }
    }
}
```
并退出登录，改为未登录的状态

校验失败，返回
```json
{
    "status": 401,
    "message": "Unauthorized, can't logout",
    "data": {}
}
```

其中User数据在service层模拟实现即可，不需要加数据库。

#### 2.2 非功能需求

要求使用动态代理的机制(不要引入spring-framework)，实现在调用某些session方法前，对session进行校验

login demo要求使用前后端分离的架构(只需要后端接口)完成，且返回json格式，允许在用户不手动设置response.setContentType的前提下正确允许

login demo要求打成war包，放在jerry-mouse的webapp目录下运行，且jerry-mouse本身不允许加spring依赖库(可以加上方便调试，后面删除)

要求高可用，用apifox或postman测试，可以稳定复现结果，不会出现未响应的情况

### 3、实现要求
#### 实现接口

1、让下列接口生效
```java
public interface HttpSession {/* 所有方法 */}
public interface HttpSessionListener extends EventListener {/* 所有方法 */}
public interface HttpSessionAttributeListener extends EventListener {/* 所有方法 */}

public interface HttpServletRequest extends ServletRequest {
    String getRequestedSessionId();
    HttpSession getSession(boolean create);
    HttpSession getSession();
    String changeSessionId();
    boolean isRequestedSessionIdValid();
    Cookie[] getCookies();
}
// 目前只支持到COOKIE即可，因此后两者都先返回COOKIE即可，set也只先允许set进COOKIE方式
public interface ServletContext {
    setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes);
    Set<SessionTrackingMode> getDefaultSessionTrackingModes();
    Set<SessionTrackingMode> getEffectiveSessionTrackingModes();
}

public interface HttpServletResponse extends ServletResponse {
    void addCookie(Cookie cookie);
}
```

#### 输出文档
- 输出springmvc数据类型转化(messageConvert)流程以及注意事项
- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 1.5 DAY，要求 `2024.7.10` 提交代码，文档`2024.8.31前`统一完成
