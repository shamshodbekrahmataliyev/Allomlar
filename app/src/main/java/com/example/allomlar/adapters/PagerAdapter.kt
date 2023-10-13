package com.mac.allomalar.adapters

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mac.allomalar.ui.fragments.PagerFragment

class PagerAdapter(var list: List<PagerFragment>, var number: Int, fm: FragmentManager,  behavior: Int) : FragmentPagerAdapter(fm, behavior){
    override fun getCount(): Int  = number

    override fun getItem(position: Int): PagerFragment {
    return list[position]
    }

}