package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.base.BasePresenter
import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.LoginContract
import com.release.wanandroid.mvp.model.LoginModel

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
class LoginPresenter :BasePresenter<LoginContract.Model,LoginContract.View>(),LoginContract.Presenter{

    override fun createModel(): LoginContract.Model?  = LoginModel()

    override fun loginWanAndroid(username: String, password: String) {
        mModel?.loginWanAndroid(username,password)?.ss(mModel,mView){
            mView?.loginSuccess(it.data)
        }
    }

}