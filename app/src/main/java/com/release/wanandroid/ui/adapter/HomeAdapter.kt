package com.release.wanandroid.ui.adapter

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.release.wanandroid.R
import com.release.wanandroid.mvp.model.bean.Article
import com.release.wanandroid.utils.ImageLoader

/**
 * @author Mr.release
 * @create 2019/6/24
 * @Describe
 */
class HomeAdapter(private val context: Context?, datas: MutableList<Article>) :
    BaseQuickAdapter<Article, BaseViewHolder>(com.release.wanandroid.R.layout.item_home_list, datas) {


    override fun convert(helper: BaseViewHolder?, item: Article?) {
        item ?: return
        helper ?: return
        val charSequence: CharSequence

        //FROM_HTML_MODE_COMPACT：html块元素之间使用一个换行符分隔
        //FROM_HTML_MODE_LEGACY：html块元素之间使用两个换行符分隔
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(item.title, Html.FROM_HTML_MODE_LEGACY)
        } else {
            charSequence = Html.fromHtml(item.title)
        }

        val chapterName = when {
            item.superChapterName.isNotEmpty() and item.chapterName.isNotEmpty() ->
                "${item.superChapterName} / ${item.chapterName}"
            item.superChapterName.isNotEmpty() -> item.superChapterName
            item.chapterName.isNotEmpty() -> item.chapterName
            else -> ""
        }

        helper.setText(com.release.wanandroid.R.id.tv_article_title, charSequence)
            .setText(R.id.tv_article_author, item.author)
            .setText(R.id.tv_article_date, item.niceDate)
            .setText(R.id.tv_article_chapterName, chapterName)
            .setImageResource(R.id.iv_like,
                if (item.collect) R.drawable.ic_like else R.drawable.ic_like_not
            )
            .addOnClickListener(R.id.iv_like)

        if (item.envelopePic.isNotEmpty()) {
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                .visibility = View.VISIBLE
            context?.let {
                ImageLoader.load(it, item.envelopePic, helper.getView(R.id.iv_article_thumbnail))
            }
        } else {
            helper.getView<ImageView>(R.id.iv_article_thumbnail)
                .visibility = View.GONE
        }

        val tv_fresh = helper.getView<TextView>(R.id.tv_article_fresh)
        if (item.fresh) {
            tv_fresh.visibility = View.VISIBLE
        } else {
            tv_fresh.visibility = View.GONE
        }
        val tv_top = helper.getView<TextView>(R.id.tv_article_top)
        if (item.top == "1") {
            tv_top.visibility = View.VISIBLE
        } else {
            tv_top.visibility = View.GONE
        }
        val tv_article_tag = helper.getView<TextView>(R.id.tv_article_tag)
        if (item.tags.size > 0) {
            tv_article_tag.visibility = View.VISIBLE
            tv_article_tag.text = item.tags[0].name
        } else {
            tv_article_tag.visibility = View.GONE
        }
    }



}
