package com.release.wanandroid.mvp.model

import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.SearchListContract
import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SearchListModel : CommonModel(), SearchListContract.Model {

    override fun queryBySearchKey(page: Int, key: String): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.queryBySearchKey(page, key)
    }

}