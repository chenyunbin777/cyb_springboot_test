# 第7讲 | int和Integer有什么区别？

- Integer缓存：默认是 -128~127
    - 这个缓存的范围是可以修改的， 通过jvm提供的参数：-XX:AutoBoxCacheMax=N       
- 自动装箱拆箱：发生在编辑阶段javac，也就是生成的字节码是一致的。
    - 装箱：Integer.valueOf()
    - 拆箱：Integer.intValue()
- 一个是对象一个是基本数据类型
    
# 其他包装类
- Boolean 缓存了true false
- Short 同样是缓存了-128~127
- Byte 数值有限全部缓存
- Character：缓存范围'\u0000' ~ '\u007F' ,   '\u则代表unicode编码，是一个字符；
    