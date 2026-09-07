# Service注解
- int executes() default 0:限制某个dubbo服务使用的最大线程数量时
- group：当一个接口有多种实现时，可以用group区分
    - @Service(executes = 2,group = "DubboTest")
    - @Service(executes = 2,group = "DubboTest2")
    
- version：当一个接口的实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用；
- loadbalance:
    - RoundRobinLoadBlance轮询（roundrobin)
    - 随机调用负载均衡（random）
    - LeastActiveLoadBlance(leastactive)：最少活跃数调用法
    - ConsistentHashLoadBalance一致性Hash算法(consistenthash)    
```
Class<?> interfaceClass() default void.class;

    String interfaceName() default "";

    String version() default "";

    String group() default "";

    String path() default "";

    boolean export() default false;

    String token() default "";

    boolean deprecated() default false;

    boolean dynamic() default false;

    String accesslog() default "";
    限制某个dubbo服务使用的最大线程数量时
    int executes() default 0;

    boolean register() default false;

    int weight() default 0;

    String document() default "";

    int delay() default 0;

    String local() default "";

    String stub() default "";

    String cluster() default "";

    String proxy() default "";

    int connections() default 0;

    int callbacks() default 0;

    String onconnect() default "";

    String ondisconnect() default "";

    String owner() default "";

    String layer() default "";

    int retries() default 0;

    String loadbalance() default "";

    boolean async() default false;

    int actives() default 0;

    boolean sent() default false;

    String mock() default "";

    String validation() default "";

    int timeout() default 0;

    String cache() default "";

    String[] filter() default {};

    String[] listener() default {};

    String[] parameters() default {};

    String application() default "";

    String module() default "";

    String provider() default "";

    String[] protocol() default {};

    String monitor() default "";

    String[] registry() default {};

    int warning() default 3000;

```