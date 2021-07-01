package com.cyb.codetest.算法;

/**
 * @author cyb
 * @date 2021/1/25 3:46 下午
 */
public class Test {


    /**
     * 在微服务的架构下，公司内部会有非常多的独立服务。服务之间可以相互调用，往往大型应用调用链条很长，如果出现循环依赖将出现非常恶劣的影响。对于一个具体应用，已知各个服务的调用关系（即依赖关系），请判断是否存在循环调用。 输入：一组服务依赖关系list，('A', 'B') 表示 A 会调用 B 服务service_relations = [['A', 'B'], ['A', 'C'], ['B', 'D'], ['D', 'A']] 输出：由于存在 A - B - D - A 故存在循环依赖，返回True；反之如果不存在，返回False
     *
     * @param args
     */
    public static void main(String[] args) {

    }
}
