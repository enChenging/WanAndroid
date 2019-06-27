package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
interface LoginContract  {

    interface View:IView{
        fun loginSuccess(data: LoginData)
        fun loginFail()
    }

    interface Presenter:IPresenter<View>{
        fun loginWanAndroid(username:String ,password:String)
    }

    interface Model:IModel{
        fun loginWanAndroid(username:String,password:String): Observable<HttpResult<LoginData>>
    }
}