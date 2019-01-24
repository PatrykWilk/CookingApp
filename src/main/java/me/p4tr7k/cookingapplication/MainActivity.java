package me.p4tr7k.cookingapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
//    private android.support.v7.widget.Toolbar topToolBar;
    private String title;
    private String imgurl;
    private String recid;
    private EditText inpt;
    private String search;
    private ImageButton searchBtn;
    private ImageButton shoppingListBtn;
    private ArrayList<String> categories;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview_recipes);
        rAdapter = new RecipeAdapter(this, recipeList);
        jsonParse();
        inpt = (EditText) findViewById(R.id.search);
        inpt.setSelected(false);
        searchBtn = findViewById(R.id.action_search);
        inpt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchBtn.performClick();
                }
                return false;
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                hideSoftKeyboard(MainActivity.this);
            }
        });

        shoppingListBtn = findViewById(R.id.action_shoppingList);
        shoppingListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shop = new Intent(MainActivity.this, ShopList.class);
                startActivity(shop);
            }
        });

        spinner = (Spinner) findViewById(R.id.category);
        categories = new ArrayList<>();
        categories.add("All");
        categories.add("Breakfast");
        categories.add("Dinner");
        categories.add("Desert");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        spinner.setAdapter(spinnerAdapter);
        int initialSelectedPosition=spinner.getSelectedItemPosition();
        spinner.setSelection(initialSelectedPosition, false);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selct = spinner.getSelectedItem().toString();
                Toast.makeText(MainActivity.this, "Showing "+selct+" Recipes", Toast.LENGTH_SHORT).show();

                if (selct == "All") {
                    recipeList.clear();
                    jsonParse();
                } else if (selct == "Breakfast") {
                    recipeList.clear();
                    jsonCategory(2);
                } else if (selct == "Dinner") {
                    recipeList.clear();
                    jsonCategory(1);
                } else if (selct == "Desert") {
                    recipeList.clear();
                    jsonCategory(3);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                recipeList.clear();
                jsonParse();
            }
        });
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

    private void jsonCategory(int id) {
        String link = "https://www.p4tr7k.me/Category.php/?id=";
        String endPoint = link + id;

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