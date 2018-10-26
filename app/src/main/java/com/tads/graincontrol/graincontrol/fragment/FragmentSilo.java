package com.tads.graincontrol.graincontrol.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.database.DatabaseReference;
import com.tads.graincontrol.graincontrol.R;
import com.tads.graincontrol.graincontrol.util.GrainControlUtils;

public class FragmentSilo extends Fragment {

    private View view;

    private TextView setPointValue;
    private EditText setPointDialog;

    private TextView average;

    private TextView changeLinkCmd;

    private DatabaseReference averageDataBase;
    private DatabaseReference setPointDataBase;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.silo_layout, container, false);

        listenerParams(view);

        changeLinkCmd = (TextView) view.findViewById(R.id.changeLink);
        changeLinkCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangeSetPoint(v);
            }
        });

        return view;
    }

    private void listenerParams(View view) {

        averageDataBase = GrainControlUtils.getFirebaseDatabase().getReference("average");
        average = view.findViewById(R.id.averageValue);
        GrainControlUtils.manipulateNewWay(averageDataBase, average);

        setPointDataBase = GrainControlUtils.getFirebaseDatabase().getReference("setpoint").child("valor");
        setPointValue = view.findViewById(R.id.setPointValue);
        GrainControlUtils.manipulateNewWay(setPointDataBase, setPointValue);
    }

    public void onClickChangeSetPoint(View v) {

        MaterialDialog dialog = new MaterialDialog.Builder(v.getContext())
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
                            Double tempNum = Double.parseDouble(temp);
                            if(tempNum >= 20  && tempNum <= 50)
                                setPointDataBase.setValue(tempNum);
                            else
                                Toast.makeText(getContext(), "SetPoint out of interval [20 - 50]", Toast.LENGTH_SHORT).show();

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
