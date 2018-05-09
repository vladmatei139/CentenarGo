package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;

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
import echipa_8.centenargo_app.adapters.RecycleViewLandmarkAdapter;
import echipa_8.centenargo_app.adapters.RecyclerViewImageGalleryAdapter;
import echipa_8.centenargo_app.utilities.MapUtility;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class Gallery_Activity extends AppCompatActivity {

    private String token;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_);
        token = SharedPreferencesUtility.getToken();
        mActionBarToolbar = findViewById(R.id.toolbar_route);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);

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
                                        "username", obj.get("username"),
                                        "likes", obj.get("likes")));
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

}
