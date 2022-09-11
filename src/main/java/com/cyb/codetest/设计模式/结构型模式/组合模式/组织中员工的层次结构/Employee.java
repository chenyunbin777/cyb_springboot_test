package com.cyb.codetest.设计模式.结构型模式.组合模式.组织中员工的层次结构;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cyb
 * @date 2022/9/9 下午9:07
 */
public class Employee {
    private String name;
    private String dept;
    private int salary;
    private List<Employee> subordinates;

    //构造函数
    public Employee(String name,String dept, int sal) {
        this.name = name;
        this.dept = dept;
        this.salary = sal;
        subordinates = new ArrayList<>();
    }

    public void add(Employee e) {
        subordinates.add(e);
    }

    public void remove(Employee e) {
        subordinates.remove(e);
    }

    public List<Employee> getSubordinates(){
        return subordinates;
    }

    public String toString(){
        return ("Employee :[ Name : "+ name
                +", dept : "+ dept + ", salary :"
                + salary+" ]");
    }
}
