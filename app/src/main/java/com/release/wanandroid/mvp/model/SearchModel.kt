package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.SearchContract
import com.release.wanandroid.mvp.model.bean.HotSearchBean
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SearchModel : BaseModel(), SearchContract.Model {

    override fun getHotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>> {
        return RetrofitHelper.service.getHotSearchData()
    }

}