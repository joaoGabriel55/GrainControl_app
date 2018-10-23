package com.tads.graincontrol.graincontrol.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.tads.graincontrol.graincontrol.R;
import com.tads.graincontrol.graincontrol.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentChart extends Fragment {

    private View view;

    private LineGraphSeries<DataPoint> series;
    private ArrayList<Double> tempAvg;

    private DatabaseReference temperaturaAvg;
    private GraphView graph;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.chart_layout, container, false);

        listenerParams(view);

        series = new LineGraphSeries();
        graph = (GraphView) view.findViewById(R.id.graphView);
        initGraph();
        return view;
    }

    private void listenerParams(View view) {
        temperaturaAvg = FirebaseUtil.getFirebaseDatabase().getReference("temperaturaSensorAvg");
        //FirebaseUtil.manipulateAvgSensors(tempAvg, temperaturaAvg);
    }

    private void initGraph() {
        final Double[] tempAux = {0.0};
        temperaturaAvg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Double value = snapshot.getValue(Double.class);

                    if (value.doubleValue() >= 1.0) {
                        dp[index] = new DataPoint(index, value);
                        tempAux[0] = value;
                    } else {
                        dp[index] = new DataPoint(index, tempAux[0]);
                    }


                    index++;
                }

                if (dp != null) {
                    series.resetData(dp);
                    series.setDrawBackground(true);
                    series.setAnimated(true);
                    series.setDrawDataPoints(true);

                    series.setOnDataPointTapListener(new OnDataPointTapListener() {
                        @Override
                        public void onTap(Series series, DataPointInterface dataPoint) {
                            Toast.makeText(graph.getContext(), "Avg Temperature: " + dataPoint, Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        graph.addSeries(series);
    }
}
