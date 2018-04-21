package org.disasatermngt4a;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;


public class ClusterItemRenderer extends DefaultClusterRenderer<ReportClusterItem> {

    private final IconGenerator mClusterIconGenerator;
    private final Context mContext;

    public ClusterItemRenderer(Context context, GoogleMap map, ClusterManager<ReportClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mContext = context;
        mClusterIconGenerator = new IconGenerator(mContext.getApplicationContext());
    }

    @Override
    protected void onBeforeClusterItemRendered(ReportClusterItem item, MarkerOptions markerOptions) {

        BitmapDescriptor markerDescriptor;
        if (item.getmReportTypeEnum() == ReportTypeEnum.Donation) {
           markerDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.donation_icon);
           markerOptions.icon(markerDescriptor);
        } else if (item.getmReportTypeEnum() == ReportTypeEnum.Request) {
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.request_icon);
            markerOptions.icon(markerDescriptor);
        } else if (item.getmReportTypeEnum() == ReportTypeEnum.Damage) {
            markerDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.damage_icon);
            markerOptions.icon(markerDescriptor);
        }
    }
}
