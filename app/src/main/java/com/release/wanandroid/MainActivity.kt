package com.release.wanandroid

import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatDelegate
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.release.wanandroid.base.BaseMvpActivity
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.event.LoginEvent
import com.release.wanandroid.event.RefreshHomeEvent
import com.release.wanandroid.ext.showToast
import com.release.wanandroid.mvp.contract.MainContract
import com.release.wanandroid.mvp.presenter.MainPresenter
import com.release.wanandroid.ui.activity.CommonActivity
import com.release.wanandroid.ui.activity.SearchActivity
import com.release.wanandroid.ui.activity.SettingActivity
import com.release.wanandroid.ui.activity.TodoActivity
import com.release.wanandroid.ui.adapter.ViewPagerAdapter
import com.release.wanandroid.ui.fragment.*
import com.release.wanandroid.ui.login.LoginActivity
import com.release.wanandroid.utils.CommonUtil.initNavigationColor
import com.release.wanandroid.utils.DialogUtil
import com.release.wanandroid.utils.SettingUtil
import com.release.wanandroid.utils.Sp
import com.release.wanandroid.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class MainActivity : BaseMvpActivity<MainContract.View, MainContract.Presenter>(), MainContract.View {

    override fun createPresenter(): MainContract.Presenter = MainPresenter()

    override fun initLayoutID(): Int = R.layout.activity_main

    private val BOTTOM_INDEX: String = "bottom_index"

    private val FRAGMENT_HOME = 0
    private val FRAGMENT_KNOWLEDGE = 1
    private val FRAGMENT_WECHAT = 2
    private val FRAGMENT_NAVIGATION = 3
    private val FRAGMENT_PROJECT = 4

    private var mIndex = FRAGMENT_HOME

    private val fragments = ArrayList<Fragment>(5)

    override fun useEventBus(): Boolean = true

    /**
     * username TextView
     */
    private var nav_username: TextView? = null

    /**
     * local username
     */
    private val username: String by Sp(Constant.USERNAME_KEY, "")

    private var mTitles: Array<String> =
        arrayOf(
            "首页",
            "知识体系",
            "公众号",
            "导航",
            "项目"
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
            mIndex = savedInstanceState.getInt(BOTTOM_INDEX)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putInt(BOTTOM_INDEX, mIndex)
    }

    private var nav_mode: MenuItem? = null

    override fun initData() {
        fragments.clear()
        fragments.add(HomeFragment.getInstance())
        fragments.add(KnowledgeTreeFragment.getInstance())
        fragments.add(WeChatFragment.getInstance())
        fragments.add(NavigationFragment.getInstance())
        fragments.add(ProjectFragment.getInstance())

        vp_main.run {
            adapter = mAdapter
            offscreenPageLimit = 5
        }
    }

    override fun initView() {
        super.initView()

        initToolBar()
        toolbar.run {
            tv_title.text = getString(R.string.home)
            iv_right.setOnClickListener {
                goSearch()
            }

            iv_left.apply {
                setImageResource(R.drawable.ic_person)
                setOnClickListener {
                    toggle()
                }
            }
        }

        bottom_navigation.apply {
            // 以前使用 BottomNavigationViewHelper.disableShiftMode(this) 方法来设置底部图标和字体都显示并去掉点击动画
            // 升级到 28.0.0 之后，官方重构了 BottomNavigationView ，目前可以使用 labelVisibilityMode = 1 来替代
            // BottomNavigationViewHelper.disableShiftMode(this)
            labelVisibilityMode = 1
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        initDrawerLayout()

        nav_view.run {
            setNavigationItemSelectedListener(onDrawerNavigationItemSelectedListener)
            nav_username = getHeaderView(0).findViewById(R.id.tv_username)
            menu.findItem(R.id.nav_logout).isVisible = isLogin
            nav_mode = menu.findItem(R.id.nav_night_mode)
            if (SettingUtil.getIsNightMode())
                nav_mode?.title = "日间模式"
            else
                nav_mode?.title = "夜间模式"
        }

        nav_username?.run {
            text = if (!isLogin) {
                getString(R.string.login)
            } else {
                username
            }

            setOnClickListener {
                if (!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }
                }
            }
        }

        naviTag(mIndex)
        floating_action_btn.run {
            setOnClickListener(onFABClickListener)
        }
    }

    private fun goSearch() {
        Intent(this, SearchActivity::class.java).run {
            startActivity(this)
        }

    }

    private val mAdapter: ViewPagerAdapter by lazy {
        ViewPagerAdapter(fragments, supportFragmentManager)
    }


    override fun initThemeColor() {
        super.initThemeColor()
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, drawer_layout,mThemeColor)
        initNavigationColor(this, mThemeColor, bottom_navigation)
        refreshColor(ColorEvent(true))
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshColor(event: ColorEvent) {
        if (event.isRefresh) {
            nav_view.getHeaderView(0).setBackgroundColor(mThemeColor)
            floating_action_btn.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshHomeEvent(event: RefreshHomeEvent) {
        if (event.isRefresh) {
            val fragment = fragments[FRAGMENT_HOME]
            if (fragment is HomeFragment)
                fragment.lazyLoad()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: LoginEvent) {
        if (event.isLogin)
            nav_username?.text = username
        else
            nav_username?.text = resources.getString(R.string.login)

        nav_view.menu.findItem(R.id.nav_logout).isVisible = isLogin
        val fragment = fragments[FRAGMENT_HOME]
        if (fragment is HomeFragment)
            fragment.lazyLoad()
    }


    private fun initDrawerLayout() {

        drawer_layout.run {

            setScrimColor(ContextCompat.getColor(this@MainActivity, R.color.Black_alpha_32))

            addDrawerListener(object : DrawerLayout.DrawerListener {

                override fun onDrawerStateChanged(newState: Int) {
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    drawer_layout.getChildAt(0).translationX = drawerView.width * slideOffset
                }

                override fun onDrawerClosed(drawerView: View) {
                }

                override fun onDrawerOpened(drawerView: View) {
                }

            })
        }
    }


    /**
     * FAB 监听
     */
    private val onFABClickListener = View.OnClickListener {
        when (mIndex) {
            FRAGMENT_HOME -> {
                (fragments[FRAGMENT_HOME] as HomeFragment).scrollToTop()
            }
            FRAGMENT_KNOWLEDGE -> {
                (fragments[FRAGMENT_KNOWLEDGE] as KnowledgeFragment).scrollToTop()
            }
            FRAGMENT_NAVIGATION -> {
                (fragments[FRAGMENT_NAVIGATION] as NavigationFragment).scrollToTop()
            }
            FRAGMENT_PROJECT -> {
                (fragments[FRAGMENT_PROJECT] as ProjectFragment).scrollToTop()
            }
            FRAGMENT_WECHAT -> {
                (fragments[FRAGMENT_WECHAT] as WeChatFragment).scrollToTop()
            }
        }
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            return@OnNavigationItemSelectedListener when (item.itemId) {
                R.id.action_home -> {
                    naviTag(FRAGMENT_HOME)
                    true
                }
                R.id.action_knowledge_system -> {
                    naviTag(FRAGMENT_KNOWLEDGE)
                    true
                }
                R.id.action_wechat -> {
                    naviTag(FRAGMENT_WECHAT)
                    true
                }
                R.id.action_navigation -> {
                    naviTag(FRAGMENT_NAVIGATION)
                    true
                }
                R.id.action_project -> {
                    naviTag(FRAGMENT_PROJECT)
                    true
                }

                else -> {
                    false
                }

            }
        }

    private fun naviTag(position: Int) {
        mIndex = position
        vp_main.currentItem = position

        toolbar.run {
            tv_title.text = mTitles[position]
            if (position == FRAGMENT_HOME) {
                iv_left.visibility = View.VISIBLE
                iv_right.visibility = View.VISIBLE
            } else {
                iv_left.visibility = View.INVISIBLE
                iv_right.visibility = View.INVISIBLE
            }
        }

    }

    /**
     * NavigationView 监听
     */
    private val onDrawerNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_collect -> {
                    if (isLogin) {
                        Intent(this@MainActivity, CommonActivity::class.java).run {
                            putExtra(Constant.TYPE_KEY, Constant.Type.COLLECT_TYPE_KEY)
                            startActivity(this)
                        }
                    } else {
                        showToast(resources.getString(R.string.login_tint))
                        Intent(this@MainActivity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                    }
                }
                R.id.nav_setting -> {
                    Intent(this@MainActivity, SettingActivity::class.java).run {
                        startActivity(this)
                    }
                }
                R.id.nav_about_us -> {
                    Intent(this@MainActivity, CommonActivity::class.java).run {
                        putExtra(Constant.TYPE_KEY, Constant.Type.ABOUT_US_TYPE_KEY)
                        startActivity(this)
                    }
                }
                R.id.nav_logout -> {
                    logout()
                }
                R.id.nav_night_mode -> {
                    if (SettingUtil.getIsNightMode()) {
                        SettingUtil.setIsNightMode(false)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//日间模式
                        nav_mode?.title = "夜间模式"

                    } else {
                        SettingUtil.setIsNightMode(true)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)//夜间模式
                        nav_mode?.title = "日间模式"
                    }
                    window.setWindowAnimations(R.style.WindowAnimationFadeInOut)
                    recreate()
                }
                R.id.nav_todo -> {
                    if (isLogin) {
                        Intent(this@MainActivity, TodoActivity::class.java).run {
                            startActivity(this)
                        }
                    } else {
                        showToast(resources.getString(R.string.login_tint))
                        Intent(this@MainActivity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                    }
                }
            }
            toggle()
            true
        }

    private fun toggle() {
        val drawerLockMode = drawer_layout.getDrawerLockMode(GravityCompat.START)
        if (drawer_layout.isDrawerVisible(GravityCompat.START) && drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_OPEN)
            drawer_layout.closeDrawer(GravityCompat.START)
        else if (drawerLockMode != DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            drawer_layout.openDrawer(GravityCompat.START)
    }


    /**
     * 退出登录 Dialog
     */
    private val mDialog by lazy {
        DialogUtil.getWaitDialog(this@MainActivity, resources.getString(R.string.logout_ing))
    }

    /**
     * Logout
     */
    private fun logout() {
        DialogUtil.getConfirmDialog(this, resources.getString(R.string.confirm_logout),
            DialogInterface.OnClickListener { _, _ ->
                mDialog.show()
                mPresenter?.logout()
            }).show()
    }

    override fun showLogoutSuccess(success: Boolean) {
        if (success) {
            doAsync {
                Sp.clearPreference()
                uiThread {
                    mDialog.dismiss()
                    showToast(resources.getString(R.string.logout_success))
                    isLogin = false
                    EventBus.getDefault().post(LoginEvent(false))
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivity(this)
                    }

                }
            }
        }
    }

    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast(getString(R.string.exit_tip))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}


