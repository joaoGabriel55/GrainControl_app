package com.tads.graincontrol.graincontrol;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tads.graincontrol.graincontrol.util.FirebaseUtil;

public class MainActivity extends AppCompatActivity {

    private TextView setPointValue;

    private TextView temperatura1;

    private TextView temperatura2;

    private TextView temperatura3;

    private TextView temperatura4;

    private FrameLayout frameLayout;

    private ChildEventListener childEventListTemperatura;

    private DatabaseReference temperatura1DataBase;
    private DatabaseReference temperatura2DataBase;
    private DatabaseReference temperatura3DataBase;
    private DatabaseReference temperatura4DataBase;


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

        frameLayout = findViewById(R.id.frameLayoutSilo);

        listenerParams();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void listenerParams(){

        temperatura1DataBase = FirebaseUtil.getFirebaseDatabase().getReference("temperatura").child("9/graus");
        temperatura1 = findViewById(R.id.temperatura1);
        FirebaseUtil.manipulateNewWay(childEventListTemperatura, temperatura1DataBase, temperatura1);

        temperatura2 = findViewById(R.id.temperatura2);
        temperatura3 = findViewById(R.id.temperatura3);
        temperatura4 = findViewById(R.id.temperatura4);
    }

    public void sendSetPoint(View v) {
        setPointValue.getText().toString();
    }

    public void onClickChangeSetPoint(View v) {

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.defineSetPoint)
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .input(getString(R.string.set_point), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        input.toString();
                    }
                })
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        return;
                    }
                })
                .build();
        dialog.show();
    }

}
