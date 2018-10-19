package com.tads.graincontrol.graincontrol.util;

import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FirebaseUtil {

    private static FirebaseDatabase mFirebaseDatabase;

    public static FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase.getInstance();
    }

    public static void manipulateNewWay(DatabaseReference databaseReference,
                                        final TextView textView) {
        final DecimalFormat formatador = new DecimalFormat("0.0");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Double value = dataSnapshot.getValue(Double.class);

                if (value != null) {
                    if (value.doubleValue() >= 1.0)
                        textView.setText(formatador.format(value.doubleValue()));
//                    else
//                        textView.setText("0.0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        databaseReference.addValueEventListener(postListener);

    }

    public static void manipulateAvgSensors(final List<Double> tempAvg, DatabaseReference databaseReference) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Double avg = postSnapshot.getValue(Double.class);
                    tempAvg.add(avg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO
            }
        };
        databaseReference.addValueEventListener(postListener);
    }

}
