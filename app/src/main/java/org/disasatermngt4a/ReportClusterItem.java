package org.disasatermngt4a;

import com.google.maps.android.clustering.ClusterItem;
import com.google.android.gms.maps.model.LatLng;


public class ReportClusterItem implements ClusterItem {

        private LatLng mPosition;
        private String mTitle;
        private String mSnippet;
        private ReportTypeEnum mReportTypeEnum;

//        public ReportClusterItem(double lat, double lng) {
//            mPosition = new LatLng(lat, lng);
//        }

        public ReportClusterItem(double lat, double lng, String title, String snippet, ReportTypeEnum rprType) {
            mPosition = new LatLng(lat, lng);
            mTitle = title;
            mSnippet = snippet;
            mReportTypeEnum = rprType;
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

        @Override
        public String getTitle() {
            return mTitle;
        }

        @Override
        public String getSnippet() {
            return mSnippet;
        }

        public ReportTypeEnum getmReportTypeEnum() {
            return mReportTypeEnum;
        }
}
