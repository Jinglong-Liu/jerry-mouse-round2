## jerry-mouse prd 08

### 支持 Session 和 Cookie

| 版本号 | 发布日期       | 变更内容                                  | 负责人 |
|-----|------------|---------------------------------------|-----|
| 1.8 | 2024-07-08 | 添加 [jerry-mouse prd08.md](./prd08.md) | ljl |

### 1、项目背景
为学习类JavaWeb中的Session和Cookie的基本使用，并让servlet编写者可以使用Cookie, Session以及其Listener的基本功能，需要完善服务器的功能

### 2、需求描述
#### 2.1 功能需求

1、程序启动时，加载web.xml中的SessionConfig配置，使以下配置生效：
```xml
<session-config>
<!--        单位:分钟， <=0 (默认)表示没有时限-->
        <session-timeout>1</session-timeout>
        <cookie-config>
<!--        默认sessionId字段名称JSESSIONID, 可以在此处指定 -->
            <name>JSESSIONID</name>
<!--        默认，代表app下的所有request url，暂时只支持'/'-->
            <path>/</path>
        </cookie-config>
<!--        暂时只支持COOKIE跟踪模式-->
        <tracking-mode>COOKIE</tracking-mode>
</session-config>
```


2、让Servlet编写者可以正确使用Session，并支持其相关Listener配置

3、解析报文时，增加Cookie的解析，request header中，key为"Cookie"的行即为Cookie, 形式
```bash
Cookie: key1=value1; key2=value2
```

4、编写测试用例，完成一个登录校验的例子(模拟后端实现接口即可，无需界面),sessionId字段名默认为：`JSESSIONID`，也可以自行设置
```bash
# root是本地服务名，请换成其他对应app name
# 数据写死即可，仅模拟测试用，无需数据库支持
# 测试的servlet只能使用javax.servlet标准接口
# 1
> POST http://127.0.0.1:8888/root/login?username=admin&password=admin
返回
header:
Set-Cookie: JSESSIONID=17204401364177e3a3df); Expires=Mon, 08 Jul 2024 13:02:16 GMT

body:
{
    "message": "Login successful",
    "sessionId": "17204401364177e3a3df"
}
# 其中sessionId由时间+随机数，在创建时生成，expireTime用setMaxInactiveInterval生成，设置较长的时间
# Set-Cookie的字段，postman可以在cookie界面设置，下次直接带上Cookie，不需要每次拷贝
# 输入的username或password错误，则返回
header: 不存在Set-Cookie字段, 或Set-Cookie里面没有sessionId字段
body
{
    "message": "Invalid credentials"
}

# 2
> GET http://127.0.0.1:8888/root/welcome
# 其中Cookie带上刚才返回的sessionId, 例如JSESSIONID=1720440250034d2365a0b;
# 其实Cookie也就是Header的一个名为Cookie的字段而已
# 要求服务端校验cookie中的sessionId
# 校验成功, 返回
body
{
    "message": "SessionId checked! Welcome, admin"
}

# 校验失败(Cookie中没有JSESSIONID字段或没有Cookie，或带的sessionId在服务端查询不到), 返回
body
{
    "message": "Unauthorized"
}

# 3
> POST http://127.0.0.1:8888/root/logout
# Cookie中带上正确的sessionId, 则返回
{
    "message": "Logout successful"
}
# 没有带上正确的sessionId/已经logout, 则返回
{
    "message": "Unauthorized. Can't logout"
}

# 退出后, 返回到未登录的状态, 然后测试/welcome或/logout接口，与未登录时一致
# session超时失效后，效果同未登录
# 同时测试几个listener的效果
```


#### 2.2 非功能需求

要求使用动态代理的机制(不要引入spring-framework)，实现在调用某些session方法前，对session进行校验

要求高可用，用postman和浏览器反复测试，可以稳定复现结果，不会出现未响应的情况

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
    setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes)
    Set<SessionTrackingMode> getDefaultSessionTrackingModes();
    Set<SessionTrackingMode> getEffectiveSessionTrackingModes();
}
```

#### 输出文档
- 输出本章节的实现方案和经验总结文档

### 4、完成时间
预计用时 1.5 DAY，要求 `2024.7.9` 提交代码，文档`2024.8.31前`统一完成
