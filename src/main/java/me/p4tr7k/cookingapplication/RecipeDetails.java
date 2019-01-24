package me.p4tr7k.cookingapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {

    public RequestQueue requestQ;
    public TextView title;
    public ImageView imgurl;
    public String recid;
    private ListView iView;
    private IngredientsRecipe iRecipe;
    public ArrayList<Ingredients> ingredientList = new ArrayList<>();
    private ImageButton barrow;
    private ArrayList<String> shopList = new ArrayList<>();
    private ImageButton sendToShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        title = (TextView) findViewById(R.id.recipe_title);
        imgurl = (ImageView) findViewById(R.id.recipe_img);
        iView = findViewById(R.id.listview_ingredients);
        iRecipe = new IngredientsRecipe(this, ingredientList);
        barrow = (ImageButton) findViewById(R.id.backbutton);
        sendToShop = (ImageButton) findViewById(R.id.addToShop);
        barrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeDetails.this, MainActivity.class);
                startActivity(intent);
            }
        });
        sendToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingList shoppingList = ShoppingList.getInstance();
                shoppingList.setShoppingList(shopList);
                Log.e("123", shoppingList.getShoppingList().toString());

                }
            });

        intentination();
        jsonParse();
    }

    public void intentination() {
        Intent intent = getIntent();
        String tit = ((Intent) intent).getStringExtra("title");
        String img = intent.getStringExtra("imgurl");
        recid = intent.getStringExtra("recid");

        title.setText(tit);
        Picasso.get().load(img).into(imgurl);

    }

    private void jsonParse() {
        String link = "https://www.p4tr7k.me/Ingredients.php/?id=";
        String endPoint = link + recid;


        JsonObjectRequest objReq = new JsonObjectRequest(
                Request.Method.GET,
                endPoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray obj = response.getJSONArray("ingredients");
                            Log.e("OBJ: ", obj.toString());

                            for (int i = 0; i < obj.length(); i++) {
//                                Log.e("TAG:", "ERROR");
                                JSONObject JSON = obj.getJSONObject(i);
                                String res = JSON.getString("Ingredient_Name");
                                shopList.add(res);
                                String amt = JSON.getString("Ingredient_Amount");
                                ingredientList.add(new Ingredients(res, amt));
                                iView.setAdapter(iRecipe);
                                Log.e("TAG", shopList.toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR: ", error.toString());
                    }
                }
        );
        requestQ = Volley.newRequestQueue(this);
        requestQ.add(objReq);
    }
}
