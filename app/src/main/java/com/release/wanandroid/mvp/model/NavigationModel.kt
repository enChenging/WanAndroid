package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.NavigationContract
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.NavigationBean
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class NavigationModel : BaseModel(), NavigationContract.Model {

    override fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>> {
        return RetrofitHelper.service.getNavigationList()
    }

}