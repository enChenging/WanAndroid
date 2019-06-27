package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/6/26
 * @Describe
 */
interface MainContract {

    interface View :IView{
        fun showLogoutSuccess(success: Boolean)
    }

    interface Presenter: IPresenter<View>{
        fun logout()
    }

    interface Model: IModel {

        fun logout(): Observable<HttpResult<Any>>

    }
}