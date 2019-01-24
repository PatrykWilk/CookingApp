package me.p4tr7k.cookingapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShopList extends AppCompatActivity {

    private ListView iView;
    private ArrayList sList = new ArrayList();
    private ImageButton bArrow;
    private ImageButton removeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        iView = (ListView) findViewById(R.id.shopListView);
        bArrow = (ImageButton) findViewById(R.id.backbutton);
        removeList = (ImageButton) findViewById(R.id.removeshop);

        final ShoppingList shoppingList = ShoppingList.getInstance();
        sList = shoppingList.getShoppingList();


        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, sList);

        iView.setAdapter(arrayAdapter);

        bArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopList.this, MainActivity.class);
                startActivity(intent);
            }
        });

        removeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShopList.this);

                builder.setCancelable(true);
                builder.setTitle("Clear Shopping List?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shoppingList.clearShoppingList();
                        iView.setAdapter(arrayAdapter);

                    }

                });

                builder.show();
            }
        });

    }

}
