package com.release.wanandroid.base

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.afollestad.materialdialogs.color.CircleView
import com.classic.common.MultipleStatusView
import com.orhanobut.logger.Logger
import com.release.wanandroid.R
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.utils.CommonUtil
import com.release.wanandroid.utils.Preference
import com.release.wanandroid.utils.SettingUtil
import com.release.wanandroid.utils.StatusBarUtil

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
abstract class BaseActivity : AppCompatActivity() {


    protected var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)

    abstract fun initLayoutID(): Int

    protected var mThemeColor: Int = SettingUtil.getColor()

    protected var mLayoutStatusView: MultipleStatusView? = null

    open fun initView() {

    }

    open fun initData() {

    }

    open fun startNet() {

    }

    open fun doReConnected() {
        startNet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(initLayoutID())
        initView()
        initData()
        startNet()
        initListener()
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener { startNet() }
    }

    override fun onResume() {
        super.onResume()
        initColor()
    }

    open fun initColor() {
        mThemeColor = if (!SettingUtil.getIsAutoNightMode()) {
            SettingUtil.getColor()
        } else {
            resources.getColor(R.color.colorPrimary)
        }

        StatusBarUtil.setColor(this, mThemeColor, 0)
        if (this.supportActionBar != null) {
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (SettingUtil.getNavBar()) {
                window.navigationBarColor = CircleView.shiftColorDown(mThemeColor)
            } else {
                window.navigationBarColor = Color.BLACK
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
            Logger.i("onBackPressed----base-----2")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CommonUtil.fixInputMethodManagerLeak(this)
    }

}