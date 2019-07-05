package com.release.wanandroid

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDexApplication
import android.support.v7.app.AppCompatDelegate
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.ext.showToast
import com.release.wanandroid.utils.CommonUtil
import com.release.wanandroid.utils.DisplayManager
import com.release.wanandroid.utils.SettingUtil
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeStateListener
import com.tencent.bugly.crashreport.CrashReport
import org.litepal.LitePal
import java.util.*
import kotlin.properties.Delegates

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class App : MultiDexApplication(){


    companion object{
        var context: Context by Delegates.notNull()

        lateinit var instance:Application
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context  = applicationContext
        initConfig()
        DisplayManager.init(this)
        initTheme()
        initLitePal()
        initBugly()
    }

    /**
     * 初始化 Bugly
     */
    private fun initBugly() {
        if (BuildConfig.DEBUG) {
            return
        }
        // 获取当前包名
        val packageName = applicationContext.packageName
        // 获取当前进程名
        val processName = CommonUtil.getProcessName(android.os.Process.myPid())
        Beta.upgradeStateListener = object : UpgradeStateListener {
            override fun onDownloadCompleted(isManual: Boolean) {
            }

            override fun onUpgradeSuccess(isManual: Boolean) {
            }

            override fun onUpgradeFailed(isManual: Boolean) {
                if (isManual) {
                    showToast(getString(R.string.check_version_fail))
                }
            }

            override fun onUpgrading(isManual: Boolean) {
                if (isManual) {
                    showToast(getString(R.string.check_version_ing))
                }
            }

            override fun onUpgradeNoVersion(isManual: Boolean) {
                if (isManual) {
                    showToast(getString(R.string.check_no_version))
                }
            }
        }
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(applicationContext)
        strategy.isUploadProcess = false || processName == packageName
        Bugly.init(applicationContext, Constant.BUGLY_ID, BuildConfig.DEBUG, strategy)
    }

    private fun initLitePal() {
        LitePal.initialize(this)
    }

    /**
     * 初始化主题
     */
    private fun initTheme() {

        if (SettingUtil.getIsAutoNightMode()) {
            val nightStartHour = SettingUtil.getNightStartHour().toInt()
            val nightStartMinute = SettingUtil.getNightStartMinute().toInt()
            val dayStartHour = SettingUtil.getDayStartHour().toInt()
            val dayStartMinute = SettingUtil.getDayStartMinute().toInt()

            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val nightValue = nightStartHour * 60 + nightStartMinute
            val dayValue = dayStartHour * 60 + dayStartMinute
            val currentValue = currentHour * 60 + currentMinute

            if (currentValue >= nightValue || currentValue <= dayValue) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                SettingUtil.setIsNightMode(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                SettingUtil.setIsNightMode(false)
            }
        } else {
            // 获取当前的主题
            if (SettingUtil.getIsNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    /**
     * 初始化配置
     */
    private fun initConfig() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 隐藏线程信息 默认：显示
            .methodCount(0)         // 决定打印多少行（每一行代表一个方法）默认：2
            .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
            .tag("cyc")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }
}
