package com.example.appointmentmodule.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.appointmentmodule.RealtimeDB.BookedAppointmentFrag;
import com.example.appointmentmodule.Doctor.DoctorFrag;
import com.example.appointmentmodule.R;
import com.example.appointmentmodule.Specialization.SpecializationFrag;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(@NonNull FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }


    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 1:  return new DoctorFrag();
            case 2:  return new BookedAppointmentFrag();

            default: return new SpecializationFrag();
        }
        //return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0){
            title = "Specialization";
        }
        else if(position == 1){
            title = "Doctor";
        }
        else{
            title = "Appointments";
        }
        return title;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}