package com.gallagth.simplegpslogger.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gallagth.simplegpslogger.MainActivity;
import com.gallagth.simplegpslogger.R;
import com.gallagth.simplegpslogger.model.Run;
import com.google.gson.Gson;

import org.parceler.Parcels;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Thomas on 9/08/2014.
 */
public class RunListFragment extends Fragment {

    private ArrayAdapter<Run> mDisplayedRuns;
    private ListView mRunListView;
    private Context mContext;
    private Gson mGson;

    public int getSectionNumber() {
        return mSectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.mSectionNumber = sectionNumber;
    }

    private int mSectionNumber;

    public static RunListFragment newInstance(int sectionNumber) {
        RunListFragment fragment = new RunListFragment();
        fragment.setSectionNumber(sectionNumber);
        return fragment;
    }

    public RunListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGson = new Gson();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        mRunListView = (ListView) view.findViewById(R.id.runList);
        mRunListView.setOnItemClickListener(mOnRunClickListener);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(mSectionNumber);
        mContext = activity.getBaseContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        //do this on main thread for now
        updateListData();
    }

    private void updateListData() {
        if (mRunListView == null || mContext == null)
            return;
        File dir = mContext.getExternalFilesDir(null);
        File[] runs = dir.listFiles();
        List<Run> data = new ArrayList<Run>(runs.length);
        for (File f: runs) {
            try {
                String json = readFile(f);
                data.add(mGson.fromJson(json, Run.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mDisplayedRuns = new RunAdapter(mContext, data);
        mRunListView.setAdapter(mDisplayedRuns);
        mDisplayedRuns.notifyDataSetChanged();
    }

    private class RunAdapter extends ArrayAdapter<Run> {
        private Context context;
        private SimpleDateFormat timeFormatter;

        public RunAdapter(Context context, List<Run> runs) {
            super(context, R.layout.list_item_run, runs);
            this.context = context;
            this.timeFormatter = new SimpleDateFormat("EEE, MMM d, HH:mm");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.list_item_run, parent, false);
            }
            TextView runName = (TextView) rowView.findViewById(R.id.runName);
            TextView runTime = (TextView) rowView.findViewById(R.id.runTime);
            TextView runLength = (TextView) rowView.findViewById(R.id.runLength);
            runName.setText(getItem(position).getName());
            runTime.setText(timeFormatter.format(new Date(getItem(position).getCreationTime())));
            runLength.setText(String.format("%.1f km", getItem(position).getLength() / 1000.0));
            return rowView;
        }
    }

    private String readFile(File file) throws IOException {
        StringBuilder fileContents = new StringBuilder((int)file.length());
        Scanner scanner = new Scanner(file);
        String lineSeparator = System.getProperty("line.separator");
        try {
            while(scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine()).append(lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    private AdapterView.OnItemClickListener mOnRunClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Run clickedRun = (Run) parent.getItemAtPosition(position);

            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            RunDetailsFragment runDetailsFragment = new RunDetailsFragment();
            Bundle args = new Bundle();
            args.putParcelable("run_details", Parcels.wrap(clickedRun));
            runDetailsFragment.setArguments(args);
            transaction.replace(R.id.container, runDetailsFragment);
            transaction.addToBackStack("List");
            transaction.commit();
        }
    };

}
