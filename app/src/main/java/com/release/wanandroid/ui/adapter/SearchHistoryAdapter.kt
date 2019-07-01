package com.release.wanandroid.ui.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.release.wanandroid.R
import com.release.wanandroid.mvp.model.bean.SearchHistoryBean
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SearchHistoryAdapter(private val context: Context?, datas: MutableList<SearchHistoryBean>)
    : BaseQuickAdapter<SearchHistoryBean, BaseViewHolder>(R.layout.item_search_history, datas) {

    override fun convert(helper: BaseViewHolder?, item: SearchHistoryBean?) {
        helper ?: return
        item ?: return

        helper.setText(R.id.tv_search_key, item.key)
                .addOnClickListener(R.id.iv_clear)

    }
}