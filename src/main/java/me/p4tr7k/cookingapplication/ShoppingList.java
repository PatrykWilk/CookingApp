package me.p4tr7k.cookingapplication;

import android.app.Application;

import java.util.ArrayList;

public class ShoppingList {

    private ArrayList shoppingList = new ArrayList();
    private static ShoppingList instance;

    public ShoppingList() {
    }

    public ArrayList<String> getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ArrayList<String> shoppingList) {
        if(this.shoppingList.isEmpty()){
            this.shoppingList = shoppingList;
        } else {
            this.shoppingList.addAll(shoppingList);
        }
    }

    public void clearShoppingList () {
        this.shoppingList.clear();
    }

    public static synchronized ShoppingList getInstance() {
        if (instance == null) {
            instance = new ShoppingList();
        }
        return instance;
    }
}
