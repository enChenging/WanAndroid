package com.release.wanandroid.mvp.model

import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.KnowledgeContract
import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class KnowledgeModel : CommonModel(), KnowledgeContract.Model {

    override fun requestKnowledgeList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getKnowledgeList(page, cid)
    }

}
