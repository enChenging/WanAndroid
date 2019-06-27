package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HttpResult
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
interface CommonContract {

    interface View :IView{
        fun showCollectSuccess(success:Boolean)
        fun showCancelCollectSuccess(success: Boolean)
    }

    interface Presenter<in V:View>:IPresenter<V>{
        fun addCollectArticle(id:Int)
        fun cancelCollectArticle(id:Int)
    }

    interface Model: IModel {
        fun addCollectArticle(id:Int): Observable<HttpResult<Any>>
        fun cancelCollectArticle(id:Int):Observable<HttpResult<Any>>
    }
}