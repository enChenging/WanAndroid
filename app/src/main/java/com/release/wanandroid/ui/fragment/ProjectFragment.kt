package com.release.wanandroid.ui.fragment

import android.support.design.widget.TabLayout
import android.view.View
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseMvpFragment
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.mvp.contract.ProjectContract
import com.release.wanandroid.mvp.model.bean.ProjectTreeBean
import com.release.wanandroid.mvp.presenter.ProjectPresenter
import com.release.wanandroid.ui.adapter.ProjectPagerAdapter
import com.release.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_project.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class ProjectFragment : BaseMvpFragment<ProjectContract.View, ProjectContract.Presenter>(), ProjectContract.View {

    companion object {
        fun getInstance(): ProjectFragment = ProjectFragment()
    }

    override fun createPresenter(): ProjectContract.Presenter = ProjectPresenter()

    override fun initLayoutID(): Int = R.layout.fragment_project

    /**
     * ProjectTreeBean
     */
    private var projectTree = mutableListOf<ProjectTreeBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: ProjectPagerAdapter by lazy {
        ProjectPagerAdapter(projectTree, childFragmentManager)
    }

    override fun useEventBus(): Boolean = true

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) {
            return
        }
        val fragment: ProjectListFragment = viewPagerAdapter.getItem(viewPager.currentItem) as ProjectListFragment
        fragment.scrollToTop()
    }

    override fun setProjectTree(list: List<ProjectTreeBean>) {
        list.let {
            projectTree.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = projectTree.size
            }
        }
        if (list.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun initView(view: View) {
        super.initView(view)
        mLayoutStatusView = multiple_status_view
        viewPager.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        }

        tabLayout.run {
            setupWithViewPager(viewPager)
            // TabLayoutHelper.setUpIndicatorWidth(tabLayout)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        refreshColor(ColorEvent(true))

    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

    override fun lazyLoad() {
        mPresenter?.requestProjectTree()
    }

    override fun doReConnected() {
        if (projectTree.size == 0) {
            super.doReConnected()
        }
    }


    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            if (!SettingUtil.getIsNightMode()) {
                tabLayout.setBackgroundColor(SettingUtil.getColor())
            }
        }
    }

}
