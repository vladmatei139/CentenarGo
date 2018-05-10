package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.adapters.RecyclerViewImageGalleryAdapter;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class Gallery_Activity extends AppCompatActivity {

    private String token;
    Toolbar mActionBarToolbar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    private void setNavigationBar() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.gallery_drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;
            switch (item.getItemId()){
                case R.id.menu_gallery:
                    intent = new Intent(this.getApplicationContext(), Gallery_Activity.class);
                    this.startActivity(intent);
                    return true;

                case R.id.menu_routes:
                    intent = new Intent(this.getApplicationContext(), Routes_Activity.class);
                    this.startActivity(intent);
                    return true;

                case R.id.menu_sign_off:
                    Toast.makeText(this, "You've been logged out", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;

                case R.id.menu_tutorial:
                    intent = new Intent(this.getApplicationContext(), Intro_Activity.class);
                    startActivity(intent);
                    return true;

                case R.id.menu_stats:
                    // todo: Start Statistics intent
                    return true;

                default:
                    return true;
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_);

        Intent intent = getIntent();
        //token = intent.getStringExtra("token");
        //token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjMyNmFlZTFlLTMwMzUtNDU5Ni1iMTg0LTJiNTY3Y2IyYjFhNCIsImlhdCI6MTUyNTgwMzMwNCwiZXhwIjoxNTI1ODQ2NTA0fQ.44gNDWBl-1TJ0NdrXmFnhy1VtD6k2wnDWuwczmcFfoc";

        String token = SharedPreferencesUtility.getToken();

        mActionBarToolbar = findViewById(R.id.toolbar_gallery);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);

        setNavigationBar();

        List<Map<String, Object>> images = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        RecyclerView galleryView = findViewById(R.id.gallery_recycler_view);
        RecyclerView.LayoutManager galleryLayoutManager = new StaggeredGridLayoutManager(3, 1);
        galleryView.setLayoutManager(galleryLayoutManager);
        RecyclerView.Adapter galleryAdapter = new RecyclerViewImageGalleryAdapter(this, images);
        galleryView.setAdapter(galleryAdapter);

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("token", token);
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    "http://10.0.2.2:8080/api/images/",
                    requestObject,
                    response -> {
                        try {
                            JSONArray arr = response.getJSONArray("images");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                images.add(createMap(
                                        "id", obj.get("id"),
                                        "path", obj.get("path"),
                                        "title", obj.get("title"),
                                        "username", obj.get("username")));
                            }
                            galleryAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException jsone) {
                            Log.println(Log.ERROR, "JSONError", "Error creating JSON array from JSONObject response: " + jsone.getMessage());
                        }
                    },
                    error -> Log.println(Log.ERROR, "ImageError", "Volley request for /api/images/ failed: " + error.getMessage())
            );
            requestQueue.add(request);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> createMap (Object... objects) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length; i+=2) {
            map.put((String) objects[i], objects[i+1]);
        }
        return map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
