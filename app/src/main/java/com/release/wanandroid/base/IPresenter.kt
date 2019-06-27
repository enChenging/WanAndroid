package com.release.wanandroid.base

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
interface IPresenter<in V:IView> {

    fun attachView(mView:V)

    fun detachView()
}