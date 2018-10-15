package com.tads.graincontrol.graincontrol;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tads.graincontrol.graincontrol.model.Temperature;
import com.tads.graincontrol.graincontrol.util.FirebaseUtil;

public class MainActivity extends AppCompatActivity {

    private TextView setPointValue;
    private EditText setPointDialog;

    private TextView temperatura1;

    private TextView temperatura2;

    private TextView temperatura3;

    private TextView temperatura4;

    private TextView average;

    private FrameLayout frameLayout;

    private DatabaseReference temperatura1DataBase;
    private DatabaseReference temperatura2DataBase;
    private DatabaseReference temperatura3DataBase;
    private DatabaseReference temperatura4DataBase;

    private DatabaseReference averageDataBase;
    private DatabaseReference setPointDataBase;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    frameLayout.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_dashboard:
                    frameLayout.setVisibility(View.GONE);
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
    }

    private void listenerParams() {

        frameLayout = findViewById(R.id.frameLayoutSilo);

        temperatura1DataBase = FirebaseUtil.getFirebaseDatabase().getReference("temperaturaSensor1/9/").child("graus");
        temperatura1 = findViewById(R.id.temperatura1);
        FirebaseUtil.manipulateNewWay(temperatura1DataBase, temperatura1);

        temperatura2 = findViewById(R.id.temperatura2);
        temperatura3 = findViewById(R.id.temperatura3);
        temperatura4 = findViewById(R.id.temperatura4);

        averageDataBase = FirebaseUtil.getFirebaseDatabase().getReference("average");
        average = findViewById(R.id.averageValue);
        FirebaseUtil.manipulateNewWay(averageDataBase, average);

        setPointDataBase = FirebaseUtil.getFirebaseDatabase().getReference("setpoint").child("value");
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
