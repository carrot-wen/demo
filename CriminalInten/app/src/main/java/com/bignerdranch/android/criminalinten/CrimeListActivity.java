package com.bignerdranch.android.criminalinten;

import android.support.v4.app.Fragment;

/**
 * Created by wenzhang on 2017/12/10.
 */

public class CrimeListActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
