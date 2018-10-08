package com.tads.graincontrol.graincontrol.util;

import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class FirebaseUtil {

    private static FirebaseDatabase mFirebaseDatabase;

    public static FirebaseDatabase getFirebaseDatabase() {
        return mFirebaseDatabase.getInstance();
    }

    public static void manipulateNewWay(ChildEventListener childEventListener, DatabaseReference databaseReference,
                                        final TextView textView) {
        final DecimalFormat formatador = new DecimalFormat("0.0");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Double value = dataSnapshot.getValue(Double.class);

                if (value.doubleValue() < 0.0)
                    textView.setText("0.0");
                else
                    textView.setText(formatador.format(value.doubleValue()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        databaseReference.addValueEventListener(postListener);

    }

}
