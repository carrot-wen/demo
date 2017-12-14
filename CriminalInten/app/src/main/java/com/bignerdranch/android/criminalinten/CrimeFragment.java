package com.bignerdranch.android.criminalinten;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by wenzhang on 2017/12/9.
 */

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    public static final String CRIME_ID = "crime_id";
    public static final String JUMP_TO_FIRST = "jump_to_first";
    public static final String JUMP_TO_LAST = "jump_to_last";
    private Crime mCrime;

    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mJumpFirstButton;
    private Button mJumpLastButton;
    private Intent intent = new Intent();

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        intent.putExtra(CRIME_ID,mCrime.getId());
        getActivity().setResult(Activity.RESULT_OK,intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        mDateButton.setEnabled(false);
        mDateButton.setText(mCrime.getDate().toString());

        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mJumpFirstButton = v.findViewById(R.id.jump_to_first);
        mJumpFirstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              intent.putExtra(JUMP_TO_FIRST,true);
              getActivity().setResult(Activity.RESULT_OK,intent);
              getActivity().onBackPressed();
            }
        });
        mJumpLastButton = v.findViewById(R.id.jump_to_last);
        mJumpLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(JUMP_TO_LAST,true);
                getActivity().setResult(Activity.RESULT_OK,intent);
                getActivity().onBackPressed();
            }
        });
        return v;
    }
}
