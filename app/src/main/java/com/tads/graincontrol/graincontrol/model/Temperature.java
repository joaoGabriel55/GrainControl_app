package com.tads.graincontrol.graincontrol.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Temperature {

    public float setPoint;

    public Temperature(){}

    public Temperature(float setPoint) {
        this.setPoint = setPoint;
    }
}
