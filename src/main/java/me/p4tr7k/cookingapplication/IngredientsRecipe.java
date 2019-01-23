package me.p4tr7k.cookingapplication;

import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class IngredientsRecipe extends ArrayAdapter<Ingredients> {
    private Context rContext;
    private List<Ingredients> ingredientsList = new ArrayList<>();

    public IngredientsRecipe(@Nullable Context context, @LayoutRes ArrayList<Ingredients> list) {
        super(context, 0, list);
        rContext = context;
        ingredientsList = list;
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View covertView, @Nullable ViewGroup parent) {
        View listItem = covertView;

        if (listItem == null){
            listItem = LayoutInflater.from(rContext).inflate(R.layout.ingredient_row,parent,false);
        }

        Ingredients currentIngredient = ingredientsList.get(position);

        TextView title = (TextView) listItem.findViewById(R.id.ingredient_name);
        title.setText(currentIngredient.getIngredient());

        TextView amount = (TextView) listItem.findViewById(R.id.ingredient_amount);
        amount.setText(currentIngredient.getAmount());



        return listItem;
    }
}
