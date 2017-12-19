package com.bignerdranch.android.criminalinten;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wenzhang on 2017/12/10.
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);

        }
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();

    }

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if(crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }

    public void deleteCrime(UUID id) {
       int index = find(id);
       if(index >= 0) {
           mCrimes.remove(index);
       }
    }

    public int find(UUID id) {
        for (int i=0; i<mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

}
