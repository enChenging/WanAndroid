package com.release.wanandroid.mvp.contract

import com.release.wanandroid.base.IModel
import com.release.wanandroid.base.IPresenter
import com.release.wanandroid.base.IView
import com.release.wanandroid.mvp.model.bean.HotSearchBean
import com.release.wanandroid.mvp.model.bean.HttpResult
import com.release.wanandroid.mvp.model.bean.SearchHistoryBean
import io.reactivex.Observable
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
interface SearchContract {

    interface View : IView {

        fun showHistoryData(historyBeans: MutableList<SearchHistoryBean>)

        fun showHotSearchData(hotSearchDatas: MutableList<HotSearchBean>)

    }

    interface Presenter : IPresenter<View> {

        fun queryHistory()

        fun saveSearchKey(key: String)

        fun deleteById(id: Long)

        fun clearAllHistory()

        fun getHotSearchData()

    }

    interface Model : IModel {

        fun getHotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>>

    }

}