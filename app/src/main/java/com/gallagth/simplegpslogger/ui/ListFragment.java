package com.gallagth.simplegpslogger.ui;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gallagth.simplegpslogger.MainActivity;
import com.gallagth.simplegpslogger.R;

import java.util.List;

/**
 * Created by Thomas on 9/08/2014.
 */
public class ListFragment extends Fragment {

    public int getSectionNumber() {
        return mSectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.mSectionNumber = sectionNumber;
    }

    private int mSectionNumber;

    public static ListFragment newInstance(int sectionNumber) {
        ListFragment fragment = new ListFragment();
        fragment.setSectionNumber(sectionNumber);
        return fragment;
    }

    public ListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(mSectionNumber);
    }

}
