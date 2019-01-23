package me.p4tr7k.cookingapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
    private ArrayList<Recipes> recipeList = new ArrayList<>();
    private android.support.v7.widget.Toolbar topToolBar;
    private String title;
    private String imgurl;
    private String recid;
    private EditText inpt;
    private String search;

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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        inpt = (EditText) findViewById(R.id.search);
        inpt.setSelected(false);
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
        if (id == R.id.action_search) {
            if (inpt.getText().toString().isEmpty()) {
                recipeList.clear();
                jsonParse();
                inpt.clearFocus();
            } else {
                recipeList.clear();
                search = inpt.getText().toString();
                jsonSearch();
                inpt.clearFocus();
            }
        }
        hideSoftKeyboard(this);



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
                                recid = JSON.getString("Recipe_ID");
                                imgurl = JSON.getString("Recipe_Image");
                                title = JSON.getString("Recipe_Name");
                                recipeList.add(new Recipes(imgurl,title,recid));
                                mListView.setAdapter(rAdapter);
                                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent rec = new Intent(MainActivity.this, RecipeDetails.class);
                                        String rid = recipeList.get(position).getId();
                                        String tit = recipeList.get(position).getTitle();
                                        String url = recipeList.get(position).getImgurl();
                                        rec.putExtra("recid", rid);
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

    //     JSON SEARCH
    private void jsonSearch() {
        String link = "https://www.p4tr7k.me/Search.php/?Q=";
        String endPoint = link + search;

        JsonObjectRequest objReq = new JsonObjectRequest(
                Request.Method.GET,
                endPoint,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray obj = response.getJSONArray("results");
                            Log.e("OBJ: ", obj.toString());

                            for (int i = 0; i < obj.length(); i++) {
                                    //                                Log.e("TAG:", "ERROR");
                                    JSONObject JSON = obj.getJSONObject(i);
                                    recid = JSON.getString("Recipe_ID");
                                    imgurl = JSON.getString("Recipe_Image");
                                    title = JSON.getString("Recipe_Name");
                                    recipeList.add(new Recipes(imgurl, title, recid));
                                    mListView.setAdapter(rAdapter);
                                    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            Intent rec = new Intent(MainActivity.this, RecipeDetails.class);
                                            String rid = recipeList.get(position).getId();
                                            String tit = recipeList.get(position).getTitle();
                                            String url = recipeList.get(position).getImgurl();
                                            rec.putExtra("recid", rid);
                                            rec.putExtra("title", tit);
                                            rec.putExtra("imgurl", url);
                                            startActivity(rec);
                                        }
                                    });
                                }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "No Results Found", Toast.LENGTH_SHORT).show();
                            inpt.setText("");
                            recipeList.clear();
                            jsonParse();
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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}