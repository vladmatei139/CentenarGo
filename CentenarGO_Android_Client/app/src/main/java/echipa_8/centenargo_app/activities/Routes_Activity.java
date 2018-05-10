package echipa_8.centenargo_app.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.util.Pair;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import echipa_8.centenargo_app.R;
import echipa_8.centenargo_app.adapters.RecyclerViewRouteAdapter;
import echipa_8.centenargo_app.services.ChangeRoute_Service;
import echipa_8.centenargo_app.services.Routes_Service;
import echipa_8.centenargo_app.utilities.MapUtility;
import echipa_8.centenargo_app.utilities.RouteInfo;
import echipa_8.centenargo_app.utilities.SharedPreferencesUtility;

public class Routes_Activity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float CITY_ZOOM = 11f;
    private static final LatLng ZERO_KM_BUCHAREST = new LatLng(44.4327025, 26.104049400000008);

    private String[] dataset;

    private GoogleMap mMap;

    Toolbar mActionBarToolbar;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private void setNavigationBar() {
        mDrawerLayout = (DrawerLayout)findViewById(R.id.routes_drawer);
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
                    finish();
                    return true;

                case R.id.menu_sign_off:
                    Toast.makeText(this, "You've been logged out", Toast.LENGTH_SHORT).show();
                    intent = new Intent(getApplicationContext(), Login_Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    return true;

                case R.id.menu_tutorial:
                    intent = new Intent(this.getApplicationContext(), Intro_Activity.class);
                    this.startActivity(intent);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_);

        mActionBarToolbar = findViewById(R.id.toolbar_routes);
        mActionBarToolbar.setTitle(R.string.app_name);
        setSupportActionBar(mActionBarToolbar);
        setNavigationBar();
        Routes_Service routes_service = new Routes_Service(this);
        routes_service.execute();
    }

    public void setRoutes(String response) {

        dataset = response.split("\\r?\\n");
        List<RouteInfo> routesInfo = getNamesAndIdsFromDataset(dataset);
        RecyclerView routesView = findViewById(R.id.recyclerView_routes);
        RecyclerView.LayoutManager routesLayoutManager = new LinearLayoutManager(this);
        routesView.setLayoutManager(routesLayoutManager);
        RecyclerView.Adapter routesAdapter = new RecyclerViewRouteAdapter(this, routesInfo);
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

    private List<RouteInfo> getNamesAndIdsFromDataset(String[] dataset) {
        List<RouteInfo> routesInfo = new ArrayList<>();
        for (int i = 0; i < dataset.length; i++) {
            String[] splitted = dataset[i].split(",");
            String name = splitted[1];
            int id = Integer.parseInt(splitted[0]);
            boolean completed = splitted[4].equals("1");

            routesInfo.add(new RouteInfo(name, id, completed));
        }
        return routesInfo;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (MapUtility.requestPermission(requestCode, permissions, grantResults))
            initMap();
    }

    public void setRoute(final String result, final int currentCompleted, final int newCompleted, String lastRoute) {
        if (result.equals(lastRoute)) {
            Intent intent = new Intent(this.getApplicationContext(), Route_Activity.class);
            intent.putExtra(getString(R.string.route_id_key), Integer.parseInt(result));
            this.startActivity(intent);
            return;
        }

        if (newCompleted == 0) {
            if (currentCompleted == 0 && !lastRoute.equals("null")) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                changeRoute(result, false);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Daca vei shimba ruta, progresul de la ruta curenta va fi sters. Esti sigur ca doresti asta?").setPositiveButton("Da", dialogClickListener)
                        .setNegativeButton("Nu", dialogClickListener).show();
            } else
                changeRoute(result, true);
        }
        else {
            Intent intent = new Intent(this.getApplicationContext(), Route_Activity.class);
            intent.putExtra(getString(R.string.route_id_key), Integer.parseInt(result));
            intent.putExtra("Completed", true);
            this.startActivity(intent);
        }
    }

    private void changeRoute(String result, boolean completed) {
        ChangeRoute_Service changeRoute_service = new ChangeRoute_Service(this);
        changeRoute_service.execute(result, Boolean.toString(completed));
    }

    public void changeRouteIntent(String result) {
        Intent intent = new Intent(this.getApplicationContext(), Route_Activity.class);
        String[] splitted = result.split(",");
        intent.putExtra(getString(R.string.route_id_key), Integer.parseInt(splitted[0]));
        intent.putExtra("Completed", Boolean.parseBoolean(splitted[1]));
        this.startActivity(intent);
    }
}
