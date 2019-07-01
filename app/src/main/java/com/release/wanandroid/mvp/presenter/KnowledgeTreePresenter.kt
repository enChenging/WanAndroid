package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.base.BasePresenter
import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.KnowledgeTreeContract
import com.release.wanandroid.mvp.model.KnowlegeTreeModel

/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class KnowledgeTreePresenter :BasePresenter<KnowledgeTreeContract.Model,KnowledgeTreeContract.View>(),KnowledgeTreeContract.Presenter{
    override fun createModel(): KnowledgeTreeContract.Model?  = KnowlegeTreeModel()
    override fun requestKnowledgeTree() {
        mModel?.requestKnowledgeTree()?.ss(mModel,mView){
            mView?.setKnowledgeTree(it.data)
        }
    }

}