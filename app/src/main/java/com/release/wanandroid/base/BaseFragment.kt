package com.release.wanandroid.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.classic.common.MultipleStatusView
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.utils.Preference
import org.greenrobot.eventbus.EventBus

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
abstract class BaseFragment : Fragment() {

    /**
     * 视图是否加载完毕
     */
    private var isViewPrepare = false
    /**
     * 数据是否加载过了
     */
    private var hasLoadData = false
    /**
     * check login
     */
    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    protected var mLayoutStatusView: MultipleStatusView? = null

    abstract fun initLayoutID(): Int

    open fun initView(view: View) {}

    open fun initData() {}

    open fun lazyLoad() {}

    /**
     * 是否使用 EventBus
     */
    open fun useEventBus(): Boolean = false

    open fun doReConnected() {
        lazyLoad()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(initLayoutID(), null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEventBus()){
            EventBus.getDefault().register(this)
        }
        isViewPrepare = true
        initData()
        initView(view)
        lazyLoadDataIfPrepared()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            lazyLoadDataIfPrepared()
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }
}