package com.ltb.orderfoodapp

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OngoingFragment()
            1 -> HistoryFragment()
            else -> OngoingFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2 // Số lượng tab
    }
}