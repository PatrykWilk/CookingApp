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

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private RequestQueue requestQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jsonParse();
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.listview_recipes);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                //context
                this,
                //layout (view)
                R.layout.list_row,
                //row (view)
                R.id.recipe_title,
                //data (model) with bogus data to test our listview
                new String[]{"first record", "second record", "third record"});

        mListView.setAdapter(arrayAdapter);
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

                            for (int i = 0; i < 1; i++) {
                                JSONObject JSON = obj.getJSONObject(i);
//                                res = JSON.getString("Recipe_Name");
//                                img = JSON.getString("Recipe_Image");
//                                textView.setText(res);

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
        requestQ = Volley.newRequestQueue(getApplicationContext());
        requestQ.add(objReq);
    }
}