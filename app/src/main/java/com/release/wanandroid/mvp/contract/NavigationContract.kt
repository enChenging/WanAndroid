package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.NavigationBean
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface NavigationContract {

    interface View : IView {
        fun setNavigationData(list: List<NavigationBean>)
    }

    interface Presenter : IPresenter<View> {
        fun requestNavigationList()
    }

    interface Model : IModel {
        fun requestNavigationList(): Observable<HttpResult<List<NavigationBean>>>
    }

}