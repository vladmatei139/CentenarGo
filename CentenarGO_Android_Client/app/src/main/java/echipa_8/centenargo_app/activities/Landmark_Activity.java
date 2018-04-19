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
        loadImageFromURL("http://www.cmn.ro/uploads/tur/tur-virtual.jpg");
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

            String title = json.getString("name");
            String content = json.getString("content");

            mLandmarkTitle.setText(title);
            mLandmarkDescription.setText(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
