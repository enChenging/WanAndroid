package com.release.wanandroid.ui.activity

import com.release.wanandroid.R
import com.release.wanandroid.base.BaseSwipeBackActivity
import com.release.wanandroid.constant.Constant
import com.release.wanandroid.event.ColorEvent
import com.release.wanandroid.ui.fragment.*
import kotlinx.android.synthetic.main.item_todo_list.view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.greenrobot.eventbus.EventBus

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class CommonActivity : BaseSwipeBackActivity() {

    override fun initLayoutID(): Int = R.layout.activity_common

    override fun initData() {
    }

    override fun initView() {
        val extras = intent.extras
        val type = extras.getString(Constant.TYPE_KEY, "")

        initToolBar()

        val fragment = when (type) {
            Constant.Type.COLLECT_TYPE_KEY -> {
                tv_title.text = getString(R.string.collect)
                CollectFragment.getInstance(extras)
            }
            Constant.Type.ABOUT_US_TYPE_KEY -> {
                tv_title.text = getString(R.string.about_us)
                AboutFragment.getInstance(extras)
            }

            Constant.Type.SEARCH_TYPE_KEY -> {
                tv_title.text = extras.getString(Constant.SEARCH_KEY, "")
                SearchListFragment.getInstance(extras)
            }
            Constant.Type.ADD_TODO_TYPE_KEY -> {
                tv_title.text = getString(R.string.add)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                tv_title.text = getString(R.string.edit)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SEE_TODO_TYPE_KEY -> {
                tv_title.text = getString(R.string.see)
                AddTodoFragment.getInstance(extras)
            }
            else -> {
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.common_frame_layout, fragment, Constant.Type.COLLECT_TYPE_KEY)
            .commit()

    }

    override fun initThemeColor() {
        super.initThemeColor()
        EventBus.getDefault().post(ColorEvent(true, mThemeColor))
    }

}
