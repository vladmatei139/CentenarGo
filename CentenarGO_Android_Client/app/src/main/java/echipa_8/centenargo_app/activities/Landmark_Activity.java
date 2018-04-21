package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.master.glideimageview.GlideImageView;

import org.json.JSONException;
import org.json.JSONObject;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.Landmark_Service;

public class Landmark_Activity extends AppCompatActivity {

    private GlideImageView mLandmarkImage;
    private TextView mLandmarkTitle;
    private TextView mLandmarkDescription;
    private Integer mLandmarkId;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");
        setContentView(R.layout.activity_landmark_);
        mLandmarkId = intent.getIntExtra("LandmarkId", 0);

        Landmark_Service landmarkService = new Landmark_Service(this);
        landmarkService.execute(token, Integer.toString(mLandmarkId));

        linkFields();

    }

    private void linkFields(){
        mLandmarkImage = findViewById(R.id.landmark_image_view);
        mLandmarkTitle = findViewById(R.id.landmark_title);
        mLandmarkDescription = findViewById(R.id.landmark_description);
    }

    private void loadImageFromURL(String URL){
        mLandmarkImage.loadImageUrl(URL);
    }

    public void setLandmark(String landmarkJSON) {
        try {
            JSONObject json = new JSONObject(landmarkJSON);

            JSONObject infoJSON = new JSONObject(json.getString("landmark"));

            String title = infoJSON.getString("name");
            String content = infoJSON.getString("content");

            String imgName = json.getString("image");

            mLandmarkTitle.setText(title);
            mLandmarkDescription.setText(content);

            loadImageFromURL("http://192.168.1.2:8080/" + imgName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
