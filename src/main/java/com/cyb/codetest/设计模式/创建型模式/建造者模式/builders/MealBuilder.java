package com.cyb.codetest.设计模式.创建型模式.建造者模式.builders;

import com.cyb.codetest.设计模式.创建型模式.建造者模式.*;

/**
 *
 * 创建一个 MealBuilder 类，实际的 builder 类负责创建 Meal 对象。
 *
 * 与工厂模式的区别是：建造者模式更加关注与零件装配的顺序。
 * @author cyb
 * @date 2022/9/9 下午5:20
 */
public class MealBuilder {


    public Meal prepareVegMeal (){
        Meal meal = new Meal();
        meal.addItem(new VegBurger());
        meal.addItem(new Coke());
        return meal;
    }

    public Meal prepareNonVegMeal (){
        Meal meal = new Meal();
        meal.addItem(new ChickenBurger());
        meal.addItem(new Pepsi());
        return meal;
    }
}
