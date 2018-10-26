package com.tads.graincontrol.graincontrol.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import com.tads.graincontrol.graincontrol.util.GrainControlUtils;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentChart extends Fragment {

    private View view;

    private LineGraphSeries<DataPoint> series;
    private LineGraphSeries<DataPoint> seriesSP;
    private ArrayList<Double> tempAvg;

    private DatabaseReference temperaturaAvg;
    private DatabaseReference setPointDataBase;

    private GraphView graph;

    private TextView dateTV;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.chart_layout, container, false);

        listenerParams(view);

        series = new LineGraphSeries();
        seriesSP = new LineGraphSeries();
        graph = (GraphView) view.findViewById(R.id.graphView);

        dateTV = view.findViewById(R.id.dateGraph);

        //dateTV.setText(Calendar.getInstance().getTime().toString());

        GrainControlUtils.getTime(getActivity(), dateTV);

        initGraph();
        return view;
    }

    private void listenerParams(View view) {
        temperaturaAvg = GrainControlUtils.getFirebaseDatabase().getReference("temperaturaSensorAvg");
        setPointDataBase = GrainControlUtils.getFirebaseDatabase().getReference("setpoint");
        //GrainControlUtils.manipulateAvgSensors(tempAvg, temperaturaAvg);
    }

    private void initGraph() {
        final Double[] tempAux = {0.0};
        final Double[] spAux = {0.0};
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

        setPointDataBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dpSp = new DataPoint[1];
                int index = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Double value = snapshot.getValue(Double.class);

                    if (value.doubleValue() >= 1.0) {
                        dpSp[index] = new DataPoint(0, value);
                        spAux[0] = value;
                    } else {
                        dpSp[index] = new DataPoint(index, spAux[0]);
                    }

                    index++;
                }

                if (dpSp != null) {
                    seriesSP.resetData(dpSp);
                    seriesSP.setColor(Color.GREEN);
                    seriesSP.setAnimated(true);
                    seriesSP.setDrawDataPoints(true);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        graph.addSeries(series);
        graph.addSeries(seriesSP);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(9);
        graph.getViewport().setMinY(15.0);
        graph.getViewport().setMaxY(40.0);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
    }
}
