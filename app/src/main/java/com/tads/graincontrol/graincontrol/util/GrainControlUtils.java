package com.tads.graincontrol.graincontrol.util;

import android.app.Activity;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GrainControlUtils {

    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getFirebaseDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
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

    public static void getTime(final Activity activity, final TextView textView) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                Calendar.getInstance().getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");
                                textView.setText(sdf.format(Calendar.getInstance().getTime()));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();


    }

}
