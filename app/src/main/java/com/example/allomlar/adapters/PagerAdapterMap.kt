package com.mac.allomalar.adapters

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mac.allomalar.ui.fragments.PageFirstFragment
import com.mac.allomalar.ui.fragments.PageSecondFragment
import com.mac.allomalar.ui.fragments.PageThirdFragment
import com.mac.allomalar.ui.fragments.PagerFragment

class PagerAdapterMap(
    var page1: PageFirstFragment,
    var page2: PageSecondFragment,
    var page3: PageThirdFragment,
    var number: Int,
    fm: FragmentManager,
    behavior: Int
) : FragmentPagerAdapter(fm, behavior) {
    override fun getCount(): Int = number

    override fun getItem(position: Int): Fragment {
        return if (position == 0) {
            page1
        } else if (position == 1) {
            page2
        } else {
            page3
        }
    }

}