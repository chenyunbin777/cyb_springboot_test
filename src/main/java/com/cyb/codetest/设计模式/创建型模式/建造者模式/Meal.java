package com.cyb.codetest.设计模式.创建型模式.建造者模式;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cyb
 * @date 2022/9/9 下午5:19
 */
public class Meal {
    private List<Item> items = new ArrayList<Item>();

    public void addItem(Item item){
        items.add(item);
    }

    public float getCost(){
        float cost = 0.0f;
        for (Item item : items) {
            cost += item.price();
        }
        return cost;
    }

    public void showItems(){
        for (Item item : items) {
            System.out.print("Item : "+item.name());
            System.out.print(", Packing : "+item.packing().pack());
            System.out.println(", Price : "+item.price());
        }
    }
}
