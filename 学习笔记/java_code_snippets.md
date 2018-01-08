# java code snippets


枚举的巧妙用法

```java
public enum Env {

        INNER("inner") {
            @Override
            public String env() {
                return "";
            }
        },

        COMMON("common") {
            @Override
            public String env() {
                return "";
            }
        },

        TEST("test") {
            @Override
            public String env() {
                return "_test";
            }
        };

        public final String env;

        Env(String evn) {
            this.env = evn;
        }

        public abstract String env();

        public static Env of(final String evn) {
            Env[] envs = Env.values();
            for (Env env : envs) {
                if (env.env.equalsIgnoreCase(evn)) {
                    return env;
                }
            }
            throw new BossBizException("不合法的env:" + Objects.toString(evn));
        }
    }
```

