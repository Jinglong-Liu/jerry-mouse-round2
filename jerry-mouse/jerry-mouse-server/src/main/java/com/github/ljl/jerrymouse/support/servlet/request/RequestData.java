package com.github.ljl.jerrymouse.support.servlet.request;

import com.github.ljl.jerrymouse.support.context.ApplicationContext;
import com.github.ljl.jerrymouse.support.servlet.session.HttpSessionFactory;
import com.github.ljl.jerrymouse.support.servlet.session.HttpSessionWrapper;
import com.github.ljl.jerrymouse.utils.HttpUtils;
import com.github.ljl.jerrymouse.utils.SessionUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.ReadListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 17:17
 **/

@Data
public class RequestData {

    private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
    private static final String CHARACTER_ENCODING_NAME = "charset";
    private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";
    private static final String CONTENT_ENCODING_HEADER_NAME = "Content-Encoding";

    private final String requestMessage;

    private final Map<String, String> headers;

    private final Map<String, String[]> parameters;

    private final Map<String, Object> attributes = new HashMap<>();

    private final Cookie[] cookies;

    private final String body;

    private String uri;

    private String method;

    private String requestURI;

    private final HttpServletRequest request;
    @Getter
    private ServletInputStream inputStream;

    @Getter
    @Setter
    private HttpSession session;

    @Setter
    @Getter
    private String protocol;

    public RequestData(HttpServletRequest request, String requestMessage) {
        this.request = request;
        this.requestMessage = requestMessage;
        this.headers = RequestParser.parseHeaders(requestMessage);
        this.cookies = RequestParser.parseCookies(this.headers);
        this.parameters = RequestParser.parseQueryParams(requestMessage);
        this.body = RequestParser.parseRequestBody(requestMessage);
        this.inputStream = new ServletInputStreamWrapper(this.body);
        RequestParser.parseRequestLine(requestMessage, this);
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.headers.putAll(headers);
    }

    public Enumeration<String> getHeaders(String s) {
        if (headers.containsKey(s)) {
            return Collections.enumeration(Collections.singletonList(headers.get(s)));
        }
        return Collections.emptyEnumeration();
    }

    public long getDateHeader(String s) {
        if (headers.containsKey(s)) {
            return Long.parseLong(headers.get(s));
        }
        return 0;
    }

    public String getHeader(String s) {
        return headers.get(s);
    }

    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    public int getIntHeader(String s) {
        return Integer.parseInt(headers.getOrDefault(s, "0"));
    }

    public void setQueryParams(Map<String, String[]> queryParams) {
        parameters.clear();
        parameters.putAll(queryParams);
    }
    public String getParameter(String name) {
        return parameters.getOrDefault(name, new String[1])[0];
    }

    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameters.keySet());
    }

    public String[] getParameterValues(String name) {
        return parameters.getOrDefault(name, new String[0]);
    }

    public Map<String, String[]> getParameterMap() {
        return parameters;
    }

    public void removeAttribute(String name) {
        synchronized (attributes) {
            if (attributes.containsKey(name)) {
                Object value = attributes.get(name);
                attributes.remove(name);
                // removed
                ApplicationContext applicationContext = (ApplicationContext) request.getServletContext();
                applicationContext.requestAttributeRemoved(request, name, value);
            }
        }
    }

    public void setAttribute(String name, Object o) {
        synchronized (attributes) {
            if (attributes.containsKey(name)) {
                if (!attributes.get(name).equals(o)) {
                    attributes.put(name, o);
                    // replaced
                    ApplicationContext applicationContext = (ApplicationContext) request.getServletContext();
                    applicationContext.requestAttributeReplaced(request, name, o);
                }
            } else {
                // added
                attributes.put(name, o);
                ApplicationContext applicationContext = (ApplicationContext) request.getServletContext();
                applicationContext.requestAttributeAdded(request, name, o);
            }
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public String getContentType() {
        return headers.get(CONTENT_TYPE_HEADER_NAME);
    }

    public int getContentLength() {
        return Integer.parseInt(headers.getOrDefault(CONTENT_LENGTH_HEADER_NAME, "-1"));
    }

    public long getContentLengthLong() {
        return Long.parseLong(headers.getOrDefault(CONTENT_LENGTH_HEADER_NAME, "-1"));
    }

    public String getCharacterEncoding() {
        final String contentType = headers.getOrDefault(CONTENT_TYPE_HEADER_NAME, "");
        return HttpUtils.extractCharacterEncoding(contentType);
    }

    public void setCharacterEncoding(String charSet) {
        String oldContentType = headers.getOrDefault(CONTENT_TYPE_HEADER_NAME, "");
        String contextType = HttpUtils.replaceCharacterEncoding(charSet, oldContentType);
        headers.put(CONTENT_TYPE_HEADER_NAME, contextType);
    }

    public HttpSession getSession(boolean create) {
        final ApplicationContext applicationContext = (ApplicationContext) request.getServletContext();
        final String sessionIdFieldName = applicationContext.getSessionIdFieldName();
        // 同一个request, 已经有了session
        if (isRequestedSessionIdValid()) {
            return session;
        }
        // sessionId
        if (Objects.nonNull(cookies)) {
            String sessionId = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().trim().equalsIgnoreCase(sessionIdFieldName))
                    .map(Cookie::getValue)
                    .findFirst().orElse(null);
            if (Objects.nonNull(sessionId)) {
                HttpSession httpSession = applicationContext.findSession(sessionId);
                if (Objects.nonNull(httpSession)) {
                    return httpSession;
                }
            }
        }

        if (create) {
            session = HttpSessionFactory.createHttpSession(request);
        } else {
            session = null;
        }
        return session;
    }
    /**
     * Returns the current session associated with this request,
     * or if the request does not have a session, creates one.
     * @return
     */
    public HttpSession getSession() {
        if (!isRequestedSessionIdValid()) {
            session = HttpSessionFactory.createHttpSession(this.request);
        }
        return session;
    }

    public String changeSessionId() {
        String oldId = session.getId();
        ((HttpSessionWrapper)session).setId(SessionUtils.generateSessionId());
        ApplicationContext applicationContext = (ApplicationContext) request.getServletContext();
        applicationContext.changeSessionId(session, oldId);
        return session.getId();
    }

    public boolean isRequestedSessionIdValid() {
        if (Objects.isNull(session)) {
            return false;
        }
        // 判断session是否合法
        HttpSessionWrapper httpSession = (HttpSessionWrapper) session;
        return httpSession.isValid();
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    class ServletInputStreamWrapper extends ServletInputStream {

        private ByteArrayInputStream byteArrayInputStream;

        public ServletInputStreamWrapper(String body, Charset charset) {
            byte bytes[] = body.getBytes(charset);
            this.byteArrayInputStream = new ByteArrayInputStream(bytes);
        }
        public ServletInputStreamWrapper(String body) {
            this(body, Charset.defaultCharset());
        }
        @Override
        public boolean isFinished() {
            return byteArrayInputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException("ReadListener is not supported");
        }

        @Override
        public int read() {
            return byteArrayInputStream.read();
        }
    }
}
