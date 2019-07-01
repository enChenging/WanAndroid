package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.WXChapterBean
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface WeChatContract {

    interface View : IView {

        fun scrollToTop()

        fun showWXChapters(chapters: MutableList<WXChapterBean>)

    }

    interface Presenter : IPresenter<View> {
        fun getWXChapters()
    }

    interface Model : IModel {
        fun getWXChapters(): Observable<HttpResult<MutableList<WXChapterBean>>>
    }

}