package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.ProjectContract
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.ProjectTreeBean
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class ProjectModel : BaseModel(), ProjectContract.Model {

    override fun requestProjectTree(): Observable<HttpResult<List<ProjectTreeBean>>> {
        return RetrofitHelper.service.getProjectTree()
    }

}