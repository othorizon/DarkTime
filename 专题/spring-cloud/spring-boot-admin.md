# spring boot admin 监控服务

## 重写spring boot的邮件通知服务

### 原理

查阅源码可以看到 邮件发送是调用了`MailSender`的实现类，在`org.springframework.mail.javamail`包中`JavaMailSenderImpl`实现了该接口，

```java
@Configuration
@ConditionalOnBean(MailSender.class)
@AutoConfigureAfter({ MailSenderAutoConfiguration.class })
@AutoConfigureBefore({ NotifierListenerConfiguration.class,
    CompositeNotifierConfiguration.class })
public static class MailNotifierConfiguration {
    @Autowired
    private MailSender mailSender;

    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties("spring.boot.admin.notify.mail")
    public MailNotifier mailNotifier() {
        return new MailNotifier(mailSender);
    }
}
```

继续查阅`JavaMailSenderImpl`的配置文件代码可以看到`JavaMailSenderImpl`的注册配置中启用了`@ConditionalOnMissingBean(MailSender.class)`这个注解，所以只要重新写代码实现`MailSender`接口就可以替代原有的邮件发送代码

```java
@Configuration
@ConditionalOnClass({ MimeMessage.class, MimeType.class })
@ConditionalOnMissingBean(MailSender.class)
@Conditional(MailSenderCondition.class)
@EnableConfigurationProperties(MailProperties.class)
@Import(JndiSessionConfiguration.class)
public class MailSenderAutoConfiguration {

    private final MailProperties properties;

    private final Session session;

    public MailSenderAutoConfiguration(MailProperties properties,
            ObjectProvider<Session> session) {
        this.properties = properties;
        this.session = session.getIfAvailable();
    }

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        if (this.session != null) {
            sender.setSession(this.session);
        }
        else {
            applyProperties(sender);
        }
        return sender;
    }
    //....省略代码
}
```

### 实现

以下是具体的实现代码

```java
import com.google.gson.Gson;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * Created by Rizon on 2018/4/18.
 */
@Component
public class EmailSender implements MailSender {
    private final static Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);

    @Value("${mail.server.url}")
    private String EMAIL_SERVER_URL;

    @Override
    public void send(SimpleMailMessage simpleMailMessage){
        this.send(new SimpleMailMessage[]{simpleMailMessage});
    }

    @Override
    public void send(SimpleMailMessage... simpleMailMessages){
        for (SimpleMailMessage simpleMailMessage : simpleMailMessages) {
            String to = StringUtils.join(simpleMailMessage.getTo(), ",");
            String cc = StringUtils.join(simpleMailMessage.getCc(), ",");
            String subject = simpleMailMessage.getSubject();
            String body = simpleMailMessage.getText();
            doSend(to, cc, subject, body);
        }
    }

    public void doSend(String receiver, String ccAddress, String subject, String body) {
        Email email = new Email(receiver, ccAddress, subject, body);
        LOGGER.info(String.format("Send-Email-Request: [url=%s, to=%s, subject=%s, body=%s]", EMAIL_SERVER_URL,
                email.receiver, email.subject, email.body));
        HttpRequest request = HttpRequest.post(EMAIL_SERVER_URL).multipart(true);

        HttpResponse response = request
                .form("sendEmailInfo", new Gson().toJson(email)).timeout(5000).send();
        LOGGER.info(String.format("Send-Email-Response: [response=%s]", response.bodyText()));
    }
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Email {
        private String receiver;   //多收件人逗号分隔
        private String ccAddress;
        private String subject;
        private String body;
    }
}

```