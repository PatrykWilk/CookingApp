package me.p4tr7k.cookingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RecipeDetails extends AppCompatActivity {

    public TextView title;
    public ImageView imgurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        title = (TextView) findViewById(R.id.recipe_title);
        imgurl = (ImageView) findViewById(R.id.recipe_img);

        intentination();




    }

    public void intentination() {
        Intent intent = getIntent();
        String tit = ((Intent) intent).getStringExtra("title");
        String img = intent.getStringExtra("imgurl");

        title.setText(tit);
        Picasso.get().load(img).into(imgurl);

    }
}
