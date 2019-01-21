package me.p4tr7k.cookingapplication;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private RequestQueue requestQ;
    private RecipeAdapter rAdapter;
    public ArrayList<Recipes> recipeList = new ArrayList<>();
    private android.support.v7.widget.Toolbar topToolBar;
    private ImageView iView;
    public String title;
    public String imgurl;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview_recipes);
        rAdapter = new RecipeAdapter(this, recipeList);
        jsonParse();
        topToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(topToolBar);
        iView = (ImageView) findViewById(R.id.recipe_img);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent we = new Intent(MainActivity.this,Ingredients.class);
            startActivity(we);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //     JSON PARSER
    private void jsonParse() {
        String endPoint = "https://www.p4tr7k.me/Recipes.php";

        JsonObjectRequest objReq = new JsonObjectRequest(
                Request.Method.GET,
                endPoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray obj = response.getJSONArray("data");
                            Log.e("OBJ: ", obj.toString());

                            for (int i = 0; i < obj.length(); i++) {
//                                Log.e("TAG:", "ERROR");
                                JSONObject JSON = obj.getJSONObject(i);
                                imgurl = JSON.getString("Recipe_Image");
                                title = JSON.getString("Recipe_Name");
                                recipeList.add(new Recipes(imgurl,title));
                                mListView.setAdapter(rAdapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent rec = new Intent(MainActivity.this, RecipeDetails.class);
                                        String tit = recipeList.get(position).getTitle();
                                        String url = recipeList.get(position).getImgurl();
                                        rec.putExtra("title", tit);
                                        rec.putExtra("imgurl", url);
                                        startActivity(rec);
                                    }
                                });
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