package org.disasatermngt4a;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

public class AsyncHttpPost extends AsyncTask<String, Void, JSONArray> {
    private HashMap<String, String> mData;// post data
    private GoogleMap mMap;
    private ClusterManager<ReportClusterItem> mClusterManager;

    public AsyncHttpPost(HashMap<String, String> data, GoogleMap map, ClusterManager<ReportClusterItem> clusterManager) {
        mData = data;
        mMap = map;
        mClusterManager = clusterManager;
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray arr = null;
        HttpClient client = new DefaultHttpClient();

        try {
            HttpPost post = new HttpPost(params[0]); //In this case, params[0] is a URL

            //set up data
            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
            Iterator<String> it = mData.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));

            HttpResponse response = client.execute(post);

            byte[] result = EntityUtils.toByteArray(response.getEntity());
            String str = new String(result, "UTF-8");
            //System.out.println("Here is the str variable");
            //System.out.println(str);
            arr = new JSONArray(str);
        } catch (UnsupportedEncodingException e) {
            android.util.Log.v("INFO", e.toString());
        } catch (Exception e) {
            android.util.Log.v("INFO", e.toString());
        }
        return arr;
    }

    @Override
    protected void onPostExecute(JSONArray Result) {
        if (mData.get("tab_id").equalsIgnoreCase("1")) {
            onQueryReportExecute(Result);
        }
        if (mData.get("tab_id").equalsIgnoreCase("0")) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("tab_id", "1");
            //setUpClusterer();
            AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data, mMap, mClusterManager);
            asyncHttpPost.execute("http://10.0.2.2:8081/WebProject/HTTPServlet");
        }
    }

    private void onQueryReportExecute(JSONArray Result) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < Result.length(); i++) {
            try {
                JSONObject report = Result.getJSONObject(i);
                //System.out.println("Here is the full report:");
                //System.out.println(report);
                Double lng = Double.parseDouble(report.getString("longitude"));
                Double lat = Double.parseDouble(report.getString("latitude"));
                LatLng latlng = new LatLng(lat, lng);
                builder.include(latlng);

                String rprtType = report.getString("report_type");
                //System.out.println("Here are the report types with lat,lng");
                //System.out.println("'" + rprtType + "' at location " + latlng);
                String distType = report.getString("disaster").toUpperCase();


                if (rprtType.equals("donation")) {
                    String resourceType = report.getString("resource_type");
                    String message = "Resource Donation: " + resourceType;

                    ReportClusterItem rci = new ReportClusterItem(lat, lng, distType, message, ReportTypeEnum.Donation);
                    mClusterManager.addItem(rci);
                }

                if (rprtType.equals("request")) {
                    String resourceType = report.getString("resource_type");
                    String message = "Resource Request: " + resourceType;

                    ReportClusterItem rci = new ReportClusterItem(lat, lng, distType, message, ReportTypeEnum.Request);
                    mClusterManager.addItem(rci);
                }

                if (rprtType.equals("damage")) {
                    String damageType = report.getString("damage_type");
                    String message = "Reported Damage: " + damageType;

                    ReportClusterItem rci = new ReportClusterItem(lat, lng, distType, message, ReportTypeEnum.Damage);
                    mClusterManager.addItem(rci);
                }

            } catch (JSONException e) {
                android.util.Log.v("INFO", e.toString());
            }
        }

        if (Result.length() > 0) {
            LatLngBounds bounds = builder.build();

            int padding = 0; //offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            mMap.moveCamera(cu);
        }
    }

  /*  private void onCreateReportExecute(JSONArray Result) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < Result.length(); i++) {
            try {
                JSONObject report = Result.getJSONObject(i);

                String reportType = report.getString("report_type");
                String disasterType = report.getString("disaster_type");
                Double latitude = Double.parseDouble(report.getString("latitude"));
                Double longitude = Double.parseDouble(report.getString("longitude"));

                if (reportType.equals("donation")) {
                    String resourceType = report.getString("resource_type");
                    String message = "Resource Donation: " + resourceType;

                    ReportClusterItem rci = new ReportClusterItem(latitude, longitude, disasterType, message, ReportTypeEnum.Donation);
                    mClusterManager.addItem(rci);
                }

                if (reportType.equals("request")) {
                    String resourceType = report.getString("resource_type");
                    String message = "Resource Request: " + resourceType;

                    ReportClusterItem rci = new ReportClusterItem(latitude, longitude, disasterType, message, ReportTypeEnum.Request);
                    mClusterManager.addItem(rci);
                }

                if (reportType.equals("damage")) {
                    String damageType = report.getString("damage_type");
                    String message = "Reported Damage: " + damageType;

                    ReportClusterItem rci = new ReportClusterItem(latitude, longitude, disasterType, message, ReportTypeEnum.Damage);
                    mClusterManager.addItem(rci);
                }

            } catch (JSONException e) {
                android.util.Log.v("INFO", e.toString());
            }
        }
    }*/
}
