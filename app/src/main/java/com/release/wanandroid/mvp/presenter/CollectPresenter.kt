package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.base.BasePresenter
import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.CollectContract
import com.release.wanandroid.mvp.model.CollectModel

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class CollectPresenter : BasePresenter<CollectContract.Model, CollectContract.View>(), CollectContract.Presenter {

    override fun createModel(): CollectContract.Model? = CollectModel()

    override fun getCollectList(page: Int) {
        mModel?.getCollectList(page)?.ss(mModel, mView, page == 0) {
            mView?.setCollectList(it.data)
        }
    }

    override fun removeCollectArticle(id: Int, originId: Int) {
        mModel?.removeCollectArticle(id, originId)?.ss(mModel, mView) {
            mView?.showRemoveCollectSuccess(true)
        }
    }

}