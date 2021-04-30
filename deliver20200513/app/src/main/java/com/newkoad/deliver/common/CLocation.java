package com.newkoad.deliver.common;

import android.location.Location;

public class CLocation extends Location {

    private boolean bUseMetricUnits = false;

    public CLocation(Location location){
        this(location, true);
    }

    public CLocation(Location location, boolean bUseMetricUnits) {

        super(location);
        this.bUseMetricUnits = bUseMetricUnits;
    }


    public boolean getUseMetricUnits( ){

        return this.bUseMetricUnits;

    }

    public void setUseMetricunits(boolean bUseMetricUntis){

        this.bUseMetricUnits = bUseMetricUntis;
    }

    @Override
    public float distanceTo(Location dest) {

        float nDistance = super.distanceTo(dest);
        if(!this.getUseMetricUnits()) {
            //Convert meters to feet
            nDistance = nDistance * 3.28083989501312f;
        }
        return nDistance;
    }


    @Override
    public double getAltitude() {

        double nAltitude = super.getAltitude();
        if(!this.getUseMetricUnits())
        {

            nAltitude = nAltitude * 3.28083989501312d;
        }
        return nAltitude;
    }
    @Override
    public float getAccuracy() {

        float nAccuracy = super.getAccuracy();
        if(!this.getUseMetricUnits())
        {

            nAccuracy = nAccuracy * 3.28083989501312f;
        }
        return nAccuracy;
    }


    @Override
    public float getSpeed() {

        float nSpeed = super.getSpeed() * 3.6f;
        if(!this.getUseMetricUnits()){

            nSpeed = super.getSpeed() * 2.2369362920544f/3.6f;
        }
        return nSpeed;
    }




}
