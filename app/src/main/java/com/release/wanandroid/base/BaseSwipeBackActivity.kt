package com.release.wanandroid.base

import android.os.Bundle
import com.cxz.swipelibrary.SwipeBackActivityBase
import com.cxz.swipelibrary.SwipeBackActivityHelper
import com.cxz.swipelibrary.SwipeBackLayout
import com.cxz.swipelibrary.Utils

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
abstract class BaseSwipeBackActivity:BaseActivity(), SwipeBackActivityBase {

    /**
     * SwipeBack Enable
     */
    open fun enableSwipeBack(): Boolean = true

    private lateinit var mHelper: SwipeBackActivityHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHelper = SwipeBackActivityHelper(this)
        mHelper.onActivityCreate()
        initSwipBack()
    }

    private fun initSwipBack() {
        setSwipeBackEnable(enableSwipeBack())
        swipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mHelper.onPostCreate()
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mHelper.swipeBackLayout
    }

    override fun setSwipeBackEnable(enable: Boolean) {
        swipeBackLayout.setEnableGesture(enable)
    }

    override fun scrollToFinishActivity() {
        Utils.convertActivityFromTranslucent(this)
        swipeBackLayout.scrollToFinishActivity()
    }
}