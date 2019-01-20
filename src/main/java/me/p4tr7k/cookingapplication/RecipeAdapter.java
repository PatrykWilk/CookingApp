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

public class RecipeAdapter extends ArrayAdapter<Recipes> {
    private Context rContext;
    private List<Recipes> recipeList = new ArrayList<>();

    public RecipeAdapter(@Nullable Context context, @LayoutRes ArrayList<Recipes> list) {
        super(context, 0, list);
        rContext = context;
        recipeList = list;
    }

    @Nullable
    @Override
    public View getView(int position, @Nullable View covertView, @Nullable ViewGroup parent) {
        View listItem = covertView;

        if (listItem == null){
            listItem = LayoutInflater.from(rContext).inflate(R.layout.list_row,parent,false);
        }

        Recipes currentRecipe = recipeList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.recipe_img);
        Picasso.get().load(currentRecipe.getImgurl()).into(image);

        TextView title = (TextView) listItem.findViewById(R.id.recipe_title);
        title.setText(currentRecipe.getTitle());

        return listItem;
    }
}
