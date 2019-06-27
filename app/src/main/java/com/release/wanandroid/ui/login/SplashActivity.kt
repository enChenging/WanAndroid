package com.release.wanandroid.ui.login

import com.release.wanandroid.MainActivity
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class SplashActivity : BaseActivity() {

    override fun initLayoutID(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        tv_jump.run {
            setOnClickListener {
                MainActivity.start(this@SplashActivity)
                finish()
            }
        }
    }


}