package com.release.wanandroid.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import com.release.wanandroid.R
import com.release.wanandroid.mvp.model.bean.NavigationBean
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class NavigationTabAdapter(context: Context?, list: List<NavigationBean>) : TabAdapter {

    private var context: Context = context!!
    private var list = mutableListOf<NavigationBean>()

    init {
        this.list = list as MutableList<NavigationBean>
    }

    override fun getIcon(position: Int): ITabView.TabIcon? = null


    override fun getBadge(position: Int): ITabView.TabBadge? = null

    override fun getBackground(position: Int): Int = -1

    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
                .setContent(list[position].name)
                .setTextColor(ContextCompat.getColor(context, R.color.colorAccent),
                        ContextCompat.getColor(context, R.color.Grey500))
                .build()
    }

    override fun getCount(): Int = list.size

}