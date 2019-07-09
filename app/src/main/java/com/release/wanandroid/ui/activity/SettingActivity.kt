package com.release.wanandroid.ui.activity

import android.widget.CompoundButton
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.release.wanandroid.R
import com.release.wanandroid.base.BaseSwipeBackActivity
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.event.RefreshHomeEvent
import com.release.wanandroid.rx.SchedulerUtils
import com.release.wanandroid.ui.fragment.AutoNightModeFragment
import com.release.wanandroid.utils.CacheDataUtil
import com.release.wanandroid.utils.SettingUtil
import com.release.wanandroid.utils.Sp
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_setting2.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus
import java.util.concurrent.TimeUnit

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SettingActivity : BaseSwipeBackActivity(), ColorChooserDialog.ColorCallback {

    private val EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title"

    override fun initLayoutID(): Int = R.layout.activity_setting2

    var switch_noPhotoMode: Boolean  by Sp("switch_noPhotoMode", false)
    var auto_nightMode: Boolean  by Sp("auto_nightMode", false)
    var switch_show_top: Boolean  by Sp("switch_show_top", false)
    var nav_bar: Boolean  by Sp("nav_bar", false)


    override fun initView() {

        val initTitle: String = intent.getStringExtra(EXTRA_SHOW_FRAGMENT_TITLE) ?: "设置"
        initToolBar()
        tv_title.text = initTitle

        initListener()
    }

    private fun initListener() {


        sc_no_image.apply {
            isSelected = switch_noPhotoMode
            setOnCheckedChangeListener { _, isChecked -> switch_noPhotoMode = isChecked }
        }


        sc_tv_auto_night.apply {
            isSelected = auto_nightMode
            setOnCheckedChangeListener { _, isChecked -> auto_nightMode = isChecked }
        }

        sc_no_image.apply {
            isSelected = switch_show_top
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    //通知首页刷新数据
                    // 延迟发送通知：为了保证刷新数据时 SettingUtil.getIsShowTopArticle() 得到最新的值
                    Observable.timer(100, TimeUnit.MILLISECONDS)
                        .compose(SchedulerUtils.ioToMain())
                        .subscribe({
                            EventBus.getDefault().post(RefreshHomeEvent(true))
                        }, {})
                }
                switch_show_top = isChecked
            }
        }


        sc_bar_coloration_summary.apply {
            isSelected = nav_bar
            setOnCheckedChangeListener { _, isChecked -> nav_bar = isChecked }
        }


        ll_cache.apply {

            setOnClickListener {
                CacheDataUtil.clearAllCache(context!!)
//            context?.showSnackMsg(getString(R.string.clear_cache_successfully))
//            setDefaultText()
            }
        }
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

}
