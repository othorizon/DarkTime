# spring boot Configuration

## 配置Spring Boot通过@ConditionalOnProperty来控制Configuration是否生效

[配置Spring Boot通过@ConditionalOnProperty来控制Configuration是否生效](https://blog.csdn.net/dalangzhonghangxing/article/details/78420057)

```java

@Configuration
@AutoConfigureBefore({NotifierListenerConfiguration.class,CompositeNotifierConfiguration.class})
public class EmailNotifierConfiguration {

    @Bean
    @ConditionalOnMissingBean
    // @ConditionalOnProperty("admin.notify.email.to")
    @ConditionalOnProperty(value="admin.notify.email.to", havingValue = "true")
    public EmailNotifier mailNotifier() {
        return new EmailNotifier();
    }

    @ConfigurationProperties("admin.notify.email")
    public class EmailNotifier extends AbstractStatusChangeNotifier {
        private String to[];
        private String cc[];
        @Override
        protected void doNotify(ClientApplicationEvent event) throws Exception {
            System.out.println("XXXXX");
        }

        public String[] getTo() {
            return to;
        }

        public void setTo(String[] to) {
            this.to = to;
        }

        public String[] getCc() {
            return cc;
        }

        public void setCc(String[] cc) {
            this.cc = cc;
        }
    }
}

```

```properties application.properties
admin.notify.email.to=aa
admin.notify.email.cc=bb
```