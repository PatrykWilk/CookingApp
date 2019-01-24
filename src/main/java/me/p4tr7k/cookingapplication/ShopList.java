package me.p4tr7k.cookingapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShopList extends AppCompatActivity {

    private ListView iView;
    private ArrayList sList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        iView = (ListView) findViewById(R.id.shopListView);

        ShoppingList shoppingList = ShoppingList.getInstance();
        sList = shoppingList.getShoppingList();


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, sList);

        iView.setAdapter(arrayAdapter);

    }

}
