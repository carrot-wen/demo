package com.bignerdranch.android.criminalinten;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

/**
 * Created by wenzhang on 2017/12/10.
 */

public class CrimeListFragment extends Fragment{

    private RecyclerView mCrimeRecyclerView;
    private LinearLayout mNoCrimeHintLayout;
    private Button mAddCrimeButton;
    private CrimeAdapter mAdapter;
    private LinearLayoutManager mManager;
    private boolean mSubtitleVisible;

    private static final int NEED_POLICE = 0;
    private static final int NO_NEED_POLICE = 1;
    private static final int REQUEST_CRIME = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mNoCrimeHintLayout = view.findViewById(R.id.no_crime_hint);
        mAddCrimeButton = view.findViewById(R.id.add_crime);
        mAddCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewCrime();
            }
        });
        mManager = new LinearLayoutManager(getActivity());
        mCrimeRecyclerView.setLayoutManager(mManager);
        updateUI();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CRIME) {
            UUID id = (UUID) data.getSerializableExtra(CrimeFragment.CRIME_ID);
            updateUI(id);
            if (data.getBooleanExtra(CrimeFragment.JUMP_TO_FIRST,false) ) {
                mCrimeRecyclerView.scrollToPosition(0);
                View view = mCrimeRecyclerView.getChildAt(0);
                view.performClick();
            } else if (data.getBooleanExtra(CrimeFragment.JUMP_TO_LAST,false)) {
                int last = mManager.getItemCount()-1;
                mCrimeRecyclerView.scrollToPosition(last);
                View view = mCrimeRecyclerView.getChildAt(mCrimeRecyclerView.getChildCount()-1);
                view.performClick();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime:
                addNewCrime();
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle() {
        int crimeSize = CrimeLab.get(getActivity()).getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeSize, crimeSize);
        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateNoCrimeHint() {
        int crimeSize = CrimeLab.get(getActivity()).getCrimes().size();
        if (crimeSize == 0) {
            mNoCrimeHintLayout.setVisibility(View.VISIBLE);
        } else {
            mNoCrimeHintLayout.setVisibility(View.GONE);
        }
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        mAdapter = new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);
        updateSubtitle();
        updateNoCrimeHint();
    }

    private void updateUI(UUID id) {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        int result = crimeLab.find(id);
        if(result != -1) {
            mAdapter.notifyItemChanged(result);
            updateSubtitle();
            updateNoCrimeHint();
        }

    }

    private void addNewCrime() {
        Crime crime = new Crime();
        CrimeLab.get(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(),crime.getId());
        startActivity(intent);
    }


    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private Crime mCrime;

        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mSolvedImageView = itemView.findViewById(R.id.crime_solved);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            String date = (String) DateFormat.format("EEEE, MMM dd,yyyy",mCrime.getDate());
            mDateTextView.setText(date);
            mSolvedImageView.setVisibility(crime.isSolved()?View.VISIBLE:View.GONE);

        }

        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent,REQUEST_CRIME);
        }
    }

    private class SeriousCrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;

        public SeriousCrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime_serious,parent,false));
            itemView.setOnClickListener(this);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
        }

        public void bind(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(),mCrime.getTitle()+ "clicked!",Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
           if (viewType == NO_NEED_POLICE) {
               return new CrimeHolder(layoutInflater,parent);
           } else {
               return new SeriousCrimeHolder(layoutInflater,parent);
           }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (holder instanceof CrimeHolder) {
                ((CrimeHolder)holder).bind(crime);
            } else if (holder instanceof SeriousCrimeHolder) {
                ((SeriousCrimeHolder)holder).bind(crime);
            }

        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            return NO_NEED_POLICE;
        }
    }
}
