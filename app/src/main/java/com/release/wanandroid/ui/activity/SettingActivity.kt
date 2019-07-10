package com.release.wanandroid.ui.activity

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.preference.PreferenceManager
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.release.wanandroid.App
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseSwipeBackActivity
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.event.RefreshHomeEvent
import com.release.wanandroid.ext.showSnackMsg
import com.release.wanandroid.rx.SchedulerUtils
import com.release.wanandroid.utils.CacheDataUtil
import com.release.wanandroid.utils.SettingUtil
import com.tencent.bugly.beta.Beta
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SettingActivity : BaseSwipeBackActivity(), ColorChooserDialog.ColorCallback,
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun initLayoutID(): Int = R.layout.activity_setting

    private lateinit var nightStartHour: String
    private lateinit var nightStartMinute: String
    private lateinit var dayStartHour: String
    private lateinit var dayStartMinute: String

    override fun initView() {

        initToolBar()
        tv_title.text = "设置"

        setDefaultText()
        setDefaultColor(SettingUtil.getIsAutoNightMode())

        initListener()

    }

    private fun initListener() {

        //是否显示首页置顶文章
        sc_top_summary.apply {
            isChecked = SettingUtil.getIsShowTopArticle()
            setOnCheckedChangeListener { _, isChecked ->
                SettingUtil.setIsShowTopArticle(isChecked)
                if (isChecked) {
                    //通知首页刷新数据
                    // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
                    Observable.timer(100, TimeUnit.MILLISECONDS)
                        .compose(SchedulerUtils.ioToMain())
                        .subscribe({
                            EventBus.getDefault().post(RefreshHomeEvent(true))
                        }, {})
                }
            }
        }

        //导航栏着色
        sc_bar_coloration_summary.apply {
            isChecked = SettingUtil.getNavBar()
            setOnCheckedChangeListener { _, isChecked -> SettingUtil.setNavBar(isChecked) }
        }


        //自动切换夜间模式
        sc_tv_auto_night.apply {
            isChecked = SettingUtil.getIsAutoNightMode()

            setOnCheckedChangeListener { _, isChecked ->
                SettingUtil.setIsAutoNightMode(isChecked)
                if (isChecked) {
                    setDefaultColor(isChecked)
                } else {
                    setDefaultColor(isChecked)
                }
            }
        }

        rl_night_time.apply {
            setOnClickListener {
                if (SettingUtil.getIsAutoNightMode()) {
                    val dialog = TimePickerDialog(this@SettingActivity, { _, hour, minute ->
                        SettingUtil.setNightStartHour(if (hour > 9) hour.toString() else "0$hour")
                        SettingUtil.setNightStartMinute(if (minute > 9) minute.toString() else "0$minute")
                        setDefaultText()
                    }, nightStartHour.toInt(), nightStartMinute.toInt(), true)
                    dialog.show()
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.done)
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(R.string.cancel)
                }

            }

        }

        rl_day_time.apply {
            setOnClickListener {
                if (SettingUtil.getIsAutoNightMode()) {
                    val dialog = TimePickerDialog(this@SettingActivity, { _, hour, minute ->
                        SettingUtil.setDayStartHour(if (hour > 9) hour.toString() else "0$hour")
                        SettingUtil.setDayStartMinute(if (minute > 9) minute.toString() else "0$minute")
                        setDefaultText()
                    }, dayStartHour.toInt(), dayStartMinute.toInt(), true)
                    dialog.show()
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(R.string.done)
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText(R.string.cancel)
                }
            }
        }

        //自定义主题颜色
        cv_theme_color.setBackgroundColor(SettingUtil.getColor())
        rl_theme_color.apply {
            setOnClickListener {
                ColorChooserDialog.Builder(context!!, R.string.choose_theme_color)
                    .backButton(R.string.back)
                    .cancelButton(R.string.cancel)
                    .doneButton(R.string.done)
                    .customButton(R.string.custom)
                    .presetsButton(R.string.back)
                    .allowUserColorInputAlpha(false)
                    .show(this@SettingActivity)

            }
        }

        //清除缓存
        setDefaultText()
        ll_cache.apply {

            setOnClickListener {
                CacheDataUtil.clearAllCache(context!!)
                this@SettingActivity.showSnackMsg(getString(R.string.clear_cache_successfully))
                setDefaultText()
            }
        }

        //版本更新
        try {
            val version = "当前版本 " + packageManager?.getPackageInfo(packageName, 0)?.versionName
            tv_has_update.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        ll_update.apply {
            Beta.checkUpgrade()
        }
    }

    private fun setDefaultText() {
        tv_time_notice.setTextColor(mThemeColor)
        try {
            tv_cache.text = CacheDataUtil.getTotalCacheSize(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        nightStartHour = SettingUtil.getNightStartHour()
        nightStartMinute = SettingUtil.getNightStartMinute()
        dayStartHour = SettingUtil.getDayStartHour()
        dayStartMinute = SettingUtil.getDayStartMinute()

        tv_night_time.text = "$nightStartHour:$nightStartMinute"
        tv_day_time.text = "$dayStartHour:$dayStartMinute"
    }


    private fun setDefaultColor(isChecked: Boolean) {
        rl_night_time.isClickable = isChecked
        rl_day_time.isClickable = isChecked
        tv_night_title.isSelected = isChecked
        tv_day_title.isSelected = isChecked
        tv_night_time.isSelected = isChecked
        tv_day_time.isSelected = isChecked
    }


    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(App.context).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(App.context).unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if (!dialog.isAccentMode) {
            SettingUtil.setColor(selectedColor)
        }
        initThemeColor()
        EventBus.getDefault().post(ColorEvent(true))
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        key ?: return
        if (key == "color") {
            val color = SettingUtil.getColor()
            cv_theme_color.setBackgroundColor(color)
            tv_time_notice.setTextColor(color)
        }
    }
}

