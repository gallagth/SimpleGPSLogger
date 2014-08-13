package com.gallagth.simplegpslogger.ui;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.IBinder;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.gallagth.simplegpslogger.MainActivity;
import com.gallagth.simplegpslogger.R;
import com.gallagth.simplegpslogger.utils.LocationRecorder;
import com.gallagth.simplegpslogger.utils.ServiceHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class RecordFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private int mSectionNumber;

    private NumberPicker mRefreshRatePicker;
    private ToggleButton mRecordButton;
    private EditText mRunNameEditText;

    public static RecordFragment newInstance(int sectionNumber) {
        RecordFragment fragment = new RecordFragment();
        fragment.setSectionNumber(sectionNumber);
        return fragment;
    }

    public RecordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //TODO
        //boolean isRecording = LocationRecorder.isRecording();
        //mRecordButton.setChecked(isRecording);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        mRefreshRatePicker = (NumberPicker) view.findViewById(R.id.refreshRatePicker);
        configureRefreshRatePicker(mRefreshRatePicker);
        mRunNameEditText = (EditText) view.findViewById(R.id.runName);
        mRecordButton = (ToggleButton) view.findViewById(R.id.recordButton);
        mRecordButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Messenger service = ((MainActivity) getActivity()).getLocationService();
                try {
                    if (isChecked) {
                        String runName = getRunName();
                        if (runName.isEmpty()) {
                            //TODO toast
                        } else {
                            ServiceHelper.startRecording(service, runName);
                        }
                    } else {
                        ServiceHelper.stopRecording(service);
                    }
                } catch (RemoteException e) {
                    Toast.makeText(getActivity(), "Failed to launch service", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }

    private String getRunName() {
        return mRunNameEditText.getText().toString();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(mSectionNumber);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int getSectionNumber() {
        return mSectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.mSectionNumber = sectionNumber;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void configureRefreshRatePicker(NumberPicker picker) {
        picker.setEnabled(true);
        picker.setMinValue(1);
        picker.setMaxValue(30);
    }

}
