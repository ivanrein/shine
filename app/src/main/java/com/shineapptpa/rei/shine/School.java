package com.shineapptpa.rei.shine;

import java.io.Serializable;

/**
 * Created by rei on 12/20/2015.
 */
public class School implements Serializable {
    private String name;
    private double latitude;
    private double longitude;

    public School(String name, double lat, double lon){
        this.name = name;
        this.latitude = lat;
        this.longitude = lon;

    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public String toString() {
        return getName();
    }
}
