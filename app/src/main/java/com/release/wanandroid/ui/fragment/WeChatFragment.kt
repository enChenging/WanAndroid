package com.release.wanandroid.ui.fragment

import android.support.design.widget.TabLayout
import android.view.View
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseMvpFragment
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.mvp.contract.WeChatContract
import com.release.wanandroid.mvp.model.bean.WXChapterBean
import com.release.wanandroid.mvp.presenter.WeChatPresenter
import com.release.wanandroid.ui.adapter.WeChatPagerAdapter
import com.release.wanandroid.utils.SettingUtil
import kotlinx.android.synthetic.main.fragment_home.multiple_status_view
import kotlinx.android.synthetic.main.fragment_wechat.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class WeChatFragment : BaseMvpFragment<WeChatContract.View, WeChatContract.Presenter>(), WeChatContract.View {

    companion object {
        fun getInstance(): WeChatFragment = WeChatFragment()
    }

    override fun createPresenter(): WeChatContract.Presenter = WeChatPresenter()

    override fun initLayoutID(): Int = R.layout.fragment_wechat


    /**
     * datas
     */
    private val datas = mutableListOf<WXChapterBean>()

    /**
     * ViewPagerAdapter
     */
    private val viewPagerAdapter: WeChatPagerAdapter by lazy {
        WeChatPagerAdapter(datas, childFragmentManager)
    }

    override fun useEventBus(): Boolean = true

    override fun scrollToTop() {
        if (viewPagerAdapter.count == 0) {
            return
        }
        val fragment: KnowledgeFragment = viewPagerAdapter.getItem(viewPager.currentItem) as KnowledgeFragment
        fragment.scrollToTop()
    }

    override fun showWXChapters(chapters: MutableList<WXChapterBean>) {
        chapters.let {
            datas.addAll(it)
            viewPager.run {
                adapter = viewPagerAdapter
                offscreenPageLimit = datas.size
            }
        }
        if (chapters.isEmpty()) {
            mLayoutStatusView?.showEmpty()
        } else {
            mLayoutStatusView?.showContent()
        }
    }

    override fun lazyLoad() {
        mPresenter?.getWXChapters()
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

    override fun doReConnected() {
        if (datas.size == 0) {
            super.doReConnected()
        }
    }

    override fun hideLoading() {
        super.hideLoading()
    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        mLayoutStatusView?.showError()
    }

}
