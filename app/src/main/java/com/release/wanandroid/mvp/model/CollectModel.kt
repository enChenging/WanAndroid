package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.CollectContract
import com.release.wanandroid.mvp.model.bean.CollectionArticle
import com.release.wanandroid.mvp.model.bean.CollectionResponseBody
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class CollectModel : BaseModel(), CollectContract.Model {

    override fun getCollectList(page: Int): Observable<HttpResult<CollectionResponseBody<CollectionArticle>>> {
        return RetrofitHelper.service.getCollectList(page)
    }

    override fun removeCollectArticle(id: Int, originId: Int): Observable<HttpResult<Any>> {
        return RetrofitHelper.service.removeCollectArticle(id, originId)
    }

}