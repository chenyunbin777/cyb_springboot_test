package com.cyb.codetest.设计模式.创建型模式.原型模式;

/**
 *
 * 原型模式的注意事项和细节：
 *
 * 1 创建新的对象比较复杂时，可以利用原型模式简化对象的创建过程，同时也能够提高效率
 *
 * 2 不用重新初始化对象，而是动态地获得对象运行时的状态
 *
 * 3 如果原始对象发生变化(增加或者减少属性)，其它克隆对象的也会发生相应的变化，无需修改代码
 *
 * 4 在实现深克隆的时候可能需要比较复杂的代码
 *
 * 5 缺点：需要为每一个类配备一个克隆方法，这对全新的类来说不是很难，但对已有的类进行改造时，需要修改其源代码，违背了 ocp 原则。
 * 原文链接：https://blog.csdn.net/weixin_39428894/article/details/124301094
 *
 * @author cyb
 * @date 2022/9/9 下午4:42
 */
public abstract class Shape implements Cloneable {

    private String id;
    protected String type;

    abstract void draw();

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 浅拷贝  基本类型拷贝值，引用类型拷贝引用地址，多个实例指向相同的地址
     * 如果原始对象发生变化(增加或者减少属性)，其它克隆对象的也会发生相应的变化，无需修改代码
     * @return
     */
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
