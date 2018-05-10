package echipa_8.centenargo_app.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import echipa_8.centenargo_app.activities.Landmark_Activity;

public class MapUtility {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static boolean mLocationPermissionsGranted;
    private static Location mLastKnownLocation;

    public static boolean getLocationPermissionGranted() {
        return mLocationPermissionsGranted;
    }

    public static boolean getLocationPermission(final AppCompatActivity context) {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            return mLocationPermissionsGranted = true;
        } else {
            ActivityCompat.requestPermissions(context, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    public static void getDeviceLocation(final Activity context, final GoogleMap googleMap) {
        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            if (mLocationPermissionsGranted) {
                Task locationResult =  mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(context, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
                            if (mLastKnownLocation != null) {
                                LatLng currentLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                googleMap.addMarker(new MarkerOptions()
                                        .position(currentLatLng)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                        .title("You are here"));

                            } else {
                                Toast.makeText(context, "You should enable location", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Can't get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public static void validateLocation(final Landmark_Activity context, final double latitude, final double longitude) {
        final FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            if (mLocationPermissionsGranted) {
                Task<Location> locationResult =  mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(context, task -> {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        if (mLastKnownLocation != null) {
                            if (mLastKnownLocation.getAccuracy() > 50) {
                                Toast.makeText(context, "Activeaza Wi-fi, Locatie, Retea mobila", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Location landmarkLocation = new Location("");
                            landmarkLocation.setLatitude(latitude);
                            landmarkLocation.setLongitude(longitude);
                            context.locationResponse(mLastKnownLocation.distanceTo(landmarkLocation) < 50);

                        } else {
                            Toast.makeText(context, "You should enable location", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Can't get current location", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public static String geoLocateByLatLng(Context context, LatLng latLNg) {
        String addressStr = "";
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = new ArrayList<>();

        try{
            addresses = geocoder.getFromLocation(latLNg.latitude, latLNg.longitude,1);
        } catch (IOException e) {
            Log.e("Exception: %s", e.getMessage());
        }

        if(addresses.size() > 0) {
            Address address = addresses.get(0);
            addressStr = address.getAddressLine(0);
        }

        return addressStr;
    }

    public static boolean requestPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return mLocationPermissionsGranted = true;
            } else {
                mLocationPermissionsGranted = false;
            }
        }
        return false;
    }

    public static boolean mapInit(AppCompatActivity context, GoogleMap googleMap) {
        if (mLocationPermissionsGranted) {
            getDeviceLocation(context, googleMap);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }

            googleMap.setMyLocationEnabled(true);
            return true;
        } else {
            googleMap.setMyLocationEnabled(false);
            getLocationPermission(context);
            mLastKnownLocation = null;
        }
        return false;
    }
}
