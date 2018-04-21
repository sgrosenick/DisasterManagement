package org.disasatermngt4a;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<ReportClusterItem> mClusterManager;
    private static final int QUERY_REPORT_REQUEST = 1;
    private static final int CREATE_REPORT_REQUEST = 2;
    public static String servletURL = "http://10.0.2.2:8081/WebProject/HTTPServlet";
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    ;

    private void setUpClusterer() {
        // Position the map.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(51.503186, -0.126446), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<ReportClusterItem>(this, mMap);

        ClusterItemRenderer renderer = new ClusterItemRenderer(this, mMap, mClusterManager);

        mClusterManager.setRenderer(renderer);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (!mMap.isMyLocationEnabled()){
                mMap.setMyLocationEnabled(true);
            }
            /*Criteria criteria = new Criteria();
            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            String provider = lm.getBestProvider(criteria, true);
            //Location location = lm.getLastKnownLocation(provider);

            if (location != null) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                System.out.println("Here are the lat lon");
                System.out.println(userLocation);
            }*/
        }

        HashMap<String, String> data = new HashMap<String, String>();
        data.put("tab_id", "1");
        setUpClusterer();
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data, mMap, mClusterManager);
        asyncHttpPost.execute(servletURL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Add the menu to the action bar, if present
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.report_filter) {
            Intent intent = new Intent(this, ReportFilterActivity.class);
            startActivityForResult(intent, QUERY_REPORT_REQUEST);
            return true;
        }
        if (id == R.id.create_report) {
            Intent intent = new Intent(this, CreateReportActivity.class);
            startActivityForResult(intent, CREATE_REPORT_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == QUERY_REPORT_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                mClusterManager.clearItems();
                mClusterManager.cluster();

                HashMap<String, String> postData = null;
                postData = (HashMap<String, String>) data.getSerializableExtra("data");

                System.out.println("Here is the postData:");
                System.out.println(postData);

                AsyncHttpPost asyncHttpPost = new AsyncHttpPost(postData, mMap, mClusterManager);
                asyncHttpPost.execute(servletURL);
            }
        }
        if (requestCode == CREATE_REPORT_REQUEST) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                if (!mMap.isMyLocationEnabled()) {
                    mMap.setMyLocationEnabled(true);
                }
            }

            if (resultCode == Activity.RESULT_OK) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String provider = locationManager.getBestProvider(criteria, false);

                //List<String> providersList = locationManager.getProviders(false);
                String netwrkProvider = String.valueOf(locationManager.getProvider("network"));

                Location location = locationManager.getLastKnownLocation(provider);

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();

                String latString = Double.toString(latitude);
                String lonString = Double.toString(longitude);

                //String latString = "-21.039242";
                //String lonString = "142.050244";

                HashMap<String, String> postData = null;
                postData = (HashMap<String, String>) data.getSerializableExtra("data");

                postData.put("latitude", latString);
                postData.put("longitude", lonString);

                System.out.println("Here is the data to be submitted:");
                System.out.println(postData);

                AsyncHttpPost asyncHttpPost = new AsyncHttpPost(postData, mMap, mClusterManager);
                asyncHttpPost.execute(servletURL);
            }
        }
    }
}