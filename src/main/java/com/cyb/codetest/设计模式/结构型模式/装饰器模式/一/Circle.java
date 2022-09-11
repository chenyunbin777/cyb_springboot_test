package com.cyb.codetest.设计模式.结构型模式.装饰器模式.一;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cyb
 * @date 2022/9/9 下午4:08
 */
@Data
@NoArgsConstructor
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Shape: Circle");
    }
}
