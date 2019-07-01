package com.release.wanandroid.mvp.contract

import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface KnowledgeContract {

    interface View : CommonContract.View {

        fun scrollToTop()

        fun setKnowledgeList(articles: ArticleResponseBody)

    }

    interface Presenter : CommonContract.Presenter<View> {

        fun requestKnowledgeList(page: Int, cid: Int)

    }

    interface Model : CommonContract.Model {

        fun requestKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>>

    }

}