package me.p4tr7k.cookingapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview_recipes);
        rAdapter = new RecipeAdapter(this, recipeList);
        jsonParse();

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
                                Log.e("TAG:", "ERROR");
                                JSONObject JSON = obj.getJSONObject(i);
                                String imgurl = JSON.getString("Recipe_Image");
                                String title = JSON.getString("Recipe_Name");
                                recipeList.add(new Recipes(imgurl,title));
                                mListView.setAdapter(rAdapter);

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