package com.tads.graincontrol.graincontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.tads.graincontrol.graincontrol.util.FirebaseUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GraphView graph;

    private LineGraphSeries<DataPoint> series;
    final List<Double> tempAvg;

    private TextView setPointValue;
    private EditText setPointDialog;

    private TextView average;

    private FrameLayout frameLayout;
    private FrameLayout frameLayoutChart;

    private DatabaseReference temperaturaAvg;

    private DatabaseReference averageDataBase;
    private DatabaseReference setPointDataBase;


    MainActivity() {
        tempAvg = new ArrayList<>();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    frameLayout.setVisibility(View.VISIBLE);
                    frameLayoutChart.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    frameLayout.setVisibility(View.GONE);
                    frameLayoutChart.setVisibility(View.VISIBLE);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUtil.getFirebaseDatabase().setPersistenceEnabled(true);

        listenerParams();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //Chart create
        series = new LineGraphSeries();
        graph = (GraphView) findViewById(R.id.graph);
    }



    @Override
    protected void onStart() {
        super.onStart();

        temperaturaAvg.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Double value = snapshot.getValue(Double.class);
                    dp[index] = new DataPoint(index, value);
                    index++;
                }

                series.resetData(dp);
                series.setDrawBackground(true);
                series.setAnimated(true);
                series.setDrawDataPoints(true);

                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(graph.getContext(), "Avg Temperature: "+dataPoint, Toast.LENGTH_SHORT).show();
                    }
                });

                graph.addSeries(series);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //    @Override
//    protected void onResume() {
//        super.onResume();
//        initGraph(graph);
//
//    }

    private void listenerParams() {

        frameLayout = findViewById(R.id.frameLayoutSilo);

        frameLayoutChart = findViewById(R.id.chart_frame);

        temperaturaAvg = FirebaseUtil.getFirebaseDatabase().getReference("temperaturaSensorAvg");
        //FirebaseUtil.manipulateAvgSensors(tempAvg, temperaturaAvg);

        averageDataBase = FirebaseUtil.getFirebaseDatabase().getReference("average");
        average = findViewById(R.id.averageValue);
        FirebaseUtil.manipulateNewWay(averageDataBase, average);

        setPointDataBase = FirebaseUtil.getFirebaseDatabase().getReference("setpoint").child("valor");
        setPointValue = findViewById(R.id.setPointValue);
        FirebaseUtil.manipulateNewWay(setPointDataBase, setPointValue);
    }

    public void onClickChangeSetPoint(View v) {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.defineSetPoint)
                .customView(R.layout.setpoint_layout, true)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String temp = setPointDialog.getText().toString();

                        //TODO Limiar de temperatura
                        if (setPointDialog.getText().length() > 0) {
                            setPointDialog.setText(temp);
                            setPointDataBase.setValue(Double.parseDouble(temp));
                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        return;
                    }
                })
                .build();
        setPointDialog = (EditText) dialog.getCustomView().findViewById(R.id.setPointValueModal);
        dialog.show();
    }

}
