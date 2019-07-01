package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.SearchListContract
import com.release.wanandroid.mvp.model.SearchListModel
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SearchListPresenter : CommonPresenter<SearchListContract.Model, SearchListContract.View>(), SearchListContract.Presenter {

    override fun createModel(): SearchListContract.Model? = SearchListModel()

    override fun queryBySearchKey(page: Int, key: String) {
        mModel?.queryBySearchKey(page, key)?.ss(mModel, mView, page == 0) {
            mView?.showArticles(it.data)
        }
    }

}