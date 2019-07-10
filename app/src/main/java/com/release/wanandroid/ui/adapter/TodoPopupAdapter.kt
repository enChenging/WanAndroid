package com.release.wanandroid.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.release.wanandroid.R
import com.release.wanandroid.mvp.model.bean.TodoTypeBean

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class TodoPopupAdapter : BaseQuickAdapter<TodoTypeBean, BaseViewHolder>(R.layout.item_todo_popup_list) {

    override fun convert(helper: BaseViewHolder?, item: TodoTypeBean?) {
        helper ?: return
        item ?: return
        val tv_popup = helper.getView<TextView>(R.id.tv_popup)
        tv_popup.text = item.name
        if (item.isSelected) {
            tv_popup.setTextColor(mContext.resources.getColor(R.color.colorPrimary))
        } else {
            tv_popup.setTextColor(mContext.resources.getColor(R.color.common_color))
        }
    }
}