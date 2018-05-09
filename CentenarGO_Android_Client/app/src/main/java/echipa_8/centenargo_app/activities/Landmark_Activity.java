package echipa_8.centenargo_app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.master.glideimageview.GlideImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.LandmarkContent_Service;
import echipa_8.centenargo_app.services.Landmark_Service;
import echipa_8.centenargo_app.utilities.MapUtility;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class Landmark_Activity extends AppCompatActivity {

    private GlideImageView mLandmarkImage;
    private TextView mLandmarkTitle;
    private TextView mLandmarkDescription;
    private Button mQuizButton;
    private Button mCheckLocationButton;
    private Button mUploadButton;
    private Integer mLandmarkId;
    private Integer mRouteId;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        setContentView(R.layout.activity_landmark_);
        mLandmarkId = intent.getIntExtra(getString(R.string.landmark_id_key), 0);
        mRouteId = intent.getIntExtra(getString(R.string.route_id_key), 0);
        Landmark_Service landmarkService = new Landmark_Service(this);
        landmarkService.execute(mLandmarkId.toString(), mRouteId.toString());

        linkFields();

    }

    private void linkFields(){
        mLandmarkImage = findViewById(R.id.landmark_image_view);
        mLandmarkTitle = findViewById(R.id.landmark_title);
        mLandmarkDescription = findViewById(R.id.landmark_description);
        mQuizButton = findViewById(R.id.quiz_button);
        mQuizButton.setVisibility(View.INVISIBLE);
        mCheckLocationButton = findViewById(R.id.check_location_button);
        mUploadButton = findViewById(R.id.upload_button);
        mUploadButton.setVisibility(View.INVISIBLE);
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
            final Boolean current = infoJSON.getBoolean("is_current");
            final double latitude = infoJSON.getDouble("latitude");
            final double longitude = infoJSON.getDouble("longitude");
            String imgName = json.getString("image");

            loadImageFromURL("http://192.168.1.4:8080/" + imgName);

            mLandmarkTitle.setText(title);
            mLandmarkDescription.setText(content);
            final WeakReference<Landmark_Activity> context = new WeakReference<>(this);

            if (!current)
                mQuizButton.setVisibility(View.VISIBLE);

            if (!current)
                mUploadButton.setVisibility(View.VISIBLE);

            mQuizButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.get().getApplicationContext(), Questions_Activity.class);
                    intent.putExtra("LandmarkId", mLandmarkId);
                    intent.putExtra("RouteId", mRouteId);
                    context.get().startActivity(intent);
                }
            });

            mUploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context.get().getApplicationContext(), Upload_Activity.class);
                    intent.putExtra("LandmarkId", mLandmarkId);
                    context.get().startActivity(intent);
                }
            });

            mCheckLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.get().checkLocation(current, latitude, longitude);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkLocation(Boolean current, double latitude, double longitude) {
        if (!current)
            Toast.makeText(Landmark_Activity.this, "Locatia a fost deja verificata", Toast.LENGTH_SHORT).show();
        else {
            MapUtility.validateLocation(this, latitude, longitude);
        }
    }

    public void locationResponse(Boolean proximity) {
        if (proximity) {
            mQuizButton.setVisibility(View.VISIBLE);
            mUploadButton.setVisibility(View.VISIBLE);
            LandmarkContent_Service landmarkContent_service = new LandmarkContent_Service(this);
            landmarkContent_service.execute(mLandmarkId.toString());
        }
        else
            Toast.makeText(Landmark_Activity.this, "Trebuie sa te apropii mai mult", Toast.LENGTH_SHORT).show();
    }

    public void setContent(String content) {
        mLandmarkDescription.setText(content);
    }
}
