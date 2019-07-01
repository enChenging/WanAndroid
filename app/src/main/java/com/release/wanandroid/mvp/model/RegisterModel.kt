package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.RegisterContract
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.LoginData
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class RegisterModel : BaseModel(), RegisterContract.Model {

    override fun registerWanAndroid(username: String, password: String, repassword: String): Observable<HttpResult<LoginData>> {
        return RetrofitHelper.service.registerWanAndroid(username, password, repassword)
    }

}