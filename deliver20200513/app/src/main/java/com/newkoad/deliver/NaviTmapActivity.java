package com.newkoad.deliver;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.newkoad.deliver.common.Order;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

public class NaviTmapActivity extends AppCompatActivity   implements TMapGpsManager.onLocationChangedCallback {

    private static final String TAG ="NAVI_TMAP" ;
    private MainApp mainApp;

    double sPointx, sPointy;
    float ePointx, ePointy;

    float pointx, pointy;
    String pointStr;

    @Override
    public void onLocationChange(Location location) {

        if(m_bTrackingMode)
        {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }

    private     TMapView    tMapView = null;
    private 	boolean 	m_bTrackingMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_tmap);
        mainApp = (MainApp)getApplicationContext();



        Intent intent = getIntent();
        String target = intent.getStringExtra("target");
        Order order = (Order)intent.getParcelableExtra("order");

        if(target.equals("s")){
            pointStr = order.sPoints;
        }else {
            pointStr = order.ePoints;
        }


        String[] pointArray = pointStr.split(",");
        if(pointArray.length == 2) {
            pointx = Float.parseFloat(pointArray[0]);
            pointy = Float.parseFloat(pointArray[1]);
        }



        LinearLayout linearLayout = new LinearLayout(this);

        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey( "e9da241e-0a6a-4fc0-b6b3-67906028b645" );
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);


        invokeRoute();

    }


    public void invokeRoute() {
        final TMapPoint point = tMapView.getCenterPoint();

        point.setLatitude(pointx);
        point.setLongitude(pointy);


        TMapData tmapdata = new TMapData();
        if(tMapView.isValidTMapPoint(point)) {
            tmapdata.convertGpsToAddress(
                    point.getLatitude(),
                    point.getLongitude(),
                    new TMapData.ConvertGPSToAddressListenerCallback() {

                        @Override
                        public void onConvertToGPSToAddress(String strAddress) {

                            TMapTapi tmaptapi = new TMapTapi(NaviTmapActivity.this);
                            float fY = (float)point.getLatitude();
                            float fX = (float)point.getLongitude();


                            tmaptapi.invokeRoute(strAddress, fX, fY);
                            finish();


                }
            });
        }
    }

}
