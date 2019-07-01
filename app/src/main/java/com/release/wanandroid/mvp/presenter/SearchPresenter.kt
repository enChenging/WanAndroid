package com.release.wanandroid.mvp.presenter

import com.release.wanandroid.base.BasePresenter
import com.release.wanandroid.ext.ss
import com.release.wanandroid.mvp.contract.SearchContract
import com.release.wanandroid.mvp.model.SearchModel
import com.release.wanandroid.mvp.model.bean.SearchHistoryBean
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal
/**
 * @author Mr.release
 * @create 2019/7/1
 * @Describe
 */
class SearchPresenter : BasePresenter<SearchContract.Model, SearchContract.View>(), SearchContract.Presenter {


    override fun createModel(): SearchContract.Model? = SearchModel()

    override fun deleteById(id: Long) {
        doAsync {
            LitePal.delete(SearchHistoryBean::class.java, id)
        }

    }

    override fun clearAllHistory() {
        doAsync {
            LitePal.deleteAll(SearchHistoryBean::class.java)
            uiThread {

            }
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val beans = LitePal.where("key = '${key.trim()}'").find(SearchHistoryBean::class.java)
            if (beans.size == 0) {
                historyBean.save()
            } else {
                deleteById(beans[0].id)
                historyBean.save()
            }
        }
    }

    override fun queryHistory() {
        doAsync {
            val historyBeans = LitePal.findAll(SearchHistoryBean::class.java)
            historyBeans.reverse()
            uiThread {
                mView?.showHistoryData(historyBeans)
            }
        }
    }

    override fun getHotSearchData() {
        mModel?.getHotSearchData()?.ss(mModel, mView) {
            mView?.showHotSearchData(it.data)
        }
    }

}