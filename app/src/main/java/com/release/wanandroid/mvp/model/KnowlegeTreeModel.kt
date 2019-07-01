package com.release.wanandroid.mvp.model

import com.release.wanandroid.base.BaseModel
import com.release.wanandroid.http.RetrofitHelper
import com.release.wanandroid.mvp.contract.KnowledgeTreeContract
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.KnowledgeTreeBody
import io.reactivex.Observable

/**
 * @author Mr.release
 * @create 2019/6/29
 * @Describe
 */
class KnowlegeTreeModel :BaseModel(),KnowledgeTreeContract.Model {
    override fun requestKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeBody>>> {
        return RetrofitHelper.service.getKnowledgeTree()
    }


}