package com.release.wanandroid.ui.login

import android.content.Intent
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.release.wanandroid.MainActivity
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseActivity
import com.release.wanandroid.utils.StatusBarUtil
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
        val alphaAnimation = AlphaAnimation(0.5f, 1.0f)
        alphaAnimation.run {
            duration = 2000
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    jumpToMain()
                }

                override fun onAnimationStart(animation: Animation?) {
                }

            })
        }
        layout_splash.startAnimation(alphaAnimation)

    }

    private fun jumpToMain() {
        Intent(this, MainActivity::class.java).run {
            startActivity(this)
        }
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun initThemeColor() {
        StatusBarUtil.setTranslucent(this, 0)
    }
}