package com.gallagth.simplegpslogger.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gallagth.simplegpslogger.R;
import com.gallagth.simplegpslogger.model.Run;

import org.parceler.Parcels;

/**
 * Created by thomas on 27/08/2014.
 */
public class RunDetailsFragment extends Fragment {

    private Run mRun;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run_details, container, false);
        mRun = Parcels.unwrap(getArguments().getParcelable("run_details"));
        ((TextView) view.findViewById(R.id.runDetailsName)).setText(mRun.getName());
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);
        NavUtils.navigateUpFromSameTask(activity);
    }
}
