package echipa_8.centenargo_app.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.adapters.RecyclerViewRouteAdapter;
import echipa_8.centenargo_app.services.Login_Service;
import echipa_8.centenargo_app.services.Routes_Service;
import echipa_8.centenargo_app.utilities.MapUtility;

public class Routes_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float CITY_ZOOM = 11f;
    private static final LatLng ZERO_KM_BUCHAREST = new LatLng(44.4327025, 26.104049400000008);

    private String[] dataset;

    private GoogleMap mMap;

    Toolbar mActionBarToolbar;
    String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String token = intent.getStringExtra("TOKEN");
        setContentView(R.layout.activity_routes_);

        mActionBarToolbar = findViewById(R.id.toolbar_routes);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);
        mToken = token;
        Routes_Service routes_service = new Routes_Service(this);
        routes_service.execute(token);
    }

    public void setRoutes(String response) {

        dataset = response.split("\\r?\\n");
        Pair<String[], int[]> identifiers = getNamesAndIdsFromDataset(dataset);
        RecyclerView routesView = findViewById(R.id.recyclerView_routes);
        RecyclerView.LayoutManager routesLayoutManager = new LinearLayoutManager(this);
        routesView.setLayoutManager(routesLayoutManager);
        RecyclerView.Adapter routesAdapter = new RecyclerViewRouteAdapter(this, identifiers, mToken);
        routesView.setAdapter(routesAdapter);

        if (MapUtility.getLocationPermission(this))
            initMap();
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_routes);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(Routes_Activity.this, "Map is ready", Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ZERO_KM_BUCHAREST, CITY_ZOOM));
        setMarkersStartingRoutes();
        if (!MapUtility.mapInit(this, mMap) && MapUtility.getLocationPermissionGranted()) {
            initMap();
        }
    }

    private void setMarkersStartingRoutes() {
        for (int i = 0; i < dataset.length; i++) {
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

}
