package com.cyb.codetest.设计模式.创建型模式.建造者模式.builders;

import com.cyb.codetest.设计模式.创建型模式.建造者模式.Meal;

/**
 * @author cyb
 * @date 2022/9/9 下午5:21
 */
public class BuilderPatternDemo {

    public static void main(String[] args) {
        MealBuilder mealBuilder = new MealBuilder();

        Meal vegMeal = mealBuilder.prepareVegMeal();
        System.out.println("Veg Meal");
        vegMeal.showItems();
        System.out.println("Total Cost: " +vegMeal.getCost());

        Meal nonVegMeal = mealBuilder.prepareNonVegMeal();
        System.out.println("\n\nNon-Veg Meal");
        nonVegMeal.showItems();
        System.out.println("Total Cost: " +nonVegMeal.getCost());
    }
}
