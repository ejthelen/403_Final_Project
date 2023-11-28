package com.example.a403finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class WalkerList extends AppCompatActivity {

    ListView lstWalkers;
    EditText edFilter;

    Button btnFilter;

    Button btnLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walker_list);

        lstWalkers = findViewById(R.id.lstWalkers);
        edFilter = findViewById(R.id.edFilter);
        btnFilter = findViewById(R.id.btnFilter);
        btnLocation = findViewById(R.id.btnLocation);




    }


/*
    public void getData(){
        String url = "https://opentdb.com/api_category.php";
        JsonObjectRequest r = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {

                    try {
                        JSONArray categoriesArray = response.getJSONArray("trivia_categories");
                        for(int i = 0; i < categoriesArray.length(); i++){
                            JSONObject categoryObj = categoriesArray.getJSONObject(i);
                            int id = categoryObj.getInt("id");
                            String name = categoryObj.getString("name");
                            Category c = new Category(name, id);
                            category.add(c);
                        }
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                error -> Log.d("DDD",error.toString()));
        queue.add(r);
    }
    */

}