# java 开发常用工具类

## http请求

[java实现HTTP请求的三种方式](https://www.cnblogs.com/hhhshct/p/8523697.html)

[利用HttpClient4，实现get，post 参数，post json，post file - CSDN博客](https://blog.csdn.net/happy814506779/article/details/80432458)

org.apache.http.client.HttpClient
httpclient 4.x版

```xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.6</version>
</dependency>
```

```java
package datacenter.plugins.el.utils;


import datacenter.plugins.el.common.PluginException;
import org.apache.commons.collections4.MapUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * http请求工具
 *
 * @author Rizon
 * @date 2018/9/12
 */

public class HttpUtils {
    private final static Logger log = Logger.getLogger(HttpUtils.class);

    private final static int CONNECT_TIMEOUT;
    private final static int SOCKET_TIMEOUT;
    private final static HttpClient HTTP_CLIENT;

    static {
        CONNECT_TIMEOUT = Integer.valueOf(ConfigUtil.getOrDefaultValue("http.connect-timeout", "3000"));
        SOCKET_TIMEOUT = Integer.valueOf(ConfigUtil.getOrDefaultValue("http.socket-timeout", "6000"));

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setRetryHandler(
                new DefaultHttpRequestRetryHandler(3, true));
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(SOCKET_TIMEOUT)
                .setSocketTimeout(CONNECT_TIMEOUT)
                .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);
        HTTP_CLIENT = httpClientBuilder.build();
    }

    public static String get(String url, List<NameValuePair> para) {
        URI uri;
        try {
            uri = new URIBuilder(url)
                    .addParameters(para)
                    .build();
        } catch (URISyntaxException e) {
            log.error("URISyntaxException", e);
            throw new PluginException("URISyntaxException", e);
        }
        return get(uri);
    }

    public static String get(String url, Map<String, String> para) {
        URI uri;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            MapUtils.emptyIfNull(para).forEach(uriBuilder::addParameter);
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            log.error("URISyntaxException", e);
            throw new PluginException("URISyntaxException", e);
        }
        return get(uri);
    }

    public static String get(URI uri) {
        log.info("http get: " + uri);
        try {
            HttpResponse response = HTTP_CLIENT.execute(new HttpGet(uri));
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                System.out.println(response.getStatusLine().getStatusCode());
                return EntityUtils.toString(response.getEntity());
            } else {
                log.error("http status code!=200");
                throw new PluginException("http status code!=200");
            }
        } catch (IOException e) {
            log.error("http request IOException", e);
            throw new PluginException("http request IOException", e);
        }

    }
}

```

---

org.springframework.web.client.RestTemplate  
 spring的好用的htto请求工具，但是包含在spring-web中，不适合单独使用

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>5.0.4.RELEASE</version>
</dependency>
```

org.apache.commons.httpclient.HttpClient
apache的http工具3.x版，封装的不是很好，使用过于繁琐

```xml
<dependency>
            <groupId>commons-httpclient</groupId>
            <artifactId>commons-httpclient</artifactId>
            <version>3.1</version>
        </dependency>
```
