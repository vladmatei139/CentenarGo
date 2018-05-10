package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.adapters.RecycleViewLandmarkAdapter;
import echipa_8.centenargo_app.services.Route_Service;
import echipa_8.centenargo_app.utilities.MapUtility;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class Route_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float CITY_ZOOM = 11f;
    private static final LatLng ZERO_KM_BUCHAREST = new LatLng(44.4327025, 26.104049400000008);

    private String[] dataset;
    private Integer mRoute;
    private GoogleMap mMap;

    Toolbar mActionBarToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mRoute = intent.getIntExtra(getString(R.string.route_id_key), 0);
        setContentView(R.layout.activity_route_);

        mActionBarToolbar = findViewById(R.id.toolbar_route);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);
        setNavigationBar();

        Route_Service route_service = new Route_Service(this);
        route_service.execute(mRoute.toString());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbaritems, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            switch (item.getItemId()) {
                case R.id.action_settings:
                    Intent intent = new Intent(getApplicationContext(), Gallery_Activity.class);
                    startActivity(intent);
                    return true;
                default:
                    Toast.makeText(getApplicationContext(), "Unknown command", Toast.LENGTH_LONG).show();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setLandmarks(String response) {
        dataset = response.split("\\r?\\n");
        Pair<String[], int[]> identifiers = getNamesAndIdsFromDataset(dataset);
        RecyclerView landmarksView = findViewById(R.id.recyclerView_landmarks);
        RecyclerView.LayoutManager landmarksLayoutManager = new LinearLayoutManager(this);
        landmarksView.setLayoutManager(landmarksLayoutManager);
        RecyclerView.Adapter landmarksAdapter = new RecycleViewLandmarkAdapter(this, identifiers, mRoute);
        landmarksView.setAdapter(landmarksAdapter);

        if (MapUtility.getLocationPermission(this))
            initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_route);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(Route_Activity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZERO_KM_BUCHAREST, CITY_ZOOM));
        setMarkersLandmarks();
        if (!MapUtility.mapInit(this, mMap) && MapUtility.getLocationPermissionGranted()) {
            initMap();
        }
    }

    private void setMarkersLandmarks() {
        for (int i = 0; dataset != null && i < dataset.length; i++) {
            String[] route = dataset[i].split(",");
            LatLng latLng = new LatLng(Double.parseDouble(route[2]), Double.parseDouble(route[3]));
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                    .title(route[1])
                    .snippet(MapUtility.geoLocateByLatLng(this, latLng)));
        }
    }

    private Pair<String[], int[]> getNamesAndIdsFromDataset(String[] dataset) {
        Pair<String[], int[]> identifiers = new Pair<>(new String[dataset.length], new int[dataset.length]);
        for (int i = 0; i < dataset.length; i++) {
            identifiers.first[i] = dataset[i].split(",")[1];
            identifiers.second[i] = Integer.parseInt(dataset[i].split(",")[0]);
        }
        return identifiers;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (MapUtility.requestPermission(requestCode, permissions, grantResults))
            initMap();
    }


    private void setNavigationBar() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.route_drawer);
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

}
