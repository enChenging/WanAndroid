package com.release.wanandroid.mvp.model

import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.ProjectListContract
import com.release.wanandroid.mvp.model.bean.ArticleResponseBody
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class ProjectListModel : CommonModel(), ProjectListContract.Model {

    override fun requestProjectList(page: Int, cid: Int): Observable<HttpResult<ArticleResponseBody>> {
        return RetrofitHelper.service.getProjectList(page, cid)
    }

}