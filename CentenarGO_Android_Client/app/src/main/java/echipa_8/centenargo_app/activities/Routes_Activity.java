package echipa_8.centenargo_app.activities;

import android.support.annotation.NonNull;
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
import echipa_8.centenargo_app.utilities.MapUtility;

public class Routes_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float CITY_ZOOM = 11f;
    private static final LatLng ZERO_KM_BUCHAREST = new LatLng(44.4327025, 26.104049400000008);
    private static final LatLng START_ROUTE1 = new LatLng(44.4531131, 26.084638199999972);
    private static final LatLng START_ROUTE2 = new LatLng(44.4275035, 26.087350600000036);
    private static final LatLng START_ROUTE3 = new LatLng(44.41126999999999, 26.09687889999998);


    private GoogleMap mMap;

    private RecyclerView mRoutesView;
    private RecyclerView.Adapter mRoutesAdapter;
    private RecyclerView.LayoutManager mRoutesLayoutManager;

    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_);

        mActionBarToolbar = findViewById(R.id.toolbar_routes);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);

        mRoutesView = findViewById(R.id.recyclerView_routes);

        //mLandmarksView.setHasFixedSize(true);
        mRoutesLayoutManager = new LinearLayoutManager(this);
        mRoutesView.setLayoutManager(mRoutesLayoutManager);

        String[] dataset = new String[]{"LM1", "LM2", "LM3"};
        mRoutesAdapter = new RecyclerViewRouteAdapter(dataset);
        mRoutesView.setAdapter(mRoutesAdapter);

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

        mMap.addMarker(new MarkerOptions()
                .position(START_ROUTE1)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("Muzeul Antipa")
                .snippet(MapUtility.geoLocateByLatLng(this,START_ROUTE1)));
        mMap.addMarker(new MarkerOptions()
                .position(START_ROUTE2)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                .title("Palatul Parlamentului")
                .snippet(MapUtility.geoLocateByLatLng(this,START_ROUTE2)));
        mMap.addMarker(new MarkerOptions()
                .position(START_ROUTE3)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title("Memorialul Eroilor Neamului")
                .snippet(MapUtility.geoLocateByLatLng(this, START_ROUTE3)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (MapUtility.requestPermission(requestCode, permissions, grantResults))
            initMap();
    }
}
