package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.base.BasePresenter
import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.CommonContract

/**
 * @author Mr.release
 * @create 2019/6/25
 * @Describe
 */
open class CommonPresenter<M : CommonContract.Model, V : CommonContract.View> : BasePresenter<M, V>(), CommonContract.Presenter<V> {
    override fun addCollectArticle(id: Int) {
        mModel?.addCollectArticle(id)?.ss(mModel, mView) {
            mView?.showCollectSuccess(true)
        }
    }

    override fun cancelCollectArticle(id: Int) {
        mModel?.cancelCollectArticle(id)?.ss(mModel, mView) {
            mView?.showCancelCollectSuccess(true)
        }
    }

}