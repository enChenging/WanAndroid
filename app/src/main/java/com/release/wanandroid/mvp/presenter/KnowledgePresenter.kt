package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.KnowledgeContract
import com.release.wanandroid.mvp.model.KnowledgeModel

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class KnowledgePresenter : CommonPresenter<KnowledgeContract.Model, KnowledgeContract.View>(), KnowledgeContract.Presenter {

    override fun createModel(): KnowledgeContract.Model? = KnowledgeModel()

    override fun requestKnowledgeList(page: Int, cid: Int) {
        mModel?.requestKnowledgeList(page, cid)?.ss(mModel, mView) {
            mView?.setKnowledgeList(it.data)
        }
    }

}