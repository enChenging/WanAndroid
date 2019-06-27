package com.release.wanandroid.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class ViewPagerAdapter(val list: List<Fragment>,fm :FragmentManager) : FragmentPagerAdapter(fm){
    override fun getItem(p0: Int): Fragment {
        return list.get(p0)
    }

    override fun getCount(): Int {
        return list.size
    }

}