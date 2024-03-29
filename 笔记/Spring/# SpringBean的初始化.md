# SpringBean的初始化

1 Spring通过反射的方式 实例化我们的对象
2 对实例化好的对象进行属性赋值
3 初始化：调用我们的init-method 方法
4 之后会将实例添加到 单例池中，其实就是一个Map（singletonObjects 一级缓存）， 通过Spring上下文 getBean("")的时候就是去map中去对应的实例。
5 BeanPostProcessor：bean的后置处理器
    该接口提供了两个函数：
    1 postProcessBeforeInitialzation( Object bean, String beanName )
    当前正在初始化的bean对象会被传递进来，我们就可以对这个bean作任何处理。
    这个函数会先于InitialzationBean执行，因此称为前置处理。
    所有Aware接口的注入就是在这一步完成的。
    2 postProcessAfterInitialzation( Object bean, String beanName )
    当前正在初始化的bean对象会被传递进来，我们就可以对这个bean作任何处理。
    这个函数会在InitialzationBean完成后执行，因此称为后置处理。

5.1 在一个就是使用给中接口方法 为bean初始化前后和销毁前后做一些事情。 例如上边的接口。



原文链接：https://blog.csdn.net/zhangmingan123/article/details/111178263
# 1 Spring 循环依赖如何解决？ 提前暴露 循环依赖对象的半成品
三级缓存：
# 2 Spring通过三级缓存来解决。 为什么要使用三级缓存？二级缓存行不行？
- 不行：
- 成品放在singletonObjects中，半成品放在earlySingletonObjects中
- 流程可以这样走：
    - 实例化A ->将半成品的A放入earlySingletonObjects中 ->填充A的属性时发现取不到B->
    实例化B->将半成品的B放入earlySingletonObjects中->从earlySingletonObjects中取出A填充B的属性->将成品B放入singletonObjects,并从earlySingletonObjects中删除B->
    将B填充到A的属性中->将成品A放入singletonObjects并删除earlySingletonObjects。
    - 1 实例化 A，此时 A 还未完成属性填充和初始化方法（@PostConstruct）的执行，A 只是一个半成品。
      2 为 A 创建一个 Bean工厂，并放入到 singletonFactories 中。
      3 发现 A 需要注入 B 对象，但是一级、二级、三级缓存均为发现对象 B。
      4 实例化 B，此时 B 还未完成属性填充和初始化方法（@PostConstruct）的执行，B 只是一个半成品。
      5 为 B 创建一个 Bean工厂，并放入到 singletonFactories 中。
      6 发现 B 需要注入 A 对象，此时在一级、二级未发现对象
      A，但是在三级缓存中发现了对象 A，从三级缓存中得到对象 A，并将对象 A 放入二级缓存中，同时删除三级缓存中的对象 A。（注意，此时的 A
      还是一个半成品，并没有完成属性填充和执行初始化方法）
      7 将对象 A 注入到对象 B 中。
      8 对象 B 完成属性填充，执行初始化方法，并放入到一级缓存中，同时删除二级缓存中的对象 B。（此时对象 B 已经是一个成品）
      9 对象 A 得到对象B，将对象 B 注入到对象 A 中。（对象 A 得到的是一个完整的对象 B）
      10 对象 A完成属性填充，执行初始化方法，并放入到一级缓存中，同时删除二级缓存中的对象 A。
      原文链接：https://blog.csdn.net/cristianoxm/article/details/113246104
这样的流程是线程安全的，**不过如果A上加个切面（AOP）**，这种做法就没法满足需求了，
因为earlySingletonObjects中存放的都是原始对象，而我们需要注入的其实是A的代理对象。


- 第一级缓存：Map<String, Object> singletonObjects：存储完整版的实例
- 第二级缓存：Map<String, Object> earlySingletonObjects：存储半成品，没有赋初值的对象实例
- 第三级缓存：Map<String, ObjectFactory<?>> singletonFactories：生成一个对象的代理，如果对象上有aop的实现就可以直接通过
getObject()获取代理类。







- 答：二级缓存的作用：保存了暴露的早期对象，这个早期对象就是 还没有给属性赋初值的对象。 就为了在多线程的情况下， Thread1 执行完A的 Object bean = Clazz.getInstance()，创建了实例。 此时B过来 getBean(A)，返回的时候不完整的A。
     二级缓存就保存的是上边的bean（不完整，属性没有赋初值）。 一级缓存保存的是一个完整的bean（Object bean = Clazz.getInstance()  bean.getFields()中的所有属性也进行了赋值）
     - 三级缓存中提到出现循环依赖才去解决，也就是说出现循环依赖时，才会执行工厂的getObject生成(获取)早期依赖，
     这个时候就需要给它挪个窝了，因为真正暴露的不是工厂，而是对象，所以需要使用一个新的缓存保存暴露的早期对象(earlySingletonObjects)，
     同时移除提前暴露的工厂，也不需要在多重循环依赖时每次去执行getObject(虽然个人觉得不会出问题，因为代理对象不会重复生成，详细可以了解下代理里面的逻辑，如wrapIfNecessary)。  
- 为什么要使用三级缓存？
    ObjectFactory？
    - 如果仅仅是解决循环依赖问题，使用二级缓存就可以了，但是如果对象实现了AOP，那么注入到其他bean的时候，
    并不是最终的代理对象，而是原始的。这时就需要通过三级缓存的ObjectFactory才能提前产生最终的需要代理的对象。
    - **这个bean工厂不仅可以暴露早期bean还可以暴露代理bean，如果存在aop代理**，则**依赖的应该是代理对象**，而不是原始的bean。
     
    
3 构造函数的循环依赖？
- Spring没有解决，因为加入singletonFactories三级缓存的前提是执行了构造器（Object bean = Clazz.getInstance()执行），
所以构造器的循环依赖没法解决。
4 多例bean的循环依赖如何处理？
- 答：多例bean每次都会创建新的bean，也就是scope = prototype ，不会用到缓存，没法解决循环依赖。


A 依赖 B b属性。 
B 依赖 A a属性。 如下：
```
@Service
public class A {  
    @Autowired  
    private B b;
}

@Service
public class B {  
    @Autowired  
    public C c;
}
```


一级缓存singletonObjects：单例对象的cache，保存的是实例化之后并且已经赋初值的实例
二级缓存earlySingletonObjects ：提前暴光的单例对象的Cache，就是bean刚刚调用了构造方法，还没来的及给bean的属性赋值的对象。
三级缓存singletonFactories ： 单例对象工厂的cache


```
Spring 在注入属性的时候是如何去获取依赖的呢？
他是通过一个getSingleton()方法去获取所需要的 Bean 的。

protected Object getSingleton(String beanName, boolean allowEarlyReference) {
    Object singletonObject = this.singletonObjects.get(beanName);
    //一级缓存为空并且这个bean是正在创建
    if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
        synchronized (this.singletonObjects) {
            //在尝试判断二级缓存是否存在
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                //不存在二级缓存，在判断三级缓存中是否存在
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    //如果有 则三级缓存升级到二级缓存，把三级缓存删除
                    this.earlySingletonObjects.put(beanName, singletonObject);
                    this.singletonFactories.remove(beanName);
                }
            }
        }
    }
    return (singletonObject != NULL_OBJECT ? singletonObject : null);
}
```





- 1. 实例化Bean
对于BeanFactory容器，当客户向容器请求一个尚未初始化的bean时，或初始化bean的时候需要注入另一个尚未初始化的依赖时，容器就会调用createBean进行实例化。
对于ApplicationContext容器，当容器启动结束后，便实例化所有的bean。
容器通过获取BeanDefinition对象中的信息进行实例化。并且这一步仅仅是简单的实例化，并未进行依赖注入。
实例化对象被包装在BeanWrapper对象中，BeanWrapper提供了设置对象属性的接口，从而避免了使用反射机制设置属性。
- 2. 设置对象属性（依赖注入）
实例化后的对象被封装在BeanWrapper对象中，并且此时对象仍然是一个原生的状态，并没有进行依赖注入。
紧接着，Spring根据BeanDefinition中的信息进行依赖注入。
并且通过BeanWrapper提供的设置属性的接口完成依赖注入。
- 3. 注入Aware接口
----BeanNameAware：	setBeanName(String name) 方法的到bean的id
--------------------------------------------------------------
----BeanFactoryAware：如果这个Bean实现了BeanFactoryAware接口，会调用它实现的setBeanFactory()，传递的是Spring工厂本身（可以用这个方法获取到其他Bean）
----ApplicationContextAware：如果这个Bean实现了ApplicationContextAware接口，会调用setApplicationContext(ApplicationContext)方法，传入Spring上下文，该方式同样可以实现步骤4，但比4更好，以为ApplicationContext是BeanFactory的子接口，有更多的实现方法
ApplicationContext的三个实现类：
a、ClassPathXmlApplication：把上下文文件当成类路径资源
b、FileSystemXmlApplication：从文件系统中的XML文件载入上下文定义信息
c、XmlWebApplicationContext：从Web系统中的XML文件载入上下文定义信息

紧接着，Spring会检测该对象是否实现了xxxAware接口，并将相关的xxxAware实例注入给bean。
**4. BeanPostProcessor**
当经过上述几个步骤后，bean对象已经被正确构造，但如果你想要对象被使用前再进行一些自定义的处理，就可以通过BeanPostProcessor接口实现。
该接口提供了两个函数：
postProcessBeforeInitialzation( Object bean, String beanName )
当前正在初始化的bean对象会被传递进来，我们就可以对这个bean作任何处理。
这个函数会先于InitialzationBean执行，因此称为前置处理。
所有Aware接口的注入就是在这一步完成的。
postProcessAfterInitialzation( Object bean, String beanName )
当前正在初始化的bean对象会被传递进来，我们就可以对这个bean作任何处理。
这个函数会在InitialzationBean完成后执行，因此称为后置处理。
**5. InitializingBean与init-method**
当BeanPostProcessor的前置处理完成后就会进入本阶段。
InitializingBean接口只有一个函数：
afterPropertiesSet()
这一阶段也可以在bean正式构造完成前增加我们自定义的逻辑，但它与前置处理不同，由于该函数并不会把当前bean对象传进来，因此在这一步没办法处理对象本身，只能增加一些额外的逻辑。
若要使用它，我们需要让bean实现该接口，并把要增加的逻辑写在该函数中。然后Spring会在前置处理完成后检测当前bean是否实现了该接口，并执行afterPropertiesSet函数。
当然，Spring为了降低对客户代码的侵入性，给bean的配置提供了init-method属性，该属性指定了在这一阶段需要执行的函数名。Spring便会在初始化阶段执行我们设置的函数。init-method本质上仍然使用了InitializingBean接口。
6. DisposableBean和destroy-method
和init-method一样，通过给destroy-method指定函数，就可以在bean销毁前执行指定的逻辑。


现在开始初始化容器
2014-5-18 15:46:20 org.springframework.context.support.AbstractApplicationContext prepareRefresh
信息: Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@19a0c7c: startup date [Sun May 18 15:46:20 CST 2014]; root of context hierarchy
2014-5-18 15:46:20 org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
信息: Loading XML bean definitions from class path resource [springBeanTest/beans.xml]
这是BeanFactoryPostProcessor实现类构造器！！
BeanFactoryPostProcessor调用postProcessBeanFactory方法
这是BeanPostProcessor实现类构造器！！
这是InstantiationAwareBeanPostProcessorAdapter实现类构造器！！
2014-5-18 15:46:20 org.springframework.beans.factory.support.DefaultListableBeanFactory preInstantiateSingletons
信息: Pre-instantiating singletons in org.springframework.beans.factory.support.DefaultListableBeanFactory@9934d4: defining beans [beanPostProcessor,instantiationAwareBeanPostProcessor,beanFactoryPostProcessor,person]; root of factory hierarchy
InstantiationAwareBeanPostProcessor调用postProcessBeforeInstantiation方法
【构造器】调用Person的构造器实例化
InstantiationAwareBeanPostProcessor调用postProcessPropertyValues方法
【注入属性】注入属性address
【注入属性】注入属性name
【注入属性】注入属性phone
【BeanNameAware接口】调用BeanNameAware.setBeanName()
【BeanFactoryAware接口】调用BeanFactoryAware.setBeanFactory()
BeanPostProcessor接口方法postProcessBeforeInitialization对属性进行更改！
【InitializingBean接口】调用InitializingBean.afterPropertiesSet()
【init-method】调用<bean>的init-method属性指定的初始化方法
BeanPostProcessor接口方法postProcessAfterInitialization对属性进行更改！
InstantiationAwareBeanPostProcessor调用postProcessAfterInitialization方法
容器初始化成功
Person [address=广州, name=张三, phone=110]
现在开始关闭容器！
【DiposibleBean接口】调用DiposibleBean.destory()
【destroy-method】调用<bean>的destroy-method属性指定的初始化方法




# Spring AOP
- 实现方式两种：
    - JDK 动态代理：带有接口的对象，在运行期实现
    - CGlib 静态代理：在编译期实现。
    
# Spring MVC 工作流程
1、 用户向服务端发送一次请求，这个请求会先到前端控制器DispatcherServlet(也叫中央控制器)。
2、DispatcherServlet接收到请求后会调用HandlerMapping处理器映射器。由此得知，该请求该由哪个Controller来处理（并未调用Controller，只是得知）
3、DispatcherServlet调用HandlerAdapter处理器适配器，告诉处理器适配器应该要去执行Controller中的那个方法
4、HandlerAdapter处理器适配器去执行Controller并得到ModelAndView(数据和视图)，并层层返回给DispatcherServlet
5、DispatcherServlet将ModelAndView交给ViewReslover视图解析器解析，然后返回真正的视图。
6、DispatcherServlet将模型数据填充到视图中
7、DispatcherServlet将结果响应给用户

# Spring 三种注入方式
- 1 构造方法注入：
<!-- 注册userService -->
<bean id="userService" class="com.lyu.spring.service.impl.UserService">
    <!-- 指定构造方法要注入的对象-->
    <constructor-arg ref="userDaoJdbc"></constructor-arg>
</bean>
<!-- 注册jdbc实现的dao -->
<bean id="userDaoJdbc" class="com.lyu.spring.dao.impl.UserDaoJdbc"></bean>

- 2 setter注入：

<!-- 注册userService -->
<bean id="userService" class="com.lyu.spring.service.impl.UserService">
    <!-- 写法一 -->
    <!-- <property name="UserDao" ref="userDaoMyBatis"></property> -->
    <!-- 写法二 -->
    <property name="userDao" ref="userDaoMyBatis"></property>
</bean>
    - 这两种写法都可以,spring会将name值的每个单词首字母转换成大写，
      然后再在前面拼接上”set”构成一个方法名,然后去对应的类中查找该方法,通过反射调用,实现注入。

<!-- 注册mybatis实现的dao -->
<bean id="userDaoMyBatis" class="com.lyu.spring.dao.impl.UserDaoMyBatis"></bean>

- 3 基于注解的注入
    - @Autowired：spring注解，默认是以byType的方式去匹配与属性名相同的bean的id，如果没有找到，就通过byName的方式去查找，
    - @Resource：java的注解，默认以byName的方式去匹配与属性名相同的bean的id，如果没有找到就会以byType的方式查找，如果byType查找到多个的话，使用@Qualifier注解（spring注解）指定某个具体名称的bean。
如果同一个接口的不同实现类可以通过这个注解来区别。

