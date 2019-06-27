package com.release.wanandroid.base

import io.reactivex.disposables.Disposable

/**
 * @author Mr.release
 * @create 2019/6/26
 * @Describe
 */
interface IModel {

    fun addDisposable(disposable: Disposable?)

    fun onDetach()

}