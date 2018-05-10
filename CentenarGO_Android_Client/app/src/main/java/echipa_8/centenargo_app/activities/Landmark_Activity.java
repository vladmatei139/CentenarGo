package echipa_8.centenargo_app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.master.glideimageview.GlideImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.services.CheckLocation_Service;
import echipa_8.centenargo_app.services.LandmarkContent_Service;
import echipa_8.centenargo_app.services.Landmark_Service;
import echipa_8.centenargo_app.services.ValidateCheck_Service;
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

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    private void setNavigationBar() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.landmark_drawer);
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

        Intent intent = getIntent();
        setContentView(R.layout.activity_landmark_);
        mLandmarkId = intent.getIntExtra(getString(R.string.landmark_id_key), 0);
        mRouteId = intent.getIntExtra(getString(R.string.route_id_key), 0);
        Landmark_Service landmarkService = new Landmark_Service(this);
        landmarkService.execute(mLandmarkId.toString(), mRouteId.toString());

        mActionBarToolbar = findViewById(R.id.toolbar_landmark);
        mActionBarToolbar.setTitle("CentenarGo");
        setSupportActionBar(mActionBarToolbar);

        setNavigationBar();

        linkFields();

        ValidateCheck_Service validateCheck_service = new ValidateCheck_Service(this);
        validateCheck_service.execute(mLandmarkId.toString(), mRouteId.toString());
    }

    private void linkFields(){
        mLandmarkImage = findViewById(R.id.landmark_image_view);
        mLandmarkTitle = findViewById(R.id.landmark_title);
        mLandmarkDescription = findViewById(R.id.landmark_description);
        mQuizButton = findViewById(R.id.quiz_button);
        mQuizButton.setVisibility(View.GONE);
        mCheckLocationButton = findViewById(R.id.check_location_button);
        mCheckLocationButton.setVisibility(View.GONE);
        mUploadButton = findViewById(R.id.upload_button);
        mUploadButton.setVisibility(View.GONE);
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

            loadImageFromURL("http://10.0.2.2:8080/" + imgName);

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
            mCheckLocationButton.setVisibility(View.GONE);
            CheckLocation_Service checkLocation_service = new CheckLocation_Service();
            checkLocation_service.execute(mLandmarkId.toString(), mRouteId.toString());
            LandmarkContent_Service landmarkContent_service = new LandmarkContent_Service(this);
            landmarkContent_service.execute(mLandmarkId.toString());
        }
        else
            Toast.makeText(Landmark_Activity.this, "Trebuie sa te apropii mai mult", Toast.LENGTH_SHORT).show();
    }

    public void setContent(String content) {

        //mLandmarkDescription.setText(content);

        CharSequence oldDescription = mLandmarkDescription.getText();

        if(oldDescription.equals(content)){
            return;
        }

        final int[] i = new int[1];
        i[0] = oldDescription.length();
        final int length = content.length();

        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                char c = content.charAt(i[0]);
                mLandmarkDescription.append(String.valueOf(c));
                i[0]++;
            }
        };

        final Timer timer = new Timer();
        TimerTask taskEverySplitSecond = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                if (i[0] == length - 1) {
                    timer.cancel();
                }
            }
        };
        timer.schedule(taskEverySplitSecond, 1/5, 15);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void validateCheck(Boolean o) {
        if (o != null && !o) {
            mCheckLocationButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mQuizButton.setVisibility(View.VISIBLE);
            mUploadButton.setVisibility(View.VISIBLE);
            LandmarkContent_Service landmarkContent_service = new LandmarkContent_Service(this);
            landmarkContent_service.execute(mLandmarkId.toString());
        }
    }

}
