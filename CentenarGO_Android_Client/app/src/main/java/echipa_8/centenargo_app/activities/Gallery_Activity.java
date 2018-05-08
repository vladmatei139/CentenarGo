package echipa_8.centenargo_app.activities;

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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
import echipa_8.centenargo_app.database.AppDatabase;
import echipa_8.centenargo_app.utilities.MapUtility;

public class Gallery_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_);

        List<Map<String, Object>> images = new ArrayList<>();
        AppDatabase database = AppDatabase.getInstance(this);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(configuration);

        RecyclerView galleryView = findViewById(R.id.gallery_recycler_view);
        RecyclerView.LayoutManager galleryLayoutManager = new StaggeredGridLayoutManager(3, 1);
        galleryView.setLayoutManager(galleryLayoutManager);
        RecyclerView.Adapter galleryAdapter = new RecyclerViewImageGalleryAdapter(images, LayoutInflater.from(this), database, getResources(), requestQueue, getFilesDir());
        galleryView.setAdapter(galleryAdapter);

        JSONObject requestObject = new JSONObject();
        try {
            requestObject.put("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjMyNmFlZTFlLTMwMzUtNDU5Ni1iMTg0LTJiNTY3Y2IyYjFhNCIsImlhdCI6MTUyNTc4Njg4MywiZXhwIjoxNTI1ODMwMDgzfQ.LT9SxIf7TyTfhfmjOU7EhDOdL6I594CdWiXov82K84I");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                                    "title", obj.get("title")));
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

    private Map<String, Object> createMap (Object... objects) {
        Map<String, Object> map = new HashMap<>();
        for (int i = 0; i < objects.length; i+=2) {
            map.put((String) objects[i], objects[i+1]);
        }
        return map;
    }

}
