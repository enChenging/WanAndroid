package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface RegisterContract {

    interface View : IView {

        fun registerSuccess(data: LoginData)

        fun registerFail()
    }

    interface Presenter : IPresenter<View> {

        fun registerWanAndroid(username: String, password: String, repassword: String)

    }

    interface Model : IModel {
        fun registerWanAndroid(username: String, password: String, repassword: String): Observable<HttpResult<LoginData>>
    }

}