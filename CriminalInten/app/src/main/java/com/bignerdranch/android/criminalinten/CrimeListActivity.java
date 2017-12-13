package com.bignerdranch.android.criminalinten;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by wenzhang on 2017/12/10.
 */

public class CrimeListActivity extends SingleFragmentActivity{

    private static final String EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id";


    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
