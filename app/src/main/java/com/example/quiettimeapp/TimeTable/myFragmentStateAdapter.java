package com.example.quiettimeapp.TimeTable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class myFragmentStateAdapter extends FragmentStateAdapter {

    public myFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Frag_Mon();
            case 1:
                return new Frag_Tues();
            case 2:
                return new Frag_Wed();
            case 3:
                return new Frag_Thurs();
            case 4:
                return new Frag_Fri();
            default:
                return new Frag_Mon();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}