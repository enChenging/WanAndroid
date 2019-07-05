package com.release.wanandroid.base

import android.content.Context
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.afollestad.materialdialogs.color.CircleView
import com.classic.common.MultipleStatusView
import com.cxz.wanandroid.receiver.NetworkChangeReceiver
import com.release.wanandroid.R
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.event.NetworkChangeEvent
import com.release.wanandroid.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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

    open fun useEventBus(): Boolean = false

    open fun initView() {}

    open fun initData() {}

    open fun startNet() {}
    /**
     * 提示View
     */
    protected lateinit var mTipView: View
    protected lateinit var mWindowManager: WindowManager
    protected lateinit var mLayoutParams: WindowManager.LayoutParams

    /**
     * 网络状态变化的广播
     */
    protected var mNetworkChangeReceiver: NetworkChangeReceiver? = null

    /**
     * 缓存上一次的网络状态
     */
    protected var hasNetwork: Boolean by Preference(Constant.HAS_NETWORK_KEY, true)

    /**
     * 是否需要显示 TipView
     */
    open fun enableNetworkTip(): Boolean = true


    /**
     * 无网状态—>有网状态 的自动重连操作，子类可重写该方法
     */
    open fun doReConnected() {
        startNet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(initLayoutID())
        if (useEventBus())
            EventBus.getDefault().register(this)
        initData()
        initTipView()
        initView()
        startNet()
    }

    override fun onResume() {
        super.onResume()
        initReceiver()
        initThemeColor()

        // 在无网络情况下打开APP时，系统不会发送网络状况变更的Intent，需要自己手动检查

        // 1.第一次进入界面会导致 start() 方法走两次
        // 2.后台切换到前台时，会调用 start() 方法执行相应的操作
        // 此处不应该调用，删掉，修改 #13
        // checkNetwork(hasNetwork)
    }

    override fun onPause() {
        if (mNetworkChangeReceiver != null) {
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
        super.onPause()
    }

    private fun initReceiver() {
        // 动态注册网络变化广播
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetworkChangeReceiver()
        registerReceiver(mNetworkChangeReceiver, filter)
    }

    open fun initThemeColor() {
        mThemeColor = if (!SettingUtil.getIsNightMode())
            SettingUtil.getColor()
        else
            resources.getColor(R.color.colorPrimary)

        StatusBarUtil.setColor(this, mThemeColor, 0)

        //ActionBar颜色
        if (this.supportActionBar != null) {
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
        //底部带返回键手机的导航栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (SettingUtil.getNavBar()) {
                window.navigationBarColor = CircleView.shiftColorDown(mThemeColor)
            } else {
                window.navigationBarColor = Color.BLACK
            }
        }
    }


    /**
     * 初始化 TipView
     */
    private fun initTipView() {
        mTipView = layoutInflater.inflate(R.layout.layout_network_tip, null)
        mWindowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        mLayoutParams = WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
        mLayoutParams.gravity = Gravity.TOP
        mLayoutParams.x = 0
        mLayoutParams.y = 0
        mLayoutParams.windowAnimations = R.style.anim_float_view // add animations
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_UP) {
            val v = currentFocus
            // 如果不是落在EditText区域，则需要关闭输入法
            if (KeyBoardUtil.isHideKeyboard(v, ev)) {
                KeyBoardUtil.hideKeyBoard(this, v)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Fragment 逐个出栈
        val count = supportFragmentManager.backStackEntryCount
        if (count == 0) {
            super.onBackPressed()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus())
            EventBus.getDefault().unregister(this)
        CommonUtil.fixInputMethodManagerLeak(this)
    }

    /**
     * Network Change
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangeEvent(event: NetworkChangeEvent) {
        hasNetwork = event.isConnected
        checkNetwork(event.isConnected)
    }

    private fun checkNetwork(isConnected: Boolean) {
        if (enableNetworkTip()) {
            if (isConnected) {
                doReConnected()
                if (mTipView != null && mTipView.parent != null) {
                    mWindowManager.removeView(mTipView)
                }
            } else {
                if (mTipView.parent == null) {
                    mWindowManager.addView(mTipView, mLayoutParams)
                }
            }
        }
    }
}